package net.big_oh.postoffice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import net.big_oh.postoffice.IPostOfficeService;
import net.big_oh.postoffice.PostOfficeServiceFactory;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/*
 Copyright (c) 2010 Dave Wingate dba Big-Oh Software (www.big-oh.net)

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
 * A unit test of the {@link PostOfficeServiceFactory} class.
 * 
 * @author dwingate
 * @version Jun 2, 2010
 */
public class PostOfficeServiceFactoryUnitTest
{

	/**
	 * Test method for
	 * {@link net.big_oh.postoffice.PostOfficeServiceFactory#getInstance()}
	 * .
	 */
	@Test
	public void testGetInstance()
	{
		IPostOfficeService serviceFromGetInstance = PostOfficeServiceFactory.getInstance();
		IPostOfficeService serviceFromExplicitPropsRead = PostOfficeServiceFactory.getInstance(PostOfficeServiceFactory.readDefaultPostOfficeProperties());

		// expect service objects to be different objects that are logically
		// equivalent
		assertFalse(serviceFromGetInstance == serviceFromExplicitPropsRead);
		assertEquals(serviceFromExplicitPropsRead, serviceFromGetInstance);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.postoffice.PostOfficeServiceFactory#getInstance(java.util.Properties)}
	 * .
	 */
	@Test
	public void testGetInstanceProperties()
	{

		final boolean testAuthenticatewithTlsValue = true;
		final boolean testDebugValue = true;
		final boolean testTransportWithSslValue = true;
		final String testFromValue = "foo@bar.com";
		final String testHostValue = "smtp.someserver.net";
		final int testPortValue = 28374;
		final String testSmtpPasswordValue = "super secret";
		final String testSmtpUserNameValue = "jsmith";

		final Properties testProperties = new Properties();
		testProperties.setProperty(PostOfficeServiceFactory.PROPERTY_KEY_AUTHENTICATE_WITH_TLS, Boolean.toString(testAuthenticatewithTlsValue));
		testProperties.setProperty(PostOfficeServiceFactory.PROPERTY_KEY_DEBUG, Boolean.toString(testDebugValue));
		testProperties.setProperty(PostOfficeServiceFactory.PROPERTY_KEY_TRANSPORT_WITH_SSL, Boolean.toString(testTransportWithSslValue));
		testProperties.setProperty(PostOfficeServiceFactory.PROPERTY_KEY_FROM, testFromValue);
		testProperties.setProperty(PostOfficeServiceFactory.PROPERTY_KEY_HOST, testHostValue);
		testProperties.setProperty(PostOfficeServiceFactory.PROPERTY_KEY_PORT, Integer.toString(testPortValue));
		testProperties.setProperty(PostOfficeServiceFactory.PROPERTY_KEY_SMTP_PASSWORD, testSmtpPasswordValue);
		testProperties.setProperty(PostOfficeServiceFactory.PROPERTY_KEY_SMTP_USER_NAME, testSmtpUserNameValue);

		final IPostOfficeService poService = PostOfficeServiceFactory.getInstance(testProperties);

		assertEquals(testAuthenticatewithTlsValue, poService.isAuthenticateWithTls());
		assertEquals(testDebugValue, poService.isDebug());
		assertEquals(testTransportWithSslValue, poService.isTransportWithSsl());
		assertEquals(testFromValue, poService.getSendersFromAddress());
		assertEquals(testSmtpPasswordValue, poService.getSmtpPassword());
		assertEquals(testHostValue, poService.getSmtpServerHost());
		assertEquals(testPortValue, poService.getSmtpServerPort());
		assertEquals(testSmtpUserNameValue, poService.getSmtpUserName());

	}

	/**
	 * Test method for
	 * {@link net.big_oh.postoffice.PostOfficeServiceFactory#getInstance(java.util.Properties)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testGetInstanceProperties_Illegal_NullProperties()
	{
		PostOfficeServiceFactory.getInstance(null);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.postoffice.PostOfficeServiceFactory#getInstance(java.util.Properties)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetInstanceProperties_Illegal_NoHost()
	{
		Properties props = PostOfficeServiceFactory.readDefaultPostOfficeProperties();
		props.remove(PostOfficeServiceFactory.PROPERTY_KEY_HOST);
		PostOfficeServiceFactory.getInstance(props);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.postoffice.PostOfficeServiceFactory#getInstance(java.util.Properties)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetInstanceProperties_Illegal_NoFromAddress()
	{
		Properties props = PostOfficeServiceFactory.readDefaultPostOfficeProperties();
		props.remove(PostOfficeServiceFactory.PROPERTY_KEY_FROM);
		PostOfficeServiceFactory.getInstance(props);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.postoffice.PostOfficeServiceFactory#readDefaultPostOfficeProperties()}
	 * .
	 */
	@Test
	public void testReadDefaultPostOfficeProperties()
	{
		final Properties defaultPostOfficeProperties = PostOfficeServiceFactory.readDefaultPostOfficeProperties();

		assertNotNull(defaultPostOfficeProperties);
		assertTrue(defaultPostOfficeProperties.containsKey(PostOfficeServiceFactory.PROPERTY_KEY_HOST));
		assertFalse(StringUtils.isBlank(defaultPostOfficeProperties.getProperty(PostOfficeServiceFactory.PROPERTY_KEY_HOST)));
	}

}
