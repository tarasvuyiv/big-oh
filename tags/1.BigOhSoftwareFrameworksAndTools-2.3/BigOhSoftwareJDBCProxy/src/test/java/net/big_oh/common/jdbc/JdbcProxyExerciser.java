package net.big_oh.common.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.big_oh.common.jdbc.JdbcDriverProxy;

import org.apache.commons.dbutils.DbUtils;
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
 * This class is not a unit test. Rather, it's just a preliminary
 * proof-of-concept class that exercises the JdbcDriverProxy class.
 */
public class JdbcProxyExerciser
{

	private static final Log logger = LogFactory.getLog(JdbcProxyExerciser.class);

	/**
	 * <p>
	 * Use main method as a driver that exercises one of the example
	 * JdbcDriverProxy implementations.
	 * </p>
	 * <p>
	 * This main method expect to interact with a Postgres database named
	 * 'JdbcProxyExerciser' that has a single table: <br/>
	 * CREATE TABLE TEST_TABLE ( TEST_COLUMN VARCHAR(80), PRIMARY KEY
	 * (TEST_COLUMN) );
	 * </p>
	 * 
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException
	{
		Class.forName("net.big_oh.common.jdbc.JdbcDriverProxy");

		String url = JdbcDriverProxy.class.getSimpleName() + ":" + "jdbc:postgresql://localhost:5432/" + JdbcProxyExerciser.class.getSimpleName();

		Connection con = DriverManager.getConnection(url, "davewingate", "ahechospechos");

		try
		{

			exerciseRegularInsert(con);

			exerciseBatchInsert(con);

			exercisePreparedBatchInsert(con);

			exerciseRegularSelect(con);

			exercisePreparedSelect(con);

		}
		finally
		{
			cleanOutTestTable(con);
		}

	}

	private static void exerciseRegularInsert(Connection con) throws SQLException
	{
		logger.info(StringUtils.center("exercise regular insert", 100, "-"));

		Statement stmt = null;
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO TEST_TABLE VALUES ( 'value1' )");
		}
		finally
		{
			DbUtils.closeQuietly(stmt);
		}
	}

	private static void exerciseBatchInsert(Connection con) throws SQLException
	{
		logger.info(StringUtils.center("exercise batch insert", 100, "-"));

		Statement stmt = null;
		try
		{
			stmt = con.createStatement();
			stmt.addBatch("INSERT INTO TEST_TABLE VALUES ( 'value2' )");
			stmt.addBatch("INSERT INTO TEST_TABLE VALUES ( 'value3' )");
			stmt.executeBatch();
		}
		finally
		{
			DbUtils.closeQuietly(stmt);
		}
	}

	private static void exercisePreparedBatchInsert(Connection con) throws SQLException
	{
		logger.info(StringUtils.center("exercise prepared batch insert", 100, "-"));

		PreparedStatement preparedStmnt = null;
		try
		{
			preparedStmnt = con.prepareStatement("INSERT INTO TEST_TABLE VALUES ( ? )");
			preparedStmnt.setString(1, "value4");
			preparedStmnt.addBatch();
			preparedStmnt.setString(1, "value5");
			preparedStmnt.addBatch();
			preparedStmnt.executeBatch();
		}
		finally
		{
			DbUtils.closeQuietly(preparedStmnt);
		}
	}

	private static void exerciseRegularSelect(Connection con) throws SQLException
	{
		logger.info(StringUtils.center("exercise regular select", 100, "-"));

		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM TEST_TABLE");
			while (rs.next())
			{
				System.out.println(rs.getString("TEST_COLUMN"));
			}
		}
		finally
		{
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(stmt);
		}

	}

	private static void exercisePreparedSelect(Connection con) throws SQLException
	{
		logger.info(StringUtils.center("exercise prepared select", 100, "-"));

		PreparedStatement preparedStmnt = null;
		ResultSet rs = null;
		try
		{
			preparedStmnt = con.prepareStatement("SELECT * FROM TEST_TABLE WHERE TEST_COLUMN = ?");
			preparedStmnt.setString(1, "value1");
			rs = preparedStmnt.executeQuery();
			while (rs.next())
			{
				System.out.println(rs.getString("TEST_COLUMN"));
			}
		}
		finally
		{
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(preparedStmnt);
		}
	}

	private static void cleanOutTestTable(Connection con) throws SQLException
	{
		logger.info(StringUtils.center("clean out the test table", 100, "-"));

		Statement stmt = null;
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("TRUNCATE TABLE TEST_TABLE");
		}
		finally
		{
			DbUtils.closeQuietly(stmt);
		}
	}

}
