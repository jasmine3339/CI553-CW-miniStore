package dbAccess;

import java.sql.SQLException;

import debug.DEBUG;
import middle.LoginException;
import middle.LoginReadWriter;


public class LoginRW extends LoginR implements LoginReadWriter 
{
  /*
   * Connects to database
   */
  public LoginRW() throws LoginException
  {    
    super();        // Connection done in loginR's constructor
  }
  
  public synchronized void addUser( String user, String pass )
	         throws LoginException
	  {
	  try
	    {
	      getStatementObject().executeUpdate( 
	        "INSERT INTO Users(username, password) VALUES ("+user+", "+pass+")"
	      );
	      
	      DEBUG.trace( "DB LoginRW: adduser(%s,%s)" , user, pass );
	    } catch ( SQLException e )
	    {
	    	System.out.println("SQL addUser: " + e.getMessage());
	      throw new LoginException( "SQL addUser: " + e.getMessage() );
	    }
	  }


}
  
