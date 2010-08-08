package net.big_oh.postoffice;

import static org.junit.Assert.*;

import net.big_oh.postoffice.PostOfficeService;
import net.big_oh.postoffice.PostOfficeService.Builder;

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
 * A unit test for the {@link PostOfficeService.Builder} class.
 *
 * @author dwingate
 * @version Jun 2, 2010
 */
public class PostOfficeServiceBuilderUnitTest
{

	/**
	 * Test method for {@link net.big_oh.postoffice.PostOfficeService.Builder#Builder(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testBuilder()
	{
		assertNotNull(new Builder("smtp.someserver.net", "foo@bar.com"));
	}
	
	/**
	 * Test method for {@link net.big_oh.postoffice.PostOfficeService.Builder#Builder(java.lang.String, java.lang.String)}.
	 */
	@Test(expected=AssertionError.class)
	public void testBuilder_Illegal_NullHost()
	{
		new Builder(null, "foo@bar.com");
	}
	
	/**
	 * Test method for {@link net.big_oh.postoffice.PostOfficeService.Builder#Builder(java.lang.String, java.lang.String)}.
	 */
	@Test(expected=AssertionError.class)
	public void testBuilder_Illegal_BankFromAddress()
	{
		new Builder("smtp.someserver.net", "");
	}

	/**
	 * Test method for {@link net.big_oh.postoffice.PostOfficeService.Builder#build()}.
	 */
	@Test
	public void testBuild()
	{
		Builder builder = new Builder("smtp.someserver.net", "foo@bar.com");
		assertNotNull(builder);
		assertNotNull(builder.build());
		
		builder.setTransportWithSsl(true);
		builder.setSmtpServerPort(0);
		assertEquals(465, builder.build().getSmtpServerPort());
		
		builder.setTransportWithSsl(true);
		builder.setSmtpServerPort(12345);
		assertEquals(12345, builder.build().getSmtpServerPort());
		
		builder.setTransportWithSsl(false);
		builder.setSmtpServerPort(0);
		assertEquals(25, builder.build().getSmtpServerPort());
		
		builder.setTransportWithSsl(false);
		builder.setSmtpServerPort(12345);
		assertEquals(12345, builder.build().getSmtpServerPort());
		
	}

}
