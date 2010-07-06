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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequest;

public class ObservedServletRequest implements Serializable
{

	protected final String requestorIP;
	protected final String requestorHostName;
	protected final int requestedPort;
	protected final String requestedProtocol;
	protected final Date requestReceivedAt;

	private static ConcurrentHashMap<String, String> ipToHostNameCache;
	private static Date ipToHostNameCacheCreatedOn;
	private static final long ipToHostNameCacheExpirationMilliseconds = 1000 * 60 * 60;

	public ObservedServletRequest(ServletRequest request)
	{

		requestorIP = request.getRemoteAddr();

		requestorHostName = resolveRequestorHostName(request);

		requestedPort = request.getServerPort();

		requestedProtocol = request.getProtocol();

		requestReceivedAt = new Date();
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append(requestedProtocol + " request");
		sb.append(" from " + requestorHostName + " (" + requestorIP + ")");

		return sb.toString();
	}

	private String resolveRequestorHostName(ServletRequest request)
	{
		String hostName = request.getRemoteHost();
		try
		{
			if (hostName.equals(request.getRemoteAddr()))
			{
				Map<String, String> cache = getIpToHostNameCache();

				String cachedHostName = cache.get(request.getRemoteAddr());
				if (cachedHostName == null)
				{
					InetAddress addr = InetAddress.getByName(request.getRemoteAddr());
					cachedHostName = addr.getHostName();
					cache.put(request.getRemoteAddr(), cachedHostName);
				}
				hostName = cachedHostName;
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		return hostName;
	}

	private synchronized Map<String, String> getIpToHostNameCache()
	{

		if (ipToHostNameCacheCreatedOn != null && (new Date().getTime() - ipToHostNameCacheCreatedOn.getTime() > ipToHostNameCacheExpirationMilliseconds))
		{
			ipToHostNameCache = null;
		}

		if (ipToHostNameCache == null)
		{
			ipToHostNameCache = new ConcurrentHashMap<String, String>();
			ipToHostNameCacheCreatedOn = new Date();
		}

		return ipToHostNameCache;
	}

	public String getRequestorIP()
	{
		return requestorIP;
	}

	public String getRequestorHostName()
	{
		return requestorHostName;
	}

	public int getRequestedPort()
	{
		return requestedPort;
	}

	public String getRequestedProtocol()
	{
		return requestedProtocol;
	}

	public Date getRequestReceivedAt()
	{
		return requestReceivedAt;
	}

}
