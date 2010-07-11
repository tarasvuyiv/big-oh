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
 * Tests the methods provided by the Resource domain object.
 * 
 * @author davewingate
 * @version Sep 8, 2009
 */
public class ResourceUnitTest extends DomainObjectTestBase<Resource>
{

	Resource resource = null;

	@Override
	protected Resource buildDomainObjectToTest()
	{
		return new Resource();
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		resource = getDomainObjectToTest();
	}

	/**
	 * Test method for
	 * {@link net.big_oh.resourcestats.domain.Resource#addResourceRequest(net.big_oh.resourcestats.domain.ResourceRequest)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddResourceRequest1()
	{
		resource.addResourceRequest(new ResourceRequest());
	}

	/**
	 * Test method for
	 * {@link net.big_oh.resourcestats.domain.Resource#addResourceRequest(net.big_oh.resourcestats.domain.ResourceRequest)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddResourceRequest2()
	{
		Requestor requestor = new Requestor();

		ResourceRequest rr = new ResourceRequest();
		// rr.setRequestedResource(resource);
		rr.setResourceRequestor(requestor);

		resource.addResourceRequest(rr);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.resourcestats.domain.Resource#addResourceRequest(net.big_oh.resourcestats.domain.ResourceRequest)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddResourceRequest3()
	{
		@SuppressWarnings("unused")
		Requestor requestor = new Requestor();

		ResourceRequest rr = new ResourceRequest();
		rr.setRequestedResource(resource);
		// rr.setResourceRequestor(requestor);

		resource.addResourceRequest(rr);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.resourcestats.domain.Resource#addResourceRequest(net.big_oh.resourcestats.domain.ResourceRequest)}
	 * .
	 */
	@Test
	public void testAddResourceRequest4()
	{
		Requestor requestor = new Requestor();

		ResourceRequest rr = new ResourceRequest();
		rr.setRequestedResource(resource);
		rr.setResourceRequestor(requestor);

		resource.addResourceRequest(rr);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.resourcestats.domain.Resource#addResourceRequest(net.big_oh.resourcestats.domain.Requestor)}
	 * .
	 */
	@Test
	public void testAddResourceRequest5()
	{
		Requestor requestor = new Requestor();

		ResourceRequest rr = resource.addResourceRequest(requestor);

		assertNotNull(rr);

		assertNotNull(rr.getRequestedResource());
		assertTrue(rr.getRequestedResource().getResourceRequests().contains(rr));

		assertNotNull(rr.getResourceRequestor());
		assertTrue(rr.getResourceRequestor().getResourceRequests().contains(rr));

	}

}
