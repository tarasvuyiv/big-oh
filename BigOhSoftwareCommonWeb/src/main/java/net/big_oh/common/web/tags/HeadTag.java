package net.big_oh.common.web.tags;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

public abstract class HeadTag extends BigOhBodyTagSupport implements Serializable
{

	protected String title;
	protected boolean noCache;
	protected String description;
	protected String commaSeparatedKeywords;
	protected String commaSeparatedContextRelativeStyleSheetNms;
	protected String commaSeparatedContextRelativeJavascriptFileNms;

	public void doFinally()
	{
		title = null;
		noCache = getDefaultNoCacheValue();
		commaSeparatedKeywords = null;
		description = null;
		commaSeparatedContextRelativeStyleSheetNms = getDefaultCommaSeparatedContextRelativeStyleSheetNms();
		commaSeparatedContextRelativeJavascriptFileNms = getDefaultCommaSeparatedContextRelativeJavascriptFileNms();
	}

	protected abstract boolean getDefaultNoCacheValue();

	protected abstract String getDefaultCommaSeparatedContextRelativeStyleSheetNms();

	protected abstract String getDefaultCommaSeparatedContextRelativeJavascriptFileNms();

	protected abstract String getGlobalTitlePrefix();

	@Override
	public int doStartTag() throws JspException
	{

		printLine("<head>", 0);

		printLine("<title>" + getGlobalTitlePrefix() + title + "</title>", 1);

		printLine("", 1);

		printLine("<!-- Meta tags -->", 1);
		printLine("<meta http-equiv='content-type' content='text/html; charset=utf-8' />", 1);
		if (commaSeparatedKeywords != null)
		{
			printLine("<meta name='keywords' content='" + commaSeparatedKeywords + "' />", 1);
		}
		if (description != null)
		{
			printLine("<meta name='description' content='" + description + "' />", 1);
		}

		Map<String, String> extraMetaInfo = getExtraMetaInfo();
		for (String name : extraMetaInfo.keySet())
		{
			String content = extraMetaInfo.get(name);
			printLine("<meta name='" + name + "' content='" + content + "' />", 1);
		}

		if (noCache)
		{
			printLine("<meta http-equiv=\"pragma\" content=\"no-cache\">", 1);
			printLine("<meta http-equiv=\"expires\" content=\"-1\">", 1);

			if (pageContext.getResponse() instanceof HttpServletResponse && !pageContext.getResponse().isCommitted())
			{
				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				response.setHeader("Cache-Control", "no-cache,post-check=0,pre-check=0,no-store");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Expires", "-1");
			}
		}

		if (commaSeparatedContextRelativeStyleSheetNms != null)
		{
			printLine("", 1);

			printLine("<!-- Style sheets -->", 1);
			for (String contextRelativeStyleSheet : commaSeparatedContextRelativeStyleSheetNms.split("\\s*,\\s*"))
			{
				printLine("<link href='" + getContextPath() + ((contextRelativeStyleSheet.indexOf("/") != 0) ? "/" : "") + contextRelativeStyleSheet + "' rel='stylesheet' type='text/css' />", 1);
			}
		}

		if (commaSeparatedContextRelativeStyleSheetNms != null)
		{
			printLine("", 1);

			printLine("<!-- Javascript files -->", 1);
			for (String contextRelativeJavascriptFile : commaSeparatedContextRelativeJavascriptFileNms.split("\\s*,\\s*"))
			{
				printLine("<script type=\"text/javascript\" src=\"" + getContextPath() + ((contextRelativeJavascriptFile.indexOf("/") != 0) ? "/" : "") + contextRelativeJavascriptFile + "\"></script>", 1);

			}
		}

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException
	{

		printLine("</head>", 0);

		return EVAL_PAGE;
	}

	protected Map<String, String> getExtraMetaInfo()
	{
		// override in child classes as needed
		return Collections.emptyMap();
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public boolean isNoCache()
	{
		return noCache;
	}

	public void setNoCache(boolean noCache)
	{
		this.noCache = noCache;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getCommaSeparatedKeywords()
	{
		return commaSeparatedKeywords;
	}

	public void setCommaSeparatedKeywords(String commaSeparatedKeywords)
	{
		this.commaSeparatedKeywords = commaSeparatedKeywords;
	}

	public String getCommaSeparatedContextRelativeStyleSheetNms()
	{
		return commaSeparatedContextRelativeStyleSheetNms;
	}

	public void setCommaSeparatedContextRelativeStyleSheetNms(String commaSeparatedContextRelativeStyleSheetNms)
	{
		this.commaSeparatedContextRelativeStyleSheetNms = commaSeparatedContextRelativeStyleSheetNms;
	}

}
