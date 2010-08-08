package net.big_oh.common.utils;

import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.Statistics;

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
 * Utility class for extracting information from {@link Statistics} and
 * {@link QueryStatistics} objects.
 * 
 * @author davewingate
 * @version Nov 8, 2009
 */
public class HibernateStatisticsUtil
{

	/**
	 * Gets the hit ratio for Hibernate's second level caches.
	 * 
	 * @param hibernateStats
	 * @return A value between 0 and 1 (inclusive) representing the cache hit
	 *         ratio.
	 * @throws IllegalArgumentException
	 *             Thrown if the hibernateStats object is null, or does not have
	 *             statistics enabled.
	 */
	// TODO Unit test me!
	public static double getSecondLevelCacheHitRatio(Statistics hibernateStats) throws IllegalArgumentException
	{

		if (hibernateStats == null)
		{
			throw new IllegalArgumentException("The hibernateStats may not be null.");
		}
		if (!hibernateStats.isStatisticsEnabled())
		{
			throw new IllegalArgumentException("Hibernate stats are unexpectedly not enabled!");
		}

		return (double) hibernateStats.getSecondLevelCacheHitCount() / (double) Math.max(1, (hibernateStats.getSecondLevelCacheHitCount() + hibernateStats.getSecondLevelCacheMissCount()));

	}

	/**
	 * Gets the hit ratio for Hibernate's query cache.
	 * 
	 * @param hibernateStats
	 * @return A value between 0 and 1 (inclusive) representing the cache hit
	 *         ratio.
	 * @throws IllegalArgumentException
	 *             Thrown if the hibernateStats object is null, or does not have
	 *             statistics enabled.
	 */
	// TODO Unit test me!
	public static double getQueryCacheHitRatio(Statistics hibernateStats) throws IllegalArgumentException
	{

		if (hibernateStats == null)
		{
			throw new IllegalArgumentException("The hibernateStats may not be null.");
		}
		if (!hibernateStats.isStatisticsEnabled())
		{
			throw new IllegalArgumentException("Hibernate stats are unexpectedly not enabled!");
		}

		return (double) hibernateStats.getQueryCacheHitCount() / (double) Math.max(1, (hibernateStats.getQueryCacheHitCount() + hibernateStats.getQueryCacheMissCount()));

	}

	/**
	 * @param qStats
	 * @return Returns the average result set size for the query.
	 * @throws IllegalArgumentException
	 *             Thrown if the qStats object is null.
	 */
	// TODO Unit test me!
	public static double getAverageResultSize(QueryStatistics qStats) throws IllegalArgumentException
	{

		if (qStats == null)
		{
			throw new IllegalArgumentException("The qStats may not be null.");
		}

		return (double) qStats.getExecutionRowCount() / (double) Math.max(1, qStats.getExecutionCount());

	}

}
