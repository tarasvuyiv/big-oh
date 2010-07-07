package net.big_oh.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.big_oh.hibernate.HibernateDAO;

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
 * A class that tests the basic functionality provided by the HibernateDAO
 * class.
 * 
 * @author davewingate
 * @version Nov 1, 2009
 */
public class HibernateDAOUnitTest
{

	private HibernateDAO<HackVO, String> dao;

	private HackVO vo1;
	private HackVO vo2;
	private HackVO vo3;
	private HackVO[] allVOs;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		dao = new HibernateDAO<HackVO, String>()
		{
		};

		vo1 = new HackVO();
		vo1.setGuid("1");
		vo1.setBusinessKey("vo1");

		vo2 = new HackVO();
		vo2.setGuid("2");
		vo2.setBusinessKey("vo2");

		vo3 = new HackVO();
		vo3.setGuid("3");
		vo3.setBusinessKey("vo3");

		allVOs = new HackVO[] { vo1, vo2, vo3 };
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
	 * {@link net.big_oh.hibernate.HibernateDAO#processQueryResults(java.util.List)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testProcessQueryResults_BadInput1()
	{
		dao.processQueryResults(null);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.hibernate.HibernateDAO#processQueryResults(java.util.List)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testProcessQueryResults_BadInput2()
	{
		List<Object[]> badInputList = new ArrayList<Object[]>();
		badInputList.add(new Object[] { new Double(12), vo1 });

		dao.processQueryResults(badInputList);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.hibernate.HibernateDAO#processQueryResults(java.util.List)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testProcessQueryResults_BadInput3()
	{
		List<Object[]> badInputList = new ArrayList<Object[]>();
		badInputList.add(new Object[] { vo1, new Double(12), new Double(12) });

		dao.processQueryResults(badInputList);
	}

	/**
	 * Test method for
	 * {@link net.big_oh.hibernate.HibernateDAO#processQueryResults(java.util.List)}
	 * .
	 */
	@Test
	public void testProcessQueryResults()
	{
		List<Object[]> inputList = new ArrayList<Object[]>();

		for (int i = 0; i < allVOs.length; i++)
		{
			inputList.add(new Object[] { allVOs[i], new Double(i) });
		}

		LinkedHashMap<HackVO, Number> processedQueryResults = dao.processQueryResults(inputList);

		assertEquals(allVOs.length, processedQueryResults.size());

		int count = 0;
		for (Object key : processedQueryResults.keySet())
		{
			assertTrue(key instanceof HackVO);
			assertTrue(allVOs[count] == key);

			Object value = processedQueryResults.get(key);
			assertTrue(value instanceof Number);
			assertEquals(new Double(count), value);

			count++;
		}

	}
}
