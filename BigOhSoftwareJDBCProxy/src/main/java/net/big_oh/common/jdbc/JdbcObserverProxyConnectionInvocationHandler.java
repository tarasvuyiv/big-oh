package net.big_oh.common.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.big_oh.common.jdbc.event.StatementInstantiationEvent;
import net.big_oh.common.jdbc.event.listener.JDBCEventListener;

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
 * The invocation handler for a {@link Proxy} that stands in for a Driver-created SQL {@link Connection} so that
 * all Statements created on the Connection can be observed.
 */
class JdbcObserverProxyConnectionInvocationHandler implements InvocationHandler
{

	private static final Log logger = LogFactory.getLog(JdbcObserverProxyConnectionInvocationHandler.class);

	private final Connection originalConnection;

	private final List<JDBCEventListener> listeners;

	JdbcObserverProxyConnectionInvocationHandler(Connection originalConnection, List<JDBCEventListener> listeners)
	{
		this.originalConnection = originalConnection;

		// Create a new list of listeners to side-step the possibility of
		// ConcurrentModificationException
		this.listeners = Collections.unmodifiableList(new ArrayList<JDBCEventListener>(listeners));
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{

		// intercept calls that create statements
		if (Statement.class.isAssignableFrom(method.getReturnType()))
		{

			StatementInstantiationEvent event = new StatementInstantiationEvent((Connection) proxy);

			for (JDBCEventListener listener : listeners)
			{
				try
				{
					listener.statementRequested(event);
				}
				catch (RuntimeException rte)
				{
					logger.error(rte);
				}
			}

			Statement newStatement = (Statement) method.invoke(originalConnection, args);

			// Create proxy for the returned statement object
			String hardCodedSqlForStatement = (newStatement instanceof PreparedStatement && args != null && args.length > 0 && args[0] instanceof String) ? args[0].toString() : null;
			boolean newStatementInterfacesContainsMethodReturnTypeClass = Arrays.asList(newStatement.getClass().getInterfaces()).contains(method.getReturnType());
			Class<?>[] interfaces = new Class<?>[(newStatement.getClass().getInterfaces().length + ((newStatementInterfacesContainsMethodReturnTypeClass) ? 0 : 1))];
			System.arraycopy(newStatement.getClass().getInterfaces(), 0, interfaces, 0, newStatement.getClass().getInterfaces().length);
			if (!newStatementInterfacesContainsMethodReturnTypeClass)
			{
				interfaces[newStatement.getClass().getInterfaces().length] = method.getReturnType();
			}
			Statement statementProxy = (Statement) Proxy.newProxyInstance(this.getClass().getClassLoader(), interfaces, new JdbcObserverProxyStatementInvocationHandler(newStatement, listeners, hardCodedSqlForStatement));

			for (JDBCEventListener listener : listeners)
			{
				try
				{
					listener.statementInstantiated(event, statementProxy);
				}
				catch (RuntimeException rte)
				{
					logger.error(rte);
				}
			}

			return statementProxy;
		}

		// otherwise, just invoke the method on the underlying
		// originalConnection
		return method.invoke(originalConnection, args);

	}

}
