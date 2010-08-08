package net.big_oh.common.jdbc.event.listener.safety;

import java.sql.SQLException;

import net.big_oh.common.jdbc.event.SQLExecutionEvent;
import net.big_oh.common.jdbc.event.listener.JDBCEventAdapter;
import net.big_oh.common.utils.RegExpHelper;


/**
 * 
 * A JDBCEventListener that prevents the execution of dangerous SQL by throwing
 * an instance of SQLException.
 * 
 * @author davewingate
 * @version Aug 22, 2009
 */
public class JDBCSafetyListener extends JDBCEventAdapter
{

	@Override
	public void beforeSQLExecution(SQLExecutionEvent event) throws SQLException
	{
		
		for (String sqlCommand : event.getSqlCommands())
		{
			
			// prevent execution of DELETE statements with no WHERE clause
			if (RegExpHelper.matches(sqlCommand.toUpperCase(), "^DELETE\\s.*") && !RegExpHelper.contains(sqlCommand.toUpperCase(), "WHERE"))
			{
				throw new SQLException("Cowardly refusing to excute a DELETE statement that has no WHERE clause.  Use the TRUNCATE command if you really want to remove all data from a table?");
			}
			
			// prevent execution of statements that look suspiciously like an SQL injection attack via inserted ";"
			if (RegExpHelper.contains(sqlCommand, ";\\s*\\w+")) {
				throw new SQLException("Cowardly refusing to excute a SQL statement that smells suspiciously like an SQL injection attack.");
			}
			
		}
	
	}

}
