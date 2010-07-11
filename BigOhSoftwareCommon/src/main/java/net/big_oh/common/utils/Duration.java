package net.big_oh.common.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

/**
 * The Duration class encapsulates the concept of measuring the time required to
 * complete an activity, like the execution of one or more methods.
 */
public class Duration
{

	private static final Log defaultLogger = LogFactory.getLog(Duration.class);

	private final long started;
	private final Log logger;
	private DURATION_STATUS status;

	/**
	 * Constructs a default Duration instance using the Duration class to derive
	 * an appropriate logger.
	 */
	public Duration()
	{
		this(null);
	}

	/**
	 * Constructs a default Duration instance using callingClass to derive an
	 * appropriate logger.
	 * 
	 * @param callingClass
	 */
	public Duration(Class<?> callingClass)
	{
		started = System.currentTimeMillis();
		logger = (callingClass == null) ? defaultLogger : LogFactory.getLog(callingClass);
		status = DURATION_STATUS.STARTED;
	}

	/**
	 * Indicate that the duration has ended.
	 * 
	 * @return Length of duration in milliseconds.
	 */
	public long stop()
	{
		return stop(null);
	}

	/**
	 * Indicate that the duration has ended, logging an appropriate
	 * informational message.
	 * 
	 * @param msg
	 *            A message to be logged at info level.
	 * @return Length of duration in milliseconds.
	 */
	public long stop(String msg)
	{

		if (!status.equals(DURATION_STATUS.STARTED))
		{
			throw new RuntimeException("Duration is already been stopped.");
		}

		status = DURATION_STATUS.STOPPED;

		long durationInMilliseconds = System.currentTimeMillis() - started;

		if (!StringUtils.isBlank(msg))
		{
			logger.info("Time for " + msg + ": " + durationInMilliseconds + " millisecoonds.");
		}

		return durationInMilliseconds;

	}

	private enum DURATION_STATUS
	{
		STARTED, STOPPED;
	}

}