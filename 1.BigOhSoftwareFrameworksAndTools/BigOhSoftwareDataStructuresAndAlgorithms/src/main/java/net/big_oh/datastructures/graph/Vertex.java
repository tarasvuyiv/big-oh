package net.big_oh.datastructures.graph;

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
 * Provides a base level of abstraction graph algorithms. The underlying
 * implementation of this class might use the JUNG framework or, alternatively,
 * might provide a custom view of SQL data that adheres to the interface.
 * 
 * @author dwingate
 * @version Dec 29, 2009
 */
public interface Vertex
{

	/**
	 * Sets a unique identifier for this vertex.
	 * 
	 * @param id
	 * 
	 * @throws UnsupportedOperationException
	 *             is the vertex is read only
	 */
	public void setIdentifier(Object id) throws UnsupportedOperationException;

	/**
	 * @return the unique identifier for this vertex
	 */
	public Object getIdentifier();

	/**
	 * Sets the data pay load for this vertex.
	 * 
	 * @param value
	 * 
	 * @throws UnsupportedOperationException
	 *             is the vertex is read only
	 */
	public void setValue(Object value) throws UnsupportedOperationException;

	/**
	 * @return the data pay load for this vertex
	 */
	public Object getValue();

}
