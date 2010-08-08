package net.big_oh.algorithms.search.informed.astar;

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
 * A heuristic that is used to estimate the cost from a {@link AStarSearchNode}
 * to a goal state. If the Heuristic is to be used in a min A* search, then the
 * Heuristic must never overestimate the remaining cost. Conversely, if the
 * Heuristic is to be used in a max A* search, then the Heuristic must never
 * underestimate the remaining cost.
 * 
 * @author davewingate
 * @version Nov 29, 2009
 * @param <SearchNodeType>
 */
public abstract class Heuristic<SearchNodeType extends AStarSearchNode>
{
	private static final Log logger = LogFactory.getLog(Heuristic.class);

	public final double getH(SearchNodeType searchNode)
	{
		if (searchNode.getH(this) == null)
		{
			Duration calculateDuration = new Duration();

			double calculatedH = (searchNode.isGoalState()) ? 0 : calculateH(searchNode);

			if (logger.isDebugEnabled())
			{
				logger.debug("Calculated an h value in " + calculateDuration.stop() + " milliseconds.");
			}

			searchNode.setH(this, calculatedH);
		}
		return searchNode.getH(this);
	}

	protected abstract double calculateH(SearchNodeType searchNode);
}
