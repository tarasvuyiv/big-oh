package net.big_oh.common.algorithms.astar;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.big_oh.common.algorithms.astar.AStarSearchNode;

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
 * A testing SearchNode representing the search for the sorted solution to a
 * traveling salesman problem.
 * 
 * @author davewingate
 * @version Nov 29, 2009
 */
public class TravelingSalesmanSearchNode extends AStarSearchNode
{
	private final List<TravelingSalesmanDestination> visitedDestinations;
	private final Collection<TravelingSalesmanDestination> unvisitedDestinations;

	/**
	 * @param visited
	 * @param unvisited
	 */
	public TravelingSalesmanSearchNode(List<TravelingSalesmanDestination> visited, Collection<TravelingSalesmanDestination> unvisited)
	{
		super(visited.size());
		this.visitedDestinations = Collections.unmodifiableList(visited);
		this.unvisitedDestinations = Collections.unmodifiableCollection(unvisited);
	}

	@Override
	public double calculateG()
	{
		double costOfVisited = 0;

		TravelingSalesmanDestination previousDestination = TravelingSalesmanDestination.HOME_TOWN;
		for (TravelingSalesmanDestination nextDestination : visitedDestinations)
		{
			costOfVisited += TravelingSalesmanDestination.getDistance(previousDestination, nextDestination);
			previousDestination = nextDestination;
		}

		return costOfVisited;
	}

	@Override
	public boolean calculateGoalState()
	{
		return unvisitedDestinations.isEmpty() && visitedDestinations.size() > 0 && getLastVisitedDestination() == TravelingSalesmanDestination.HOME_TOWN;
	}

	public TravelingSalesmanDestination getLastVisitedDestination()
	{
		return (visitedDestinations.isEmpty()) ? null : visitedDestinations.get(visitedDestinations.size() - 1);
	}

	public String getPathAsString()
	{
		StringBuffer path = new StringBuffer();
		path.append(TravelingSalesmanDestination.HOME_TOWN);
		for (TravelingSalesmanDestination visited : visitedDestinations)
		{
			path.append(" --> ").append(visited);
		}
		return path.toString();
	}

	public List<TravelingSalesmanDestination> getVisitedDestinations()
	{
		return visitedDestinations;
	}

	public Collection<TravelingSalesmanDestination> getUnvisitedDestinations()
	{
		return unvisitedDestinations;
	}

}
