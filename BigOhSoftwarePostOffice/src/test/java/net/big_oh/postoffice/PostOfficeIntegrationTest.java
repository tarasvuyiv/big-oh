package net.big_oh.postoffice;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.big_oh.postoffice.IPostOfficeService;
import net.big_oh.postoffice.PostOfficeServiceFactory;

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
 * An integration test used to confirm whether a default instance of the
 * {@link IPostOfficeService} can send email via an external SMTP server.
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

		postOfficeService = PostOfficeServiceFactory.getInstance();

		attachment1 = File.createTempFile(this.getClass().getSimpleName(), ".txt");
		FileUtils.writeStringToFile(attachment1, "first attachment");

		attachment2 = File.createTempFile(this.getClass().getSimpleName(), ".txt");
		FileUtils.writeStringToFile(attachment2, "second attachment");

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
