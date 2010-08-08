package net.big_oh.datastructures.graph.jung.sparse;

import net.big_oh.datastructures.graph.Vertex;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.utils.UserDataContainer.CopyAction;

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
 * A JUNG-based implementation of {@link net.big_oh.datastructures.graph.Vertex}
 * that is suitable for modeling sparse graphs.
 * 
 * @author dwingate
 * @version Dec 29, 2009
 */
public class JungVertex implements Vertex
{
	public static final CopyAction JUNG_COPY_ACTION = new CopyAction.Shared();

	private static final String ID_USER_DATUM_KEY = "id";
	private static final String VALUE_USER_DATUM_KEY = "value";

	/**
	 * Note that by using composition instead of inheritance we have to write
	 * more awkward code, but we can ensure that consuming classes code to the
	 * {@link Vertex} interface instead of writing JUNG-specific code.
	 */
	private final SparseVertex impl;

	public JungVertex()
	{
		this.impl = new SparseVertex();
	}

	public JungVertex(Object identifier)
	{
		this();
		setIdentifier(identifier);
	}

	final SparseVertex getImpl()
	{
		return impl;
	}

	public Object getIdentifier()
	{
		return impl.getUserDatum(ID_USER_DATUM_KEY);
	}

	public Object getValue()
	{
		return impl.getUserDatum(VALUE_USER_DATUM_KEY);
	}

	public void setIdentifier(Object id)
	{
		impl.setUserDatum(ID_USER_DATUM_KEY, id, JUNG_COPY_ACTION);
	}

	public void setValue(Object value)
	{
		impl.setUserDatum(VALUE_USER_DATUM_KEY, value, JUNG_COPY_ACTION);
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

}
