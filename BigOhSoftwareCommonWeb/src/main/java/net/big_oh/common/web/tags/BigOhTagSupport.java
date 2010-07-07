/*
 * Created on Mar 10, 2008 by davidwingate
 */
package net.big_oh.common.web.tags;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import net.big_oh.common.utils.RegExpHelper;

import org.apache.commons.lang.StringUtils;


public abstract class BigOhTagSupport extends TagSupport implements TryCatchFinally, Serializable
{

	public BigOhTagSupport()
	{
		doFinally();
	}

	public void doCatch(Throwable t) throws Throwable
	{
		throw new JspException(t);
	}

	/**
	 * Print to JspWriter, handling the IOException.
	 * 
	 * @param str
	 * @throws JspException
	 */
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

	/**
	 * Convenience method that auto-formats an HTML line by pre-pending numTabs
	 * tab characters and post-pending a new line character.
	 * 
	 * @param line
	 * @param numTabs
	 * @throws JspException
	 */
	protected void printLine(String line, int numTabs) throws JspException
	{

		for (int i = 0; i < numTabs; i++)
		{
			print("\t");
		}

		print(line);

		print("\n");
	}

	/**
	 * Convenience method for starting an HTML div element.
	 * 
	 * @param divId
	 * @param numTabs
	 * @throws JspException
	 */
	protected void openDiv(String divId, int numTabs) throws JspException {
		openDiv(divId, null, null, numTabs);
	}
	
	/**
	 * Convenience method for starting an HTML div element.
	 * 
	 * @param divId
	 * @param cssClasses 
	 * @param cssStyles 
	 * @param numTabs
	 * @throws JspException
	 */
	protected void openDiv(String divId, String cssClasses, String cssStyles, int numTabs) throws JspException
	{
		printLine("", numTabs);
		printLine("<!-- start of '" + divId + "' div -->", numTabs);
		
		StringBuffer divBuffer = new StringBuffer();
		divBuffer.append("<div id=\"" + divId);
		divBuffer.append(StringUtils.isBlank(cssClasses) ? "" : " class=\"" + cssClasses + "\"");
		divBuffer.append(StringUtils.isBlank(cssStyles) ? "" : " style=\"" + cssStyles + "\"");
		divBuffer.append("\">");
		printLine(divBuffer.toString(), numTabs);
		
		printLine("", numTabs);
	}

	/**
	 * Convenience method for closing an HTML div element.
	 * 
	 * @param divId
	 * @param numTabs
	 * @throws JspException
	 */
	protected void closeDiv(String divId, int numTabs) throws JspException
	{
		printLine("", numTabs);
		printLine("</div>", numTabs);
		printLine("<!-- end of '" + divId + "' div -->", numTabs);
		printLine("", numTabs);
	}

	/**
	 * Convenience method for obtaining the context path of the current web
	 * application.
	 * 
	 * @return Returns the context path for the current web application.
	 */
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

	/**
	 * Uses the nameOrId parameter to derive a value that is safe to use as an
	 * HTML name or id attribute value.
	 * 
	 * @param nameOrId
	 *            The candidate value for an HTML name or id atribute.
	 * @return A String that is suitable for use as an HTML name or id
	 *         attribute.
	 */
	public static String sanitizeHtmlNameOrId(String nameOrId)
	{
		return RegExpHelper.replaceAll(nameOrId, "\\W", "_");
	}

}
