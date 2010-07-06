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
 * The FlatFileFieldFormat class represents the definition of a single field in
 * a flat file record.
 */
public class FlatFileFieldFormat
{

	private final int fieldStartPosition;
	private final int fieldEndPosition;
	private final FlatFileFieldPaddingType paddingType;
	private final char intraFieldPaddingChar;

	/**
	 * 
	 * @param fieldStartPosition
	 *            The 1-based start position of this field in the flat file
	 *            record.
	 * @param fieldEndPosition
	 *            The 1-based end position of this field in the flat file
	 *            record.
	 * @param paddingType
	 *            Defines how to handle values that are shorter than the field
	 *            length.
	 * @param intraFieldPaddingChar
	 *            Ignored if paddingType is set to
	 *            FlatFileFieldPaddingType.VARIABLE_WIDTH.
	 * @throws IllegalArgumentException
	 *             Thrown if fieldStartPosition is less than one or if
	 *             fieldEndPosition is less than fieldStartPosition.
	 */
	public FlatFileFieldFormat(int fieldStartPosition, int fieldEndPosition, FlatFileFieldPaddingType paddingType, char intraFieldPaddingChar) throws IllegalArgumentException
	{
		super();

		if (fieldStartPosition < 1)
		{
			throw new IllegalArgumentException("The fieldStartPosition parameter cannot be less than one.");
		}

		if (fieldEndPosition < fieldStartPosition)
		{
			throw new IllegalArgumentException("The fieldEndPosition parameter must not be less than the fieldStartPosition parameter.");
		}

		this.fieldStartPosition = fieldStartPosition;
		this.fieldEndPosition = fieldEndPosition;
		this.paddingType = paddingType;
		this.intraFieldPaddingChar = intraFieldPaddingChar;
	}

	int getFieldStartPosition()
	{
		return fieldStartPosition;
	}

	int getFieldEndPosition()
	{
		return fieldEndPosition;
	}

	FlatFileFieldPaddingType getPaddingType()
	{
		return paddingType;
	}

	char getIntraFieldPaddingChar()
	{
		return intraFieldPaddingChar;
	}

	int getFieldLength()
	{
		return getFieldEndPosition() - getFieldStartPosition() + 1;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Start Position: " + fieldStartPosition);
		sb.append(" -- End Position: " + fieldEndPosition);
		sb.append(" -- Padding Type: " + paddingType.name());
		sb.append(" -- Padding Char: " + intraFieldPaddingChar);
		return sb.toString();
	}

}
