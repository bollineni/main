package com.tutorial.hibernate.mappings.association.many2many;
// 4th
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Many2ManyClient {
	private static SessionFactory factory;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{ 
			factory = new Configuration().configure("hibernate.mysql.cfg.xml").buildSessionFactory(); 
		}catch (Throwable ex) { 
			System.err.println("Failed to create sessionFactory object." + ex); 
			throw new ExceptionInInitializerError(ex); 
		}
		HashSet certificates = new HashSet();
		certificates.add(new Certificate("SCJP")); 
		certificates.add(new Certificate("MCA")); 
		certificates.add(new Certificate("MBA"));

		Many2ManyClient mm = new Many2ManyClient();

		mm.addEmployee("Jayram", "Rout", 2000, certificates);
		certificates.add(new Certificate("HADOOP"));
		mm.addEmployee("Diyansha", "Rout", 1000, certificates);
	} 
	public Integer addEmployee(String fname, String lname, int salary, Set cert){ 
		Session session = factory.openSession(); 
		Transaction tx = null; 
		Integer employeeID = null; 
		try{ 
			tx = session.beginTransaction(); 
			Employee employee = new Employee(fname, lname, salary); 
			employee.setCertificates(cert); 
			employeeID = (Integer) session.save(employee); 
			tx.commit(); 
		}catch (HibernateException e) { 
			if (tx!=null) 
				tx.rollback(); 
			e.printStackTrace(); 
		}finally { 
			session.close(); 
		} 
		return employeeID; 
	}

	/* Method to list all the employees detail */ 
	public void listEmployees( ){ 
		Session session = factory.openSession(); 
		Transaction tx = null; 
		try{ tx = session.beginTransaction(); 
		List employees = session.createQuery("FROM Employee").list(); 
		for (Iterator iterator1 = employees.iterator();iterator1.hasNext();){ 
			Employee employee = (Employee) iterator1.next(); 
			System.out.print("First Name: " + employee.getFirstName()); 
			System.out.print(" Last Name: " + employee.getLastName()); 
			System.out.println(" Salary: " + employee.getSalary()); 
			Set certificates = employee.getCertificates(); 
			for (Iterator iterator2 = certificates.iterator(); iterator2.hasNext();){ 
				Certificate certName = (Certificate) iterator2.next(); 
				System.out.println("Certificate: " + certName.getName()); 
			} 
		} 
		tx.commit(); 
		}catch (HibernateException e) { 
			if (tx!=null) 
				tx.rollback(); 
			e.printStackTrace(); 
		}finally { 
			session.close(); 
		} 
	}

	/* Method to update salary for an employee */ 
	public void updateEmployee(Integer EmployeeID, int salary ){ 
		Session session = factory.openSession(); 
		Transaction tx = null; 
		try{ 
			tx = session.beginTransaction(); 
			Employee employee = (Employee)session.get(Employee.class, EmployeeID); 
			employee.setSalary( salary ); 
			session.update(employee); 
			tx.commit(); 
		}catch (HibernateException e) {
			if (tx!=null) 
				tx.rollback(); 
			e.printStackTrace(); 
		}finally { 
			session.close(); 
		} 
	} 
	/* Method to delete an employee from the records */ 
	public void deleteEmployee(Integer EmployeeID){ 
		Session session = factory.openSession(); 
		Transaction tx = null; 
		try{
			tx = session.beginTransaction(); 
			Employee employee = (Employee)session.get(Employee.class, EmployeeID); 
			session.delete(employee); 
			tx.commit(); 
		}catch (HibernateException e) { 
			if (tx!=null) tx.rollback(); 
			e.printStackTrace(); 
		}finally { 
			session.close(); 
		} 
	}
}

