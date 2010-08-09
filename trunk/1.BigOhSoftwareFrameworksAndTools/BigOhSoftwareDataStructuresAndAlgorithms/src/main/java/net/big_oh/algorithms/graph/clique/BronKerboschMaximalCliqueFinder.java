package net.big_oh.algorithms.graph.clique;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import net.big_oh.common.utils.Duration;
import net.big_oh.datastructures.graph.Graph;
import net.big_oh.datastructures.graph.Vertex;

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
 * An implementation of the Bron–Kerbosch algorithm for finding all maximal
 * cliques in a graph.
 * 
 * <pre>
 *    BronKerbosch1(R,P,X):
 *      if P and X are both empty:
 *          report R as a maximal clique
 *      for each vertex v in P:
 *          BronKerbosch1(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
 *          P := P \ {v}
 *          X := X ⋃ {v}
 * </pre>
 * 
 * @author dwingate
 * @version Dec 30, 2009
 * 
 * @param <V>
 */
public class BronKerboschMaximalCliqueFinder<V extends Vertex> implements MaximalCliqueFinder<V>
{

	private static final Log logger = LogFactory.getLog(BronKerboschMaximalCliqueFinder.class);

	private final Collection<MaximalCliqueFinderEventListener<V>> eventListeners;

	public BronKerboschMaximalCliqueFinder()
	{
		// Use CopyOnWriteArrayList to support dynamic listeners w/o incurring
		// synchronization overhead
		eventListeners = new CopyOnWriteArrayList<MaximalCliqueFinderEventListener<V>>();
	}

	public void addListener(MaximalCliqueFinderEventListener<V> listener)
	{
		// Note, no need to synchronize since eventListeners is an instance of
		// CopyOnWriteArrayList
		eventListeners.add(listener);
	}

	public Set<Set<V>> findMaximalCliques(Graph<V> g, int minCliqueSize) throws IllegalArgumentException
	{

		Duration d = new Duration(this.getClass());

		if (g == null)
		{
			throw new IllegalArgumentException("The Graph argument must not be null.");
		}
		if (minCliqueSize < 2)
		{
			throw new IllegalArgumentException("The minCliqueSize argument must not be less than two.");
		}

		// for the first step of the BronKerbosch algorithm, use a Vertex
		// iterator instead of fetching the entire collection. For sparse
		// graphs, use of the iterator avoids creating unnecessarily large
		// P collections.
		Set<Set<V>> maximalCliques = new HashSet<Set<V>>();

		long numVertices = g.getVertexCount();
		int completedVertexCounter = 0;
		for (Iterator<V> vertexIter = g.getAllVerticesIterator(); vertexIter.hasNext();)
		{
			V v = vertexIter.next();

			Set<V> r = new HashSet<V>();
			r.add(v);

			Set<V> p = g.getAllNeighbors(v);

			Set<V> x = g.getAllNeighbors(v);

			// process maximal cliques discovered for Vertex v
			processMaximalCliqueDiscoveries(maximalCliques, bronKerbosch1(r, p, x, g, minCliqueSize, 1));

			// count the completion of a step in the algorithm
			completedVertexCounter++;

			// signal step completion to listeners
			for (MaximalCliqueFinderEventListener<V> listener : eventListeners)
			{
				try
				{
					listener.stepCompleted(completedVertexCounter, numVertices);
				}
				catch (RuntimeException re)
				{
					logger.error(re.getMessage(), re);
				}
			}

			// log current progress of the operation?
			if (completedVertexCounter % Math.max(1, (numVertices / 100)) == 0)
			{
				logger.info("Executing " + this.getClass().getSimpleName() + ".findMaximalCliques(..) ... " + (int) ((double) completedVertexCounter / (double) numVertices * 100.0) + "% complete.");
			}
		}

		d.stop("findMaximalCliques");

		logger.info("Discovered " + maximalCliques.size() + " maximal cliques with size of at least " + minCliqueSize + ".");

		return maximalCliques;

	}

	private Set<Set<V>> bronKerbosch1(Set<V> r, Set<V> p, Set<V> x, Graph<V> g, int minCliqueSize, int searchDepth)
	{

		if (logger.isDebugEnabled())
		{
			logger.debug("Depth:" + searchDepth + " - R:" + r.size() + " - P:" + p.size() + " - X:" + x.size());
		}

		// base case ...
		// if P and X are both empty, then we're done
		if (p.isEmpty() && x.isEmpty())
		{
			if (r.size() >= minCliqueSize)
			{
				// the maximal clique contained in r is big enough to report
				return Collections.singleton(r);
			}
			else
			{
				// the maximal clique contained in r is too small
				return Collections.emptySet();
			}
		}

		// recursive case ...
		Set<Set<V>> maximalCliques = new HashSet<Set<V>>();

		Set<V> pCopy = new HashSet<V>(p);
		for (V v : pCopy)
		{

			// rPrime = R ⋃ {v}
			Set<V> rPrime = new HashSet<V>(r);
			rPrime.add(v);

			// pPrime = P ⋂ N(v)
			Set<V> pPrime = new HashSet<V>(p);
			pPrime.retainAll(g.getAllNeighbors(v));

			// xPrime = X ⋂ N(v)
			Set<V> xPrime = new HashSet<V>(x);
			xPrime.retainAll(g.getAllNeighbors(v));

			maximalCliques.addAll(bronKerbosch1(rPrime, pPrime, xPrime, g, minCliqueSize, searchDepth + 1));

			// P := P \ {v}
			p.remove(v);

			// X := X ⋃ {v}
			x.add(v);

		}

		return maximalCliques;

	}

	/**
	 * This helper method merges two sets of maximal cliques, claiming newly
	 * discovered maximal cliques and adding them to the set of well known
	 * maximal cliques.
	 * 
	 * @param wellKnownMaximalCliques
	 * @param maximalCliqueDiscoveries
	 */
	private void processMaximalCliqueDiscoveries(Set<Set<V>> wellKnownMaximalCliques, Set<Set<V>> maximalCliqueDiscoveries)
	{
		for (Set<V> maximalCliqueDiscovery : maximalCliqueDiscoveries)
		{

			if (wellKnownMaximalCliques.add(maximalCliqueDiscovery))
			{
				// this is the first discovery of the maximal clique ...

				// notify listeners
				for (MaximalCliqueFinderEventListener<V> listener : eventListeners)
				{
					try
					{
						listener.cliqueFound(maximalCliqueDiscovery);
					}
					catch (RuntimeException re)
					{
						logger.error(re.getMessage(), re);
					}
				}
			}

		}
	}

}
