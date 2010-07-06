package net.big_oh.common.web.filters.ipaddress;

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

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ClasslessInterDomainRoutingBlock
{

	private final InetAddress baseIPAddress;
	private final int prefixLength;

	public ClasslessInterDomainRoutingBlock(InetAddress baseIPAddress, int prefixLength)
	{

		this.baseIPAddress = baseIPAddress;
		this.prefixLength = prefixLength;

		if (prefixLength < 0 || prefixLength > (baseIPAddress.getAddress().length * 8))
		{
			throw new RuntimeException("The prefix length must be a value between 0 and " + (baseIPAddress.getAddress().length * 8) + " inclusive.");
		}

	}

	/**
	 * Tests to see whether a candidate address is a member of the routing
	 * block.
	 * 
	 * @param candidateAddress
	 * @return a boolean value indicating whether or not the candidateAddress is
	 *         a member of the routing block.
	 */
	public boolean isAddressMemberOfRoutingBlock(InetAddress candidateAddress)
	{

		// Can't possibly be in the same block if addresses are of different
		// types (e.g. IPv4 and IPv6).
		if (!candidateAddress.getClass().isAssignableFrom(baseIPAddress.getClass()))
		{
			return false;
		}

		Map<Integer, Integer> byteIndexToNumBitsMap = new HashMap<Integer, Integer>();
		int bitsToConsider = prefixLength;
		int byteCounter = 0;
		while (bitsToConsider > 0)
		{
			byteIndexToNumBitsMap.put(byteCounter++, Math.min(bitsToConsider, 8));
			bitsToConsider -= Math.min(bitsToConsider, 8);
		}

		// Strategy here is to compare using XOR
		for (Integer byteIndex : byteIndexToNumBitsMap.keySet())
		{

			Integer numBits = byteIndexToNumBitsMap.get(byteIndex);

			int numPlacesToShift = Math.abs(8 - numBits);

			byte baseByte = baseIPAddress.getAddress()[byteIndex];
			byte candidateByte = candidateAddress.getAddress()[byteIndex];

			// use a mask to remove zero or more least significant bits that
			// should not be considered by subsequent the XOR operation
			int mask = -1 << numPlacesToShift;
			// (numPlacesToShift > 0) ? (int) (Math.pow(2, 8) - Math.pow(2,
			// numPlacesToShift)) : -1;
			int maskedBaseByte = baseByte & mask;
			int maskedCandidateByte = candidateByte & mask;

			if ((maskedBaseByte ^ maskedCandidateByte) != 0)
			{
				return false;
			}

		}

		return true;

	}

	@Override
	public String toString()
	{
		return baseIPAddress.toString() + "/" + prefixLength;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ClasslessInterDomainRoutingBlock)
		{
			ClasslessInterDomainRoutingBlock other = (ClasslessInterDomainRoutingBlock) obj;
			return other.baseIPAddress.equals(baseIPAddress) && other.prefixLength == prefixLength;
		}
		else
		{
			return super.equals(obj);
		}
	}

	@Override
	public int hashCode()
	{
		return (baseIPAddress.toString() + Integer.toString(prefixLength)).hashCode();
	}

}
