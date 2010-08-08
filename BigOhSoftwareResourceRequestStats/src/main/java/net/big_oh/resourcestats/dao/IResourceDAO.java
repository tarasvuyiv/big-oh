package net.big_oh.resourcestats.dao;

import java.util.Date;
import java.util.LinkedHashMap;

import net.big_oh.hibernate.StateOrientedDAO;
import net.big_oh.resourcestats.domain.Resource;


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
 * Interface for the Resource DAO.
 * 
 * @author davewingate
 * @version Oct 11, 2009
 */
public interface IResourceDAO extends StateOrientedDAO<Resource, String>
{

	/**
	 * 
	 * @param maxNumResources
	 * @return Returns the N most frequently requested Resources of all time,
	 *         mapped to a count of requests.
	 * @throws IllegalArgumentException
	 *             Thrown if maxNumResources is less than 1.
	 */
	public LinkedHashMap<Resource, Number> getMostPopularResources(int maxNumResources) throws IllegalArgumentException;

	/**
	 * 
	 * @param maxNumResources
	 * @param lastNDays
	 * @return Returns the N most frequently requested Resources, considering
	 *         the last N days, mapped to a count of requests.
	 * @throws IllegalArgumentException
	 *             Thrown if maxNumResources is less than 1 or if lastNDays is
	 *             less than 1.
	 */
	public LinkedHashMap<Resource, Number> getMostPopularResources(int maxNumResources, int lastNDays) throws IllegalArgumentException;

	/**
	 * 
	 * @param maxNumResources
	 * @param since
	 *            If null, query will consider all ResourceRequests.
	 * @return Returns the N most frequently requested Resources, considering
	 *         the period of time between the since date parameter and now,
	 *         mapped to a count of requests.
	 * @throws IllegalArgumentException
	 *             Thrown if maxNumResources is less than 1 or if since date is
	 *             not null and after the current date.
	 */
	public LinkedHashMap<Resource, Number> getMostPopularResources(int maxNumResources, Date since) throws IllegalArgumentException;

	/**
	 * 
	 * @param maxNumResources
	 * @return Returns a map of the N most frequently requested Resources of all
	 *         time to the average ResourceRequest service time for each
	 *         Resource.
	 * @throws IllegalArgumentException
	 *             Thrown if maxNumResources is less than 1.
	 */
	public LinkedHashMap<Resource, Number> getSlowestResources(int maxNumResources) throws IllegalArgumentException;

}
