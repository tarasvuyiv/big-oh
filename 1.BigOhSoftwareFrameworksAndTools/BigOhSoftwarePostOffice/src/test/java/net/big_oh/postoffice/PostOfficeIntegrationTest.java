package net.big_oh.postoffice;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailException;
import org.junit.After;
import org.junit.Before;
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
 * <p>
 * An integration test used to confirm whether a default instance of the
 * {@link IPostOfficeService} can send email via an external SMTP server.
 * </p>
 * 
 * <p>
 * In general, details of the SMTP server used by this test are define in the postoffice.properties file.  The only exception
 * is that the test expects to find the related password defined in a system property.  If you're executing this test from within an IDE,
 * you'll likely need to add a JVM argument to your run configuration:
 * <pre>-Dpostoffice.smtpPassword=super_secret_password</pre>
 * Alternatively, you might be executing this test as part of the Maven build lifcycle, in which case you'll
 * want to define the needed password in your settings.xml file:
 * <pre>
 * &lt;profile&gt;
 *	 &lt;id&gt;BigOhSoftware&lt;/id&gt;
 * 	 &lt;activation&gt;
 * 		&lt;activeByDefault&gt;true&lt;/activeByDefault&gt;
 *	 &lt;/activation&gt;
 *	 &lt;properties&gt;
 * 		&lt;postoffice.smtpPassword&gt;super_secret_password&lt;/postoffice.smtpPassword&gt;
 *	 &lt;/properties&gt;
 * &lt;/profile&gt;
 * </pre>
 * </p>
 * 
 * @author dwingate
 * @version Jun 2, 2010
 */
public class PostOfficeIntegrationTest
{
	private static final String UNIT_TEST_DESTINATION_MAIL_ADDRESS = "bigohsoftware@gmail.com";

	private IPostOfficeService postOfficeService;

	private File attachment1;
	private File attachment2;

	@Before
	public void setUp() throws Exception
	{

		attachment1 = File.createTempFile(this.getClass().getSimpleName(), ".txt");
		FileUtils.writeStringToFile(attachment1, "first attachment");

		attachment2 = File.createTempFile(this.getClass().getSimpleName(), ".txt");
		FileUtils.writeStringToFile(attachment2, "second attachment");
		
		Properties postOfficeProperties = PostOfficeServiceFactory.readDefaultPostOfficeProperties();

		String smtpPassword = System.getProperty(PostOfficeServiceFactory.PROPERTY_KEY_SMTP_PASSWORD);
		assertNotNull("Failed to locate SMTP password in expected system property: " + PostOfficeServiceFactory.PROPERTY_KEY_SMTP_PASSWORD, smtpPassword);

		postOfficeProperties.setProperty(PostOfficeServiceFactory.PROPERTY_KEY_SMTP_PASSWORD, smtpPassword);

		postOfficeService = PostOfficeServiceFactory.getInstance(postOfficeProperties);

	}

	@After
	public void tearDown() throws Exception
	{
		attachment1.delete();
		attachment2.delete();
	}

	@Test(timeout = 10000)
	public void testBasicSendMail() throws EmailException
	{
		postOfficeService.sendMail(UNIT_TEST_DESTINATION_MAIL_ADDRESS, "PostOffice Unit Test - testBasicSendMail", "PostOffice Unit Test - testBasicSendMail");
	}

	@Test(timeout = 10000)
	public void testSendMailWithAttachments() throws EmailException
	{
		Set<File> attachmentCollection = new HashSet<File>();
		attachmentCollection.add(attachment1);
		attachmentCollection.add(attachment2);

		postOfficeService.sendMail(Collections.singleton(UNIT_TEST_DESTINATION_MAIL_ADDRESS), "PostOffice Unit Test - testSendMailWithAttachments", "PostOffice Unit Test - testBasicSendMail", attachmentCollection);
	}

}
