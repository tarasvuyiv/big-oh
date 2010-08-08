package net.big_oh.resourcestats.dao;

import java.util.Date;
import java.util.LinkedHashMap;

import net.big_oh.hibernate.HibernateDAO;
import net.big_oh.resourcestats.domain.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Query;


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
 * Provides CRUD and fetch operations for {@link Resource} objects.
 * 
 * @author davewingate
 * @version Oct 25, 2009
 */
public class ResourceDAO extends HibernateDAO<Resource, String> implements IResourceDAO
{

	public LinkedHashMap<Resource, Number> getMostPopularResources(int maxNumResources) throws IllegalArgumentException
	{
		return getMostPopularResources(maxNumResources, null);
	}

	public LinkedHashMap<Resource, Number> getMostPopularResources(int maxNumResources, int lastNDays) throws IllegalArgumentException
	{
		Date now = new Date();
		Date nDaysAgo = DateUtils.addDays(now, -1 * Math.abs(lastNDays));
		return getMostPopularResources(maxNumResources, nDaysAgo);
	}

	public LinkedHashMap<Resource, Number> getMostPopularResources(int maxNumResources, Date since) throws IllegalArgumentException
	{
		// translate null parameters
		if (since == null)
		{
			// set an appropriate default value
			since = new Date(0);
		}

		// sanity check for parameters
		if (maxNumResources < 1)
		{
			throw new IllegalArgumentException("Argument maxNumResources must not be less than 1.");
		}
		if (since.after(new Date()))
		{
			throw new IllegalArgumentException("Argument since must not be after the current system time.");
		}

		Query query = getQualifiedNamedQuery("getMostPopularResources");
		query.setMaxResults(maxNumResources);
		query.setDate("since", since);

		return processQueryResults(query.list());
	}

	public LinkedHashMap<Resource, Number> getSlowestResources(int maxNumResources) throws IllegalArgumentException
	{
		// sanity check for parameters
		if (maxNumResources < 1)
		{
			throw new IllegalArgumentException("Argument maxNumResources must not be less than 1.");
		}

		Query query = getQualifiedNamedQuery("getSlowestResources");
		query.setMaxResults(maxNumResources);

		return processQueryResults(query.list());
	}

}
