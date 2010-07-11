package net.big_oh.common.utils;

import java.text.DecimalFormat;

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
 * Utility class for number & decimal formatting.
 * 
 * @author davewingate
 * @version Nov 8, 2009
 */
public class NumberFormatUtil
{

	private static final String STANDARD_DECIMAL_FORMAT_STRING = "###,###,###,##0.00";
	private static final DecimalFormat STANDARD_DECIMAL_FORMAT = new DecimalFormat(STANDARD_DECIMAL_FORMAT_STRING);

	public static String toStandardFormat(double d)
	{
		return STANDARD_DECIMAL_FORMAT.format(d);
	}

	private static final String STANDARD_DECIMAL_PERCENTAGE_FORMAT_STRING = "###,###,###,##0.00 %";
	private static final DecimalFormat STANDARD_DECIMAL_PERCENTAGE_FORMAT = new DecimalFormat(STANDARD_DECIMAL_PERCENTAGE_FORMAT_STRING);

	public static String toStandardPercentageFormat(double d)
	{
		return STANDARD_DECIMAL_PERCENTAGE_FORMAT.format(d);
	}

	private static final String STANDARD_NUMBER_FORMAT_STRING = "###,###,###,##0";
	private static final DecimalFormat STANDARD_NUMBER_FORMAT = new DecimalFormat(STANDARD_NUMBER_FORMAT_STRING);

	public static String toStandardFormat(long l)
	{
		return STANDARD_NUMBER_FORMAT.format(l);
	}

}
