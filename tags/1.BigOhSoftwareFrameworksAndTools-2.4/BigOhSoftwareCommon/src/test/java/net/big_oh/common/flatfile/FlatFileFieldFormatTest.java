package net.big_oh.common.flatfile;

import static org.junit.Assert.assertEquals;

import net.big_oh.common.flatfile.FlatFileFieldFormat;
import net.big_oh.common.flatfile.FlatFileFieldPaddingType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FlatFileFieldFormatTest
{

	private FlatFileFieldFormat fieldFormat1;
	private FlatFileFieldFormat fieldFormat2;

	@Before
	public void setUp() throws Exception
	{
		fieldFormat1 = new FlatFileFieldFormat(1, 25, FlatFileFieldPaddingType.VARIABLE_WIDTH, ' ');
		fieldFormat2 = new FlatFileFieldFormat(26, 45, FlatFileFieldPaddingType.VARIABLE_WIDTH, ' ');
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testGetFieldLength()
	{
		assertEquals(fieldFormat1.getFieldEndPosition() - fieldFormat1.getFieldStartPosition() + 1, fieldFormat1.getFieldLength());
		assertEquals(fieldFormat2.getFieldEndPosition() - fieldFormat2.getFieldStartPosition() + 1, fieldFormat2.getFieldLength());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorSanity1()
	{
		new FlatFileFieldFormat(0, 25, FlatFileFieldPaddingType.VARIABLE_WIDTH, ' ');
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorSanity2()
	{
		new FlatFileFieldFormat(26, 25, FlatFileFieldPaddingType.VARIABLE_WIDTH, ' ');
	}

}
