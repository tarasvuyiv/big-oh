package net.big_oh.common.algorithms.astar;

import java.util.Collection;

import net.big_oh.common.algorithms.astar.Heuristic;

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
 * A simple heuristic for solving the max Traveling Salesman problem. In order
 * to be admissible, this heuristic must never under estimate the remaining
 * cost.
 * 
 * @author davewingate
 * @version Dec 1, 2009
 */
public class TravelingSalesmanMaxHeuristic extends Heuristic<TravelingSalesmanSearchNode>
{

	@Override
	public double calculateH(TravelingSalesmanSearchNode tsSearchNode)
	{
		Collection<TravelingSalesmanDestination> unvisitedDestinations = tsSearchNode.getUnvisitedDestinations();

		// remaining cost will never exceed twice the cost of traveling from
		// each remaining destination to home.
		double overEstimate = 0;
		for (TravelingSalesmanDestination unvisitedDestination : unvisitedDestinations) {
			overEstimate += TravelingSalesmanDestination.getDistance(unvisitedDestination, TravelingSalesmanDestination.HOME_TOWN);
		}
		
		return 2 * overEstimate;

	}

}
