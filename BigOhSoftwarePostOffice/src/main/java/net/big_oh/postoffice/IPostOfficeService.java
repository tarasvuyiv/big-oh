package net.big_oh.postoffice;

import java.io.File;
import java.util.Set;

import org.apache.commons.mail.EmailException;

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
 * 
 * As an uber-thin wrapper around the Apache Commons Email framework, instances
 * of {@link IPostOfficeService} represent the concept of a "neighborhood" post
 * office from which an application can send email messages without worrying
 * about quotidian delivery details. In other words, an
 * {@link IPostOfficeService} object allows you to send an email message with a
 * single line of code:
 * <code>PostOfficeServiceFactory.getInstance().sendMail("foo@bar.com", "Hello ...", "World!");</code>
 * 
 * @see PostOfficeServiceFactory
 * @author davewingate
 * 
 */
// TODO Update to throw a framework-specific exception instead of EmailException.
public interface IPostOfficeService
{

	/**
	 * @return the host for the outgoing SMTP mail server
	 */
	public String getSmtpServerHost();

	/**
	 * @return the port for the outgoing SMTP mail server
	 */
	public int getSmtpServerPort();

	/**
	 * @return the 'from' address used for outgoing email sent via this service
	 */
	public String getSendersFromAddress();

	/**
	 * @return a boolean value indicating whether the service will request debug
	 *         output from JavaMail classes
	 */
	public boolean isDebug();

	/**
	 * @return true if the secure SSL protocol should be used for message
	 *         transport
	 */
	public boolean isTransportWithSsl();

	/**
	 * @return true if the the secure TLS protocol should be used when
	 *         authenticating to the SMTP server
	 */
	public boolean isAuthenticateWithTls();

	/**
	 * @return the username used to authenticate to the SMTP server
	 */
	public String getSmtpUserName();

	/**
	 * @return the password used to authenticate to the SMTP server
	 */
	public String getSmtpPassword();

	/**
	 * Sends an email message to a single recipient.
	 * 
	 * @param toRecipient
	 * @param subject
	 * @param messageText
	 * @throws EmailException
	 */
	public void sendMail(String toRecipient, String subject, String messageText) throws EmailException;

	/**
	 * Sends an email message to multiple recipients.
	 * 
	 * @param toRecipients
	 * @param subject
	 * @param messageText
	 * @throws EmailException
	 */
	public void sendMail(Set<String> toRecipients, String subject, String messageText) throws EmailException;

	/**
	 * Sends an email message to multiple recipients.
	 * 
	 * @param toRecipients
	 * @param subject
	 * @param messageText
	 * @param attachments
	 * @throws EmailException
	 */
	public void sendMail(Set<String> toRecipients, String subject, String messageText, Set<File> attachments) throws EmailException;

}
