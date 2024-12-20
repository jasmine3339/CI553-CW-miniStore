package dbAccess;

import catalogue.User;
import debug.DEBUG;
import middle.LoginException;
import middle.LoginReader;
//import middle.StockException;

//import javax.swing.*;
import java.sql.*;
/**
 * Implements read only access to the stock database.
 */
public class LoginR implements LoginReader
{
	private Connection theCon    = null;      // Connection to database
	private Statement  theStmt   = null;      // Statement object

	
	/**
	 * Connects to database
	 * Uses a factory method to help setup the connection
	 * @throws StockException if problem
	 */
	public LoginR() throws LoginException
	{
	  try
	  {
	    DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
	    dbDriver.loadDriver();
	  
	    theCon  = DriverManager.getConnection
	                ( dbDriver.urlOfDatabase(), 
	                  dbDriver.username(), 
	                  dbDriver.password() );
	
	    theStmt = theCon.createStatement();
	    theCon.setAutoCommit( true );
	  }
	  catch ( SQLException e )
	  {
	    throw new LoginException( "SQL problem:" + e.getMessage() );
	  }
	  catch ( Exception e )
	  {
	    throw new LoginException("Can not load database driver.");
	  }
	}




	  /**
	   * Returns a statement object that is used to process SQL statements
	   * @return A statement object used to access the database
	   */
	
	
	protected Statement getStatementObject()
	  {
	    return theStmt;
	  }
	
	
	/**
	 * Returns a connection object that is used to process
	 * requests to the DataBase
	 * @return a connection object
	 */
	
	protected Connection getConnectionObject()
	{
	  return theCon;
	}



/** 
 * this checks if the username is in the database
 */
	@Override
	public synchronized boolean exists(String username) throws LoginException {
			    
		    try
		    {
		      ResultSet rs   = getStatementObject().executeQuery(
		        "select username from Users " +
		        "  where  username = '" + username + "'"
		      );
		      boolean res = rs.next();
		      DEBUG.trace( "DB LoginR: exists(%s) -> %s", 
		                    username, ( res ? "T" : "F" ) );
		      rs.close();
		      return res;
		    } catch ( SQLException e )
		    {
		      throw new LoginException( "SQL exists: " + e.getMessage() );
		    }
	}



	@Override
	public User getPassword(String username) throws LoginException {
		 try
		    {
		      User   dt = new User();
		      ResultSet rs = getStatementObject().executeQuery(
		        "select password " +
		        "  from Users " +
		        "  where  username = '" + username + "' "
		        
		      );
		      if ( rs.next() )
		      {
		        dt.setUsername( username );
		        dt.setPassword( rs.getString( "password" ) );
		      }
		      rs.close();
		      return dt;
		    } catch ( SQLException e )
		    {
		      throw new LoginException( "SQL getDetails: " + e.getMessage() );
		    }
	}
	



}