package net.big_oh.datastructures.graph.jung.sparse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.big_oh.datastructures.graph.Graph;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;

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
 * A JUNG-based implementation of {@link net.big_oh.datastructures.graph.Graph}
 * that is suitable for modeling sparse, undirected graphs.
 * 
 * @author dwingate
 * @version Dec 29, 2009
 */
public class JungUndirectedGraph implements Graph<JungVertex>
{
	/**
	 * Note that by using composition instead of inheritance we have to write
	 * more awkward code, but we can ensure that consuming classes code to the
	 * {@link Graph} interface instead of writing JUNG-specific code.
	 */
	private final SparseGraph impl;

	private final Map<SparseVertex, JungVertex> vertices;

	public JungUndirectedGraph()
	{
		this.impl = new SparseGraph();
		this.vertices = new HashMap<SparseVertex, JungVertex>();
	}

	public void addVertex(JungVertex v) throws IllegalArgumentException
	{
		if (v == null)
		{
			throw new IllegalArgumentException("The Vertex argument must not be null.");
		}

		impl.addVertex(v.getImpl());
		vertices.put(v.getImpl(), v);
	}

	public void addEdge(JungVertex fromVertex, JungVertex toVertex) throws IllegalArgumentException
	{
		if (fromVertex == null)
		{
			throw new IllegalArgumentException("The fromVertex argument must not be null.");
		}

		if (toVertex == null)
		{
			throw new IllegalArgumentException("The toVertex argument must not be null.");
		}

		// Add a single, undirected edge
		impl.addEdge(new UndirectedSparseEdge(fromVertex.getImpl(), toVertex.getImpl()));
	}

	@Override
	public String toString()
	{
		return impl.toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		return impl.equals(obj);
	}

	@Override
	public int hashCode()
	{
		return impl.hashCode();
	}

	@SuppressWarnings("unchecked")
	public Set<JungVertex> getAllNeighbors(JungVertex v)
	{
		Set<SparseVertex> predecessors = v.getImpl().getPredecessors();
		Set<SparseVertex> successors = v.getImpl().getSuccessors();

		Set<SparseVertex> successorsAndPredecessors = new HashSet<SparseVertex>(predecessors.size() + successors.size());
		successorsAndPredecessors.addAll(successors);
		successorsAndPredecessors.addAll(predecessors);

		Set<JungVertex> neighbors = new HashSet<JungVertex>(successorsAndPredecessors.size());

		for (SparseVertex neighborVertex : successorsAndPredecessors)
		{
			neighbors.add(vertices.get(neighborVertex));
		}

		neighbors.remove(v);

		return neighbors;
	}

	public Set<JungVertex> getAllVertices()
	{
		return new HashSet<JungVertex>(vertices.values());
	}

	public Iterator<JungVertex> getAllVerticesIterator()
	{
		return vertices.values().iterator();
	}

	public long getVertexCount()
	{
		return vertices.size();
	}

}
