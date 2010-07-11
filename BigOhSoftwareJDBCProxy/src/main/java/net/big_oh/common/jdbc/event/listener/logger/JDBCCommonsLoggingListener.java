package net.big_oh.common.jdbc.event.listener.logger;

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
 * A concrete extension of JDBCLoggingListener that logs SQL events via the
 * Apache commons-logging framework.
 */
public class JDBCCommonsLoggingListener extends JDBCLoggingListener
{

	Log logger = LogFactory.getLog(JDBCCommonsLoggingListener.class);

	public JDBCCommonsLoggingListener()
	{
		this(true, false);
	}
	
	public JDBCCommonsLoggingListener(boolean logBeforeEvent, boolean logAfterEvent)
	{
		super(logBeforeEvent, logAfterEvent);
	}

	@Override
	protected void debug(String msg)
	{
		logger.debug(msg);
	}

	@Override
	protected void info(String msg)
	{
		logger.info(msg);
	}

	

}
