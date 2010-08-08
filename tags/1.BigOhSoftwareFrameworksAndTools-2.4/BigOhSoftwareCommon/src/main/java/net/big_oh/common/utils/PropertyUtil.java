package net.big_oh.common.utils;

/*
 Copyright (c) 2008 Dave Wingate dba Big-Oh Software (www.big-oh.net)

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

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * PropertyUtil contains utility methods for creating and manipulating
 * {@link Properties} objects.
 * 
 * @author davewingate
 * 
 */
public class PropertyUtil
{

	private static final Log logger = LogFactory.getLog(PropertyUtil.class);

	/**
	 * Construct a new {@link Properties} object from the {@link ResourceBundle}
	 * identified by resourceBundleName. Prior to looking up the ResourceBundle,
	 * the resourceBundleName parameter is processed to conform to the naming
	 * conventions for ResourceBundles: <br/>
	 * <ul>
	 * <li>1) Any terminating ".properties" is stripped off.</li>
	 * <li>2) Period characters ('.') are replaced with forward slash characters
	 * ('/').</li>
	 * </ul>
	 * 
	 * @param resourceBundleName
	 *            The name of the ResourceBundle from which the new Properties
	 *            object will be constructed.
	 * @return A Properties object built from the named REsourceBundle.
	 * @throws MissingResourceException
	 */
	public static Properties loadProperties(String resourceBundleName) throws MissingResourceException
	{

		logger.info("Requested properties file from resource bundle named " + resourceBundleName + ".");

		Properties props = new Properties();

		if (resourceBundleName != null)
		{

			// correct common naming mistakes when getting resource bundles
			String requestedResourceBundleName = resourceBundleName;
			resourceBundleName = resourceBundleName.replaceAll("\\.properties$", "");
			resourceBundleName = resourceBundleName.replace('/', '.');

			if (!resourceBundleName.equals(requestedResourceBundleName))
			{
				logger.info("Translated requested resource bundle name from '" + requestedResourceBundleName + "' to '" + resourceBundleName + "'");
			}

			ResourceBundle rb = ResourceBundle.getBundle(resourceBundleName);

			for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();)
			{
				final String key = (String) keys.nextElement();
				final String value = rb.getString(key);

				props.put(key, value);
			}

		}

		return props;

	}

}
