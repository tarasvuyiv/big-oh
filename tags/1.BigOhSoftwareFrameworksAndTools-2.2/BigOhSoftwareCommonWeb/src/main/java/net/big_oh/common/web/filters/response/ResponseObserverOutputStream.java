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

import javax.servlet.ServletOutputStream;

public class ResponseObserverOutputStream extends ServletOutputStream
{

	private final ServletOutputStream servletOutputStream;
	private final ResponseObserver observer;

	public ResponseObserverOutputStream(ServletOutputStream servletOutputStream, ResponseObserver observer)
	{
		this.servletOutputStream = servletOutputStream;
		this.observer = observer;
	}

	@Override
	public void print(boolean b) throws IOException
	{
		servletOutputStream.print(b);

		observer.doAfterBytesWriten(String.valueOf(b).getBytes());
	}

	@Override
	public void print(char c) throws IOException
	{
		servletOutputStream.print(c);

		observer.doAfterBytesWriten(String.valueOf(c).getBytes());
	}

	@Override
	public void print(double d) throws IOException
	{
		servletOutputStream.print(d);

		observer.doAfterBytesWriten(String.valueOf(d).getBytes());
	}

	@Override
	public void print(float f) throws IOException
	{
		servletOutputStream.print(f);

		observer.doAfterBytesWriten(String.valueOf(f).getBytes());
	}

	@Override
	public void print(int i) throws IOException
	{
		servletOutputStream.print(i);

		observer.doAfterBytesWriten(String.valueOf(i).getBytes());
	}

	@Override
	public void print(long l) throws IOException
	{
		servletOutputStream.print(l);

		observer.doAfterBytesWriten(String.valueOf(l).getBytes());
	}

	@Override
	public void print(String s) throws IOException
	{
		servletOutputStream.print(s);

		observer.doAfterBytesWriten(s.getBytes());
	}

	@Override
	public void println() throws IOException
	{
		servletOutputStream.println();

		observer.doAfterBytesWriten("\r\n".getBytes());
	}

	@Override
	public void println(boolean b) throws IOException
	{
		print(b);
		println();
	}

	@Override
	public void println(char c) throws IOException
	{
		print(c);
		println();
	}

	@Override
	public void println(double d) throws IOException
	{
		print(d);
		println();
	}

	@Override
	public void println(float f) throws IOException
	{
		print(f);
		println();
	}

	@Override
	public void println(int i) throws IOException
	{
		print(i);
		println();
	}

	@Override
	public void println(long l) throws IOException
	{
		print(l);
		println();
	}

	@Override
	public void println(String s) throws IOException
	{
		print(s);
		println();
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		servletOutputStream.write(b, off, len);

		byte[] bytesWritten = new byte[len];
		for (int i = 0; i < len; i++)
		{
			bytesWritten[i] = b[off + i];
		}
		observer.doAfterBytesWriten(bytesWritten);
	}

	@Override
	public void write(byte[] b) throws IOException
	{
		servletOutputStream.write(b);

		observer.doAfterBytesWriten(b);
	}

	@Override
	public void write(int b) throws IOException
	{
		servletOutputStream.write(b);

		byte[] bytesWritten = new byte[1];
		bytesWritten[0] = (byte) b;
		observer.doAfterBytesWriten(bytesWritten);
	}

	@Override
	public void close() throws IOException
	{
		servletOutputStream.close();
	}

	@Override
	public void flush() throws IOException
	{
		servletOutputStream.flush();
	}

}
