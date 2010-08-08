package net.big_oh.algorithms.graph.clique;

import java.util.EventListener;
import java.util.Set;

import net.big_oh.datastructures.graph.Graph;
import net.big_oh.datastructures.graph.Vertex;


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
 * This interface defines listener methods for events that transpire while
 * finding maximal cliques in a graph.
 * 
 * @author dwingate
 * @version Feb 14, 2010
 * @param <V>
 */
public interface MaximalCliqueFinderEventListener<V extends Vertex> extends EventListener
{
	/**
	 * Implementations of MaximalCliqueFinder must signal this event upon
	 * completing a logical step in their top-level algorithm. The meaning of
	 * "logical step" can vary greatly according to the underlying algorithm.
	 * 
	 * This type of event is signaled to accommodate any clean up activities
	 * that may need to be performed as appropriate to the Graph implementation
	 * V. For example, if the Graph is backed by a Hibernate data source, it may
	 * be necessary to periodically flush and clear the first-level cache.
	 * 
	 * @param numStepsCompleted
	 * @param expectedTotalSteps
	 */
	public void stepCompleted(long numStepsCompleted, long expectedTotalSteps);

	/**
	 * This listener method handles the discovery of a new clique in the Graph.
	 * Note that this event should only be signaled for discovered cliques that
	 * meet the minCliqueSize threshold argument of the related
	 * {@link net.big_oh.algorithms.graph.clique.MaximalCliqueFinder#findMaximalCliques (Graph, int)}
	 * invocation.
	 * 
	 * @param clique
	 */
	public void cliqueFound(Set<V> clique);
}
