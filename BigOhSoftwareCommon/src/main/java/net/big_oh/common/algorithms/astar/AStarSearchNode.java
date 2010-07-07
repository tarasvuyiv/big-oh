package net.big_oh.common.algorithms.astar;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

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
 * An abstraction of a single search node in the A* algorithm.
 * 
 * @author davewingate
 * @version Nov 29, 2009
 */
public abstract class AStarSearchNode
{

	private final int nodeDepth;
	private final Map<Heuristic<?>, Double> hMap;

	private Boolean goalState;
	private Double g;

	public AStarSearchNode(int nodeDepth)
	{
		super();
		this.nodeDepth = nodeDepth;
		hMap = new HashMap<Heuristic<?>, Double>();
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

	public int getNodeDepth()
	{
		return nodeDepth;
	}

	public Boolean isGoalState()
	{
		if (goalState == null)
		{
			goalState = calculateGoalState();
		}

		return goalState;
	}

	/**
	 * @return Returns a boolean indicating whether the search node represents a
	 *         terminal goal state -- i.e. a solution to the problem.
	 */
	protected abstract boolean calculateGoalState();

	public Double getG()
	{
		if (g == null)
		{
			g = calculateG();
		}

		return g;
	}

	/**
	 * @return Returns the value/cost of all actions taken to reach this search
	 *         node.
	 */
	protected abstract double calculateG();

	public Double getH(Heuristic<? extends AStarSearchNode> heuristic)
	{
		return hMap.get(heuristic);
	}

	/**
	 * A package visible setter method that will allow a Heuristic to set the
	 * calculated h value for this node.
	 * 
	 * @param heuristic
	 * @param h
	 */
	void setH(Heuristic<? extends AStarSearchNode> heuristic, Double h)
	{
		hMap.put(heuristic, h);
	}

}
