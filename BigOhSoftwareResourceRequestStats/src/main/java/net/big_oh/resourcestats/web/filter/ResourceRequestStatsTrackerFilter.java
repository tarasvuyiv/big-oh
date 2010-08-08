package net.big_oh.resourcestats.web.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.big_oh.common.utils.Duration;
import net.big_oh.common.web.listener.request.ObservedHttpServletRequest;
import net.big_oh.resourcestats.dao.DAOFactory;
import net.big_oh.resourcestats.dao.IRequestorDAO;
import net.big_oh.resourcestats.dao.IResourceDAO;
import net.big_oh.resourcestats.dao.IResourceRequestDAO;
import net.big_oh.resourcestats.domain.Requestor;
import net.big_oh.resourcestats.domain.Resource;
import net.big_oh.resourcestats.domain.ResourceRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
 * A filter that captures resource request statistics, persisting such
 * information to a database.
 * 
 * @author davewingate
 * @version Sep 9, 2009
 */
public class ResourceRequestStatsTrackerFilter implements Filter
{

	Log logger = LogFactory.getLog(ResourceRequestStatsTrackerFilter.class);

	public void init(FilterConfig fc) throws ServletException
	{
	}

	public void destroy()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{

		// capture time at which request was received
		Date requestReceivedOn = new Date();

		// Time the operation at large
		Duration duration = new Duration();

		// Carry on with the requested operation.
		chain.doFilter(request, response);

		// record resource access stats
		try
		{
			trackResourceRequestStats(request, requestReceivedOn, duration.stop());
		}
		catch (RuntimeException re)
		{
			logger.error(re.getMessage(), re);
		}

	}

	private void trackResourceRequestStats(ServletRequest request, Date requestReceivedOn, long duration)
	{

		if (request instanceof HttpServletRequest)
		{

			ObservedHttpServletRequest observedHttpServletRequest = new ObservedHttpServletRequest((HttpServletRequest) request);

			logger.info("Begin tracking stats for an " + observedHttpServletRequest.toString());

			// Get values from servlet request
			String requestorId = observedHttpServletRequest.getContainerUserName(true);
			String resourceLocator = observedHttpServletRequest.getResourceRequested();

			// Prepare DAOs
			IResourceDAO resourceDAO = DAOFactory.getResourceDAO();
			IRequestorDAO requestorDAO = DAOFactory.getRequestorDAO();
			IResourceRequestDAO resourceRequestDAO = DAOFactory.getResourceRequestDAO();

			// TODO DSW Move this code into a IRequestorDAO method
			// load Requestor data using persistence tier
			Requestor requestor = (Requestor) requestorDAO.findById(requestorId);
			if (requestor == null)
			{
				requestor = new Requestor();
				requestor.setRequestorId(!StringUtils.isBlank(observedHttpServletRequest.getContainerUserName()) ? observedHttpServletRequest.getContainerUserName() : observedHttpServletRequest.getRequestorHostName());
				requestorDAO.makePersistent(requestor);
			}

			// TODO DSW Move this code into a IResourceDAO method
			// load Resource data using persistence tier
			Resource resource = resourceDAO.findById(resourceLocator);
			if (resource == null)
			{
				resource = new Resource();
				resource.setResourceLocator(observedHttpServletRequest.getResourceRequested());
				resourceDAO.makePersistent(resource);
			}

			// TODO DSW Move this code into a IResourceRequestDAO method
			// Create a new ResourceRequest object to model this servlet
			// request.
			ResourceRequest rr = requestor.addResourceRequest(resource);
			rr.setMillisecondsToServiceRequest(duration);
			rr.setRequestedFromAddress(observedHttpServletRequest.getRequestorIP());
			rr.setRequestedOn(requestReceivedOn);
			resourceRequestDAO.makePersistent(rr);

			logger.info("Finished tracking stats for an " + observedHttpServletRequest.toString());

		}
		else
		{
			logger.warn("Ignoring a non-HTTP request.");
		}

	}
}
