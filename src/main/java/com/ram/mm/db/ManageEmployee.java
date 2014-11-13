/*// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ManageEmployee.java

package com.ram.mm.db;

import com.ram.mm.dto.Employee;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import org.hibernate.*;

// Referenced classes of package com.ram.mm.db:
//            HibernateUtil

public class ManageEmployee
{

    public ManageEmployee()
    {
    }

    public static void main(String args[])
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Employee em = new Employee();
        em.setId(200);
        em.setFirstName("dsdsds");
        em.setLastName("dsdds");
        Transaction tx = null;
        Integer employeeID = null;
        try
        {
            tx = session.beginTransaction();
            employeeID = (Integer)session.save(em);
            System.out.println(employeeID);
            tx.commit();
        }
        catch(Exception e)
        {
            if(tx != null)
                tx.rollback();
            e.printStackTrace();
        }
    }

    public Integer addEmployee(String fname, String lname, int salary)
    {
        Session session;
        Transaction tx;
        Integer employeeID;
        session = factory.openSession();
        tx = null;
        employeeID = null;
        try
        {
            tx = session.beginTransaction();
            Employee employee = new Employee();
            employee.setFirstName(fname);
            employee.setLastName(lname);
            employee.setSalary(salary);
            employeeID = (Integer)session.save(employee);
            tx.commit();
            break MISSING_BLOCK_LABEL_119;
        }
        catch(HibernateException e)
        {
            if(tx != null)
                tx.rollback();
            e.printStackTrace();
        }
        session.close();
        break MISSING_BLOCK_LABEL_127;
        Exception exception;
        exception;
        session.close();
        throw exception;
        session.close();
        return employeeID;
    }

    public void listEmployees()
    {
        Session session;
        Transaction tx;
        session = factory.openSession();
        tx = null;
        try
        {
            tx = session.beginTransaction();
            List employees = session.createQuery("FROM Employee").list();
            Employee employee;
            for(Iterator iterator = employees.iterator(); iterator.hasNext(); System.out.println((new StringBuilder("  Salary: ")).append(employee.getSalary()).toString()))
            {
                employee = (Employee)iterator.next();
                System.out.print((new StringBuilder("First Name: ")).append(employee.getFirstName()).toString());
                System.out.print((new StringBuilder("  Last Name: ")).append(employee.getLastName()).toString());
            }

            tx.commit();
            break MISSING_BLOCK_LABEL_189;
        }
        catch(HibernateException e)
        {
            if(tx != null)
                tx.rollback();
            e.printStackTrace();
        }
        session.close();
        break MISSING_BLOCK_LABEL_196;
        Exception exception;
        exception;
        session.close();
        throw exception;
        session.close();
    }

    public void updateEmployee(Integer EmployeeID, int salary)
    {
        Session session;
        Transaction tx;
        session = factory.openSession();
        tx = null;
        try
        {
            tx = session.beginTransaction();
            Employee employee = (Employee)session.get(com/ram/mm/dto/Employee, EmployeeID);
            employee.setSalary(salary);
            session.update(employee);
            tx.commit();
            break MISSING_BLOCK_LABEL_99;
        }
        catch(HibernateException e)
        {
            if(tx != null)
                tx.rollback();
            e.printStackTrace();
        }
        session.close();
        break MISSING_BLOCK_LABEL_106;
        Exception exception;
        exception;
        session.close();
        throw exception;
        session.close();
    }

    public void deleteEmployee(Integer EmployeeID)
    {
        Session session;
        Transaction tx;
        session = factory.openSession();
        tx = null;
        try
        {
            tx = session.beginTransaction();
            Employee employee = (Employee)session.get(com/ram/mm/dto/Employee, EmployeeID);
            session.delete(employee);
            tx.commit();
            break MISSING_BLOCK_LABEL_88;
        }
        catch(HibernateException e)
        {
            if(tx != null)
                tx.rollback();
            e.printStackTrace();
        }
        session.close();
        break MISSING_BLOCK_LABEL_95;
        Exception exception;
        exception;
        session.close();
        throw exception;
        session.close();
    }

    private static SessionFactory factory;
}
*/