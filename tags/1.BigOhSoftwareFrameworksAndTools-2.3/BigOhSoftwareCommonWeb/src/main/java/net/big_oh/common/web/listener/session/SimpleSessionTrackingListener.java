package net.big_oh.common.web.listener.session;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

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
 * An <code>HttpSessionListener</code> that tracks the currently active sessions
 * for multiple web applications running in a JVM.
 * 
 * Note that the singleton member contextToSessionCollectionMap may have
 * multiple instances for multiple web applications, depending on whether each
 * web application uses the same classloader to load the
 * <code>HttpSessionListener</code> class.
 */
public class SimpleSessionTrackingListener implements HttpSessionListener
{

	private static final Log logger = LogFactory.getLog(SimpleSessionTrackingListener.class);

	// A data structure used to store the active HttpSessions.
	private static final Map<String, Map<HttpSession, Object>> contextToSessionCollectionMap = new HashMap<String, Map<HttpSession, Object>>();

	private static synchronized Map<HttpSession, Object> getSessionsCollectionForServletContext(ServletContext servletContext)
	{
		String servletContextKey = getKeyForServletContext(servletContext);

		Map<HttpSession, Object> sessions = contextToSessionCollectionMap.get(servletContextKey);

		if (sessions == null)
		{
			// Sessions map must be synchronized because multiple threads will
			// add/remove sessions concurrently.
			//
			// Sessions map is an instance of WeakHashMap so that this data
			// structure will never prevent the Session from being garbage
			// collected.
			sessions = Collections.synchronizedMap(new WeakHashMap<HttpSession, Object>());
			contextToSessionCollectionMap.put(servletContextKey, sessions);
		}

		return sessions;
	}

	private static String getKeyForServletContext(ServletContext servletContext)
	{
		if (servletContext.getServletContextName() == null || "".equals(servletContext.getServletContextName()))
		{
			logger.warn("No servletContextName defined.");
			return "undefined";
		}
		else
		{
			return servletContext.getServletContextName();
		}
	}

	/**
	 * 
	 * @param servletContext
	 *            The <code>ServletContext</code> for a web application.
	 * @return Returns a set of all <code>HttpSession</code> objects that are
	 *         active in the web application.
	 */
	// TODO DSW Need better documentation here
	public static synchronized Set<HttpSession> getSessionsForContext(ServletContext servletContext)
	{
		return new HashSet<HttpSession>(getSessionsCollectionForServletContext(servletContext).keySet());
	}

	/**
	 * 
	 * @return Returns a set of all <code>HttpSession</code> objects observed by
	 *         web applications that used the same classloader to instantiate
	 *         the <code>HttpSessionListener</code> listener.
	 */
	// TODO DSW Need better documentation here
	public static synchronized Set<HttpSession> getSessionsForClassloader()
	{
		Set<HttpSession> sessionsForJvm = new HashSet<HttpSession>();

		for (String servletContextKey : contextToSessionCollectionMap.keySet())
		{
			Map<HttpSession, Object> sessionsMapForServletContext = contextToSessionCollectionMap.get(servletContextKey);
			sessionsForJvm.addAll(sessionsMapForServletContext.keySet());
		}

		return sessionsForJvm;
	}

	public void sessionCreated(HttpSessionEvent se)
	{
		Map<HttpSession, Object> sessions = getSessionsCollectionForServletContext(se.getSession().getServletContext());

		sessions.put(se.getSession(), getObjectToAssociateWithSession(se.getSession()));

		logger.info("Registered the creation of a new HttpSession: " + se.getSession().getId());
	}

	/**
	 * A convenience method that allows you to associate any object with a newly
	 * created <code>HttpSession</code>.
	 * 
	 * @param session
	 * @return Any object that suits your needs.
	 */
	public Object getObjectToAssociateWithSession(HttpSession session)
	{
		// Override as desired in child classes
		return new Object();
	}

	public void sessionDestroyed(HttpSessionEvent se)
	{
		Map<HttpSession, Object> sessions = getSessionsCollectionForServletContext(se.getSession().getServletContext());

		sessions.remove(se.getSession());

		logger.info("Registered the destruction of an HttpSession: " + se.getSession().getId());
	}

}
