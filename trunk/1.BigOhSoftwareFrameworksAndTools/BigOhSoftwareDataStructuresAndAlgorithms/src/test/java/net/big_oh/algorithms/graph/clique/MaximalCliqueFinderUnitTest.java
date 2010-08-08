package net.big_oh.algorithms.graph.clique;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.big_oh.algorithms.graph.clique.MaximalCliqueFinder;
import net.big_oh.algorithms.graph.clique.MaximalCliqueFinderEventListener;
import net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph;
import net.big_oh.datastructures.graph.jung.sparse.JungVertex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


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
 * An abstract unit test to be used in testing all MaximalCliqueFinders.
 * 
 * @author dwingate
 * @version Dec 30, 2009
 */
public abstract class MaximalCliqueFinderUnitTest
{

	private JungVertex v1;
	private JungVertex v2;
	private JungVertex v3;
	private JungVertex v4;
	private JungVertex v5;
	private JungVertex v6;

	private JungUndirectedGraph graph;

	private Set<Set<JungVertex>> allCliquesSize2OrGreater;
	private Set<Set<JungVertex>> allCliquesSize3OrGreater;
	private Set<Set<JungVertex>> allCliquesSize4OrGreater;

	private MaximalCliqueFinder<JungVertex> cliqueFinder;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		// construct this test graph:
		// http://en.wikipedia.org/wiki/File:6n-graf.svg
		v1 = new JungVertex("v1");
		v2 = new JungVertex("v2");
		v3 = new JungVertex("v3");
		v4 = new JungVertex("v4");
		v5 = new JungVertex("v5");
		v6 = new JungVertex("v6");
		graph = new JungUndirectedGraph();
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		graph.addVertex(v5);
		graph.addVertex(v6);
		graph.addEdge(v6, v4);
		graph.addEdge(v4, v3);
		graph.addEdge(v4, v5);
		graph.addEdge(v3, v2);
		graph.addEdge(v5, v2);
		graph.addEdge(v5, v1);
		graph.addEdge(v2, v1);

		// ... also add a self-linking edge
		graph.addEdge(v6, v6);

		// ... also add multiple unconnected vertices
		for (int i = 0; i < 10000; i++)
		{
			graph.addVertex(new JungVertex("Unconnected_" + i));
		}

		// define the expected maximal cliques
		allCliquesSize2OrGreater = new HashSet<Set<JungVertex>>();
		allCliquesSize2OrGreater.add(new HashSet<JungVertex>(Arrays.asList(new JungVertex[] { v2, v3 })));
		allCliquesSize2OrGreater.add(new HashSet<JungVertex>(Arrays.asList(new JungVertex[] { v1, v2, v5 })));
		allCliquesSize2OrGreater.add(new HashSet<JungVertex>(Arrays.asList(new JungVertex[] { v3, v4 })));
		allCliquesSize2OrGreater.add(new HashSet<JungVertex>(Arrays.asList(new JungVertex[] { v4, v5 })));
		allCliquesSize2OrGreater.add(new HashSet<JungVertex>(Arrays.asList(new JungVertex[] { v4, v6 })));

		allCliquesSize3OrGreater = new HashSet<Set<JungVertex>>();
		allCliquesSize3OrGreater.add(new HashSet<JungVertex>(Arrays.asList(new JungVertex[] { v1, v2, v5 })));

		allCliquesSize4OrGreater = new HashSet<Set<JungVertex>>();

		// build the clique finder
		cliqueFinder = buildCliqueFinder();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * @return Returns the instance of MaximalCliqueFinder to be tested by this
	 *         unit test.
	 */
	protected abstract MaximalCliqueFinder<JungVertex> buildCliqueFinder();

	/**
	 * test ability to identify illegal minSize argument
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFindMaximalCliques_illegalMinSize()
	{
		cliqueFinder.findMaximalCliques(graph, 1);
	}

	/**
	 * test ability to identify illegal Graph argument
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFindMaximalCliques_illegalGraph()
	{
		cliqueFinder.findMaximalCliques(null, 2);
	}

	/**
	 * test ability to find all maximal cliques with size at least 2
	 */
	@Test
	public void testFindMaximalCliques_minSize2()
	{
		Set<Set<JungVertex>> maximalCliques = cliqueFinder.findMaximalCliques(graph, 2);
		assertEquals(allCliquesSize2OrGreater.size(), maximalCliques.size());
		assertTrue(maximalCliques.containsAll(allCliquesSize2OrGreater));
	}

	/**
	 * test ability to find all maximal cliques with size at least 3
	 */
	@Test
	public void testFindMaximalCliques_minSize3()
	{
		Set<Set<JungVertex>> maximalCliques = cliqueFinder.findMaximalCliques(graph, 3);
		assertEquals(allCliquesSize3OrGreater.size(), maximalCliques.size());
		assertTrue(maximalCliques.containsAll(allCliquesSize3OrGreater));
	}

	/**
	 * test ability to find all maximal cliques with size at least 4
	 */
	@Test
	public void testFindMaximalCliques_minSize4()
	{
		Set<Set<JungVertex>> maximalCliques = cliqueFinder.findMaximalCliques(graph, 4);
		assertEquals(allCliquesSize4OrGreater.size(), maximalCliques.size());
		assertTrue(maximalCliques.containsAll(allCliquesSize4OrGreater));
	}

	/**
	 * confirm that event listeners are notified as expected
	 */
	@Test
	public void testEventNotification()
	{

		// define a private listener class that can be used to count number of
		// events received
		class InvocationCountingListener implements MaximalCliqueFinderEventListener<JungVertex>
		{

			private int cliqueFoundCount = 0;
			private int stepCompletedCount = 0;

			public void cliqueFound(Set<JungVertex> clique)
			{
				cliqueFoundCount++;
			}

			public void stepCompleted(long numStepsCompleted, long expectedTotalSteps)
			{
				stepCompletedCount++;
			}

			public int getCliqueFoundCount()
			{
				return cliqueFoundCount;
			}

			public int getStepCompletedCount()
			{
				return stepCompletedCount;
			}

		}

		// create listeners
		InvocationCountingListener listener1 = new InvocationCountingListener();
		InvocationCountingListener listener2 = new InvocationCountingListener();

		// attach listeners
		cliqueFinder.addListener(listener1);
		cliqueFinder.addListener(listener2);

		// find cliques with listeners attached to cliqueFinder
		Set<Set<JungVertex>> maximalCliques = cliqueFinder.findMaximalCliques(graph, 2);
		assertFalse(maximalCliques.isEmpty());

		// confirm that stepCompleted(..) was called a reasonable number of
		// times
		assertTrue(listener1.getStepCompletedCount() >= graph.getVertexCount());
		assertTrue(listener2.getStepCompletedCount() >= graph.getVertexCount());

		// confirm that cliqueFound(..) was called the expected number of times
		assertEquals(maximalCliques.size(), listener1.getCliqueFoundCount());
		assertEquals(maximalCliques.size(), listener2.getCliqueFoundCount());
	}

}
