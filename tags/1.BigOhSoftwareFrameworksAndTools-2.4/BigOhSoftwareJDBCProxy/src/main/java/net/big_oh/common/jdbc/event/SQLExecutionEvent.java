package net.big_oh.common.jdbc.event;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.EventObject;
import java.util.List;

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
 * An event class that encapsulates the details of a request to execute one or
 * more SQL statements.
 * 
 * @author davewingate
 * 
 */
public class SQLExecutionEvent extends EventObject
{

	private final Statement stmt;
	private final List<String> sqlCommands;
	private final Method executeMethod;
	private final boolean isPrepared;

	/**
	 * Construct a class that encapsulates the details of a request to execute
	 * one or more SQL statements.
	 * 
	 * @param stmt
	 *            The {@link Statement} used to execute the SQL.
	 * @param sqlCommands
	 *            The SQL commands that will be executed. Commands for
	 *            PreparedStatements will contain "?".
	 * @param executeMethod
	 *            The method from the Statement used to run the SQL.
	 * @param isPrepared
	 */
	public SQLExecutionEvent(Statement stmt, List<String> sqlCommands, Method executeMethod, boolean isPrepared)
	{
		super(stmt);

		this.stmt = stmt;
		this.sqlCommands = sqlCommands;
		this.executeMethod = executeMethod;
		this.isPrepared = isPrepared;
	}

	/**
	 * @return The {@link Statement} used to execute the SQL.
	 */
	public Statement getStmt()
	{
		return stmt;
	}

	/**
	 * @return The SQL commands that will be executed. Commands for
	 *         PreparedStatements will contain "?".
	 */
	public List<String> getSqlCommands()
	{
		return sqlCommands;
	}

	/**
	 * @return The method from the Statement used to run the SQL.
	 */
	public Method getExecuteMethod()
	{
		return executeMethod;
	}

	/**
	 * @return A boolean value that indicates whether the SQL being executed
	 *         originated form a prepared statement.
	 */
	public boolean isPrepared()
	{
		return isPrepared;
	}

}
