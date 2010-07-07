package net.big_oh.common.testing.functional;

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

import net.big_oh.common.utils.Duration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * 
 * A base class supporting the setup and tear down of the
 * {@link FunctionalTestDataDependency} needed to execute the functional test.
 * 
 */

public abstract class FunctionalTest
{

	private static final Log logger = LogFactory.getLog(FunctionalTest.class);

	private final FunctionalTestDataDependency dataDependency;

	private Object semaphoreCheckout;

	protected FunctionalTest()
	{
		logger.info("------------------ Start " + FunctionalTest.class.getSimpleName() + ": " + this.getClass().getName() + " ------------------");

		this.dataDependency = buildDataDependency();

		Duration initDuration = new Duration();
		try
		{
			logger.info("Initializing " + this.getClass().getSimpleName() + ".");
			initialize();
		}
		catch (RuntimeException re)
		{
			logger.error("Initialize failed for " + this.getClass().getSimpleName() + ".", re);
			throw re;
		}
		finally
		{
			logger.info("Initialization of " + this.getClass().getSimpleName() + " completed in " + initDuration.stop() + " milliseconds.");
		}

	}

	/**
	 * Performs any extra initialization steps not covered by the test's
	 * FunctionalTestDataDependency. In most cases, you should not need to
	 * override this method.
	 */
	protected void initialize()
	{
		// Override as needed in child classes
	}

	/**
	 * Allows a functional test to be disabled (i.e. passed without execution).
	 * 
	 * @return
	 */
	protected abstract boolean isFunctionalTestEnabled();

	/**
	 * Setup all data needed to run the functional test, but only if those data
	 * resources are not currently in use by another functional test.
	 * 
	 * @throws Exception
	 * @see #pSemaphore
	 */
	@Before
	public final void setUp() throws Exception
	{
		if (isFunctionalTestEnabled())
		{
			logger.info("Setting up " + this.getClass().getSimpleName() + ".");
			Duration setUpDuration = new Duration();
			try
			{
				semaphoreCheckout = pSemaphore();
				dataDependency.setUp(new FunctionalTestDataDependencyResourceVisitor());
			}
			catch (RuntimeException re)
			{
				logger.error("Setup failed for " + this.getClass().getSimpleName() + ".", re);
				throw re;
			}
			finally
			{
				logger.info("Setup of " + this.getClass().getSimpleName() + " completed in " + setUpDuration.stop() + " milliseconds.");
			}
		}
	}

	/**
	 * Optionally checkout a semaphore that is used to prevent concurrent
	 * execution of FunctionalTests that would collide. Return null if this
	 * functional test does not collide with any other functional tests and
	 * would not collide with another instance of this functional test class.
	 * 
	 * @return
	 * @throws RuntimeException
	 */
	protected abstract Object pSemaphore() throws RuntimeException;

	/**
	 * Check in (i.e. return) the semaphore that prevents this functional test
	 * from colliding with other functional tests.
	 * 
	 * @param semaphore
	 */
	protected abstract void vSemaphore(Object semaphore);

	/**
	 * Tear down all data dependencies that were setup for this functional test.
	 * 
	 * @throws Exception
	 */
	@After
	public final void tearDown() throws Exception
	{
		if (isFunctionalTestEnabled())
		{
			logger.info("Tearing down " + this.getClass().getSimpleName() + ".");
			Duration tearDownDuration = new Duration();
			try
			{
				dataDependency.tearDown(new FunctionalTestDataDependencyResourceVisitor());
				vSemaphore(semaphoreCheckout);
			}
			catch (RuntimeException re)
			{
				logger.error("Tear down failed for " + this.getClass().getSimpleName() + ".", re);
				throw re;
			}
			finally
			{
				logger.info("Tear down of " + this.getClass().getSimpleName() + " completed in " + tearDownDuration.stop() + " milliseconds.");
			}
		}
	}

	/**
	 * 
	 * @return Construct a {@link FunctionalTestDataDependency} object that
	 *         specifies the data dependencies for this FunctionalTest.
	 */
	public abstract FunctionalTestDataDependency buildDataDependency();

	/**
	 * A wrapper method that delegates to doFunctionalTest().
	 * 
	 * @see #doFunctionalTest
	 */
	@Test
	public final void functionalTest()
	{
		if (isFunctionalTestEnabled())
		{
			Duration tearDownDuration = new Duration();
			try
			{
				logger.info("Executing " + this.getClass().getSimpleName() + ".");
				doFunctionalTest();
			}
			catch (RuntimeException re)
			{
				logger.error("Functional test " + this.getClass().getSimpleName() + " failed.", re);
				throw re;
			}
			finally
			{
				logger.info("Functional test " + this.getClass().getSimpleName() + " completed in " + tearDownDuration.stop() + " milliseconds.");
			}
		}
		else
		{
			logger.warn("Functional test " + this.getClass().getSimpleName() + " is disabled.");
		}
	}

	/**
	 * This method is the entry point for all testing activities performed by
	 * the FunctionalTest. Within this method (and its delegates) you are free
	 * to rely on jUnit assertions, just as you would within a traditional unit
	 * test.
	 */
	protected abstract void doFunctionalTest();

}
