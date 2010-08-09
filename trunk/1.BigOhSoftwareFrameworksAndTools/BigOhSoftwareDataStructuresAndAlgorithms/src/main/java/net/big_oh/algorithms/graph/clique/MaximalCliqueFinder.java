package net.big_oh.algorithms.graph.clique;

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
 * An interface for algorithms that find all maximal cliques in a graph.
 * 
 * A maximal clique, sometimes called inclusion-maximal, is a clique that is not
 * included in a larger clique.
 * 
 * @author dwingate
 * @version Dec 30, 2009
 * 
 * @param <V>
 */
public interface MaximalCliqueFinder<V extends Vertex>
{
	/**
	 * Finds all maximal cliques in the graph of size minCliqueSize or greater.
	 * As cliques are discovered, all registered
	 * {@link MaximalCliqueFinderEventListener} objects are notified.
	 * 
	 * @param g
	 * @param minCliqueSize
	 * @return the set of all Vertex cliques of size minCliqueSize or greater.
	 * @throws IllegalArgumentException
	 *             Thrown if Graph g is null or if minCliqueSize is less than 2.
	 * 
	 * @see MaximalCliqueFinderEventListener#cliqueFound(Set)
	 */
	// TODO Return a more expressive data structure
	public Set<Set<V>> findMaximalCliques(Graph<V> g, int minCliqueSize) throws IllegalArgumentException;

	/**
	 * Registers a listener that should be notified of events that transpire
	 * while finding maximal cliques.
	 * 
	 * @param listener
	 */
	public void addListener(MaximalCliqueFinderEventListener<V> listener);
}
