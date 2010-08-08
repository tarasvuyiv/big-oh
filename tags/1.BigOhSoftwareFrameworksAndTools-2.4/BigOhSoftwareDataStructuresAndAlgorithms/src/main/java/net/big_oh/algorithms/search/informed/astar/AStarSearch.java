package net.big_oh.algorithms.search.informed.astar;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.big_oh.common.utils.Duration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
 * A Template Method implementation of the A* algorithm.
 * 
 * @author davewingate
 * @version Nov 29, 2009
 * @param <SearchNodeType>
 */
public class AStarSearch<SearchNodeType extends AStarSearchNode>
{

	private static final Log logger = LogFactory.getLog(AStarSearch.class);

	private final AStarSearchType searchType;
	private final Heuristic<SearchNodeType> heuristic;
	private final NextNodesGenerator<SearchNodeType> nextNodesGenerator;

	public AStarSearch(AStarSearchType searchType, Heuristic<SearchNodeType> heuristic, NextNodesGenerator<SearchNodeType> nextNodesGenerator)
	{
		this.searchType = searchType;
		this.heuristic = heuristic;
		this.nextNodesGenerator = nextNodesGenerator;
	}

	public AStarSearchResult<SearchNodeType> doSearch(SearchNodeType startNode)
	{

		Duration searchDuration = new Duration();

		// create priority queue
		SortedSet<SearchNodeType> prioritySet = new TreeSet<SearchNodeType>(new Comparator<SearchNodeType>()
		{

			public int compare(SearchNodeType o1, SearchNodeType o2)
			{
				switch (searchType)
				{
				case MIN:
					return Double.compare(getF(o1), getF(o2));
				case MAX:
					return -1 * Double.compare(getF(o1), getF(o2));
				default:
					throw new RuntimeException("Unexpected search type: " + searchType);
				}
			}

		});

		// enqueue the start node
		prioritySet.add(startNode);

		// declare tracking member variables
		int numSearchNodesGenerated = 0;
		int numSearchNodesConsidered = 0;
		int maxPossibleBranchingFactor = 1;

		// search for a goal state
		SearchNodeType goalNode = null;
		while (!prioritySet.isEmpty())
		{

			// Remove the best candidate node from the queue
			SearchNodeType candidateSearchNode = prioritySet.first();
			prioritySet.remove(candidateSearchNode);
			numSearchNodesConsidered++;

			// get the next search node candidates
			Collection<SearchNodeType> nextSearchNodes = nextNodesGenerator.getNextSearchNodes(candidateSearchNode);

			// do some record keeping
			numSearchNodesGenerated += nextSearchNodes.size();
			maxPossibleBranchingFactor = Math.max(maxPossibleBranchingFactor, nextSearchNodes.size());

			if (candidateSearchNode.isGoalState())
			{
				// sanity check
				assert (nextSearchNodes.isEmpty());

				// found an optimal solution
				goalNode = candidateSearchNode;
				break;
			}
			else
			{
				// enqueue all next search nodes
				Duration enqueueDuration = new Duration();

				prioritySet.addAll(nextSearchNodes);

				if (logger.isDebugEnabled())
				{
					logger.debug("Enqueued " + nextSearchNodes.size() + " A* search nodes in " + enqueueDuration.stop() + " milliseconds.");
				}
			}

		}

		// return the search results
		AStarSearchResult<SearchNodeType> results = new AStarSearchResult<SearchNodeType>(goalNode, numSearchNodesGenerated, calculateEffectiveBranchingFactor(goalNode.getNodeDepth(), numSearchNodesConsidered, maxPossibleBranchingFactor));

		logger.debug("Completed an A* search in " + searchDuration.stop() + " milliseconds.");
		logger.debug("Number of nodes generated: " + results.getNumSearchNodesGenerated());
		logger.debug("Depth of goal node: " + results.getGoalNode().getNodeDepth());
		logger.debug("Effective branching factor: " + results.getEfectiveBranchingFactor());

		return results;

	}

	public Heuristic<SearchNodeType> getHeuristic()
	{
		return heuristic;
	}

	public AStarSearchType getSearchType()
	{
		return searchType;
	}

	public double getF(SearchNodeType searchNode)
	{
		return searchNode.getG() + heuristic.getH(searchNode);
	}

	static double calculateEffectiveBranchingFactor(long solutionDepth, long numSearchNodesConsidered, int maxPossibleBranchingFactor)
	{
		// sanity check
		if (solutionDepth >= numSearchNodesConsidered)
		{
			throw new IllegalArgumentException("The solutionDepth argument must always be less than the numSearchNodesConsidered argument.");
		}

		// edge case
		if (solutionDepth == 0)
		{
			return 1.0;
		}

		double ebf = Math.pow((double) numSearchNodesConsidered, (1.0 / solutionDepth));
		return Math.min(ebf, maxPossibleBranchingFactor);
	}

	public static enum AStarSearchType
	{
		MIN, MAX;
	}

}
