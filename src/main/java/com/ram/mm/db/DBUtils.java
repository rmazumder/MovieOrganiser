// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DBUtils.java

package com.ram.mm.db;

import com.ram.mm.MMException;
import com.ram.mm.dto.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.*;

// Referenced classes of package com.ram.mm.db:
//            HibernateUtil

public class DBUtils
{

    public DBUtils()
    {
    }

    public static void saveEntity(MMEnity entity) throws MMException
    {
    	
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try
        {
        	convertImage(entity);
            tx = session.beginTransaction();
            session.saveOrUpdate(entity);
            tx.commit();
        }
        catch(Exception e)
        {
            if(tx != null)
                tx.rollback();
            e.printStackTrace();
            throw new MMException(e.getMessage());
        }
    }

   

	private static void convertImage(MMEnity entity) throws IOException {
		if (entity instanceof MyMovie){
			MyMovie en = (MyMovie)entity;
	
			if(en.imdbMovie != null && en.imdbMovie.base64Image != null)
				en.imdbMovie.setImage(new Base64().decode(en.imdbMovie.base64Image));
			
		} else if(entity instanceof IMDBMovie){
			IMDBMovie en = (IMDBMovie)entity;
			if(en.base64Image != null)
				en.setImage(new Base64().decode(en.base64Image));
		}
		
	}

	public static List ListEntity(String query)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List entities = new ArrayList();
        try
        {
            entities = session.createQuery(query).list();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return entities;
    }

    public static void main(String r[])
    {
        IMDBMovie movie = new IMDBMovie();
        movie.setImdbID("dsdsds");
        movie.setTitle("dsds");
        movie.setGenre("1212121");
        MyMovie mymov = new MyMovie();
        mymov.setName("dsdsds");
        mymov.setImdbMovie(movie);
        List movies = ListEntity("FROM MyMovie");
        MyMovie m;
        for(Iterator iterator = movies.iterator(); iterator.hasNext(); System.out.println(m.getImdbMovie()))
            m = (MyMovie)iterator.next();

        System.out.println(movie);
    }
}
