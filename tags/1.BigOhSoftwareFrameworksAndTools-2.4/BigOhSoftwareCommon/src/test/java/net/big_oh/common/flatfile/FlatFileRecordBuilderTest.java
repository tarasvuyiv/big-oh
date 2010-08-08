package net.big_oh.common.flatfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.big_oh.common.flatfile.FlatFileFieldFormat;
import net.big_oh.common.flatfile.FlatFileFieldPaddingType;
import net.big_oh.common.flatfile.FlatFileRecordBuilder;
import net.big_oh.common.flatfile.FlatFileRecordField;
import net.big_oh.common.utils.RegExpHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FlatFileRecordBuilderTest
{

	private final char intraFieldPaddingChar = '*';

	private final FlatFileFieldFormat leftPadField = new FlatFileFieldFormat(1, 20, FlatFileFieldPaddingType.PADDING_ON_LEFT, intraFieldPaddingChar);
	private final FlatFileFieldFormat rightPadField = new FlatFileFieldFormat(1, 20, FlatFileFieldPaddingType.PADDING_ON_RIGHT, intraFieldPaddingChar);
	private final FlatFileFieldFormat noPadField = new FlatFileFieldFormat(1, 20, FlatFileFieldPaddingType.VARIABLE_WIDTH, intraFieldPaddingChar);

	private final FlatFileFieldFormat field1 = new FlatFileFieldFormat(1, 45, FlatFileFieldPaddingType.PADDING_ON_LEFT, ' ');
	private final FlatFileFieldFormat field2 = new FlatFileFieldFormat(120, 150, FlatFileFieldPaddingType.PADDING_ON_LEFT, ' ');
	private final FlatFileFieldFormat field3 = new FlatFileFieldFormat(60, 75, FlatFileFieldPaddingType.PADDING_ON_LEFT, ' ');

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testLeftPad()
	{
		FlatFileFieldFormat fieldFormat = leftPadField;
		String value = "DEFAULT";

		assertTrue("value length is unexpectedly not less than fieldFormat length", value.length() < fieldFormat.getFieldLength());

		String flatFileRecord = FlatFileRecordBuilder.buildFlatFileRecord(Collections.singleton(new FlatFileRecordField(fieldFormat, value)), null, ' ');
		assertEquals(intraFieldPaddingChar, flatFileRecord.charAt(0));
		assertEquals(intraFieldPaddingChar, flatFileRecord.charAt(fieldFormat.getFieldLength() - value.length() - 1));
	}

	@Test
	public void testRightPad()
	{
		FlatFileFieldFormat fieldFormat = rightPadField;
		String value = "DEFAULT";

		assertTrue("value length is unexpectedly not less than fieldFormat length", value.length() < fieldFormat.getFieldLength());

		String flatFileRecord = FlatFileRecordBuilder.buildFlatFileRecord(Collections.singleton(new FlatFileRecordField(fieldFormat, value)), null, ' ');
		assertEquals(intraFieldPaddingChar, flatFileRecord.charAt(fieldFormat.getFieldEndPosition() - 1));
		assertEquals(intraFieldPaddingChar, flatFileRecord.charAt(value.length()));
	}

	@Test
	public void testNoPad()
	{
		FlatFileFieldFormat fieldFormat = noPadField;
		String value = "DEFAULT";

		assertTrue("value length is unexpectedly not less than fieldFormat length", value.length() < fieldFormat.getFieldLength());

		String flatFileRecord = FlatFileRecordBuilder.buildFlatFileRecord(Collections.singleton(new FlatFileRecordField(fieldFormat, value)), null, ' ');
		assertEquals(value, flatFileRecord);
	}

	@Test
	public void testLeftPadValueTruncation()
	{
		FlatFileFieldFormat fieldFormat = leftPadField;
		String value = "DEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULT";

		assertTrue("value length is unexpectedly less than fieldFormat length", value.length() > fieldFormat.getFieldLength());

		String flatFileRecord = FlatFileRecordBuilder.buildFlatFileRecord(Collections.singleton(new FlatFileRecordField(fieldFormat, value)), null, ' ');
		assertEquals(value.substring(0, fieldFormat.getFieldLength()), flatFileRecord);
	}

	@Test
	public void testRightPadValueTruncation()
	{
		FlatFileFieldFormat fieldFormat = rightPadField;
		String value = "DEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULT";

		assertTrue("value length is unexpectedly less than fieldFormat length", value.length() > fieldFormat.getFieldLength());

		String flatFileRecord = FlatFileRecordBuilder.buildFlatFileRecord(Collections.singleton(new FlatFileRecordField(fieldFormat, value)), null, ' ');
		assertEquals(value.substring(0, fieldFormat.getFieldLength()), flatFileRecord);
	}

	@Test
	public void testNoPadValueTruncation()
	{
		FlatFileFieldFormat fieldFormat = noPadField;
		String value = "DEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULTDEFAULT";

		assertTrue("value length is unexpectedly less than fieldFormat length", value.length() > fieldFormat.getFieldLength());

		String flatFileRecord = FlatFileRecordBuilder.buildFlatFileRecord(Collections.singleton(new FlatFileRecordField(fieldFormat, value)), null, ' ');
		assertEquals(value.substring(0, fieldFormat.getFieldLength()), flatFileRecord);
	}

	@Test
	public void testSpacingOfNonConsecutiveFields()
	{
		String value = "DEFAULT";

		Set<FlatFileRecordField> recordFields = new HashSet<FlatFileRecordField>();
		recordFields.add(new FlatFileRecordField(field1, value));
		recordFields.add(new FlatFileRecordField(field2, value));
		recordFields.add(new FlatFileRecordField(field3, value));

		int maxEndPosition = 0;
		for (FlatFileRecordField recordField : recordFields)
		{
			maxEndPosition = Math.max(maxEndPosition, recordField.getFieldFormat().getFieldEndPosition());
		}

		String flatFileRecord = FlatFileRecordBuilder.buildFlatFileRecord(recordFields, null, ' ');
		assertEquals(maxEndPosition, flatFileRecord.length());
	}

	@Test
	public void testDelimiterInclusion()
	{
		String value = "DEFAULT";

		Set<FlatFileRecordField> recordFields = new HashSet<FlatFileRecordField>();
		recordFields.add(new FlatFileRecordField(field1, value));
		recordFields.add(new FlatFileRecordField(field2, value));
		recordFields.add(new FlatFileRecordField(field3, value));

		String delimiter = "delim";

		assertEquals("Unexpectedly found that the value contains the delimiter as a substring", -1, value.indexOf(delimiter));

		String flatFileRecord = FlatFileRecordBuilder.buildFlatFileRecord(recordFields, delimiter, ' ');
		assertEquals(recordFields.size(), flatFileRecord.split(delimiter).length);
	}

	@Test
	public void testFieldOrdering()
	{
		Set<FlatFileRecordField> recordFields = new HashSet<FlatFileRecordField>();
		recordFields.add(new FlatFileRecordField(field1, "1"));
		recordFields.add(new FlatFileRecordField(field2, "3"));
		recordFields.add(new FlatFileRecordField(field3, "2"));

		String flatFileRecord = FlatFileRecordBuilder.buildFlatFileRecord(recordFields, null, ' ');
		assertEquals("123", RegExpHelper.replaceAll(flatFileRecord, "\\s", ""));
	}

}
