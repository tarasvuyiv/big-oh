package net.big_oh.algorithms.graph.clique;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph;
import net.big_oh.datastructures.graph.jung.sparse.JungVertex;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 Copyright (c) 2010 Dave Wingate dba Big-Oh Software (www.big-oh.net)

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
 * An abstract functional test to be used in testing all MaximalCliqueFinders.
 * 
 * Whereas {@link MaximalCliqueFinderUnitTest} focuses on verifying the basic
 * contractual behavior of maximal clique finders, this test exhaustively
 * confirms that the maximal clique finder is capable of finding all possible
 * maximal cliques.
 * 
 * @author dwingate
 * @version Aug 8, 2010
 */
public abstract class MaximalCliqueFinderFunctionalTest
{

	private static final int DEFAULT_NOISE_VALUE = 200;

	private MaximalCliqueFinder<JungVertex> cliqueFinder;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
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

	@Test
	public void testFindMaximalCliques_EmptyGraph()
	{
		final JungUndirectedGraph emptyGraph = new JungUndirectedGraph();

		for (int minCliqueSize = 2; minCliqueSize < 100; minCliqueSize++)
		{
			final Set<Set<JungVertex>> maximalCliques = cliqueFinder.findMaximalCliques(emptyGraph, minCliqueSize);
			assertNotNull(maximalCliques);
			assertTrue(maximalCliques.isEmpty());
		}
	}

	@Test
	public void testFindMaximalCliques_CompleteGraph_Size2()
	{
		testFindMaximalCliques_CompleteGraph(2);
	}

	@Test
	public void testFindMaximalCliques_CompleteGraph_Size3()
	{
		testFindMaximalCliques_CompleteGraph(3);
	}

	@Test
	public void testFindMaximalCliques_CompleteGraph_Size10()
	{
		testFindMaximalCliques_CompleteGraph(10);
	}

	@Test
	public void testFindMaximalCliques_CompleteGraph_Size11()
	{
		testFindMaximalCliques_CompleteGraph(11);
	}

	private void testFindMaximalCliques_CompleteGraph(int completeGraphSize)
	{
		final CliqueTestGraphBuilder completeGraphBuilder = new CliqueTestGraphBuilder();
		completeGraphBuilder.addClique(completeGraphSize);

		final JungUndirectedGraph completeGraph = completeGraphBuilder.build();

		Set<Set<JungVertex>> maximalCliques = null;

		// test findMaximalCliques(..) with minCliqueSize = completeGraphSize
		maximalCliques = cliqueFinder.findMaximalCliques(completeGraph, completeGraphSize);
		assertNotNull(maximalCliques);
		assertFalse("Failed to find the single maximal clique within a complete graph.", maximalCliques.isEmpty());
		assertEquals("Erroneously identified more than one clique in a commplete graph.", 1, maximalCliques.size());

		// test findMaximalCliques(..) with minCliqueSize = completeGraphSize-1
		maximalCliques = cliqueFinder.findMaximalCliques(completeGraph, Math.max(2, completeGraphSize - 1));
		assertNotNull(maximalCliques);
		assertFalse("Failed to find the single maximal clique within a complete graph.", maximalCliques.isEmpty());
		assertEquals("Erroneously identified more than one clique in a commplete graph.", 1, maximalCliques.size());

		// test findMaximalCliques(..) with minCliqueSize = completeGraphSize+1
		maximalCliques = cliqueFinder.findMaximalCliques(completeGraph, completeGraphSize + 1);
		assertNotNull(maximalCliques);
		assertTrue("Found a spurious clique.", maximalCliques.isEmpty());
	}

	@Test
	public void testFindMaximalCliques_MultipleCliques_3_3()
	{
		testFindMaximalCliques_MultipleCliques(3, 3);
	}

	@Test
	public void testFindMaximalCliques_MultipleCliques_3_3_WithNoise()
	{
		testFindMaximalCliques_MultipleCliques_WithNoise(DEFAULT_NOISE_VALUE, 3, 3);
	}

