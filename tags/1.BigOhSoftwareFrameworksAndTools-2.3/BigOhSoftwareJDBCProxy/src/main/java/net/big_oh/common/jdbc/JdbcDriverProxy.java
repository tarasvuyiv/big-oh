package net.big_oh.common.jdbc;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.big_oh.common.jdbc.event.ConnectionInstantiationEvent;
import net.big_oh.common.jdbc.event.listener.JDBCEventListener;
import net.big_oh.common.utils.CollectionsUtil;
import net.big_oh.common.utils.PropertyUtil;

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
 * <p>
 * A proxy class that intercepts JDBC {@link Driver} calls in order to expose
 * those calls as observable events.
 * </p>
 * <p>
 * To avoid the proliferation of child classes, this class provides only a
 * no-arg constructor that reads a list of listener classes from
 * jdbcdriverproxy.properties.
 * </p>
 */
public final class JdbcDriverProxy implements Driver
{

	private static final Log logger = LogFactory.getLog(JdbcDriverProxy.class);

	// register the driver with DriverManager
	static
	{
		try
		{
			DriverManager.registerDriver(new JdbcDriverProxy());
		}
		catch (SQLException e)
		{
			logger.error(e);
		}
	}

	private final List<JDBCEventListener> listeners;

	private Driver mostRecentDelegateDriver = null;

	/**
	 * Constructs a JdbcDriverProxy using the listeners defined in
	 * jdbcdriverproxy.properties.
	 */
	public JdbcDriverProxy()
	{
		this(buildListenersListFromPropertiesFile());
	}

	private static final List<? extends JDBCEventListener> buildListenersListFromPropertiesFile()
	{
		Properties props = PropertyUtil.loadProperties(JdbcDriverProxy.class.getSimpleName().toLowerCase());

		List<JDBCEventListener> listeners = new ArrayList<JDBCEventListener>();

		String[] listenerClassNames = props.getProperty(JDBCEventListener.class.getSimpleName() + "s", "").split(",");

		for (String listenerClassName : listenerClassNames)
		{

			if (!StringUtils.isBlank(listenerClassName))
			{

				try
				{
					Class<?> listenerClass = Class.forName(listenerClassName);
					JDBCEventListener listener = (JDBCEventListener) listenerClass.newInstance();
					listeners.add(listener);
					logger.info("Registered a new JDBC event listener: " + listenerClass.toString());
				}
				catch (ClassNotFoundException cnfe)
				{
					logger.error("Failed to find a JDBC event listener class named '" + listenerClassName + "'", cnfe);
				}
				catch (InstantiationException e)
				{
					logger.error("Failed to instantiate a JDBC event listener class named '" + listenerClassName + "'", e);
				}
				catch (IllegalAccessException e)
				{
					logger.error(e);
				}

			}

		}

		return listeners;

	}

	/**
	 * Constructs a JdbcDriverProxy from a list of JDBCEventListener object.
	 * 
	 * @param listeners
	 */
	private JdbcDriverProxy(List<? extends JDBCEventListener> listeners)
	{

		// Create a new list of listeners to side-step the possibility of
		// ConcurrentModificationException
		this.listeners = Collections.unmodifiableList(new ArrayList<JDBCEventListener>(listeners));

		for (String driverClassName : getDriverClassNamesToAutoLoad())
		{
			logger.debug("Attempting to auto-load delegate Driver " + driverClassName + ".");
			try
			{
				Class.forName(driverClassName);
			}
			catch (ClassNotFoundException cnfe)
			{
				logger.warn("Failed to auto-load delegate Driver " + driverClassName + " because the class could not be found.");
			}
		}

	}

	/**
	 * 
	 * @return The prefix that this ProxyDriver will expect to be pre-pended to
	 *         URL connection strings.
	 */
	private final String getJdbcObserverUrlPrefix()
	{
		return this.getClass().getSimpleName() + ":";
	}

	/**
	 * Translates the URL expected by this ProxyDriver to a URL expected by the
	 * delegateDriver.
	 * 
	 * @param url
	 *            A URL string that begins with the <code>String</code> returned
	 *            by <code>getJdbcObserverUrlPrefix()</code>
	 * @return
	 */
	private final String stripJdbcObserverPrefixFromUrl(String url)
	{
		if (!url.startsWith(getJdbcObserverUrlPrefix()))
		{
			return url;
		}
		else
		{
			return url.substring(getJdbcObserverUrlPrefix().length());
		}
	}

	/**
	 * 
	 * @return A <code>Set</code> of delegate driver names that you'd like to
	 *         use without having to explicitly call
	 *         <code>Class.forName(com.somepackage.SomeDriver);</code>.
	 */
	private Set<String> getDriverClassNamesToAutoLoad()
	{
		return JdbcDriverProxyHelper.getStandardDriversToAutoLoad();
	}

