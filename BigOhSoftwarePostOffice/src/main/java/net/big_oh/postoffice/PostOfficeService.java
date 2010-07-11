package net.big_oh.postoffice;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.big_oh.common.utils.Duration;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;


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
 * A concrete implementation of the {@link IPostOfficeService} interface.
 * </p>
 * 
 * <p>
 * This service implementation is designed to be immutable, a design decision
 * that eases sharing among multiple threads of execution.
 * </p>
 * 
 * <p>
 * New instances of {@link PostOfficeService} should be built using the
 * associated {@link Builder} class, which helps to ensure expected invariants
 * for newly constructed service objects..
 * </p>
 * 
 * @author davewingate
 * 
 */
final class PostOfficeService implements IPostOfficeService
{

	private static final Log logger = LogFactory.getLog(PostOfficeService.class);

	private final String smtpServerHost;
	private final int smtpServerPort;
	private final boolean authenticateWithTls;
	private final String smtpUserName;
	private final String smtpPassword;
	private final String sendersFromAddress;
	private final boolean transportWithSsl;
	private final boolean debug;

	private PostOfficeService(Builder builder)
	{
		super();

		this.smtpServerHost = builder.builderSmtpServerHost;

		this.transportWithSsl = builder.builderTransportWithSsl;

		if (builder.builderSmtpServerPort > 0)
		{
			this.smtpServerPort = builder.builderSmtpServerPort;
		}
		else
		{
			this.smtpServerPort = (this.transportWithSsl) ? 465 : 25;
		}

		this.sendersFromAddress = builder.builderSendersFromAddress;

		this.debug = builder.builderDebug;

		this.authenticateWithTls = builder.builderAuthenticateWithTls;
		this.smtpUserName = builder.builderSmtpUserName;
		this.smtpPassword = builder.builderSmtpPassword;

		// validate expected invariants now that member variables are set for
		// newly constructed object
		assert (smtpServerHost != null);
		assert (sendersFromAddress != null);
		assert (smtpServerPort > 0);
		assert (smtpUserName == null || smtpPassword != null);
	}

	/**
	 * A builder that eases the construction of well-formed
	 * {@link PostOfficeService} objects and obviates the need for telescoping
	 * constructors that would otherwise be needed to create those objects.
	 * 
	 * @author dwingate
	 * @version Jun 1, 2010
	 */
	static class Builder
	{
		// required
		private final String builderSmtpServerHost;
		private final String builderSendersFromAddress;

		// optional
		private int builderSmtpServerPort;
		private boolean builderDebug;
		private boolean builderAuthenticateWithTls;
		private String builderSmtpUserName;
		private String builderSmtpPassword;
		private boolean builderTransportWithSsl;

		Builder(String smtpServerHost, String sendersFromAddress)
		{
			assert (!StringUtils.isBlank(smtpServerHost));
			this.builderSmtpServerHost = smtpServerHost;

			assert (!StringUtils.isBlank(sendersFromAddress));
			this.builderSendersFromAddress = sendersFromAddress;
		}

		Builder setSmtpServerPort(int smtpServerPort)
		{
			this.builderSmtpServerPort = smtpServerPort;
			return this;
		}

		Builder setDebug(boolean debug)
		{
			this.builderDebug = debug;
			return this;
		}

		Builder setAuthenticateWithTls(boolean authenticateWithTls)
		{
			this.builderAuthenticateWithTls = authenticateWithTls;
			return this;
		}

		Builder setSmtpUserName(String smtpUserName)
		{
			this.builderSmtpUserName = smtpUserName;
			return this;
		}

		Builder setSmtpPassword(String smtpPassword)
		{
			this.builderSmtpPassword = smtpPassword;
			return this;
		}

		Builder setTransportWithSsl(boolean transportWithSsl)
		{
			this.builderTransportWithSsl = transportWithSsl;
			return this;
		}

		/**
		 * @return a new instance of {@link PostOfficeService} built according
		 *         to the attributes of this builder
		 * @throws IllegalStateException
		 *             if this builder's attributes do not define a valid
		 *             configuration for a new {@link PostOfficeService} object
		 */
		public PostOfficeService build() throws IllegalStateException
		{
			// check state of builder is valid
			if (this.builderSmtpUserName != null && this.builderSmtpPassword == null)
			{
				throw new IllegalStateException("Must specify a password when specifying a username for SMTP authentication.");
			}
			if (this.builderSmtpUserName == null && this.builderSmtpPassword != null)
			{
				throw new IllegalStateException("Must specify a username when specifying a password for SMTP authentication.");
			}

			return new PostOfficeService(this);
		}

	}

	public String getSmtpServerHost()
	{
		return smtpServerHost;
	}

	public int getSmtpServerPort()
	{
		return smtpServerPort;
	}

	public boolean isAuthenticateWithTls()
	{
		return authenticateWithTls;
	}

	public String getSmtpUserName()
	{
		return smtpUserName;
	}

	public String getSmtpPassword()
	{
		return smtpPassword;
	}

	public String getSendersFromAddress()
	{
		return sendersFromAddress;
	}

	public boolean isTransportWithSsl()
	{
		return transportWithSsl;
	}

	public boolean isDebug()
	{
		return debug;
	}

	public void sendMail(String toRecipient, String subject, String messageText) throws EmailException
	{
		sendMail(Collections.singleton(toRecipient), subject, messageText);
	}

	public void sendMail(Set<String> toRecipients, String subject, String messageText) throws EmailException
	{
		sendMail(toRecipients, subject, messageText, new HashSet<File>(0));
	}

	public void sendMail(Set<String> toRecipients, String subject, String messageText, Set<File> attachments) throws EmailException
	{

		Duration dur = new Duration(this.getClass());
		logger.info("Begin send email to " + toRecipients.size() + " recipient(s) with " + attachments.size() + " attachment(s).");

		// validate the method parameters
		if (toRecipients.isEmpty())
		{
			throw new IllegalArgumentException("Cowardly refusing to send an email message with no recipients.");
		}

		// instantiate an email object
		MultiPartEmail email = new MultiPartEmail();

		// establish SMTP end-point details
		email.setHostName(smtpServerHost);
		email.setSSL(transportWithSsl);
		if (transportWithSsl)
		{
			email.setSslSmtpPort(Integer.toString(smtpServerPort));
		}
		else
		{
			email.setSmtpPort(smtpServerPort);
		}

		// establish SMTP authentication details
		if (!StringUtils.isBlank(smtpUserName) && !StringUtils.isBlank(smtpPassword))
		{
			email.setAuthentication(smtpUserName, smtpPassword);
		}
		email.setTLS(authenticateWithTls);

		// establish basic email delivery details
		email.setDebug(debug);
		email.setFrom(sendersFromAddress);
		for (String toRecipient : toRecipients)
		{
			email.addTo(toRecipient);
		}

		// set email content
		email.setSubject(subject);
		email.setMsg(messageText);

		// create attachments to the email
		for (File file : attachments)
		{

			if (!file.exists())
			{
				logger.error("Skipping attachment file that does not exist: " + file.toString());
				continue;
			}

			if (!file.isFile())
			{
				logger.error("Skipping attachment file that is not a normal file: " + file.toString());
				continue;
			}

			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(file.getPath());
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setName(file.getName());

			email.attach(attachment);

		}

		// Send message
		email.send();

		dur.stop("Finished sending email to " + toRecipients.size() + " recipient(s)");

	}
	
	@Override
	public boolean equals(Object obj)
	{
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public int hashCode()
	{
		// This calculation _could_ be cached later since this object is immutable
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
