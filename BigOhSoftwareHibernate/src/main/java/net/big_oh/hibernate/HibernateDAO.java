package net.big_oh.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.List;

import net.big_oh.common.utils.HibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;


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
 * A top-level abstract class for Hibernate DAOs.
 * 
 * @param <T>
 *            The type of the persistent domain object.
 * @param <ID>
 *            The persistent domain object's key type.
 * @author davewingate
 * @version Oct 11, 2009
 */
//TODO DSW Move the ID generic type parameter into the VO interface?
public abstract class HibernateDAO<T, ID extends Serializable> implements StateOrientedDAO<T, ID>
{

	private Class<T> persistentClass;
	private Session session;

	@SuppressWarnings("unchecked")
	public HibernateDAO()
	{
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void setSession(Session s)
	{
		this.session = s;
	}

	protected Session getSession()
	{
		if (session == null)
		{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}

		return session;
	}

	public Class<T> getPersistentClass()
	{
		return persistentClass;
	}

	public T findById(ID id)
	{
		return findById(id, false);
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id, boolean lock)
	{
		T entity;
		if (lock)
			entity = (T) getSession().get(getPersistentClass(), id, LockMode.UPGRADE);
		else
			entity = (T) getSession().get(getPersistentClass(), id);

		return entity;
	}

	public List<T> findAll()
	{
		return findByCriteria();
	}

	public List<T> findByExample(T exampleInstance)
	{
		return findByExample(exampleInstance, null);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String[] excludeProperty)
	{
		Criteria crit = getSession().createCriteria(getPersistentClass());

		Example example = Example.create(exampleInstance);
		if (excludeProperty != null)
		{
			for (String exclude : excludeProperty)
			{
				example.excludeProperty(exclude);
			}
		}
		crit.add(example);

		return crit.list();
	}
	
	public Long count()
	{
		return (Long) getSession().createQuery("select count(entity) from " + getPersistentClass().getSimpleName() + " entity").uniqueResult();
	}

	public T makePersistent(T entity)
	{
		getSession().saveOrUpdate(entity);
		return entity;
	}

	public void makeTransient(T entity)
	{
		getSession().delete(entity);
	}

	public void flush()
	{
		getSession().flush();
	}

	public void clear()
	{
		getSession().clear();
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion)
	{
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion)
		{
			crit.add(c);
		}
		return crit.list();
	}

	/**
	 * Retrieves a named query using the DAO's session.
	 * 
	 * @param queryName
	 *            This argument is treated as the last portion of a qualified
	 *            named query.
	 * @return
	 */
	protected Query getQualifiedNamedQuery(String queryName)
	{
		return getSession().getNamedQuery(persistentClass.getName() + "." + queryName);
	}

	/**
	 * Provides support for the common query pattern of selecting all fields for
	 * a domain object along with a single aggregation value.
	 * 
	 * @param queryResults
	 *            For this process method, the queryResults list parameter is
	 *            expected to contain Object array elements of size 2 in which
	 *            the first array element is an object of type T and the second
	 *            array element is an aggregation column that can be cast to
	 *            Number.
	 * @return Returns a LinkedHashMap so that iteration order is preserved from
	 *         the original querResults.
	 * @throws IllegalArgumentException
	 *             Thrown when the contents of the queryResults parameter are
	 *             null or unexpected.
	 */
	@SuppressWarnings("unchecked")
	protected LinkedHashMap<T, Number> processQueryResults(List<?> queryResults) throws IllegalArgumentException
	{
		String parameterErrorMessage = "Each query result must be a size-two Object[] containing an instance of " + persistentClass.getSimpleName() + " at index zero and an instance of " + Number.class.getSimpleName() + " at position one.";

		// check parameter sanity
		if (queryResults == null)
		{
			throw new IllegalArgumentException(parameterErrorMessage);
		}

		LinkedHashMap<T, Number> processedQueryResults = new LinkedHashMap<T, Number>();

		for (Object obj : queryResults)
		{

			// check query result sanity
			if (!(obj instanceof Object[]))
			{
				throw new IllegalArgumentException(parameterErrorMessage);
			}

			Object[] qr = (Object[]) obj;

			if (qr.length != 2)
			{
				throw new IllegalArgumentException(parameterErrorMessage);
			}
			if (!persistentClass.isAssignableFrom(qr[0].getClass()))
			{
				throw new IllegalArgumentException(parameterErrorMessage);
			}
			if (!Number.class.isAssignableFrom(qr[1].getClass()))
			{
				throw new IllegalArgumentException(parameterErrorMessage);
			}

			// if no problems, just process the query result
			processedQueryResults.put((T) qr[0], (Number) qr[1]);

		}

		return processedQueryResults;

	}
}
