package net.big_oh.common.web;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
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
 * A collection of static helper methods that facilitate inspection and
 * manipulation of objects from the javax.servlet.* library.
 */
public class WebUtil
{

	private static final Log logger = LogFactory.getLog(WebUtil.class);

	/**
	 * Calculate an <b>approximation</b> of the memory consumed by all objects
	 * stored in the {@link HttpSession}. The estimate will often be greater
	 * than the actual value because of "double counting" objects that appear in
	 * the object graphs of multiple session attributes.
	 * 
	 * @param session
	 *            An HttpSession object from any web application.
	 * @return An <b>approximation</b> of the memory consumed by objects stored
	 *         in the HttpSession object.
	 */
	public static long approximateSessionSizeInBytes(HttpSession session)
	{
		// Strategy used here is to sum memory consumed by all key/value pairs
		// stored in the session

		long sizeInBytes = 0;

		Map<String, Integer> attributeNameMap = getSessionAttributeNameToApproxSizeInBytesMap(session);
		for (String attributeName : attributeNameMap.keySet())
		{
			sizeInBytes += attributeNameMap.get(attributeName);
		}

		// Map<String, Integer> attributeMap =
		// getSessionAttributeNameToApproxSizeInBytesMap(session);
		for (String attributeName : attributeNameMap.keySet())
		{
			try
			{
				sizeInBytes += approximateObjectSize(session.getAttribute(attributeName));
			}
			catch (IOException ioe)
			{
				logger.error("Failed to approximate size of value associated with session attribute name: " + attributeName.getClass().getName(), ioe);
			}
		}

		return sizeInBytes;

	}

	/**
	 * 
	 * Calculate an <b>approximation</b> of the memory consumed by the objects
	 * stored under each attribute of a user's {@link HttpSession}. The estimate
	 * will often be greater than the actual value because of "double counting"
	 * objects that appear multiple times in the attribute value's object graph.
	 * 
	 * @param session
	 *            An HttpSession object from any web application.
	 * @return An <b>approximation</b> of the memory consumed for each attribute
	 *         <b>name</b> in the session.
	 */
	public static Map<String, Integer> getSessionAttributeNameToApproxSizeInBytesMap(HttpSession session)
	{

		// Use an IdentityHashMap because we don't want to miss distinct objects
		// that are equivalent according to equals(..) method.
		Map<String, Integer> sessionAttributeNameToApproxSizeInBytesMap = new IdentityHashMap<String, Integer>();

		Enumeration<?> enumeration = session.getAttributeNames();
		while (enumeration.hasMoreElements())
		{
			String attributeName = (String) enumeration.nextElement();
			session.getAttribute(attributeName);

			try
			{
				sessionAttributeNameToApproxSizeInBytesMap.put(attributeName, new Integer(approximateObjectSize(attributeName)));
			}
			catch (IOException ioe)
			{
				logger.error("Failed to approximate size of session attribute name: " + attributeName.getClass().getName(), ioe);
				sessionAttributeNameToApproxSizeInBytesMap.put(attributeName, new Integer(0));
			}

		}

		return sessionAttributeNameToApproxSizeInBytesMap;

	}

	private static int approximateObjectSize(Object obj) throws IOException
	{
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);

		try
		{

			int sizeBefore = baos.size();
			oos.writeObject(obj);
			logger.debug("Approximated size of a " + obj.getClass().getName() + " object as " + (baos.size() - sizeBefore) + " bytes.");

			return baos.size();

		}
		catch (NotSerializableException e)
		{
			logger.warn("Failed to approximate size because of a non-serializable, non-transient object in the object graph for " + obj.getClass().getName(), e);
			return 0;
		}
		finally
		{
			IOUtils.closeQuietly(baos);
			IOUtils.closeQuietly(oos);
		}

	}

	/**
	 * A convenience method that counts number of attributes stored in a user's
	 * {@link HttpSession}.
	 * 
	 * @param session
	 *            An HttpSession object from any web application.
	 * @return A count of the number of attributes stored in an HttpSession.
	 */
	public static int countAttributesInSession(HttpSession session)
	{

		int count = 0;

		Enumeration<?> attributeEnum = session.getAttributeNames();
		while (attributeEnum.hasMoreElements())
		{
			attributeEnum.nextElement();
			count++;
		}

		return count;

	}

	/**
	 * A convenience method that performs a reverse lookup to find all names
	 * under which an attribute is stored in a user's {@link HttpSession}.
	 * 
	 * @param session
	 *            An HttpSession object from any web application.
	 * @param sessionAttribute
	 *            Attribute name of interest.
	 * @return A seet of all attribute names under which the sessionAttribute
	 *         parameter is stored in the session.
	 */
	public static Set<String> getNamesForSessionAttribute(HttpSession session, Object sessionAttribute)
	{

		Set<String> attributeNames = new HashSet<String>();

		Enumeration<?> attributeEnum = session.getAttributeNames();
		while (attributeEnum.hasMoreElements())
		{
			String attributeName = (String) attributeEnum.nextElement();
			if (sessionAttribute == session.getAttribute(attributeName))
			{
				attributeNames.add(attributeName);
			}

		}

		return attributeNames;

	}

}
