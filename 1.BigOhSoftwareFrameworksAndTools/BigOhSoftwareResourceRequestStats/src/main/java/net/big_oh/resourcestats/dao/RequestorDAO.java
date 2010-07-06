package net.big_oh.resourcestats.dao;

import java.util.Date;
import java.util.LinkedHashMap;

import net.big_oh.hibernate.HibernateDAO;
import net.big_oh.resourcestats.domain.Requestor;

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
 * Provides CRUD and fetch operations for {@link Requestor} objects.
 * 
 * @author davewingate
 * @version Oct 25, 2009
 */
public class RequestorDAO extends HibernateDAO<Requestor, String> implements IRequestorDAO
{

	public LinkedHashMap<Requestor, Number> getMostFrequentRequestors(int maxNumRequestors)
	{
		return getMostFrequentRequestors(maxNumRequestors, null);
	}

	public LinkedHashMap<Requestor, Number> getMostFrequentRequestors(int maxNumRequestors, int lastNDays)
	{
		Date now = new Date();
		Date nDaysAgo = DateUtils.addDays(now, -1 * Math.abs(lastNDays));
		return getMostFrequentRequestors(maxNumRequestors, nDaysAgo);
	}

	public LinkedHashMap<Requestor, Number> getMostFrequentRequestors(int maxNumRequestors, Date since) throws IllegalArgumentException
	{
		// translate null parameters
		if (since == null)
		{
			// set an appropriate default value
			since = new Date(0);
		}

		// sanity check for parameters
		if (maxNumRequestors < 1)
		{
			throw new IllegalArgumentException("Argument maxNumRequestors must not be less than 1.");
		}
		if (since.after(new Date()))
		{
			throw new IllegalArgumentException("Argument since must not be after the current system time.");
		}

		Query query = getQualifiedNamedQuery("getMostFrequentRequestors");
		query.setMaxResults(maxNumRequestors);
		query.setDate("since", since);

		return processQueryResults(query.list());
	}

}
