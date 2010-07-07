package net.big_oh.common.jdbc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
 * A class that defines helper methods that simplify the definition of a
 * concrete JdbcDriverProxy class.
 */
public class JdbcDriverProxyHelper
{

	private static Set<String> standardDriversToAutoLoad = new HashSet<String>();
	static
	{
		standardDriversToAutoLoad.add("org.postgresql.Driver");
		standardDriversToAutoLoad.add("com.ibm.db2.jcc.DB2Driver");
		standardDriversToAutoLoad.add("com.ibm.as400.access.AS400JDBCDriver");
		standardDriversToAutoLoad.add("com.microsoft.jdbc.sqlserver.SQLServerDriver");
		standardDriversToAutoLoad.add("oracle.jdbc.driver.OracleDriver");
		standardDriversToAutoLoad.add("org.hsqldb.jdbcDriver");
	}

	/**
	 * @return A set of strings representing the most commonly encountered JDBC drivers. 
	 */
	public static Set<String> getStandardDriversToAutoLoad()
	{
		return Collections.unmodifiableSet(standardDriversToAutoLoad);
	}

}