	@Test
	public void testFindMaximalCliques_MultipleCliques_3_3_3()
	{
		testFindMaximalCliques_MultipleCliques(3, 3, 3);
	}

	@Test
	public void testFindMaximalCliques_MultipleCliques_3_3_3_WithNoise()
	{
		testFindMaximalCliques_MultipleCliques_WithNoise(DEFAULT_NOISE_VALUE, 3, 3, 3);
	}

	@Test
	public void testFindMaximalCliques_MultipleCliques_7_4_9_3()
	{
		testFindMaximalCliques_MultipleCliques(7, 4, 9, 3);
	}

	@Test
	public void testFindMaximalCliques_MultipleCliques_7_4_9_3_WithNoise()
	{
		testFindMaximalCliques_MultipleCliques_WithNoise(DEFAULT_NOISE_VALUE, 7, 4, 9, 3);
	}

	@Test
	public void testFindMaximalCliques_MultipleCliques_3_4_5_6_7_8_9_10()
	{
		testFindMaximalCliques_MultipleCliques(3, 4, 5, 6, 7, 8, 9, 10);
	}

	@Test
	public void testFindMaximalCliques_MultipleCliques_3_4_5_6_7_8_9_10_WithNoise()
	{
		testFindMaximalCliques_MultipleCliques_WithNoise(DEFAULT_NOISE_VALUE, 3, 4, 5, 6, 7, 8, 9, 10);
	}

	@Test
	public void testFindMaximalCliques_MultipleCliques_10_10_10_10_10()
	{
		testFindMaximalCliques_MultipleCliques(10, 10, 10, 10, 10);
	}

	@Test
	public void testFindMaximalCliques_MultipleCliques_10_10_10_10_10_WithNoise()
	{
		testFindMaximalCliques_MultipleCliques_WithNoise(DEFAULT_NOISE_VALUE, 10, 10, 10, 10, 10);
	}

	private void testFindMaximalCliques_MultipleCliques(int... cliqueSizes)
	{
		testFindMaximalCliques_MultipleCliques_WithNoise(0, cliqueSizes);
	}

	private void testFindMaximalCliques_MultipleCliques_WithNoise(int numNonCliqueNodes, int... cliqueSizes)
	{
		if (numNonCliqueNodes < 0)
		{
			throw new IllegalArgumentException("numNonCliqueNodes cannot be less than zero.");
		}

		if (cliqueSizes.length <= 0)
		{
			throw new IllegalArgumentException("must provide one or more cliqueSize arguments.");
		}

		final CliqueTestGraphBuilder graphBuilder = new CliqueTestGraphBuilder();
		for (int cliqueSize : cliqueSizes)
		{
			graphBuilder.addClique(cliqueSize);
		}
		graphBuilder.setNumNonCliqueNodes(numNonCliqueNodes);

		final JungUndirectedGraph multiCliqueGraph = graphBuilder.build();

		// find cliques of size 3 or greater; can't use size 2 because of noise
		// nodes that are connected to one node in each clique
		Set<Set<JungVertex>> maximalCliques = cliqueFinder.findMaximalCliques(multiCliqueGraph, 3);

		assertNotNull(maximalCliques);
		assertEquals("Failed to find expected number of maximal cliques.", cliqueSizes.length, maximalCliques.size());

		// extract the _sizes_ of all discovered maximal cliques
		@SuppressWarnings("unchecked")
		Collection<Integer> maximalCliqueSizes = CollectionUtils.collect(maximalCliques, new Transformer()
		{
			public Object transform(Object input)
			{
				Set<JungVertex> clique = (Set<JungVertex>) input;
				return Integer.valueOf(clique.size());
			}
		});

		// the collection of input clique sizes should be equal to the
		// collection of discovered clique sizes
		assertTrue(CollectionUtils.isEqualCollection(Arrays.asList(ArrayUtils.toObject(cliqueSizes)), maximalCliqueSizes));
	}

