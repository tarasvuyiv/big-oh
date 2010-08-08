package net.big_oh.common.testing.functional;

import java.util.HashSet;
import java.util.Set;

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

/**
 * 
 * Allows domain-specific FunctionalTestDataDependency objects to record which
 * resources have already been setup for a particular functional test. For
 * example, some FunctionalTestDataDependency objects will be composed from
 * multiple other FunctionalTestDataDependency objects that partially overlap
 * with respect to the data that they setup. In such cases, it is desirable (and
 * maybe even necessary) to avoid setting up the same data twice.
 * 
 */
public class FunctionalTestDataDependencyResourceVisitor
{

	Set<Object> visitedResources = new HashSet<Object>();

	/**
	 * Domain-specific FunctionalTestDataDependency objects should call this
	 * method as they setup data resources in the setUp() method.
	 * 
	 * @see FunctionalTestDataDependency#setUp
	 * @param resource
	 */
	public void visitResource(Object resource)
	{
		visitedResources.add(resource);
	}

	/**
	 * Domain-specific FunctionalTestDataDependency objects can make use of this
	 * method to prevent setting up the same data multiple times for a single
	 * functional test.
	 * 
	 * @param resource
	 * @return Returns true if the (data) resource has already been visited
	 *         (i.e. setup).
	 */
	public boolean hasResourceBeenVisited(Object resource)
	{
		return visitedResources.contains(resource);
	}

}
