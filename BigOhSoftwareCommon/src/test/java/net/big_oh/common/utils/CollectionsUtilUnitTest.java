package net.big_oh.common.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.big_oh.common.utils.CollectionsUtil;
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
 * A unit test exercising the methods provided by the CollectionsUtil class.
 * 
 * @author davewingate
 * @version Dec 10, 2009
 */
public class CollectionsUtilUnitTest
{

	private Object a;
	private Object b;
	private Object c;
	private Object d;

	private Set<Object> emptySet;
	private Set<Object> singletonSet;
	private Set<Object> sizeThreeSet;
	private Set<Object> sizeFourSet;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		a = new String("a");
		b = new String("b");
		c = new String("c");
		d = new String("d");

		emptySet = new HashSet<Object>();
		emptySet = Collections.unmodifiableSet(emptySet);
		assertEquals(0, emptySet.size());

		singletonSet = new HashSet<Object>();
		singletonSet.add(a);
		singletonSet = Collections.unmodifiableSet(singletonSet);
		assertEquals(1, singletonSet.size());

		sizeThreeSet = new HashSet<Object>();
		sizeThreeSet.add(a);
		sizeThreeSet.add(b);
		sizeThreeSet.add(c);
		sizeThreeSet = Collections.unmodifiableSet(sizeThreeSet);
		assertEquals(3, sizeThreeSet.size());

		sizeFourSet = new HashSet<Object>();
		sizeFourSet.add(a);
		sizeFourSet.add(b);
		sizeFourSet.add(c);
		sizeFourSet.add(d);
		sizeFourSet = Collections.unmodifiableSet(sizeFourSet);
		assertEquals(4, sizeFourSet.size());
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
	 * {@link net.big_oh.common.utils.CollectionsUtil#getCombinations(java.util.Set, int)}
	 * .
	 */
	@Test
	public void testGetCombinationsSizes()
	{

		for (int originalCollectionSize = 0; originalCollectionSize < 10; originalCollectionSize++)
		{

			// create the set of original values from which combinations will be
			// derived
			Set<Object> originalCollectionSet = new HashSet<Object>();
			for (int i = 0; i < originalCollectionSize; i++)
			{
				originalCollectionSet.add(new Object());
			}
			originalCollectionSet = Collections.unmodifiableSet(originalCollectionSet);

			// evaluate combinations generated for various values of k
			for (int k = 0; k <= originalCollectionSize; k++)
			{

				BigInteger expectedNumberOfCombinations = MathUtil.factorial(originalCollectionSize).divide(((MathUtil.factorial(k)).multiply((MathUtil.factorial(originalCollectionSize - k)))));

				Set<Set<Object>> combinations = CollectionsUtil.getCombinations(originalCollectionSet, k);
				assertEquals(expectedNumberOfCombinations.longValue(), combinations.size());
				for (Set<Object> combination : combinations)
				{
					assertEquals(k, combination.size());
				}

			}

		}

	}

	/**
	 * Test method for
	 * {@link net.big_oh.common.utils.CollectionsUtil#getCombinations(java.util.Set, int)}
	 * .
	 */
	@Test
	public void testGetCombinationsValues()
	{

		Set<Set<Object>> combinationsForChoose2From3 = CollectionsUtil.getCombinations(sizeThreeSet, 2);
		assertEquals(3, combinationsForChoose2From3.size());
		assertTrue(combinationsForChoose2From3.contains(new HashSet<Object>(Arrays.asList(new Object[] { a, b }))));
		assertTrue(combinationsForChoose2From3.contains(new HashSet<Object>(Arrays.asList(new Object[] { a, c }))));
		assertTrue(combinationsForChoose2From3.contains(new HashSet<Object>(Arrays.asList(new Object[] { b, c }))));

		Set<Set<Object>> combinationsForChoose2From4 = CollectionsUtil.getCombinations(sizeFourSet, 2);
		assertEquals(6, combinationsForChoose2From4.size());
		assertTrue(combinationsForChoose2From4.contains(new HashSet<Object>(Arrays.asList(new Object[] { a, b }))));
		assertTrue(combinationsForChoose2From4.contains(new HashSet<Object>(Arrays.asList(new Object[] { a, c }))));
		assertTrue(combinationsForChoose2From4.contains(new HashSet<Object>(Arrays.asList(new Object[] { a, d }))));
		assertTrue(combinationsForChoose2From4.contains(new HashSet<Object>(Arrays.asList(new Object[] { b, c }))));
		assertTrue(combinationsForChoose2From4.contains(new HashSet<Object>(Arrays.asList(new Object[] { b, d }))));
		assertTrue(combinationsForChoose2From4.contains(new HashSet<Object>(Arrays.asList(new Object[] { c, d }))));
	}

	/**
	 * Test method for
	 * {@link net.big_oh.common.utils.CollectionsUtil#getCombinations(java.util.Set, int)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetCombinationsFail1()
	{
		CollectionsUtil.getCombinations(Collections.emptySet(), -1);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.common.utils.CollectionsUtil#getCombinations(java.util.Set, int)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetCombinationsFail2()
	{
		CollectionsUtil.getCombinations(Collections.emptySet(), 1);
	}
	
	/**
	 * Test method for
	 * {@link net.big_oh.common.utils.CollectionsUtil#countCombinations(java.util.Set, int)}
	 * .
	 */
	@Test
	public void testCountCombinations_Set_int()
	{
		assertEquals(BigInteger.valueOf(1), CollectionsUtil.countCombinations(emptySet, 0));
		assertEquals(BigInteger.valueOf(1), CollectionsUtil.countCombinations(sizeFourSet, 0));
		assertEquals(BigInteger.valueOf(4), CollectionsUtil.countCombinations(sizeFourSet, 1));
		assertEquals(BigInteger.valueOf(6), CollectionsUtil.countCombinations(sizeFourSet, 2));
		assertEquals(BigInteger.valueOf(4), CollectionsUtil.countCombinations(sizeFourSet, 3));
		assertEquals(BigInteger.valueOf(1), CollectionsUtil.countCombinations(sizeFourSet, 4));
	}
	
	/**
	 * Test method for
	 * {@link net.big_oh.common.utils.CollectionsUtil#countCombinations(int, int)}
	 * .
	 */
	@Test
	public void testCountCombinations_int_int()
	{
		assertEquals(BigInteger.valueOf(82160), CollectionsUtil.countCombinations(80, 3));
	}

	/**
	 * Test method for
	 * {@link net.big_oh.common.utils.CollectionsUtil#page(java.util.Collection, int)}
	 * .
	 */
	@Test
	public void testPage()
	{
		// test size of collections returned
		assertEquals(0, CollectionsUtil.page(emptySet, 1).size());
		assertEquals(4, CollectionsUtil.page(sizeFourSet, 1).size());
		assertEquals(2, CollectionsUtil.page(sizeFourSet, 2).size());
		assertEquals(2, CollectionsUtil.page(sizeFourSet, 3).size());
		assertEquals(1, CollectionsUtil.page(sizeFourSet, 4).size());
		assertEquals(1, CollectionsUtil.page(sizeFourSet, 5).size());

		// test content of collections returned
		assertEquals(sizeFourSet, new HashSet<Object>(CollectionsUtil.page(sizeFourSet, 5).get(0)));
	}

}
