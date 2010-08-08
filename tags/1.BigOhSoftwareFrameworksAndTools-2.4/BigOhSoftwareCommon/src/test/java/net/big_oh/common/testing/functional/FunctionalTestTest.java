package net.big_oh.common.testing.functional;

import java.util.HashSet;

import net.big_oh.common.testing.functional.FunctionalTest;
import net.big_oh.common.testing.functional.FunctionalTestDataDependency;
import net.big_oh.common.testing.functional.FunctionalTestDataDependencyResourceVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FunctionalTestTest extends FunctionalTest
{

	private static final Log logger = LogFactory.getLog(FunctionalTestTest.class);

	@Override
	public FunctionalTestDataDependency buildDataDependency()
	{
		return new FunctionalTestDataDependency(new HashSet<FunctionalTestDataDependency>(0))
		{

			@Override
			protected void setUpTestData(FunctionalTestDataDependencyResourceVisitor decorator)
			{
				logger.info("Simulated setUpTestData.");
			}

			@Override
			protected void tearDownTestData(FunctionalTestDataDependencyResourceVisitor decorator)
			{
				logger.info("Simulated tearDownTestData.");
			}

		};
	}

	@Override
	protected void doFunctionalTest()
	{
		logger.info("Simulated doFunctionalTest.");
	}

	@Override
	protected boolean isFunctionalTestEnabled()
	{
		return true;
	}

	@Override
	protected Object pSemaphore()
	{
		logger.info("Simulated pSemaphore.");
		return new Object();
	}

	@Override
	protected void vSemaphore(Object semaphoreCheckout)
	{
		logger.info("Simulated vSemaphore.");
	}

}
