package net.big_oh.common.web.filters.response;

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

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * This filter can be used to monitor attributes of an HTTPServletResponse. 
 */

public abstract class ResponseObserverFilter implements Filter, ResponseObserver
{

	// This set contains requests (as opposed to responses) because we want to
	// observe the response for each request only once, even if the response is
	// wrapped by this (or another) filter.
	private final Set<HttpServletRequest> requestsForWhichResponsesHaveBeenObserved = Collections.synchronizedSet(new HashSet<HttpServletRequest>());

	private FilterConfig fc = null;

	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.fc = filterConfig;
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
	{

		if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse)
		{

			HttpServletRequest httpRequest = (HttpServletRequest) req;
			HttpServletResponse httpResponse = (HttpServletResponse) resp;

			// No need to worry about a race condition in the block below since
			// a unique response should not pass through this Filter
			// concurrently
			if (!requestsForWhichResponsesHaveBeenObserved.contains(httpRequest))
			{
				try
				{

					requestsForWhichResponsesHaveBeenObserved.add(httpRequest);

					doBeforeResponseStarted(req, resp);

					// Observe this response as it travles through the filter
					// chain
					ServletResponse wrappedResponse = new ResponseObserverResponseWrapper(httpResponse, this, fc.getFilterName());
					chain.doFilter(req, wrappedResponse);

					doAfterResponseFinished(req, resp);

				}
				finally
				{
					requestsForWhichResponsesHaveBeenObserved.remove(httpRequest);
				}

			}
			else
			{

				// Just carry on with the filter chain for responses that are
				// already being observed
				chain.doFilter(req, resp);

			}

		}
		else
		{

			// Just carry on with the filter chain for non-http
			// requests/responses
			chain.doFilter(req, resp);

		}

	}

	public void destroy()
	{
	}

}
