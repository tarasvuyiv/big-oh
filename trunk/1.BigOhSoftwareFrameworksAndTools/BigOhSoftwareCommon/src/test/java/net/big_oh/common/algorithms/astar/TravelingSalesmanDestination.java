package net.big_oh.common.algorithms.astar;

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
 * Represents a destination in the Traveling Salesman problem.
 * 
 * @author davewingate
 * @version Nov 29, 2009
 */
public class TravelingSalesmanDestination
{

	public static final TravelingSalesmanDestination[] ALL_DESTINATIONS = { new A(), new B(), new C(), new D(), new E() };

	public static final TravelingSalesmanDestination HOME_TOWN = ALL_DESTINATIONS[0];

	private int x;
	private int y;

	public TravelingSalesmanDestination(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public static int getDistance(TravelingSalesmanDestination d1, TravelingSalesmanDestination d2)
	{

		return Math.abs(d1.x - d2.x) + Math.abs(d1.y - d2.y);

	}
	
	@Override
	public String toString()
	{
		return this.getClass().getSimpleName();
	}
	
	public static final class A extends TravelingSalesmanDestination
	{
		public A()
		{
			super(0, 0);
		}
	}

	public static final class B extends TravelingSalesmanDestination
	{
		public B()
		{
			super(10, 0);
		}
	}

	public static final class C extends TravelingSalesmanDestination
	{
		public C()
		{
			super(6, 6);
		}
	}

	public static final class D extends TravelingSalesmanDestination
	{
		public D()
		{
			super(0, 10);
		}
	}

	public static final class E extends TravelingSalesmanDestination
	{
		public E()
		{
			super(5, 5);
		}
	}

}
