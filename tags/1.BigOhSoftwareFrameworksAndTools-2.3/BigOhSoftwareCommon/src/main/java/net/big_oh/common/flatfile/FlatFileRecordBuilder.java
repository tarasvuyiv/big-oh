package net.big_oh.common.flatfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
 * The FlatFileRecordBuilder class provides utility methods for creating the
 * individual records (i.e. lines) that comprise a flat file.
 */
public class FlatFileRecordBuilder
{

	/**
	 * Builds a single record (i.e. line) in the flat file using the collection
	 * of {@link FlatFileRecordField} objects.
	 * 
	 * @param fieldsForRecord
	 *            The FlatFileRecordField objects used to construct a single
	 *            record for the flat file.
	 * @param fieldDelimiter
	 *            An optional character that can be placed between fields in the
	 *            flat file record.
	 * @param interFieldPadChar
	 *            The character used to fill any spaces that do not correspond
	 *            to a field in the flat file record.
	 * @return A single record (i.e. line) to be included in a flat file.
	 * @throws IllegalArgumentException
	 *             Thrown if the Collection of FlatFileRecordField objects
	 *             overlap.
	 */
	public static String buildFlatFileRecord(Collection<FlatFileRecordField> fieldsForRecord, String fieldDelimiter, char interFieldPadChar) throws IllegalArgumentException
	{

		StringBuffer recordContent = new StringBuffer();

		List<FlatFileRecordField> sortedFieldsForRecord = new ArrayList<FlatFileRecordField>(fieldsForRecord);
		Collections.sort(sortedFieldsForRecord, new Comparator<FlatFileRecordField>()
		{
			public int compare(FlatFileRecordField o1, FlatFileRecordField o2)
			{
				return new Integer(o1.getFieldFormat().getFieldStartPosition()).compareTo(new Integer(o2.getFieldFormat().getFieldStartPosition()));
			}
		});

		int lastPositionPopulated = 0;
		for (FlatFileRecordField recordField : sortedFieldsForRecord)
		{
			FlatFileFieldFormat fieldFormat = recordField.getFieldFormat();

			// add the field delimiter before all fields but the first
			if (fieldDelimiter != null && lastPositionPopulated > 0)
			{
				recordContent.append(fieldDelimiter);
			}

			// check to see whether we've encountered two overlapping fields
			if (lastPositionPopulated >= fieldFormat.getFieldStartPosition())
			{
				throw new IllegalArgumentException("Field " + fieldFormat.toString() + " overlaps with another field.  This is not allowed.");
			}

			// add whitespace as needed to fill gap between consecutive fields
			while (lastPositionPopulated < fieldFormat.getFieldStartPosition() - 1)
			{
				recordContent.append(interFieldPadChar);
				lastPositionPopulated++;
			}

			int fieldLength = fieldFormat.getFieldLength();

			String fieldValue = recordField.getFieldValue();

			// abbreviate values that are too long
			if (fieldValue.length() > fieldLength)
			{
				fieldValue = fieldValue.substring(0, fieldLength);
			}

			// pad values that are too short
			if (fieldFormat.getPaddingType().equals(FlatFileFieldPaddingType.PADDING_ON_LEFT))
			{
				fieldValue = StringUtils.leftPad(fieldValue, fieldLength, fieldFormat.getIntraFieldPaddingChar());
			}
			else if (fieldFormat.getPaddingType().equals(FlatFileFieldPaddingType.PADDING_ON_RIGHT))
			{
				fieldValue = StringUtils.rightPad(fieldValue, fieldLength, fieldFormat.getIntraFieldPaddingChar());
			}

			// add the field value now that it is the proper size for the field
			recordContent.append(fieldValue);
			lastPositionPopulated += fieldValue.length();

		}

		return recordContent.toString();

	}
}
