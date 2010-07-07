package net.big_oh.resourcestats.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.big_oh.hibernate.HibernateVO;
import net.big_oh.hibernate.IHibernateVO;


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
 * A domain object that models a user/requestor that requests potentially many resources.
 * 
 * @author davewingate
 * @version Aug 31, 2009
 */
public class Requestor extends HibernateVO implements IHibernateVO
{

	private String requestorId = null;
	private Set<ResourceRequest> resourceRequests = new HashSet<ResourceRequest>();

	public Object[] getBusinessKeys()
	{
		Object[] businessKeys = { requestorId };
		return businessKeys;
	}

	public String getRequestorId()
	{
		return requestorId;
	}

	public void setRequestorId(String requestorId)
	{
		this.requestorId = requestorId;
	}

	public Set<ResourceRequest> getResourceRequests()
	{
		return Collections.unmodifiableSet(resourceRequests);
	}

	public void setResourceRequests(Set<ResourceRequest> resourceRequests)
	{
		this.resourceRequests = resourceRequests;
	}

	protected void addResourceRequest(ResourceRequest resourceRequest)
	{
		if (resourceRequest.getRequestedResource() == null)
		{
			throw new IllegalArgumentException("Cannot add a resourceRequest without a non-null Resource.");
		}

		if (resourceRequest.getResourceRequestor() == null)
		{
			throw new IllegalArgumentException("Cannot add a resourceRequest without a non-null Requestor.");
		}

		resourceRequests.add(resourceRequest);
	}

	public ResourceRequest addResourceRequest(Resource resource)
	{
		ResourceRequest rr = new ResourceRequest();

		rr.setRequestedResource(resource);
		rr.setResourceRequestor(this);

		resource.addResourceRequest(rr);
		this.addResourceRequest(rr);

		return rr;
	}

}
