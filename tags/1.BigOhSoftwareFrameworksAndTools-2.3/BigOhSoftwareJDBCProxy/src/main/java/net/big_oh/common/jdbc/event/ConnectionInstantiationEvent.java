package net.big_oh.common.jdbc.event;

import java.sql.Driver;
import java.util.EventObject;
import java.util.Properties;

import net.big_oh.common.jdbc.JdbcDriverProxy;


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
 * An event class that encapsulates the details of a request to instantiate a
 * new database {@link java.sql.Connection}.
 * 
 * @author davewingate
 * 
 */
public class ConnectionInstantiationEvent extends EventObject
{

	private final Driver proxyDriver;
	private final Driver delegateDriver;
	private final String connectionUrl;
	private final Properties connectionProperties;

	/**
	 * Construct a class that encapsulates the details of a request to
	 * instantiate a new database {@link java.sql.Connection}.
	 * 
	 * @param proxyDriver
	 *            The instance of {@link JdbcDriverProxy} from which the new
	 *            {@link java.sql.Connection} was requested.
	 * @param delegateDriver
	 *            The {@link java.sql.Driver} used "under the hood" to create
	 *            the new Connection.
	 * @param connectionUrl
	 *            The URL of the database to be connected to. This is the URL
	 *            that the delegateDriver uses to create a Connection.
	 * @param connectionProperties
	 *            The connection properties used by the delegateDriver to create
	 *            a Connection.
	 */
	public ConnectionInstantiationEvent(Driver proxyDriver, Driver delegateDriver, String connectionUrl, Properties connectionProperties)
	{
		super(proxyDriver);

		this.proxyDriver = proxyDriver;
		this.delegateDriver = delegateDriver;
		this.connectionUrl = connectionUrl;
		this.connectionProperties = connectionProperties;
	}

	/**
	 * @return The instance of {@link JdbcDriverProxy} from which the new
	 *         {@link java.sql.Connection} was requested.
	 */
	public Driver getProxyDriver()
	{
		return proxyDriver;
	}

	/**
	 * @return The {@link java.sql.Driver} used "under the hood" to create the
	 *         new Connection.
	 */
	public Driver getDelegateDriver()
	{
		return delegateDriver;
	}

	/**
	 * @return The URL of the database to be connected to. This is the URL that
	 *         the delegateDriver uses to create a Connection.
	 */
	public String getConnectionUrl()
	{
		return connectionUrl;
	}

	/**
	 * @return The connection properties used by the delegateDriver to create a
	 *         Connection.
	 */
	public Properties getConnectionProperties()
	{
		return connectionProperties;
	}

}
