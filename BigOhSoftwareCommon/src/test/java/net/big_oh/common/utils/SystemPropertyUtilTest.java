package net.big_oh.common.utils;

import static org.junit.Assert.*;

import net.big_oh.common.utils.SystemPropertyUtil;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemPropertyUtilTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testGetSystemFileSeparator()
	{
		assertFalse(StringUtils.isBlank(SystemPropertyUtil.getSystemFileSeparator()));
	}

	@Test
	public void testGetSystemPathSeparator()
	{
		assertFalse(StringUtils.isBlank(SystemPropertyUtil.getSystemPathSeparator()));
	}

	@Test
	public void testGetSystemLineSeparator()
	{
		assertFalse(StringUtils.isEmpty(SystemPropertyUtil.getSystemLineSeparator()));
	}

}
