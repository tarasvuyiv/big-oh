package net.big_oh.algorithms.graph.clique;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import net.big_oh.datastructures.graph.jung.sparse.JungUndirectedGraph;
import net.big_oh.datastructures.graph.jung.sparse.JungVertex;

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
// TODO DSW finish writing tests
public abstract class MaximalCliqueFinderFunctionalTest
{

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
		completeGraphBuilder.setNumCliques(1).setCliqueSize(completeGraphSize);

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

	private static final class CliqueTestGraphBuilder
	{

		private int numCliques = 0;
		private int cliqueSize = 2;

		public CliqueTestGraphBuilder setNumCliques(int numCliques)
		{
			if (numCliques < 0)
			{
				throw new IllegalArgumentException("numCliques cannot be less than zero.");
			}

			this.numCliques = numCliques;

			return this;
		}

		public CliqueTestGraphBuilder setCliqueSize(int cliqueSize)
		{
			if (cliqueSize < 2)
			{
				throw new IllegalArgumentException("cliqueSize cannot be less than two");
			}

			this.cliqueSize = cliqueSize;

			return this;
		}

		public JungUndirectedGraph build()
		{

			final JungUndirectedGraph graph = new JungUndirectedGraph();

			// populate cliques into the graph
			final Set<Set<JungVertex>> cliques = new HashSet<Set<JungVertex>>();
			for (int cliqueCounter = 0; cliqueCounter < numCliques; cliqueCounter++)
			{
				// create a new clique in the graph
				final Set<JungVertex> clique = new HashSet<JungVertex>();
				cliques.add(clique);

				for (int i = 0; i < cliqueSize; i++)
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

			return graph;

		}

	}

}
