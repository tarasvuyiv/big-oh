package net.big_oh.algorithms.search.informed.astar;

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
 * Encapsulates the results from an A* search.
 * 
 * @author davewingate
 * @version Dec 1, 2009
 * @param <SearchNodeType>
 */
public class AStarSearchResult<SearchNodeType extends AStarSearchNode>
{
	private final SearchNodeType goalNode;
	private final long numSearchNodesGenerated;
	private final double efectiveBranchingFactor;

	public AStarSearchResult(SearchNodeType goalNode, long numSearchNodesGenerated, double efectiveBranchingFactor)
	{
		super();
		this.goalNode = goalNode;
		this.numSearchNodesGenerated = numSearchNodesGenerated;
		this.efectiveBranchingFactor = efectiveBranchingFactor;
	}

	/**
	 * 
	 * @return Returns the goal state search node (if one was found).
	 */
	public SearchNodeType getGoalNode()
	{
		return goalNode;
	}

	/**
	 * 
	 * @return Returns an estimate of the effective branching factor for the
	 *         entire search operation.
	 */
	public double getEfectiveBranchingFactor()
	{
		return efectiveBranchingFactor;
	}

	/**
	 * 
	 * @return Returns a count of all search space nodes that were generated
	 *         (but not necessarily evaluated) during the search operation.
	 */
	public long getNumSearchNodesGenerated()
	{
		return numSearchNodesGenerated;
	}

}
