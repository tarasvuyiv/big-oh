package net.big_oh.common.utils;

import static org.junit.Assert.*;

import java.math.BigInteger;

import net.big_oh.common.utils.MathUtil;

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
 * TODO davewingate DOCUMENT ME!!!
 *
 * @author davewingate
 * @version Dec 15, 2009
 */
public class MathUtilUnitTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link net.big_oh.common.utils.MathUtil#factorial(int)}.
	 */
	@Test
	public void testFactorial()
	{
		assertEquals(BigInteger.valueOf(1), MathUtil.factorial(0));
		assertEquals(BigInteger.valueOf(1), MathUtil.factorial(1));
		assertEquals(BigInteger.valueOf(2), MathUtil.factorial(2));
		assertEquals(BigInteger.valueOf(6), MathUtil.factorial(3));
		assertEquals(BigInteger.valueOf(24), MathUtil.factorial(4));
		assertEquals(BigInteger.valueOf(6402373705728000l), MathUtil.factorial(18));
	}

}
