package net.big_oh.resourcestats.dao;

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
 * A quick and dirty factory for generating DAO objects.
 * 
 * @author davewingate
 * @version Oct 11, 2009
 */
public class DAOFactory
{

	public static IRequestorDAO getRequestorDAO()
	{
		// Must return a new object since DAOs hold and utilize a reference to
		// the current session.
		return new RequestorDAO();
	}

	public static IResourceDAO getResourceDAO()
	{
		// Must return a new object since DAOs hold and utilize a reference to
		// the current session.
		return new ResourceDAO();
	}

	public static IResourceRequestDAO getResourceRequestDAO()
	{
		// Must return a new object since DAOs hold and utilize a reference to
		// the current session.
		return new ResourceRequestDAO();
	}

}
