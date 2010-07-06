package net.big_oh.hibernate;

import static junit.framework.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * A class that tests the basic functionality provided by the HibernateVO class.
 * 
 * @author davewingate
 * @version Aug 24, 2009
 */
public class HibernateVOUnitTest
{
	
	private static final Log logger = LogFactory.getLog(HibernateVOUnitTest.class);
	
	private HackVO emptyVo;
	private HackVO voFrankA;
	private HackVO voFrankB;
	private HackVO voAngie;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		emptyVo = new HackVO();
		
		voFrankA = new HackVO();
		voFrankA.setGuid("1");
		voFrankA.setBusinessKey("Frank");
		populateSomeInts(voFrankA);
		
		voFrankB = new HackVO();
		voFrankB.setGuid("2");
		voFrankB.setBusinessKey("Frank");
		populateSomeInts(voFrankB);
		
		voAngie = new HackVO();
		voAngie.setGuid("3");
		voAngie.setBusinessKey("Angie");
		populateSomeInts(voAngie);
	}
	
	private void populateSomeInts(HackVO hackVo) {
		for (int i = 0; i < 100; i++) {
			hackVo.getSomeInts().add(new Integer(i));
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testToString()
	{
		// output objects to console for easy diagnostics
		logger.info(emptyVo);
		logger.info(voFrankA);
		logger.info(voFrankB);
		logger.info(voAngie);
		
		assertNotNull(emptyVo.toString());
		assertNotNull(voFrankA.toString());
		assertNotNull(voFrankB.toString());
		assertFalse(StringUtils.equals(voFrankA.toString(), voFrankB.toString()));
		assertFalse(StringUtils.equals(voFrankA.toString(), voAngie.toString()));
		assertTrue(StringUtils.equals(voFrankA.toString(), voFrankA.toString()));
	}

	@Test
	public void testHashCode()
	{
		assertEquals(voFrankA.hashCode(), voFrankB.hashCode());
	}

	@Test
	public void testEquals()
	{
		assertTrue(voFrankA.equals(voFrankB));
		assertTrue(voFrankB.equals(voFrankA));
		assertFalse(voFrankA.equals(emptyVo));
		assertFalse(emptyVo.equals(voFrankA));
		assertFalse(voFrankA.equals(voAngie));
		assertFalse(voAngie.equals(voFrankA));
	}

}
