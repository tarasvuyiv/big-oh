package net.big_oh.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO DSW Rename to HibernateDomainObject
public abstract class HibernateVO implements IHibernateVO, Serializable
{

	private static final Log logger = LogFactory.getLog(HibernateVO.class);

	@Override
	public String toString()
	{
		
		return (new ReflectionToStringBuilder(this)
		{
			@Override
			@SuppressWarnings("unchecked")
			protected boolean accept(Field f)
			{
				try
				{
					return super.accept(f) && (f.get(this.getObject()) == null || !(f.get(this.getObject()) instanceof Collection));
				}
				catch (IllegalArgumentException e)
				{
					logger.error(e);
					return false;
				}
				catch (IllegalAccessException e)
				{
					logger.error(e);
					return false;
				}
			}
		}).toString();

	}

	@Override
	public int hashCode()
	{
		HashCodeBuilder hashBuilder = new HashCodeBuilder();
		for (Object businessKey : getBusinessKeys())
		{
			hashBuilder.append(businessKey);
		}
		return hashBuilder.toHashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (obj == this)
		{
			return true;
		}

		if (getClass() != obj.getClass())
		{
			return false;
		}

		IHibernateVO rhs = (IHibernateVO) obj;

		EqualsBuilder equalsBuilder = new EqualsBuilder();

		Object[] lhsBusinessKeys = getBusinessKeys();
		Object[] rhsBusinessKeys = rhs.getBusinessKeys();

		for (int i = 0; i < lhsBusinessKeys.length; i++)
		{
			equalsBuilder.append(lhsBusinessKeys[i], rhsBusinessKeys[i]);
		}

		return equalsBuilder.isEquals();

	}

}
