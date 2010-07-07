package net.big_oh.common.web.filters.ipaddress;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.big_oh.common.web.filters.ipaddress.ClasslessInterDomainRoutingBlock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClasslessInterDomainRoutingBlockTest
{

	ClasslessInterDomainRoutingBlock leftEdge;
	ClasslessInterDomainRoutingBlock middle;
	ClasslessInterDomainRoutingBlock unusualPrefixLength;
	ClasslessInterDomainRoutingBlock rightEdge;

	@Before
	public void setUp() throws Exception
	{
		leftEdge = new ClasslessInterDomainRoutingBlock(InetAddress.getByName("10.0.0.0"), 0);
		middle = new ClasslessInterDomainRoutingBlock(InetAddress.getByName("208.130.28.0"), 24);
		unusualPrefixLength = new ClasslessInterDomainRoutingBlock(InetAddress.getByName("208.130.28.255"), 25);
		rightEdge = new ClasslessInterDomainRoutingBlock(InetAddress.getByName("207.71.25.66"), 32);
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testIsAddressMemberOfRoutingBlock() throws UnknownHostException
	{

		// all valid addresses are members of the "left edge" block
		assertTrue(leftEdge.isAddressMemberOfRoutingBlock(InetAddress.getByName("10.0.0.0")));
		assertTrue(leftEdge.isAddressMemberOfRoutingBlock(InetAddress.getByName("192.168.0.1")));
		assertTrue(leftEdge.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.0")));
		assertTrue(leftEdge.isAddressMemberOfRoutingBlock(InetAddress.getByName("127.0.0.1")));
		assertTrue(leftEdge.isAddressMemberOfRoutingBlock(InetAddress.getByName("207.71.25.66")));

		// only certain addresses are members of the "middle" block
		// first test some of the myriad addresses that are within the block
		assertTrue(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.0")));
		assertTrue(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.1")));
		assertTrue(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.2")));
		assertTrue(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.3")));
		assertTrue(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.4")));
		assertTrue(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.252")));
		assertTrue(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.253")));
		assertTrue(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.254")));
		assertTrue(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.255")));

		// test addresses that are just to the left of the block
		assertFalse(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.27.0")));
		assertFalse(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.27.124")));
		assertFalse(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.27.255")));

		// test addresses that are just to the right of the block
		assertFalse(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.29.0")));
		assertFalse(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.29.124")));
		assertFalse(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.29.255")));

		// test some addresses that are obviously way outside of the block
		assertFalse(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("10.0.0.0")));
		assertFalse(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("192.168.0.1")));
		assertFalse(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("127.0.0.1")));
		assertFalse(middle.isAddressMemberOfRoutingBlock(InetAddress.getByName("207.71.25.66")));

		// for the unusualPrefixLength range, only addresses with odd value in
		// final octet are members
		assertFalse(unusualPrefixLength.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.0")));
		assertFalse(unusualPrefixLength.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.1")));
		assertFalse(unusualPrefixLength.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.2")));
		assertTrue(unusualPrefixLength.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.129")));
		assertTrue(unusualPrefixLength.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.254")));
		assertTrue(unusualPrefixLength.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.255")));

		// only one address is a valid member of the "right edge" block
		assertFalse(rightEdge.isAddressMemberOfRoutingBlock(InetAddress.getByName("10.0.0.0")));
		assertFalse(rightEdge.isAddressMemberOfRoutingBlock(InetAddress.getByName("192.168.0.1")));
		assertFalse(rightEdge.isAddressMemberOfRoutingBlock(InetAddress.getByName("208.130.28.0")));
		assertFalse(rightEdge.isAddressMemberOfRoutingBlock(InetAddress.getByName("127.0.0.1")));
		assertTrue(rightEdge.isAddressMemberOfRoutingBlock(InetAddress.getByName("207.71.25.66")));

	}

}
