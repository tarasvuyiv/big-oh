package net.big_oh.algorithms.search.informed.astar;

import net.big_oh.algorithms.search.informed.astar.Heuristic;

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
 * A simple heuristic for solving the min Traveling Salesman problem. In order
 * to be admissible, this heuristic must never over estimate the remaining cost.
 * 
 * @author davewingate
 * @version Nov 29, 2009
 */
public class TravelingSalesmanMinHeuristic extends Heuristic<TravelingSalesmanSearchNode>
{

	@Override
	public double calculateH(TravelingSalesmanSearchNode tsSearchNode)
	{
		TravelingSalesmanDestination lastVisitedDestination = tsSearchNode.getLastVisitedDestination();

		// we must always cover at least the distance from the curent location
		// back to home
		return (lastVisitedDestination == null) ? Integer.MIN_VALUE : TravelingSalesmanDestination.getDistance(lastVisitedDestination, TravelingSalesmanDestination.HOME_TOWN);
	}

}
