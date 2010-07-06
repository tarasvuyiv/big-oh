package net.big_oh.hibernate;

import java.util.ArrayList;
import java.util.Collection;

import net.big_oh.hibernate.HibernateVO;

public class HackVO extends HibernateVO
{

	private String guid;
	private String businessKey;
	private Collection<Integer> someInts = new ArrayList<Integer>();

	public String getBusinessKey()
	{
		return businessKey;
	}

	public String getGuid()
	{
		return guid;
	}

	public void setBusinessKey(String businessKey)
	{
		this.businessKey = businessKey;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public Object[] getBusinessKeys()
	{
		Object[] businessKeys = { businessKey };
		return businessKeys;
	}

	public Collection<Integer> getSomeInts()
	{
		return someInts;
	}

	public void setSomeInts(Collection<Integer> someInts)
	{
		this.someInts = someInts;
	}

}
