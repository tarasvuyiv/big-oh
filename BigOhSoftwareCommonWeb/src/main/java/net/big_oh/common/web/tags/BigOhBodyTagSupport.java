package net.big_oh.common.web.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

public abstract class BigOhBodyTagSupport extends BodyTagSupport implements TryCatchFinally
{

	public BigOhBodyTagSupport()
	{
		doFinally();
	}

	public void doCatch(Throwable t) throws Throwable
	{
		throw new JspException(t);
	}

	protected void print(String str) throws JspException
	{

		try
		{
			pageContext.getOut().write(str);
		}
		catch (IOException ioe)
		{
			throw new JspTagException(ioe);
		}

	}

	protected void printLine(String line, int numTabs) throws JspException
	{

		for (int i = 0; i < numTabs; i++)
		{
			print("\t");
		}

		print(line);

		print("\n");
	}

	protected String getContextPath()
	{
		if (pageContext.getRequest() instanceof HttpServletRequest)
		{
			return ((HttpServletRequest) pageContext.getRequest()).getContextPath();
		}
		else
		{
			return null;
		}
	}

}
