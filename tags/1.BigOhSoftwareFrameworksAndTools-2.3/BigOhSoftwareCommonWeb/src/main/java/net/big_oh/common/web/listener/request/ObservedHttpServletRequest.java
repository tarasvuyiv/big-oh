package net.big_oh.common.web.listener.request;

/*
 Copyright (c) 2008 Dave Wingate dba Big-Oh Software (www.big-oh.net)

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

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

public class ObservedHttpServletRequest extends ObservedServletRequest implements Serializable
{

	protected final String httpMethod;
	protected final String resourceRequested;
	protected final String queryString;
	protected final String contextName;
	protected final String containerUserName;
	protected final String jSessionId;

	public ObservedHttpServletRequest(HttpServletRequest request)
	{
		super(request);

		httpMethod = request.getMethod();

		resourceRequested = request.getRequestURL().toString();

		queryString = request.getQueryString();

		contextName = request.getContextPath();

		containerUserName = request.getRemoteUser();

		HttpSession session = request.getSession(false);
		jSessionId = (session == null) ? null : session.getId();
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append(super.toString());
		sb.append(" for resource " + resourceRequested + " (" + httpMethod + ")");
		sb.append(" --  jSessionId: " + ((jSessionId == null || jSessionId.equals("")) ? "undefined" : jSessionId));
		sb.append(" --  userName: " + ((containerUserName == null || containerUserName.equals("")) ? "unknown" : containerUserName));

		return sb.toString();
	}

	public String getHttpMethod()
	{
		return httpMethod;
	}

	public String getResourceRequested()
	{
		return resourceRequested;
	}

	public String getQueryString()
	{
		return queryString;
	}

	public String getContextName()
	{
		return contextName;
	}

	public String getContainerUserName() {
		return getContainerUserName(false);
	}
	
	public String getContainerUserName(boolean returnHostNameIfUserNameUnknown)
	{
		return (returnHostNameIfUserNameUnknown && StringUtils.isBlank(containerUserName)) ? requestorHostName : containerUserName;
	}

	public String getJSessionId()
	{
		return jSessionId;
	}

}
