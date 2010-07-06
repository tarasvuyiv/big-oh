package net.big_oh.postoffice;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.big_oh.postoffice.PostOfficeService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subethamail.wiser.Wiser;

/**
 * A true unit test that exercises the {@link PostOfficeService} class using a
 * Wiser fixture to stand in for the needed SMTP server.
 * 
 * @author dwingate
 * @version May 30, 2010
 */
public class PostOfficeServiceUnitTest
{

	private static final String UNIT_TEST_DESTINATION_MAIL_ADDRESS = "bigohsoftware@gmail.com";

	private Wiser wiserTestFixture;
	private int unitTestSmtpPort;

	private PostOfficeService postOfficeService;

	private File attachment1;
	private File attachment2;

	@Before
	public void setUp() throws Exception
	{

		wiserTestFixture = new Wiser();

		unitTestSmtpPort = 2500 + new Random().nextInt(1000);
		wiserTestFixture.setPort(unitTestSmtpPort);

		wiserTestFixture.start();

		postOfficeService = new PostOfficeService.Builder("localhost", "foo+from@bar.com").setSmtpServerPort(unitTestSmtpPort).build();

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

		wiserTestFixture.stop();
	}

	@Test(timeout = 5000)
	public void testBasicSendMail() throws EmailException
	{
		assertEquals(0, wiserTestFixture.getMessages().size());

		postOfficeService.sendMail(UNIT_TEST_DESTINATION_MAIL_ADDRESS, "PostOffice Unit Test - testBasicSendMail", "PostOffice Unit Test - testBasicSendMail");

		assertEquals(1, wiserTestFixture.getMessages().size());
	}

	@Test(timeout = 5000)
	public void testSendMailWithAttachments() throws EmailException
	{
		Set<File> attachmentCollection = new HashSet<File>();
		attachmentCollection.add(attachment1);
		attachmentCollection.add(attachment2);

		assertEquals(0, wiserTestFixture.getMessages().size());

		postOfficeService.sendMail(Collections.singleton(UNIT_TEST_DESTINATION_MAIL_ADDRESS), "PostOffice Unit Test - testSendMailWithAttachments", "PostOffice Unit Test - testBasicSendMail", attachmentCollection);

		assertEquals(1, wiserTestFixture.getMessages().size());
	}

}
