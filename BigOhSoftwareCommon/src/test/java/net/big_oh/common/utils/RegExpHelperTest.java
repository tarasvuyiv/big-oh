package net.big_oh.common.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import net.big_oh.common.utils.RegExpHelper;

import org.junit.Test;

public class RegExpHelperTest
{

	private static final String regExpPattern = "a*b";
	private static final String sourceString = "aaaaabbbbbbaaaaabbb";
	private static final String replacement = "c";

	@Test
	public void testReplaceFirst()
	{
		assertEquals(sourceString.replaceFirst(regExpPattern, replacement), RegExpHelper.replaceFirst(sourceString, regExpPattern, replacement));
		
		// test for meta character processing
		assertEquals("foo-$-bar", RegExpHelper.replaceFirst("foo?bar", "\\?", "-$-"));
		
	}

	@Test
	public void testReplaceAll()
	{
		assertEquals(sourceString.replaceAll(regExpPattern, replacement), RegExpHelper.replaceAll(sourceString, regExpPattern, replacement));
		
		// test for meta character processing
		assertEquals("-$-foo-$-bar-$-", RegExpHelper.replaceAll("?foo?bar?", "\\?", "-$-"));
	}

	@Test
	public void testMatches()
	{
		assertEquals(sourceString.matches(regExpPattern), RegExpHelper.matches(sourceString, regExpPattern));
	}

	@Test
	public void testSplitString()
	{
		assertArrayEquals(sourceString.split(regExpPattern), RegExpHelper.split(sourceString, regExpPattern));
		assertArrayEquals(sourceString.split(regExpPattern, 1), RegExpHelper.split(sourceString, regExpPattern, 1));
	}

	@Test
	public void testPerformance()
	{

		int numTimes = 100000;

		long begin1 = System.currentTimeMillis();
		for (int i = 0; i < numTimes; i++)
		{
			sourceString.matches(regExpPattern);
		}
		long duration1 = System.currentTimeMillis() - begin1;

		long begin2 = System.currentTimeMillis();
		Pattern p = Pattern.compile(regExpPattern);
		for (int i = 0; i < numTimes; i++)
		{
			p.matcher(sourceString).matches();
		}
		long duration2 = System.currentTimeMillis() - begin2;

		assertTrue(duration2 < duration1);

	}

}
