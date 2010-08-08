package net.big_oh.common.web.impl;

import net.big_oh.common.web.listener.request.ObservedServletRequest;
import net.big_oh.common.web.listener.request.RequestObserverListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class RequestLoggerListener extends RequestObserverListener
{

	private static final Log logger = LogFactory.getLog(RequestLoggerListener.class);

	@Override
	protected void doOnServletRequestInitialized(ObservedServletRequest observedServletRequest)
	{
		logger.info("REQUEST  -- START  -- " + observedServletRequest);
	}

	@Override
	protected void doOnServletRequestDestroyed(ObservedServletRequest observedServletRequest)
	{
		logger.info("REQUEST  -- FINISH -- " + observedServletRequest);
	}

}
