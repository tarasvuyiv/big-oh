package net.big_oh.resourcestats.domain;

import java.util.Date;

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
 * A domain object that models a request by a particular user for a particular
 * resource.
 * 
 * @author davewingate
 * @version Aug 31, 2009
 */
public class ResourceRequest extends HibernateVO implements IHibernateVO
{

	private long id;
	private Date requestedOn = null;
	// TODO Eventually figure out how to change type from String to InetAddress
	private String requestedFromAddress = null;
	private long millisecondsToServiceRequest = -1;
	private Resource requestedResource = null;
	private Requestor resourceRequestor = null;

	public Object[] getBusinessKeys()
	{
		Object[] businessKeys = { requestedResource, resourceRequestor };
		return businessKeys;
	}

	public Date getRequestedOn()
	{
		return requestedOn;
	}

	public void setRequestedOn(Date requestedOn)
	{
		this.requestedOn = requestedOn;
	}

	public String getRequestedFromAddress()
	{
		return requestedFromAddress;
	}

	public void setRequestedFromAddress(String requestedFromAddress)
	{
		this.requestedFromAddress = requestedFromAddress;
	}

	public long getMillisecondsToServiceRequest()
	{
		return millisecondsToServiceRequest;
	}

	public void setMillisecondsToServiceRequest(long millisecondsTosErviceRequest)
	{
		this.millisecondsToServiceRequest = millisecondsTosErviceRequest;
	}

	public Resource getRequestedResource()
	{
		return requestedResource;
	}

	public void setRequestedResource(Resource requestedResource)
	{
		this.requestedResource = requestedResource;
	}

	public Requestor getResourceRequestor()
	{
		return resourceRequestor;
	}

	public void setResourceRequestor(Requestor resourceRequestor)
	{
		this.resourceRequestor = resourceRequestor;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

}
