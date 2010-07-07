package net.big_oh.common.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.big_oh.common.jdbc.event.SQLExecutionEvent;
import net.big_oh.common.jdbc.event.listener.JDBCEventListener;
import net.big_oh.common.utils.RegExpHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/*
 Copyright (c) 2009 Dave Wingate dba Big-Oh Software (www.big-oh.net)

 Permission is hereby granted, free of charge, to any person
 obtaining a copy of this software and associated documentation
 files (the "Software"), to deal in the Software without
 restriction, including without limitation the rights to use,
 copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the
 Software is furnished to do so, subject to the following
 conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * The invocation handler for a {@link Proxy} that stands in for a
 * Driver-created SQL {@link Statement} so that execution of the Statement can
 * be observed.
 */
class JdbcObserverProxyStatementInvocationHandler implements InvocationHandler
{

	private static final Log logger = LogFactory.getLog(JdbcObserverProxyStatementInvocationHandler.class);

	private static final Integer[] SQL_CHARACTER_TYPES = { Types.CHAR, Types.LONGVARCHAR, Types.VARCHAR };

	private final Statement originalStatement;

	private final List<JDBCEventListener> listeners;

	private final String preparedSQLTemplate;

	private List<String> batchSQLStatements = new ArrayList<String>();

	private Map<Integer, String> preparedStatementParameters = new HashMap<Integer, String>();

	JdbcObserverProxyStatementInvocationHandler(Statement originalStatement, List<JDBCEventListener> listeners, String hardCodedSqlForStatement)
	{
		this.originalStatement = originalStatement;

		// Create a new list of listeners to side-step the possibility of
		// ConcurrentModificationException
		this.listeners = Collections.unmodifiableList(new ArrayList<JDBCEventListener>(listeners));

		this.preparedSQLTemplate = hardCodedSqlForStatement;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{

		// intercept calls that execute SQL
		if ("execute".equals(method.getName()) || "executeQuery".equals(method.getName()) || "executeUpdate".equals(method.getName()))
		{

			Statement stmnt = (Statement) proxy;

			String sqlCommand = getSqlStringForMethodInvocation(stmnt, args);

			SQLExecutionEvent event = new SQLExecutionEvent(stmnt, Collections.singletonList(sqlCommand), method, PreparedStatement.class.isAssignableFrom(proxy.getClass()));

			for (JDBCEventListener listener : listeners)
			{
				try
				{
					listener.beforeSQLExecution(event);
				}
				catch (RuntimeException rte)
				{
					logger.error(rte);
				}
			}

			Object result = method.invoke(originalStatement, args);

			for (JDBCEventListener listener : listeners)
			{
				try
				{
					listener.afterSQLExecution(event);
				}
				catch (RuntimeException rte)
				{
					logger.error(rte);
				}
			}

			return result;

		}

		// intercept the call to clear out batched SQL
		if ("clearBatch".equals(method.getName()))
		{

			// remove any previously noted batch SQL statments
			batchSQLStatements.clear();

			Object result = method.invoke(originalStatement, args);

			return result;

		}

		// intercept calls to add batch SQL
		if ("addBatch".equals(method.getName()))
		{

			Statement stmnt = (Statement) proxy;

			String sqlCommand = getSqlStringForMethodInvocation(stmnt, args);

			// just note that the sqlCommand will be included in the next batch
			// execution
			batchSQLStatements.add(sqlCommand);

			Object result = method.invoke(originalStatement, args);

			return result;

		}

		// intercept calls to execute a batch of SQL statements
		if ("executeBatch".equals(method.getName()))
		{

			// retrieve the batched SQL commands.
			SQLExecutionEvent event = new SQLExecutionEvent((Statement) proxy, batchSQLStatements, method, PreparedStatement.class.isAssignableFrom(proxy.getClass()));

			for (JDBCEventListener listener : listeners)
			{
				try
				{
					listener.beforeSQLExecution(event);
				}
				catch (RuntimeException rte)
				{
					logger.error(rte);
				}
			}

			Object result = method.invoke(originalStatement, args);

			for (JDBCEventListener listener : listeners)
			{
				try
				{
					listener.afterSQLExecution(event);
				}
				catch (RuntimeException rte)
				{
					logger.error(rte);
				}
			}

			return result;

		}

		// intercept calls to set prepared statement parameters
		if (method.getName().startsWith("set") && args[0] instanceof Integer && args.length >= 2)
		{

			preparedStatementParameters.put((Integer) args[0], (args[1] == null) ? null : args[1].toString());

			return method.invoke(originalStatement, args);

		}

		// intercept calls to clear prepared statement parameters
		if ("clearParameters".equals(method.getName()))
		{

			preparedStatementParameters.clear();

			return method.invoke(originalStatement, args);

		}

		// otherwise, just invoke the method on the underlying
		// originalStatement
		return method.invoke(originalStatement, args);

	}

	private String getSqlStringForMethodInvocation(Statement stmnt, Object[] args)
	{

		String sqlString = null;

		if (PreparedStatement.class.isAssignableFrom(stmnt.getClass()))
		{
			sqlString = populatePreparedStatementSQLString((PreparedStatement) stmnt, preparedSQLTemplate);
		}
		else if (args != null && args.length > 0)
		{
			sqlString = args[0].toString();
		}
		else
		{
			sqlString = "Failed to derive an appropriate SQL statement :(";
		}

		return sqlString;

	}

	private String populatePreparedStatementSQLString(PreparedStatement preparedStmnt, String preparedSQL)
	{
		String result = preparedSQL;

		try
		{
			for (int i = 1; i <= preparedStmnt.getParameterMetaData().getParameterCount(); i++)
			{
				// lookup the parameter value
				String preparedStatementParameter = preparedStatementParameters.get(i);

				// handle missing parameter
				if (StringUtils.isBlank(preparedStatementParameter))
				{
					preparedStatementParameter = "UNKNOWN_PARAMETER_VALUE";
				}

				// formatting for some parameter types
				try
				{
					if (Arrays.asList(SQL_CHARACTER_TYPES).contains(preparedStmnt.getParameterMetaData().getParameterType(i)))
					{
						preparedStatementParameter = "'" + preparedStatementParameter + "'";
					}
				}
				catch (SQLException sqle)
				{
					// Just don't worry about formatting if proxied driver
					// throws SQLException
					logger.debug(sqle);
				}

				result = RegExpHelper.replaceFirst(result, "\\?", preparedStatementParameter);
			}
		}
		catch (SQLException e)
		{
			logger.error("Failed while populating prepared statement:", e);
		}

		return result;
	}

}
