package net.big_oh.common.utils;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.LRUMap;

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

/**
 * The RegExpHelper class provides static methods that make use of previously
 * compiled & cached regular expression {@link Pattern} objects to speed the
 * evaluation of regular expressions.
 */
public class RegExpHelper
{

	private static final Map<String, Pattern> compiledPatterns = buildCompiledPatternsMap();

	@SuppressWarnings("unchecked")
	private static Map<String, Pattern> buildCompiledPatternsMap()
	{
		// The compiledPatterns Map is synchronized because it will be accessed
		// concurrently by multiple threads
		return Collections.synchronizedMap(new LRUMap(1000));
	}

	private static Pattern getCompiledPatern(String regExpPattern)
	{

		Pattern pattern = compiledPatterns.get(regExpPattern);

		// No need to protect this pseudo-critical section from concurrent
		// execution since regular expression compilation is idempotent
		if (pattern == null)
		{
			pattern = Pattern.compile(regExpPattern);
			compiledPatterns.put(regExpPattern, pattern);
		}

		return pattern;

	}

	/**
	 * Logically equivalent to: <br/>
	 * <code>
	 * sourceString.replaceFirst(regExpPattern, replacementString)
	 * </code> <br/>
	 * ... but performance is vastly superior for regular expression patterns
	 * that have previously been used.
	 * 
	 * @param sourceString
	 * @param regExpPattern
	 * @param replacementString
	 *            A replacement text string. The regexp group character ('$') is
	 *            escaped.
	 * @return A new string with the first instance of regExpPattern replaced by
	 *         replacementString.
	 */
	public static String replaceFirst(String sourceString, String regExpPattern, String replacementString)
	{
		return getCompiledPatern(regExpPattern).matcher(sourceString).replaceFirst(replacementString.replace("$", "\\$"));
	}

	/**
	 * Logically equivalent to: <br/>
	 * <code>
	 * sourceString.replaceAll(regExpPattern, replacementString)
	 * </code> <br/>
	 * ... but performance is vastly superior for regular expression patterns
	 * that have previously been used.
	 * 
	 * @param sourceString
	 * @param regExpPattern
	 * @param replacementString
	 *            A replacement text string. The regexp group character ('$') is
	 *            escaped.
	 * @return A new string with all instances of regExpPattern replaced by
	 *         replacementString.
	 */
	public static String replaceAll(String sourceString, String regExpPattern, String replacementString)
	{
		return getCompiledPatern(regExpPattern).matcher(sourceString).replaceAll(replacementString.replace("$", "\\$"));
	}

	/**
	 * Logically equivalent to: <br/>
	 * <code>
	 * sourceString.matches(regExpPattern)
	 * </code> <br/>
	 * ... but performance is vastly superior for regular expression patterns
	 * that have previously been used.
	 * 
	 * @param sourceString
	 * @param regExpPattern
	 * @return A boolean indicating whether the sourceString matches the regular
	 *         expression.
	 */
	public static boolean matches(String sourceString, String regExpPattern)
	{
		return getCompiledPatern(regExpPattern).matcher(sourceString).matches();
	}

	/**
	 * Logically equivalent to: <br/>
	 * <code>
	 * sourceString.split(regExpPattern)
	 * </code> <br/>
	 * ... but performance is vastly superior for regular expression patterns
	 * that have previously been used.
	 * 
	 * @param sourceString
	 * @param regExpPattern
	 * @return A String array.
	 */
	public static String[] split(String sourceString, String regExpPattern)
	{
		return getCompiledPatern(regExpPattern).split(sourceString);
	}

	/**
	 * Logically equivalent to: <br/>
	 * <code>
	 * sourceString.split(regExpPattern, limit)
	 * </code> <br/>
	 * ... but performance is vastly superior for regular expression patterns
	 * that have previously been used.
	 * 
	 * @param sourceString
	 * @param regExpPattern
	 * @param limit
	 * @return A String array.
	 */
	public static String[] split(String sourceString, String regExpPattern, int limit)
	{
		return getCompiledPatern(regExpPattern).split(sourceString, limit);
	}

	/**
	 * A convenience method that is equivalent to calling: <br/>
	 * <code>matches(sourceString, "^.*" + regExpPattern + ".*$")</code>
	 * 
	 * @param sourceString
	 * @param regExpPattern
	 * @return A boolean indicating whether the sourceString contains a
	 *         substring that matches the regular expression.
	 */
	public static boolean contains(String sourceString, String regExpPattern)
	{
		return getCompiledPatern("^.*" + regExpPattern + ".*$").matcher(sourceString).matches();
	}

}
