package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	
	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource=theDataSource;
		
	}
	public List<Student> getStudents() throws Exception{
		
		List<Student> students=new ArrayList<>();
		
		Connection myConn=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		
		try {
		//get a connection 
		myConn=dataSource.getConnection();
		
		//create a sql statement
		String sql="select * from student order by last_name";
		
		myStmt=myConn.createStatement();
		
		//execute query
		myRs=myStmt.executeQuery(sql);
		
		//process result set
		while(myRs.next()) {
			
			//retrieve data from result set row
			int id=myRs.getInt("id");
			String firstName=myRs.getString("first_name");
			String lastName=myRs.getString("last_name");
			String email=myRs.getString("email");
			
			//create new student object
			Student tempStudent=new Student(id,firstName,lastName,email);
			
			//add it to the list of students
			students.add(tempStudent);
			
		    }
		
			return students;
			
	   }
		finally {
			//close jdbc objects
			close(myConn,myStmt,myRs);
		}
		
	}
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		
		try {
			if(myRs!=null) {
				myRs.close();
			}
			
			if(myStmt!=null) {
				myStmt.close();
			}
			if(myConn!=null) {
				myConn.close();//doesn't really close it...just puts back in pool
			}
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
		
	}
	public void addStudent(Student theStudent) throws SQLException {
		
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try {
			//get the db connection
			myConn=dataSource.getConnection();
			
			//create the sql for insert
			String sql="insert into student "
					   +"(first_name,last_name,email) "
					   +"values(?,?,?)";
			myStmt=myConn.prepareStatement(sql);
			
			//set the param values for the student
			myStmt.setString(1,theStudent.getFirstName());
			myStmt.setString(2,theStudent.getLastName());
			myStmt.setString(3,theStudent.getEmail());
			
			//execute the sql insert
			myStmt.execute();
		}
		finally {
			//clean up JDBC objects
			close(myConn,myStmt,null);
			
		}
		
	}
	public Student getStudent(String theStudentId) throws Exception {
		
		Student theStudent=null;
		
		Connection myConn=null;
		PreparedStatement myStmt=null;
		ResultSet myRs=null;
		int studentId;
		
		try 
		{
			//convert student id to int
			studentId=Integer.parseInt(theStudentId);
			
			//get connection to database
			myConn=dataSource.getConnection();
			
			//create sql to get selected student
			String sql="select * from student where id=?";
			
			//create the prepared statements
			myStmt=myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute the query
			myRs=myStmt.executeQuery();
			
			//retrieve the data from resultset row
			if(myRs.next()) {
				String firstName=myRs.getString("first_name");
				String lastName=myRs.getString("last_name");
				String email=myRs.getString("email");
				
				//use the studentId during construction
				theStudent=new Student(studentId,firstName,lastName,email);
			}
			else {
				throw new Exception("could not find the student id:"+studentId);
			}
			return theStudent;
		}
		finally {
			//clean up JDBC objects
			close(myConn,myStmt,myRs);
		}
		
		
	}
	
	public void updateStudent(Student theStudent) throws Exception
	{
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try {
		//get Db connection
		myConn=dataSource.getConnection();
		
		//create a sql update statement
		String sql="update student "+"set first_name=?,last_name=?,email=? "+"where id=?";
		
		//prepare statement
		myStmt=myConn.prepareStatement(sql);
		
		//set params
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());
		myStmt.setInt(4, theStudent.getId());
		
		//execute the statement
		myStmt.execute();
	}
	finally {
		//clean up jdbc objects
		close(myConn,myStmt,null);
		}
		
	}
	public void deleteStudent(String theStudentId) throws Exception 
	{
		
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try 
		{
			//convert Student id to int
			int studentId=Integer.parseInt(theStudentId);
			
			
			
			//get the database connection
			myConn=dataSource.getConnection();
			
			//create sql to delete the student
			String sql="delete from student where id=?";
			
			//prepare statement
			myStmt=myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute the sql statement
			myStmt.execute();
		}
		finally 
		{
			//clean up JDBC objects
			close(myConn,myStmt,null);
		}
		
	}
		
	}
	

	






