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
import java.net.UnknownHostException;

public abstract class ClasslessInterDomainRoutingFilterRule implements IPAddressFilterRule
{

	private final ClasslessInterDomainRoutingBlock routingBlock;

	public ClasslessInterDomainRoutingFilterRule(String filterInitParamValue) throws UnknownHostException, NumberFormatException
	{
		this(InetAddress.getByName(filterInitParamValue.split("\\/")[0]), Integer.parseInt(filterInitParamValue.split("\\/")[1]));
	}

	public ClasslessInterDomainRoutingFilterRule(InetAddress baseAddress, int prefixLength) throws UnknownHostException, NumberFormatException
	{
		routingBlock = new ClasslessInterDomainRoutingBlock(baseAddress, prefixLength);
	}

	public boolean doesRequestingAddressMatchRule(InetAddress requestingIPAddress)
	{
		return routingBlock.isAddressMemberOfRoutingBlock(requestingIPAddress);
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + ": " + routingBlock.toString();
	}

}
