package net.big_oh.resourcestats.domain;

import net.big_oh.resourcestats.domain.ResourceRequest;


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
 * Tests the methods provided by the ResourceRequest domain object.
 * 
 * @author davewingate
 * @version Sep 9, 2009
 */
public class ResourceRequestUnitTest extends DomainObjectTestBase<ResourceRequest>
{

	@SuppressWarnings("unused")
	private ResourceRequest rr = null;

	@Override
	protected ResourceRequest buildDomainObjectToTest()
	{
		return new ResourceRequest();
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		rr = getDomainObjectToTest();
	}

}
