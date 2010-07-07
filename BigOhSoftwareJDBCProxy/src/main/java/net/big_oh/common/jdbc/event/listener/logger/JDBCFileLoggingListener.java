package net.big_oh.common.jdbc.event.listener.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.big_oh.common.utils.SystemPropertyUtil;

import org.apache.commons.io.IOUtils;
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
 * A concrete extension of JDBCLoggingListener that logs SQL events to a temp
 * file.
 */
public class JDBCFileLoggingListener extends JDBCLoggingListener
{

	Log logger = LogFactory.getLog(JDBCFileLoggingListener.class);

	FileWriter writer = null;

	public JDBCFileLoggingListener(boolean logBeforeEvent, boolean logAfterEvent)
	{
		super(logBeforeEvent, logAfterEvent);

		try
		{
			writer = new FileWriter(File.createTempFile(JDBCFileLoggingListener.class.getSimpleName(), ".log"), true);
		}
		catch (IOException e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	protected void debug(String msg)
	{
		// File-based logger doesn't differentiate log levels.
		log(msg);
	}

	@Override
	protected void info(String msg)
	{
		// File-based logger doesn't differentiate log levels.
		log(msg);
	}
	
	protected void log(String msg)
	{
		try
		{
			if (writer != null)
			{
				writer.write(msg + SystemPropertyUtil.getSystemLineSeparator());
				writer.flush();
			}
		}
		catch (IOException e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		IOUtils.closeQuietly(writer);
	}

}
