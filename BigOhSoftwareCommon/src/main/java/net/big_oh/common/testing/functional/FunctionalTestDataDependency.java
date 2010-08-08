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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * A class that represents the data dependencies for a {@link FunctionalTest}.
 * Data dependencies are setup before the FunctionalTest executes and are torn
 * down after the FunctionalTest executes. A single FunctionalTestDataDependency
 * can be recursively defined from multiple parent FunctionalTestDataDependency
 * objects.
 * 
 */
public abstract class FunctionalTestDataDependency
{

	private static final Log logger = LogFactory.getLog(FunctionalTestDataDependency.class);

	private final Set<FunctionalTestDataDependency> parentDependencies;

	/**
	 * 
	 * @param parentDependencies
	 *            A single FunctionalTestDataDependency can be recursively
	 *            defined from multiple parent FunctionalTestDataDependency
	 *            objects.
	 */
	public FunctionalTestDataDependency(Set<FunctionalTestDataDependency> parentDependencies)
	{
		this.parentDependencies = parentDependencies;
	}

	final void setUp(FunctionalTestDataDependencyResourceVisitor visitor) throws Exception
	{
		setUp(new HashSet<FunctionalTestDataDependency>(), visitor);
	}

	private void setUp(Set<FunctionalTestDataDependency> visitedDependencies, FunctionalTestDataDependencyResourceVisitor visitor) throws Exception
	{
		visitedDependencies.add(this);

		for (FunctionalTestDataDependency parentDep : parentDependencies)
		{
			if (!visitedDependencies.contains(parentDep))
			{
				parentDep.setUp(visitedDependencies, visitor);
			}
		}

		// finally, after all parent dependencies are setup, visit this
		// dependency
		logger.info("Setting up " + FunctionalTestDataDependency.class.getSimpleName() + ": " + this.getClass().getName());
		setUpTestData(visitor);
	}

	/**
	 * Setup test data in whatever manner is appropriate for your project.
	 * Consider making use of the
	 * {@link FunctionalTestDataDependencyResourceVisitor} to avoid setting up
	 * the same data resource multiple times.
	 * 
	 * @param visitor
	 * @throws Exception
	 */
	protected abstract void setUpTestData(FunctionalTestDataDependencyResourceVisitor visitor) throws Exception;

	final void tearDown(FunctionalTestDataDependencyResourceVisitor visitor)
	{
		tearDown(new HashSet<FunctionalTestDataDependency>(), visitor);
	}

	private void tearDown(Set<FunctionalTestDataDependency> visitedDependencies, FunctionalTestDataDependencyResourceVisitor visitor)
	{
		visitedDependencies.add(this);

		// prior to tearing down parent dependencies, tear down data for this
		// dependency
		logger.info("Tearing down " + FunctionalTestDataDependency.class.getSimpleName() + ": " + this.getClass().getName());
		tearDownTestData(visitor);

		for (FunctionalTestDataDependency parentDep : parentDependencies)
		{
			if (!visitedDependencies.contains(parentDep))
			{
				parentDep.tearDown(visitedDependencies, visitor);
			}
		}
	}

	/**
	 * Tear down test data in whatever manner is appropriate for your project.
	 * Consider making use of the
	 * {@link FunctionalTestDataDependencyResourceVisitor} to avoid tearing down
	 * the same data resource multiple times.
	 * 
	 * @param visitor
	 */
	protected abstract void tearDownTestData(FunctionalTestDataDependencyResourceVisitor visitor);

	public Set<FunctionalTestDataDependency> getParentDependencies()
	{
		return Collections.unmodifiableSet(parentDependencies);
	}

}