	@Test
	public void testFindMaximalCliques_NoSuchClique_3()
	{
		testFindMaximalCliques_NoSuchClique(3);
	}

	@Test
	public void testFindMaximalCliques_NoSuchClique_4()
	{
		testFindMaximalCliques_NoSuchClique(4);
	}

	@Test
	public void testFindMaximalCliques_NoSuchClique_7()
	{
		testFindMaximalCliques_NoSuchClique(7);
	}

	@Test
	public void testFindMaximalCliques_NoSuchClique_11()
	{
		testFindMaximalCliques_NoSuchClique(11);
	}

	private void testFindMaximalCliques_NoSuchClique(int minCliqueSize)
	{
		if (minCliqueSize < 3)
		{
			throw new IllegalArgumentException("minCliqueSize must be greater than two.");
		}

		// create a complete graph of size (minCliqueSize - 1)
		JungUndirectedGraph graph = new CliqueTestGraphBuilder().addClique(minCliqueSize - 1).build();

		// add a node to the graph and connect to all but one of the other nodes
		// in the graph
		final JungVertex extraNode = new JungVertex();
		graph.addVertex(extraNode);

		Iterator<JungVertex> nodeIterator = graph.getAllVerticesIterator();

		// skip one node from the original, complete graph
		JungVertex skippedNode = nodeIterator.next();
		if (skippedNode == extraNode)
		{
			skippedNode = nodeIterator.next();
		}
		assert (skippedNode != extraNode);

		// connect extra node to all other nodes from the original, complete
		// graph
		while (nodeIterator.hasNext())
		{
			graph.addEdge(extraNode, nodeIterator.next());
		}

		// confirm that there is no clique of size minCliqueSize
		Set<Set<JungVertex>> maximalCliques = cliqueFinder.findMaximalCliques(graph, minCliqueSize);
		assertTrue("Unexpectedly found one or more cliques of size " + minCliqueSize + " or greater.", maximalCliques.isEmpty());

	}

	private static final class CliqueTestGraphBuilder
	{

		private int numNonCliqueNodes = 0;
		private List<Integer> cliquesToBuild = new ArrayList<Integer>();

		public CliqueTestGraphBuilder addClique(int cliqueSize) throws IllegalArgumentException
		{
			if (cliqueSize < 2)
			{
				throw new IllegalArgumentException("Clique size must be greater than or equal to two.");
			}

			cliquesToBuild.add(Integer.valueOf(cliqueSize));

			return this;
		}

		public CliqueTestGraphBuilder setNumNonCliqueNodes(int numNonCliqueNodes)
		{
			this.numNonCliqueNodes = numNonCliqueNodes;
			return this;
		}

		public JungUndirectedGraph build()
		{

			final JungUndirectedGraph graph = new JungUndirectedGraph();

			// populate cliques into the graph
			final Set<Set<JungVertex>> cliques = new HashSet<Set<JungVertex>>();
			for (final Integer cliqueToBuild : cliquesToBuild)
			{
				// create a new clique in the graph
				final Set<JungVertex> clique = new HashSet<JungVertex>();
				cliques.add(clique);

				for (int i = 0; i < cliqueToBuild.intValue(); i++)
				{

					// add member to clique
					final JungVertex cliqueMember = new JungVertex();
					graph.addVertex(cliqueMember);
					clique.add(cliqueMember);

					// connect cliqueMember to all other clique members
					for (JungVertex anotherCliqueMember : clique)
					{
						graph.addEdge(cliqueMember, anotherCliqueMember);
					}

				}

			}

			// add non-clique nodes
			for (int i = 0; i < numNonCliqueNodes; i++)
			{

				// add node to graph
				final JungVertex nonCliqueMember = new JungVertex();
				graph.addVertex(nonCliqueMember);

				// connect to one node in each clique
				for (Set<JungVertex> clique : cliques)
				{
					graph.addEdge(nonCliqueMember, clique.iterator().next());
				}
			}

			return graph;

		}

	}

}
