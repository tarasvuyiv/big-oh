package net.big_oh.datastructures.graph.jung.sparse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.Semaphore;

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
 * A unit test of the {@link JungVertex} class.
 * 
 * @author dwingate
 * @version Aug 8, 2010
 */
public class JungVertexUnitTest
{

	private JungVertex vertex;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		vertex = new JungVertex();
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
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungVertex#JungVertex()}
	 * .
	 */
	@Test
	public void testJungVertex()
	{
		assertNull(new JungVertex().getIdentifier());
		assertNull(new JungVertex().getValue());
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungVertex#JungVertex(java.lang.Object)}
	 * .
	 */
	@Test
	public void testJungVertexObject()
	{
		final Object[] vertexIdentifiers = { "foo bar", Integer.valueOf(123), new Semaphore(3), new String[] { "foo", "bar" } };

		for (Object vertexIdentifier : vertexIdentifiers)
		{
			assertEquals(vertexIdentifier, new JungVertex(vertexIdentifier).getIdentifier());
			assertNull(new JungVertex(vertexIdentifier).getValue());
		}
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungVertex#getImpl()}.
	 */
	@Test
	public void testGetImpl()
	{
		assertNotNull(vertex.getImpl());
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungVertex#setIdentifier(java.lang.Object)}
	 * and
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungVertex#getIdentifier()}
	 * .
	 */
	@Test
	public void testSetAndGetIdentifier()
	{
		final Object[] vertexIdentifiers = { "foo bar", Integer.valueOf(123), new Semaphore(3), new String[] { "foo", "bar" } };

		for (Object vertexIdentifier : vertexIdentifiers)
		{
			vertex.setIdentifier(vertexIdentifier);

			assertEquals(vertexIdentifier, vertex.getIdentifier());
		}
	}

	/**
	 * Test method for
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungVertex#setValue(java.lang.Object)}
	 * and
	 * {@link net.big_oh.datastructures.graph.jung.sparse.JungVertex#getValue()}
	 * .
	 */
	@Test
	public void testSetAndGetValue()
	{
		final Object[] vertexValues = { "foo bar", Integer.valueOf(123), new Semaphore(3), new String[] { "foo", "bar" } };

		for (Object vertexValue : vertexValues)
		{
			vertex.setValue(vertexValue);

			assertEquals(vertexValue, vertex.getValue());
		}
	}

}
