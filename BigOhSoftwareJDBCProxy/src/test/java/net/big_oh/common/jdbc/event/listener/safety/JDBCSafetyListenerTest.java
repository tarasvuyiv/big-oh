package net.big_oh.common.jdbc.event.listener.safety;

import java.sql.SQLException;
import java.util.Collections;

import net.big_oh.common.jdbc.DummyStatement;
import net.big_oh.common.jdbc.event.SQLExecutionEvent;
import net.big_oh.common.jdbc.event.listener.safety.JDBCSafetyListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * 
 * A unit test that exercises the JBBCSafetyListener.
 * 
 * @author davewingate
 * 
 */
public class JDBCSafetyListenerTest
{

	private JDBCSafetyListener listener;

	@Before
	public void setUp() throws Exception
	{
		this.listener = new JDBCSafetyListener();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test(expected = SQLException.class)
	public void testDangerousDeletePrevented() throws SQLException
	{
		listener.beforeSQLExecution(new SQLExecutionEvent(new DummyStatement(), Collections.singletonList("DELETE FROM TEST_TABLE"), null, false));
	}

	@Test
	public void testTolerableDeleteAllowed() throws SQLException
	{
		listener.beforeSQLExecution(new SQLExecutionEvent(new DummyStatement(), Collections.singletonList("DELETE FROM TEST_TABLE WHERE TEST_COLUMN = 'some_arbitrary_value'"), null, false));
	}
	
	@Test
	public void testNonSQLInjectionAllowed() throws SQLException {
		
		String sqlString1 = "DELETE FROM TEST_TABLE WHERE TEST_COLUMN = 'some_arbitrary_value'; -- This query deletes an entry from TEST_TABLE"; 
		listener.beforeSQLExecution(new SQLExecutionEvent(new DummyStatement(), Collections.singletonList(sqlString1), null, false));
		
		String sqlString2 = "DELETE FROM TEST_TABLE WHERE TEST_COLUMN = 'some_arbitrary_value';--This query deletes an entry from TEST_TABLE"; 
		listener.beforeSQLExecution(new SQLExecutionEvent(new DummyStatement(), Collections.singletonList(sqlString2), null, false));
	}
	
	@Test(expected = SQLException.class)
	public void testSQLInjectionPrevented1() throws SQLException {
		
		String sqlInjectionAttack = "hack'; DROP TABLE SOME_TABLE_TO_DROP;  -- ";
		String sqlString = "DELETE FROM TEST_TABLE WHERE TEST_COLUMN = '" + sqlInjectionAttack + "' OR TEST_COLUMN IS NULL;"; 
		listener.beforeSQLExecution(new SQLExecutionEvent(new DummyStatement(), Collections.singletonList(sqlString), null, false));
	}
	
	@Test(expected = SQLException.class)
	public void testSQLInjectionPrevented2() throws SQLException {
		
		String sqlInjectionAttack = "hack'; DROP TABLE SOME_TABLE_TO_DROP;--";
		String sqlString = "DELETE FROM TEST_TABLE WHERE TEST_COLUMN = '" + sqlInjectionAttack + "' OR TEST_COLUMN IS NULL;"; 
		listener.beforeSQLExecution(new SQLExecutionEvent(new DummyStatement(), Collections.singletonList(sqlString), null, false));
	}
	
	@Test(expected = SQLException.class)
	public void testSQLInjectionPrevented3() throws SQLException {
		
		String sqlInjectionAttack = "hack'; DROP TABLE SOME_TABLE_TO_DROP -- ";
		String sqlString = "DELETE FROM TEST_TABLE WHERE TEST_COLUMN = '" + sqlInjectionAttack + "' OR TEST_COLUMN IS NULL;"; 
		listener.beforeSQLExecution(new SQLExecutionEvent(new DummyStatement(), Collections.singletonList(sqlString), null, false));
	}

}
