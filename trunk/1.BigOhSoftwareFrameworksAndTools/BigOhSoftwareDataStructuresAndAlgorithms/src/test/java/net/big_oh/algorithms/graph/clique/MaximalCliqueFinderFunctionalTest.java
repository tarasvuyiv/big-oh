package net.big_oh.algorithms.graph.clique;

import net.big_oh.datastructures.graph.jung.sparse.JungVertex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/*
 Copyright (c) 2010 Dave Wingate dba Big-Oh Software (www.big-oh.net)

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
 * An abstract functional test to be used in testing all MaximalCliqueFinders.
 * 
 * Whereas {@link MaximalCliqueFinderUnitTest} focuses on verifying the basic
 * contractual behavior of maximal clique finders, this test exhaustively
 * confirms that the maximal clique finder is capable of finding all possible
 * maximal cliques.
 * 
 * @author dwingate
 * @version Aug 8, 2010
 */
public abstract class MaximalCliqueFinderFunctionalTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * @return Returns the instance of MaximalCliqueFinder to be tested by this
	 *         unit test.
	 */
	protected abstract MaximalCliqueFinder<JungVertex> buildCliqueFinder();

	@Test
	public void testFindMaximalCliques()
	{
		// TODO DSW
	}

}