	private final Driver lookupDelegateDriver(String url) throws SQLException
	{
		if (!url.startsWith(getJdbcObserverUrlPrefix()))
		{
			logger.debug("Requested url '" + url + "' is not recognized by " + this.getClass().getName());
			return null;
		}

		url = stripJdbcObserverPrefixFromUrl(url);

		for (Driver delegateDriverCandidate : CollectionsUtil.toIterable(DriverManager.getDrivers()))
		{
			if (delegateDriverCandidate != this && delegateDriverCandidate.acceptsURL(url))
			{
				logger.debug("Selected driver " + delegateDriverCandidate.getClass().getName() + " to service url '" + url + "'");
				registerMostRecentDelegateDriver(mostRecentDelegateDriver);
				return delegateDriverCandidate;
			}
		}

		logger.warn("Failed to find an appropriate delegate driver for url '" + url + "'");
		logger.warn("Maybe you need to load the delegate Driver class by calling 'Class.forName(com.somepackage.SomeDriver);' or by updating the overridden getDriverClassNamesToAutoLoad() method ?");
		return null;

	}

	private final void registerMostRecentDelegateDriver(Driver driver)
	{
		if (this.mostRecentDelegateDriver != null && !this.mostRecentDelegateDriver.getClass().isAssignableFrom(driver.getClass()))
		{
			logger.warn("Encountered multiple delegate driver clas types (" + mostRecentDelegateDriver.getClass().getName() + " & " + driver.getClass().getName()
					+ ").  Using this proxy with multiple delegate types can cause surprising results to be returned by the getMajorVersion(), getMinorVersion() & jdbcCompliant() methods.");
		}
		this.mostRecentDelegateDriver = driver;
	}

	public boolean acceptsURL(String url) throws SQLException
	{
		Driver delegateDriver = lookupDelegateDriver(url);
		return (delegateDriver == null) ? false : delegateDriver.acceptsURL(url);
	}

	public Connection connect(String url, Properties connectionProperties) throws SQLException
	{
		Driver delegateDriver = lookupDelegateDriver(url);

		if (delegateDriver == null)
		{
			// return null per the API guidelines
			return null;
		}

		url = stripJdbcObserverPrefixFromUrl(url);

		ConnectionInstantiationEvent connectionInstantiationEvent = new ConnectionInstantiationEvent(this, delegateDriver, url, connectionProperties);

		for (JDBCEventListener listener : listeners)
		{
			try
			{
				listener.connectionRequested(connectionInstantiationEvent);
			}
			catch (RuntimeException rte)
			{
				logger.error(rte);
			}
		}

		Connection newConnection = delegateDriver.connect(url, connectionProperties);

		if (newConnection == null)
		{
			throw new SQLException("Failed to connect to url '" + url + "' using delegate driver " + delegateDriver.getClass().getName());
		}

		// Create a proxy for the returned connection
		boolean newConnectionInterfacesContainsConnection = Arrays.asList(newConnection.getClass().getInterfaces()).contains(Connection.class);
		Class<?>[] interfaces = new Class<?>[(newConnection.getClass().getInterfaces().length + ((newConnectionInterfacesContainsConnection) ? 0 : 1))];
		System.arraycopy(newConnection.getClass().getInterfaces(), 0, interfaces, 0, newConnection.getClass().getInterfaces().length);
		if (!newConnectionInterfacesContainsConnection)
		{
			interfaces[newConnection.getClass().getInterfaces().length] = Connection.class;
		}
		Connection connectionProxy = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(), interfaces, new JdbcObserverProxyConnectionInvocationHandler(newConnection, listeners));

		for (JDBCEventListener listener : listeners)
		{
			try
			{
				listener.connectionInstantiated(connectionInstantiationEvent, connectionProxy);
			}
			catch (RuntimeException rte)
			{
				logger.error(rte);
			}
		}

		return connectionProxy;
	}

	public int getMajorVersion()
	{
		return (mostRecentDelegateDriver == null) ? 1 : mostRecentDelegateDriver.getMajorVersion();
	}

	public int getMinorVersion()
	{
		return (mostRecentDelegateDriver == null) ? 0 : mostRecentDelegateDriver.getMinorVersion();
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException
	{
		Driver d = lookupDelegateDriver(url);
		return (d == null) ? new DriverPropertyInfo[0] : d.getPropertyInfo(url, info);
	}

	public boolean jdbcCompliant()
	{
		return mostRecentDelegateDriver != null && mostRecentDelegateDriver.jdbcCompliant();
	}

}
