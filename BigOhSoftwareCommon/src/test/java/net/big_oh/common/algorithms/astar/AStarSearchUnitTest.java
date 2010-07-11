package net.big_oh.common.algorithms.astar;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import net.big_oh.common.algorithms.astar.AStarSearch;
import net.big_oh.common.algorithms.astar.AStarSearchResult;
import net.big_oh.common.algorithms.astar.AStarSearch.AStarSearchType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


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
 * A unit test exercising the AStarSearch class.
 * 
 * @author davewingate
 * @version Nov 29, 2009
 */
public class AStarSearchUnitTest
{

	private static final Log logger = LogFactory.getLog(AStarSearchUnitTest.class);

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
	 * Test method for
	 * {@link net.big_oh.common.algorithms.astar.AStarSearch#doSearch(net.big_oh.common.algorithms.astar.AStarSearchNode)}
	 * .
	 */
	@Test
	public void testMinDoSearch()
	{
		TravelingSalesmanSearchNode minStartNode = new TravelingSalesmanSearchNode(new ArrayList<TravelingSalesmanDestination>(), Arrays.asList(TravelingSalesmanDestination.ALL_DESTINATIONS));
		AStarSearch<TravelingSalesmanSearchNode> minSearch = new AStarSearch<TravelingSalesmanSearchNode>(AStarSearchType.MIN, new TravelingSalesmanMinHeuristic(), new TravelingSalesmanNextNodeGenerator());
		AStarSearchResult<TravelingSalesmanSearchNode> minResult = minSearch.doSearch(minStartNode);

		assertNotNull(minResult.getGoalNode());
		assertTrue(minResult.getEfectiveBranchingFactor() >= 1.0);

		logger.info("Min path: " + minResult.getGoalNode().getPathAsString());
		logger.info("Min cost: " + minSearch.getF(minResult.getGoalNode()));
	}

	/**
	 * Test method for
	 * {@link net.big_oh.common.algorithms.astar.AStarSearch#doSearch(net.big_oh.common.algorithms.astar.AStarSearchNode)}
	 * .
	 */
	@Test
	public void testMaxDoSearch()
	{
		TravelingSalesmanSearchNode maxStartNode = new TravelingSalesmanSearchNode(new ArrayList<TravelingSalesmanDestination>(), Arrays.asList(TravelingSalesmanDestination.ALL_DESTINATIONS));
		AStarSearch<TravelingSalesmanSearchNode> maxSearch = new AStarSearch<TravelingSalesmanSearchNode>(AStarSearchType.MAX, new TravelingSalesmanMaxHeuristic(), new TravelingSalesmanNextNodeGenerator());
		AStarSearchResult<TravelingSalesmanSearchNode> maxResult = maxSearch.doSearch(maxStartNode);

		assertNotNull(maxResult.getGoalNode());
		assertTrue(maxResult.getEfectiveBranchingFactor() >= 1.0);

		logger.info("Max path: " + maxResult.getGoalNode().getPathAsString());
		logger.info("Max cost: " + maxSearch.getF(maxResult.getGoalNode()));
	}

	/**
	 * Test method for
	 * {@link net.big_oh.common.algorithms.astar.AStarSearch#calculateEffectiveBranchingFactor(long, long, int)}
	 * .
	 */
	@Test
	public void testCalculateEffectiveBranchingFactor()
	{
		assertTrue(1.0 == AStarSearch.calculateEffectiveBranchingFactor(0, 1, 10000));
	}

	/**
	 * Test method for
	 * {@link net.big_oh.common.algorithms.astar.AStarSearch#calculateEffectiveBranchingFactor(long, long, int)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCalculateEffectiveBranchingFactorIllegal()
	{
		AStarSearch.calculateEffectiveBranchingFactor(11, 11, 10000);
	}

}
