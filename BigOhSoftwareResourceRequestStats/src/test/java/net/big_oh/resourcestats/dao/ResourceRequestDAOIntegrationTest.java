package net.big_oh.resourcestats.dao;

import static org.junit.Assert.assertTrue;

import net.big_oh.common.utils.HibernateUtil;
import net.big_oh.resourcestats.dao.DAOFactory;
import net.big_oh.resourcestats.dao.IResourceRequestDAO;
import net.big_oh.resourcestats.dao.ResourceRequestDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
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
 * An integration test for confirming that the data access methods provided by
 * {@link ResourceRequestDAO} function correctly.
 * 
 * @author davewingate
 * @version Oct 31, 2009
 */
public class ResourceRequestDAOIntegrationTest
{

	private static final Log log = LogFactory.getLog(ResourceRequestDAOIntegrationTest.class);
	private static final SessionFactory sf = HibernateUtil.getSessionFactory();

	private IResourceRequestDAO dao;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		dao = DAOFactory.getResourceRequestDAO();
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
	 * {@link net.big_oh.resourcestats.dao.ResourceRequestDAO#getAverageResourceRequestServiceTime()}
	 * .
	 */
	@Test
	public void testGetAverageResourceRequestServiceTime()
	{
		try
		{
			sf.getCurrentSession().beginTransaction();

			Double avgServiceTime = dao.getAverageResourceRequestServiceTime();
			assertTrue(avgServiceTime == null || avgServiceTime >= 0);

			sf.getCurrentSession().getTransaction().commit();
		}
		catch (Throwable t)
		{
			HibernateUtil.rollback(t, sf, log);
			throw new RuntimeException(t);
		}
	}

}
