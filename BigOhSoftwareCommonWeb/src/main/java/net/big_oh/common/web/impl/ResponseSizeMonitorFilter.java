package net.big_oh.common.web.impl;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.big_oh.common.web.filters.response.ResponseObserverFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ResponseSizeMonitorFilter extends ResponseObserverFilter
{

	private static final Log logger = LogFactory.getLog(ResponseSizeMonitorFilter.class);

	private final ConcurrentHashMap<Thread, Long> threadToByteCountMap = new ConcurrentHashMap<Thread, Long>();

	public void doBeforeResponseStarted(ServletRequest req, ServletResponse resp)
	{
		threadToByteCountMap.put(Thread.currentThread(), new Long(0));
	}

	public void doAfterResponseFinished(ServletRequest req, ServletResponse resp)
	{
		long numBytesInResponse = threadToByteCountMap.get(Thread.currentThread());
		logger.info("RESPONSE -- FINISH -- Response for the requested url (" + getUrl(req) + ") contained about " + new DecimalFormat("###,##0.00").format(numBytesInResponse / 1024.0) + " kilobytes.");

		// remove entry in threadToByteCountMap so that the Long value can be
		// garbage collected
		threadToByteCountMap.remove(Thread.currentThread());
	}

	private String getUrl(ServletRequest req)
	{
		if (req instanceof HttpServletRequest)
		{
			return ((HttpServletRequest) req).getRequestURL().toString();
		}
		else
		{
			return "unknown; non HTTP";
		}
	}

	public void doAfterCharsWritten(char[] charsWritten)
	{
		synchronized (threadToByteCountMap)
		{
			Long currentByteCount = threadToByteCountMap.get(Thread.currentThread());
			// Note: assuming that print writer is using the ISO-8859-1
			// encoding, which prints 1 byte per character.
			// Would need to update if you want to support counting response
			// bytes for other CharSets.
			threadToByteCountMap.put(Thread.currentThread(), new Long(currentByteCount + (charsWritten.length * 1)));
		}
	}

	public void doAfterBytesWriten(byte[] bytesWritten)
	{
		synchronized (threadToByteCountMap)
		{
			Long currentByteCount = threadToByteCountMap.get(Thread.currentThread());
			threadToByteCountMap.put(Thread.currentThread(), new Long(currentByteCount + bytesWritten.length));
		}
	}

}
