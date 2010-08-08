package net.big_oh.common.jdbc.event.listener.logger;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.big_oh.common.jdbc.event.ConnectionInstantiationEvent;
import net.big_oh.common.jdbc.event.SQLExecutionEvent;
import net.big_oh.common.jdbc.event.StatementInstantiationEvent;
import net.big_oh.common.jdbc.event.listener.JDBCEventListener;
import net.big_oh.common.utils.CollectionsUtil;
import net.big_oh.common.utils.SystemPropertyUtil;


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
 * A an abstract implementation of the JDBCEventListener interface that logs all
 * JDBC events. The responsibility of defining the logging method is delegated
 * to extending child classes.
 */
public abstract class JDBCLoggingListener implements JDBCEventListener
{

	private final boolean logBeforeEvent;
	private final boolean logAfterEvent;

	public JDBCLoggingListener(boolean logBeforeEvent, boolean logAfterEvent)
	{
		super();
		this.logBeforeEvent = logBeforeEvent;
		this.logAfterEvent = logAfterEvent;
	}

	public void connectionRequested(ConnectionInstantiationEvent event)
	{
		if (logBeforeEvent)
		{
			debug("About to connect to url '" + event.getConnectionUrl() + "' using the " + event.getDelegateDriver().getClass().getName() + " driver.");
		}
	}

	public void connectionInstantiated(ConnectionInstantiationEvent event, Connection newConnection)
	{
		if (logAfterEvent)
		{
			debug("Connected to url '" + event.getConnectionUrl() + "' using the " + event.getDelegateDriver().getClass().getName() + " driver.  New Connection: " + newConnection.toString());
		}
	}

	public void statementRequested(StatementInstantiationEvent event)
	{
		if (logBeforeEvent)
		{
			debug("About to create a new statement.");
		}
	}

	public void statementInstantiated(StatementInstantiationEvent event, Statement newStatement)
	{
		if (logAfterEvent)
		{
			debug("Created a new statement: " + newStatement.toString());
		}
	}

	public void beforeSQLExecution(SQLExecutionEvent event)
	{
		if (logBeforeEvent)
		{
			info("About to use the " + event.getExecuteMethod().getName() + " method to execute one or more" + ((event.isPrepared()) ? " prepared" : "") + " SQL commands using Satement " + getIdentifierForStatement(event.getStmt()) + ":" + SystemPropertyUtil.getSystemLineSeparator()
					+ CollectionsUtil.toXsvString(getLogFriendlySQLCommands(event), SystemPropertyUtil.getSystemLineSeparator()));
		}
	}

	public void afterSQLExecution(SQLExecutionEvent event)
	{
		if (logAfterEvent)
		{
			info("Used the " + event.getExecuteMethod().getName() + " method to execute one or more" + ((event.isPrepared()) ? " prepared" : "") + " SQL commands using Statement " + getIdentifierForStatement(event.getStmt()) + ":" + SystemPropertyUtil.getSystemLineSeparator()
					+ CollectionsUtil.toXsvString(getLogFriendlySQLCommands(event), SystemPropertyUtil.getSystemLineSeparator()));
		}
	}

	private int getIdentifierForStatement(Statement stmnt)
	{
		return stmnt.hashCode();
	}

	private List<String> getLogFriendlySQLCommands(SQLExecutionEvent event)
	{
		List<String> logFriendlySQLCommands = new ArrayList<String>();

		for (String sqlCommand : event.getSqlCommands())
		{
			StringBuffer sb = new StringBuffer();
			sb.append("\t");
			sb.append(sqlCommand);
			sb.append(";");
			logFriendlySQLCommands.add(sb.toString());
		}

		return logFriendlySQLCommands;
	}

	/**
	 * An abstract method that implements the logging of SQL events at a debug
	 * level. Those statements might,for example, be logged to the console or to
	 * a File.
	 * 
	 * @param msg
	 */
	protected abstract void debug(String msg);

	/**
	 * An abstract method that implements the logging of SQL events at an info
	 * level. Those statements might,for example, be logged to the console or to
	 * a File.
	 * 
	 * @param msg
	 */
	protected abstract void info(String msg);

}
