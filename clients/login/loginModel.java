package clients.login;

import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.User;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.LoginException;
import middle.LoginReader;
import middle.LoginReadWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
 

/**
 * Implements the Model of the back door client
 */
public class loginModel extends Observable
{
  private Basket      theBasket  = null;            // Bought items
  private String      checkingname = "";  					// Product being processed
  private LoginReader loginLink = null;			//link to the users database

  public ArrayList<Integer> numbers = new ArrayList<Integer>(List.of(0,1,2,3,4,5,6,7,8,9));
  private LoginReadWriter loginwriter     = null;
  
  private User 		  currentUser = null;

  /*
   * Construct the model of the back door client
   * @param mf The factory to create the connection objects
   */

  public loginModel(MiddleFactory mf)
  {
    try                                           // 
    {      
      loginLink = mf.makeLoginReader();        // Database access
      loginwriter = mf.makeLoginWriter();
    } catch ( Exception e )
    {
      DEBUG.error("CustomerModel.constructor\n%s", e.getMessage() );
    }

  }
  
  /**
   * Get the Basket of products
   * @return basket
   */
  public Basket getBasket()
  {
    return theBasket;
  }

  /**
   * Check The current stock level
   * @param productNum The product number
   */
  public User doCheck(String username )
  {
	checkingname = username.trim(); //username with no whitespace
	String theAction = "";
	try {
		if ( loginLink.exists( checkingname ) )    {
			User returninguser = loginLink.getPassword(checkingname);
			System.out.println("doCheck user check");
			return returninguser;
		}
	} catch (Exception e){
		System.out.println(e+"exception");
		
		return null;
				
	}
	return null;
	
                    
  }
  
  
  
  public void doLogin(String username, String password) {
	  boolean loginFlag = false;
	  String theAction = "";
	  try {
		  currentUser = doCheck(username);
		  String actualpassword = currentUser.getPassword();
		  if (actualpassword.equals(password)) {
			  loginFlag = true;
			  theAction = "logging in";
			  
		  } else {
			  theAction = "username or password incorrect";
		  }
	  } catch (Exception e) {
		  System.out.println("exception "+e);
		  theAction = "username or password incorrect";
	  }
	  System.out.println("login"+username +password);
	  System.out.println("have they been logged in"+loginFlag);
	  setChanged(); notifyObservers(theAction);

  }
  public User getUser() {
	  System.out.println("getUser in model");
	  return currentUser;
  }

  public void doCreate(String username, String password, String passcheck) {
	  boolean loginFlag = false;
	  System.out.println("create"+passcheck);
	  username = username.trim();
	  password = password.trim();
	  passcheck = password.trim();
	  String theAction = "";
	  boolean numFlag = false;
	  User usernamecheck = doCheck(username);
	  if (usernamecheck == null) {
		  try
		    {
			  //check if the username is empty
			  //check in db if there is a username with that name already
			  if (password.equals(passcheck)) {
				  if ( password.length() > 7 && password.length() < 16) {
					  if (!(password.toLowerCase().equals(password))) {
						  if (!(password.toUpperCase().equals(password))) {
							  for (int num:numbers) {
								  if (password.contains(String.valueOf(num))) 
								  {
									  numFlag = true;
									  
									  break;
								  } 
							  }
							  if (numFlag == true) {
								  theAction =  "adding your account";
								  loginwriter.addUser(username, password);
								  currentUser = new User(username,password);
								  loginFlag = true;
								  
								  
								  
								  
								  
								  //add account to the database
							  } else {
								  theAction = "try adding a number";
							  }
							  
							   
						  } else {
							  theAction =  "try adding a lowercase letter";			  
						  }
					  } else {
						  theAction = "try adding an uppercase letter";
					  }
				  } else {
					  theAction =  "is your password between 8-15 characters";
				  }
			   } else {
				   if (passcheck.equals("")) 
				   {
					   theAction = "make sure to re enter your password";   
				   } else {
					   theAction =  "passwords do not match";
				   }
			   } 
			  
		    } catch (Exception e) {
		    	theAction = e.getMessage();
		    }
	  } else {
		  theAction = "username already exists, please choose another";
		  }
		      
	  setChanged(); notifyObservers(theAction);
  }
  

  public void doProgress()
  {
	  //TODO fill in here 
  }
  public void doOrderCheck()
  {
	  //TODO fill in here
  }
  public void doReturn()
  {
	  //TODO fill in here
  }
  /**
   * Clear the product()
   */
  public void doClear()
  {
    String theAction = "";
    theBasket.clear();                        // Clear s. list
    theAction = "Enter Product Number";       // Set display
    setChanged(); notifyObservers(theAction);  // inform the observer view that model changed
  }
 
 
}

