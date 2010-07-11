package net.big_oh.common.utils;

/*
 Copyright (c) 2008 Dave Wingate dba Big-Oh Software (www.big-oh.net)

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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 * A set of static convenience methods for working with collections.
 * 
 * @author davewingate
 * 
 */
public class CollectionsUtil
{

	/**
	 * Constructs a single String by iterating over the elements in the
	 * collection parameter, calling toString() on each method.
	 * 
	 * @param <T>
	 * @param collection
	 * @return A comma-separated value String.
	 */
	public static <T extends Object> String toCsvString(Collection<T> collection)
	{
		return toXsvString(collection, ", ");
	}

	/**
	 * Constructs a single String by iterating over the elements in the
	 * collection parameter, calling toString() on each method.
	 * 
	 * @param <T>
	 * @param collection
	 * @param separator
	 * @return A separator-separated value String.
	 */
	public static <T extends Object> String toXsvString(Collection<T> collection, String separator)
	{
		if (collection == null)
		{
			return null;
		}

		StringBuffer sb = new StringBuffer();

		int count = 0;
		for (Iterator<T> iter = collection.iterator(); iter.hasNext(); count++)
		{
			if (count > 0)
			{
				sb.append(separator);
			}
			sb.append(iter.next().toString());
		}

		return sb.toString();
	}

	/**
	 * Wraps an {@link Enumeration} as an instance of {@link Iterable} so that
	 * it can be passed into a "for each" logical construct.
	 * 
	 * @param <T>
	 * @param enumeration
	 * @return An Iterable representation of the enumeration parameter.
	 */
	public static <T> Iterable<T> toIterable(final Enumeration<T> enumeration)
	{

		return new Iterable<T>()
		{
			public Iterator<T> iterator()
			{
				return new Iterator<T>()
				{

					public boolean hasNext()
					{
						return enumeration.hasMoreElements();
					}

					public T next()
					{
						return enumeration.nextElement();
					}

					public void remove()
					{
						throw new UnsupportedOperationException();
					}
				};

			}
		};

	}

	/**
	 * 
	 * @param <T>
	 * @param originalSet
	 *            The set of original objects from which combinations of size k
	 *            would be generated.
	 * @param k
	 *            The size of combinations that would be generated.
	 * @return Returns the number of possible ways to construct subsets of size
	 *         k from the originalSet.
	 * @throws IllegalArgumentException
	 *             thrown if k is less than zero of greater than the size of the
	 *             original set.
	 */
	public static <T> BigInteger countCombinations(Set<T> originalSet, int k) throws IllegalArgumentException
	{
		return countCombinations(originalSet.size(), k);
	}

	protected static <T> BigInteger countCombinations(int n, int k) throws IllegalArgumentException
	{
		// sanity check
		if (k < 0)
		{
			throw new IllegalArgumentException("The value of the k parameter cannot be less than zero.");
		}
		if (k > n)
		{
			throw new IllegalArgumentException("The value of the k parameter cannot be greater than n, the size of the originalSet.");
		}

		// The end result will be equal to n! / (k! * ((n-k)!))
		
		// start by doing some up front evaluation of the denominator
		int maxDenomArg;
		int minDenomArg;
		if ((n-k) > k) {
			maxDenomArg = (n-k);
			minDenomArg = k;
		}
		else {
			maxDenomArg = k;
			minDenomArg = (n-k);
		}
		
		// First, do an efficient calculation of n! / maxDenomArg!
		BigInteger partialCalculation = BigInteger.ONE;
		for (int i = maxDenomArg + 1; i <= n; i++) {
			partialCalculation = partialCalculation.multiply(BigInteger.valueOf(i));
		}
		
		// Lastly, produce the final solution by calculating partialCalculation / minDenomArg!
		return partialCalculation.divide(MathUtil.factorial(minDenomArg));
	}

	/**
	 * 
	 * @param <T>
	 * @param originalSet
	 *            The set of original objects from which combinations of size k
	 *            will be generated.
	 * @param k
	 *            The size of combinations to be generated.
	 * @return Returns the set of all size k sets (or combinations) that can be
	 *         constructed from the originalSet
	 * @throws IllegalArgumentException
	 *             thrown if k is less than zero of greater than the size of the
	 *             original set.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<Set<T>> getCombinations(Set<T> originalSet, int k) throws IllegalArgumentException
	{

		// sanity check
		if (k < 0)
		{
			throw new IllegalArgumentException("The value of the k parameter cannot be less than zero.");
		}
		if (k > originalSet.size())
		{
			throw new IllegalArgumentException("The value of the k parameter cannot be greater than the size of originalSet.");
		}

		// base case ... k == 0
		if (k == 0)
		{
			Set<Set<T>> combinations = new HashSet<Set<T>>();
			combinations.add(new HashSet<T>());
			return combinations;
		}

		// base case ... k == 1
		if (k == 1)
		{
			Set<Set<T>> combinations = new HashSet<Set<T>>();
			for (T t : originalSet)
			{
				Set<T> combination = new HashSet<T>();
				combination.add(t);
				combinations.add(combination);
			}
			return combinations;
		}

		// recursive case
		T arbitraryElement = (T) CollectionUtils.get(originalSet, 0);

		Set<T> everythingElse = new HashSet<T>(originalSet);
		everythingElse.remove(arbitraryElement);

		Set<Set<T>> combinations = new HashSet<Set<T>>();

		// pair arbitraryElelement with combinations of size k-1 from the
		// everythingElse collection
		for (Set<T> combinationForEverythingElse : getCombinations(everythingElse, k - 1))
		{
			combinationForEverythingElse.add(arbitraryElement);
			combinations.add(combinationForEverythingElse);
		}

		// get combinations of size k from the everythingElse collection
		if (k <= everythingElse.size())
		{
			for (Set<T> combinationForEverythingElse : getCombinations(everythingElse, k))
			{
				combinations.add(combinationForEverythingElse);
			}
		}

		return combinations;
	}

	/**
	 * 
	 * @param <T>
	 * @param originals
	 * @param pageSize
	 * @return Returns one or more pages containing at most pageSize original
	 *         objects.
	 * @throws IllegalArgumentException
	 *             Thrown if the originals parameter is null or if the
	 *             pageSizeParameter is less than one.
	 */
	public static <T> List<List<T>> page(Collection<T> originals, int pageSize) throws IllegalArgumentException
	{

		if (originals == null)
		{
			throw new IllegalArgumentException("Cannot page a null collection or originals.");
		}
		if (pageSize < 1)
		{
			throw new IllegalArgumentException("The pageSize argument must be greater than or equal to one.");
		}

		List<List<T>> pages = new ArrayList<List<T>>();

		List<T> currentPage = null;

		for (T original : originals)
		{

			if (currentPage == null || currentPage.size() == pageSize)
			{
				currentPage = new ArrayList<T>();
				pages.add(currentPage);
			}

			currentPage.add(original);

		}

		return pages;

	}

}
