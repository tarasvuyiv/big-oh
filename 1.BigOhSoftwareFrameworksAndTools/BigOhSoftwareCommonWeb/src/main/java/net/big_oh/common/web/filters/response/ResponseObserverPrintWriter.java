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

import java.io.PrintWriter;

public class ResponseObserverPrintWriter extends PrintWriter
{
	private final ResponseObserver observer;

	ResponseObserverPrintWriter(PrintWriter writer, ResponseObserver observer)
	{
		super(writer);
		this.observer = observer;
	}

	@Override
	public void write(int c)
	{
		super.write(c);

		char[] charsWritten = new char[1];
		charsWritten[0] = (char) c;
		observer.doAfterCharsWritten(charsWritten);
	}

	@Override
	public void write(char[] buf, int off, int len)
	{
		super.write(buf, off, len);

		char[] charsWritten = new char[len];
		for (int i = 0; i < len; i++)
		{
			charsWritten[i] = buf[off + i];
		}
		observer.doAfterCharsWritten(charsWritten);
	}

	@Override
	public void write(char[] buf)
	{
		super.write(buf);

		observer.doAfterCharsWritten(buf);
	}

	@Override
	public void write(String s, int off, int len)
	{
		super.write(s, off, len);

		observer.doAfterCharsWritten(s.substring(off, off + len).toCharArray());
	}

	@Override
	public void write(String s)
	{
		super.write(s);

		observer.doAfterCharsWritten(s.toCharArray());
	}

}
