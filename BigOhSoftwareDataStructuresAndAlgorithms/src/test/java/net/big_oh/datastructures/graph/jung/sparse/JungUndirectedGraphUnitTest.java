package net.big_oh.datastructures.graph.jung.sparse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import net.big_oh.datastructures.graph.Graph;
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
 * A unit test for the {@link JungUndirectedGraph} implementation of the
 * {@link Graph} interface.
 * 
 * @author dwingate
 * @version Dec 29, 2009
 */
public class JungUndirectedGraphUnitTest
{

	private JungVertex v1;
	private JungVertex v2;
	private JungVertex v3;
	private JungVertex v4;

	private JungUndirectedGraph graph;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		v1 = new JungVertex();
		v1.setIdentifier("v1");

		v2 = new JungVertex();
		v2.setIdentifier("v2");

		v3 = new JungVertex();
		v3.setIdentifier("v3");

		v4 = new JungVertex();
		v4.setIdentifier("v4");

		graph = new JungUndirectedGraph();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph#addVertex(net.big_oh.datastructures.graph.jung.sparse.JungVertex)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddVertex_Illegal()
	{
		graph.addVertex(null);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph#addVertex(net.big_oh.datastructures.graph.jung.sparse.JungVertex)}
	 * .
	 */
	@Test
	public void testAddVertex()
	{
		assertFalse(graph.getAllVertices().contains(v1));

		// add a single vertex
		graph.addVertex(v1);

		assertTrue(graph.getAllVertices().contains(v1));
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph#addEdge(JungVertex, JungVertex)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddEdge_Illegal_1()
	{
		graph.addEdge(v1, null);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph#addEdge(JungVertex, JungVertex)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddEdge_Illegal_2()
	{
		graph.addEdge(null, v2);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph#addEdge(JungVertex, JungVertex)}
	 * .
	 */
	@Test(expected = RuntimeException.class)
	public void testAddEdge_Illegal_3()
	{
		// attempt to add edge for vertices that are not members of the graph
		graph.addEdge(v1, v2);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph#addEdge(JungVertex, JungVertex)}
	 * .
	 */
	@Test
	public void testAddEdge()
	{
		assertFalse(graph.getAllNeighbors(v1).contains(v2));
		assertFalse(graph.getAllNeighbors(v2).contains(v1));

		// add a single edge
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addEdge(v1, v2);

		assertTrue(graph.getAllNeighbors(v1).contains(v2));
		assertTrue(graph.getAllNeighbors(v2).contains(v1));
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph#getAllNeighbors(net.big_oh.datastructures.graph.jung.sparse.JungVertex)}
	 * .
	 */
	@Test
	public void testGetAllNeighbors()
	{
		// add some vertices to the graph
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);

		// add some edges to the graph
		graph.addEdge(v1, v2);
		graph.addEdge(v1, v4);
		graph.addEdge(v4, v4);

		// confirm expected properties for v1 neighbors
		Set<JungVertex> v1Neighbors = graph.getAllNeighbors(v1);
		assertEquals(2, v1Neighbors.size());
		assertTrue(v1Neighbors.contains(v2));
		assertTrue(v1Neighbors.contains(v4));

		// confirm expected properties for v2 neighbors
		Set<JungVertex> v2Neighbors = graph.getAllNeighbors(v2);
		assertEquals(1, v2Neighbors.size());
		assertTrue(v2Neighbors.contains(v1));

		// confirm expected properties for v3 neighbors
		Set<JungVertex> v3Neighbors = graph.getAllNeighbors(v3);
		assertEquals(0, v3Neighbors.size());

		// confirm expected properties for v4 neighbors
		Set<JungVertex> v4Neighbors = graph.getAllNeighbors(v4);
		assertEquals(1, v4Neighbors.size());
		assertTrue(v4Neighbors.contains(v1));
		assertFalse(v4Neighbors.contains(v4));
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph#getAllVertices()}
	 * .
	 */
	@Test
	public void testGetAllVertices()
	{
		// add some vertices to the graph
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);

		// note that we explicitly did NOT add v4 to the graph

		// get vertices back from the graph
		Set<JungVertex> allVerticesInGraph = graph.getAllVertices();

		// alter the collection returned by graph
		allVerticesInGraph.clear();

		// get a second copy of all vertices from the graph
		allVerticesInGraph = graph.getAllVertices();

		// assert expected properties for vertices from graph
		assertEquals(3, allVerticesInGraph.size());
		assertTrue(allVerticesInGraph.contains(v1));
		assertTrue(allVerticesInGraph.contains(v2));
		assertTrue(allVerticesInGraph.contains(v3));
		assertFalse(allVerticesInGraph.contains(v4));
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph#getAllVerticesIterator()}
	 * .
	 */
	@Test
	public void testGetAllVerticesIterator()
	{
		// add some vertices to the graph
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);

		// get vertices back from the graph
		Set<JungVertex> allVerticesInGraph = graph.getAllVertices();

		// confirm that collection is non-empty
		assertFalse(allVerticesInGraph.isEmpty());

		// use iterator to remove vertices from the allVerticesInGraph
		// collection
		for (Iterator<JungVertex> iter = graph.getAllVerticesIterator(); iter.hasNext();)
		{
			assertTrue(allVerticesInGraph.remove(iter.next()));
		}

		// confirm that collection is now empty
		assertTrue(allVerticesInGraph.isEmpty());
	}

}
