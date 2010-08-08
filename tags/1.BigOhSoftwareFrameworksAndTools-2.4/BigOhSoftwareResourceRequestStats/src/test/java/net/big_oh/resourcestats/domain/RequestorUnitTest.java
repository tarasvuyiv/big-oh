package net.big_oh.resourcestats.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import net.big_oh.resourcestats.domain.Requestor;
import net.big_oh.resourcestats.domain.Resource;
import net.big_oh.resourcestats.domain.ResourceRequest;

import org.junit.Test;


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
 * Tests the methods provided by the Requestor domain object.
 * 
 * @author davewingate
 * @version Sep 9, 2009
 */
public class RequestorUnitTest extends DomainObjectTestBase<Requestor>
{

	Requestor requestor = null;

	@Override
	protected Requestor buildDomainObjectToTest()
	{
		return new Requestor();
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		requestor = getDomainObjectToTest();
	}

	/**
	 * Test method for
	 * {@link net.big_oh.resourcestats.domain.Requestor#addResourceRequest(net.big_oh.resourcestats.domain.ResourceRequest)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddResourceRequest1()
	{
		requestor.addResourceRequest(new ResourceRequest());
	}

	/**
	 * Test method for
	 * {@link net.big_oh.resourcestats.domain.Requestor#addResourceRequest(net.big_oh.resourcestats.domain.ResourceRequest)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddResourceRequest2()
	{
		@SuppressWarnings("unused")
		Resource resource = new Resource();

		ResourceRequest rr = new ResourceRequest();
		// rr.setRequestedResource(resource);
		rr.setResourceRequestor(requestor);

		requestor.addResourceRequest(rr);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.resourcestats.domain.Requestor#addResourceRequest(net.big_oh.resourcestats.domain.ResourceRequest)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddResourceRequest3()
	{
		Resource resource = new Resource();

		ResourceRequest rr = new ResourceRequest();
		rr.setRequestedResource(resource);
		// rr.setResourceRequestor(requestor);

		requestor.addResourceRequest(rr);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.resourcestats.domain.Requestor#addResourceRequest(net.big_oh.resourcestats.domain.ResourceRequest)}
	 * .
	 */
	@Test
	public void testAddResourceRequest4()
	{
		Resource resource = new Resource();

		ResourceRequest rr = new ResourceRequest();
		rr.setRequestedResource(resource);
		rr.setResourceRequestor(requestor);

		requestor.addResourceRequest(rr);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.resourcestats.domain.Requestor#addResourceRequest(net.big_oh.resourcestats.domain.Resource)}
	 * .
	 */
	@Test
	public void testAddResourceRequest5()
	{
		Resource resource = new Resource();

		ResourceRequest rr = requestor.addResourceRequest(resource);

		assertNotNull(rr);

		assertNotNull(rr.getRequestedResource());
		assertTrue(rr.getRequestedResource().getResourceRequests().contains(rr));

		assertNotNull(rr.getResourceRequestor());
		assertTrue(rr.getResourceRequestor().getResourceRequests().contains(rr));
	}

}
