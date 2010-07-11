package net.big_oh.common.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil
{

	private static final Log logger = LogFactory.getLog(HibernateUtil.class);

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory()
	{
		try
		{
			return new Configuration().configure().buildSessionFactory();
		}
		catch (Throwable t)
		{
			logger.fatal(t.getMessage(), t);
			throw new ExceptionInInitializerError(t);
		}
	}

	public static SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	public static void rollback(Transaction tx)
	{
		if (tx != null)
		{
			tx.rollback();
		}
	}

	public static void rollback(Throwable t, SessionFactory sf, Log log)
	{
		log.error(t.getMessage(), t);

		try
		{
			if (sf.getCurrentSession().getTransaction().isActive())
			{
				log.debug("Trying to rollback database transaction after exception");
				sf.getCurrentSession().getTransaction().rollback();
			}
		}
		catch (Throwable rbEx)
		{
			log.error("Could not rollback transaction after exception!", rbEx);
		}
	}

}
