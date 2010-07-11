package net.big_oh.common.web.servlets.mime;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class MimeServlet extends HttpServlet
{

	private static final Log logger = LogFactory.getLog(MimeServlet.class);

	public static final String REQUESTED_RESOURCE_NAME = "requestedResourceName";

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		logger.info(getServletConfig().getServletName() + " invoked.  Requested mime resource: " + req.getParameter(REQUESTED_RESOURCE_NAME));

		// Get the name of the requested resource
		String requestedResourceName = req.getParameter(REQUESTED_RESOURCE_NAME);

		if (requestedResourceName == null || requestedResourceName.equals(""))
		{
			logger.error("Called " + getServletConfig().getServletName() + " without providing a parameter for '" + REQUESTED_RESOURCE_NAME + "'");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		// Ensure that the user is allowed to access the requested resource
		if (!isCanUserAccessRequestedResource(requestedResourceName, req.getSession(true)))
		{
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		// Get a byte array representation of the resource to be returned in the
		// response
		byte[] resourceBytes = getMimeResourceBytes(requestedResourceName);

		if (resourceBytes == null || resourceBytes.length == 0)
		{
			logger.error("No resource found under the name \"" + requestedResourceName + "\"");
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// Set content length for the response
		resp.setContentLength(resourceBytes.length);

		// Get the MIME type for the resource
		String mimeType = getMimeType(requestedResourceName);

		if (mimeType == null || mimeType.equals(""))
		{
			logger.error("Failed to get MIME type for the requested resource.");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		// Set the content type for the response
		resp.setContentType(mimeType);

		// Control the HTTP caching of the response
		// This setting controls how frequently the cached resource is
		// revalidated (which is not necessarily the same as reloaded)
		resp.setHeader("Cache-Control", "max-age=" + getMaxAgeInSeconds(requestedResourceName));

		// Use streams to return the requested resource
		ByteArrayInputStream in = new ByteArrayInputStream(resourceBytes);
		OutputStream out = resp.getOutputStream();

		byte[] buf = new byte[1024];
		int count = 0;
		while ((count = in.read(buf)) >= 0)
		{
			out.write(buf, 0, count);
		}

		in.close();
		out.close();

	}

	/**
	 * @param requestedResourceName
	 * @param session
	 * @return Returns a boolean indicating whether the user may access the
	 *         requested resource.
	 */
	protected boolean isCanUserAccessRequestedResource(String requestedResourceName, HttpSession session)
	{
		return true;
	}

	/**
	 * @param requestedResourceName
	 * @return Returns the numbers of seconds after which the response should be
	 *         considered stale by HTTP caches.
	 */
	protected long getMaxAgeInSeconds(String requestedResourceName)
	{
		// return 1 hour by default
		return 60 * 60;
	}

	@Override
	protected long getLastModified(HttpServletRequest req)
	{
		String requestedResourceName = req.getParameter(REQUESTED_RESOURCE_NAME);

		if (requestedResourceName == null || requestedResourceName.equals(""))
		{
			return -1;
		}
		else
		{
			return getLastModified(requestedResourceName);
		}
	}

	/**
	 * @param requestedResourceName
	 * @return Returns the system time when the requested resource was last
	 *         modified. A return value of -1 means that the resource is always
	 *         "stale."
	 */
	protected abstract long getLastModified(String requestedResourceName);

	/**
	 * If you're returning a resource for which you know the original file name,
	 * this method cold easily be implemented by returning:
	 * 
	 * getServletContext().getMimeType(originalFileName.toLowerCase());
	 * 
	 * @param requestedResourceName
	 * @return Returns a String representation of the resource's MIME type.
	 */
	protected abstract String getMimeType(String requestedResourceName);

	/**
	 * @param requestedResourceName
	 * @return Returns the bytes for the requested resource.
	 */
	protected abstract byte[] getMimeResourceBytes(String requestedResourceName) throws IOException;

}
