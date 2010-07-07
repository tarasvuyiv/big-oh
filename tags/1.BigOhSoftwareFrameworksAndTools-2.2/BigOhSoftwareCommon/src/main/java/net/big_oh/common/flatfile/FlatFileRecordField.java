package net.big_oh.common.flatfile;

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
 * The FlatFileRecordField class represents a value to be written for a specific
 * field in a flat file record. In other words, this class composes a field's
 * format with its value.
 */
public class FlatFileRecordField
{
	private final FlatFileFieldFormat fieldFormat;
	private final String fieldValue;

	/**
	 * 
	 * @param fieldFormat
	 *            The definition of a field in a flat file.
	 * @param fieldValue
	 *            The value to be written to the flat file field.
	 */
	public FlatFileRecordField(FlatFileFieldFormat fieldFormat, String fieldValue)
	{
		super();
		this.fieldFormat = fieldFormat;
		this.fieldValue = fieldValue;
	}

	FlatFileFieldFormat getFieldFormat()
	{
		return fieldFormat;
	}

	String getFieldValue()
	{
		return fieldValue;
	}

	@Override
	public String toString()
	{

		StringBuffer sb = new StringBuffer();
		sb.append(fieldFormat.toString());
		sb.append(" -- Value: " + fieldValue);
		return sb.toString();
	}

}
