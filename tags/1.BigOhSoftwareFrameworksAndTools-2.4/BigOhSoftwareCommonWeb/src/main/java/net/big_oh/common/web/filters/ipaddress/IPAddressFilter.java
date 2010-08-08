package net.big_oh.common.web.filters.ipaddress;

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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A filter that supports securing a web application so that only certain IP
 * addresses may access it. This filter supports two type of rules: black list
 * rules & white list rules. A request that matches a white list rule will
 * ignore any matching black list rule, allowing the request to process
 * normally.
 * 
 * @author dwingate
 * 
 */
public class IPAddressFilter implements Filter
{

	private static final Log logger = LogFactory.getLog(IPAddressFilter.class);

	private Set<IPAddressWhiteListFilterRule> whiteListRules;
	private Set<IPAddressBlackListFilterRule> blackListRules;

	public void init(FilterConfig filterConfig) throws ServletException
	{
		whiteListRules = new HashSet<IPAddressWhiteListFilterRule>();
		blackListRules = new HashSet<IPAddressBlackListFilterRule>();

		// Add rules defined in filter config
		initIPAddressFilteringRules(filterConfig);

		// Add programmatically-defined rules
		initIPAddressFilteringRules();

	}

	@SuppressWarnings(value = "unchecked")
	private void initIPAddressFilteringRules(FilterConfig filterConfig)
	{

		Enumeration<String> filterInitParamNames = filterConfig.getInitParameterNames();
		while (filterInitParamNames.hasMoreElements())
		{
			String initParamName = filterInitParamNames.nextElement();
			String initParamValue = filterConfig.getInitParameter(initParamName);

			if (initParamName.indexOf(".") != -1)
			{

				IPAddressFilterRule newFilterRuleFromFilterConfigParam = instantiateIPAddressFilterRule(initParamName, initParamValue);

				if (newFilterRuleFromFilterConfigParam != null)
				{
					registerFilterRuleWithFilter(newFilterRuleFromFilterConfigParam);
				}

			}
			else
			{
				logger.warn("Ignoring filter init parameter '" + initParamName + "' because it doesn't seem to be the name of a Class.");
			}
		}

	}

	/**
	 * A hook that child classes can use to add filter rules that were not
	 * defined in the FilterConfig.
	 */
	protected void initIPAddressFilteringRules()
	{
		// override as needed.
	}

	protected final boolean registerFilterRuleWithFilter(IPAddressFilterRule filterRule)
	{

		boolean addedRuleSomewhere = false;

		if (IPAddressWhiteListFilterRule.class.isAssignableFrom(filterRule.getClass()))
		{
			whiteListRules.add((IPAddressWhiteListFilterRule) filterRule);
			addedRuleSomewhere = true;
		}

		if (IPAddressBlackListFilterRule.class.isAssignableFrom(filterRule.getClass()))
		{
			blackListRules.add((IPAddressBlackListFilterRule) filterRule);
			addedRuleSomewhere = true;
		}

		if (!addedRuleSomewhere)
		{
			logger.warn(IPAddressFilterRule.class.getSimpleName() + " (" + filterRule.toString() + ") is an unexpected type; ignoring.");
		}
		else
		{
			logger.info("Registered new filter rule (" + filterRule.toString() + ").");
		}

		return addedRuleSomewhere;

	}

	public void destroy()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{

		if (response instanceof HttpServletResponse)
		{

			HttpServletResponse resp = (HttpServletResponse) response;

			// check white list rules
			for (IPAddressWhiteListFilterRule whiteListRule : whiteListRules)
			{
				if (whiteListRule.doesRequestingAddressMatchRule(InetAddress.getByName(request.getRemoteAddr())))
				{
					logger.info("Request from " + request.getRemoteAddr() + " permitted because of matching white list rule (" + whiteListRule.toString() + ").");
					chain.doFilter(request, response);
					return;
				}
			}

			// check blacklist rules
			for (IPAddressBlackListFilterRule blackListRule : blackListRules)
			{
				if (blackListRule.doesRequestingAddressMatchRule(InetAddress.getByName(request.getRemoteAddr())))
				{
					logger.info("Request from " + request.getRemoteAddr() + " rejected because of matching black list rule (" + blackListRule.toString() + ").");
					resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
			}

			logger.info("Request from " + request.getRemoteAddr() + " permitted because no matching rules were found.");
			chain.doFilter(request, response);
			return;

		}
		else
		{
			logger.info("Encountered a non-HTTP request.  Processing normally.");
			chain.doFilter(request, response);
			return;
		}

	}

	private static IPAddressFilterRule instantiateIPAddressFilterRule(String className, String constructorParam)
	{

		IPAddressFilterRule newIPAddressFilterRule = null;

		try
		{
			// Get the class declared in the FilterConfig parameter.
			Class<?> filterRuleClass = Class.forName(className);

			// Try to instantiate an instance of the IPAddressFilterRule class
			// Look for an appropriate constructor that takes a single
			// java.lang.String
			Constructor<?> constructor = filterRuleClass.getConstructor(String.class);

			newIPAddressFilterRule = (IPAddressFilterRule) constructor.newInstance(constructorParam);
		}
		catch (ClassNotFoundException cnfe)
		{
			logger.error("Could not find a class named " + className);
		}
		catch (NoSuchMethodException e)
		{
			logger.error("Could not instantiate an instance of class " + className + ".");
			logger.error(e);
		}
		catch (InstantiationException e)
		{
			logger.error("Could not instantiate an instance of class " + className + ".");
			logger.error(e);
		}
		catch (IllegalAccessException e)
		{
			logger.error("Could not instantiate an instance of class " + className + ".");
			logger.error(e);
		}
		catch (InvocationTargetException e)
		{
			logger.error("Could not instantiate an instance of class " + className + ".");
			logger.error(e);
		}
		catch (RuntimeException e)
		{
			logger.error("Could not instantiate an instance of class " + className + ".");
			logger.error(e);
		}

		return newIPAddressFilterRule;

	}

}
