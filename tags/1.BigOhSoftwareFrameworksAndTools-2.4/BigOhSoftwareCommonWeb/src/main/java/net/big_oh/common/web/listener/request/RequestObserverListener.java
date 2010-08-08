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

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

public abstract class RequestObserverListener implements ServletRequestListener
{

	protected abstract void doOnServletRequestInitialized(ObservedServletRequest observedServletRequest);

	protected abstract void doOnServletRequestDestroyed(ObservedServletRequest observedServletRequest);

	public void requestInitialized(ServletRequestEvent event)
	{
		ServletRequest request = event.getServletRequest();

		if (request instanceof HttpServletRequest)
		{
			doOnServletRequestInitialized(new ObservedHttpServletRequest((HttpServletRequest) request));
		}
		else
		{
			doOnServletRequestInitialized(new ObservedServletRequest(request));
		}

	}

	public void requestDestroyed(ServletRequestEvent event)
	{
		ServletRequest request = event.getServletRequest();

		if (request instanceof HttpServletRequest)
		{
			doOnServletRequestDestroyed(new ObservedHttpServletRequest((HttpServletRequest) request));
		}
		else
		{
			doOnServletRequestDestroyed(new ObservedServletRequest(request));
		}
	}

}
