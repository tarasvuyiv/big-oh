package net.big_oh.datastructures.graph;

import java.util.Iterator;
import java.util.Set;

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
 * Provides a base level of abstraction for graph algorithms. The underlying
 * implementation of this class might use the JUNG framework or, alternatively,
 * might provide a custom view of SQL data that adheres to the interface.
 * 
 * @author dwingate
 * @version Dec 29, 2009
 * @param <V>
 */
public interface Graph<V extends Vertex>
{
	/**
	 * Add a vertex to the graph.
	 * 
	 * @param v
	 * @throws IllegalArgumentException
	 *             if v is null.
	 * @throws UnsupportedOperationException
	 *             if the graph cannot be modified.
	 */
	public void addVertex(V v) throws IllegalArgumentException, UnsupportedOperationException;

	/**
	 * Connects two vertices in the graph with an edge. The implementing class
	 * may choose to implement either a directed or undirected edge. Depending
	 * on that implementation choice, the post conditions of this method will
	 * vary.
	 * 
	 * @param fromVertex
	 * @param toVertex
	 * @throws IllegalArgumentException
	 *             if either {@link Vertex} argument is null.
	 * @throws UnsupportedOperationException
	 *             if the graph cannot be modified.
	 */
	public void addEdge(V fromVertex, V toVertex) throws IllegalArgumentException, UnsupportedOperationException;

	/**
	 * @return Returns the set of all vertices contained in the graph.
	 */
	public Set<V> getAllVertices();

	/**
	 * @return Returns an iterator that traverses the set of all vertices
	 *         contained in the graph.
	 */
	public Iterator<V> getAllVerticesIterator();

	/**
	 * @return Returns a count of all vertices in the graph.
	 */
	public long getVertexCount();

	/**
	 * @param v
	 * @return Returns the set of all neighbors to the vertex.
	 */
	public Set<V> getAllNeighbors(V v);
}
