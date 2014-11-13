
package com.ram.mm.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil
{

    public HibernateUtil()
    {
    }

    private static SessionFactory buildSessionFactory()
    {
        try
        {
            return (new Configuration()).configure().buildSessionFactory();
        }
        catch(Throwable ex)
        {
            System.err.println((new StringBuilder("Initial SessionFactory creation failed.")).append(ex).toString());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public static void shutdown()
    {
        getSessionFactory().close();
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();

}
