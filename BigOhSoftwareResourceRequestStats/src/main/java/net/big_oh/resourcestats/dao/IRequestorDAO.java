package net.big_oh.resourcestats.dao;

import java.util.Date;
import java.util.LinkedHashMap;

import net.big_oh.hibernate.StateOrientedDAO;
import net.big_oh.resourcestats.domain.Requestor;


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
 * Interface for the Requestor DAO.
 * 
 * @author davewingate
 * @version Oct 11, 2009
 */
public interface IRequestorDAO extends StateOrientedDAO<Requestor, String>
{

	/**
	 * @param maxNumRequestors
	 * @return Returns a list of the N most frequent Requestors of all time,
	 *         mapped to a count of requests.
	 * @throws IllegalArgumentException
	 *             Thrown if maxNumRequestors is less than 1.
	 */
	public LinkedHashMap<Requestor, Number> getMostFrequentRequestors(int maxNumRequestors) throws IllegalArgumentException;

	/**
	 * @param maxNumRequestors
	 * @param lastNDays
	 * @return Returns a list of the N most frequent Requestors, considering the
	 *         last N days, mapped to a count of requests.
	 * @throws IllegalArgumentException
	 *             Thrown if maxNumRequestors is less than 1 or if lastNDays is
	 *             less than 1.
	 */
	public LinkedHashMap<Requestor, Number> getMostFrequentRequestors(int maxNumRequestors, int lastNDays) throws IllegalArgumentException;

	/**
	 * @param maxNumRequestors
	 * @param since
	 *            If null, query will consider all ResourceRequests.
	 * @return Returns a list of the N most frequent Requestors, considering the
	 *         period of time between the since date parameter and now, mapped
	 *         to a count of requests.
	 * @throws IllegalArgumentException
	 *             Thrown if maxNumRequestors is less than 1 or if since date is
	 *             not null and after the current date.
	 */
	public LinkedHashMap<Requestor, Number> getMostFrequentRequestors(int maxNumRequestors, Date since) throws IllegalArgumentException;
}
