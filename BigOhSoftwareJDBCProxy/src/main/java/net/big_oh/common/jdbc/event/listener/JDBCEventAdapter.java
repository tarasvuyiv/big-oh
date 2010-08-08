package net.big_oh.common.jdbc.event.listener;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.big_oh.common.jdbc.event.ConnectionInstantiationEvent;
import net.big_oh.common.jdbc.event.SQLExecutionEvent;
import net.big_oh.common.jdbc.event.StatementInstantiationEvent;


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
 * A convenient implementation of the {@link JDBCEventListener} interface. All
 * methods from that interface are implemented as no-ops. Extending this adapter
 * class reduces the work required to implement a new JDBCEventListener that is
 * interested in only one type of event.
 * 
 * @author davewingate
 * 
 */
public abstract class JDBCEventAdapter implements JDBCEventListener
{

	public void connectionRequested(ConnectionInstantiationEvent event)
	{
	}

	public void connectionInstantiated(ConnectionInstantiationEvent event, Connection newConnection)
	{
	}

	public void statementRequested(StatementInstantiationEvent event)
	{
	}

	public void statementInstantiated(StatementInstantiationEvent event, Statement newStatement)
	{
	}

	public void beforeSQLExecution(SQLExecutionEvent event) throws SQLException
	{
	}

	public void afterSQLExecution(SQLExecutionEvent event)
	{
	}

}
