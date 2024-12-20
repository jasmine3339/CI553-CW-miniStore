package clients.login;

import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.User;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.LoginException;
import middle.LoginReader;
import middle.LoginReadWriter;
import middle.StockException;
import middle.StockReader;
import middle.StockReadWriter;
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
  private StockReadWriter theStock = null;
  private User 		  currentUser = null;

  private String orderDetails = "";
  /*
   * Construct the model of the back door client
   * @param mf The factory to create the connection objects
   */

  public loginModel(MiddleFactory mf)
  {
    try                                           // 
    {      
      loginLink = mf.makeLoginReader();        // Database access
      loginwriter = mf.makeLoginWriter();		//database writing access
      theStock = mf.makeStockReadWriter(); //stock access
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
  
  
  /**
   * takes both username and password, checks if username is in database, and that password that 
   * has been entered is the right one
   * @param username
   * @param password
   */
  public void doLogin(String username, String password) {
	  boolean loginFlag = false;
	  String theAction = "";
	  try {
		  //check if user is already in database
		  currentUser = doCheck(username); 
		  //get the password the user was supposed to enter
		  String actualpassword = currentUser.getPassword();
		  //compare passwords and change action acordingly.
		  if (actualpassword.equals(password)) {
			  loginFlag = true;
			  theAction = "logging in";
			  
		  } else {
			  theAction = "username or password incorrect";
		  }
		  //exception if anything goes wrong to make them enter again
	  } catch (Exception e) {
		  System.out.println("exception "+e);
		  theAction = "username or password incorrect";
	  }
	  System.out.println("login"+username +password);
	  System.out.println("have they been logged in"+loginFlag);
	  setChanged(); notifyObservers(theAction); //update the observer that the actino has changed

  }
  
  /**
   * gets current user so can be used in other parts of code
   * @return current user
   */
  public User getUser() {
	  System.out.println("getUser in model");
	  return currentUser;
  }

  /** 
   * creating a new account. checks if username already exists, if both passwords match and if 
   * they are long and complicated enough
   * @param username
   * @param password
   * @param passcheck
   */
  public void doCreate(String username, String password, String passcheck) {
	  boolean loginFlag = false; //not logged in yet
	  System.out.println("create"+passcheck);
	  //get rid of any whitepace from the inputs
	  username = username.trim();
	  password = password.trim();
	  passcheck = password.trim();
	  String theAction = "";
	  boolean numFlag = false;
	  //check if username is already in the database, if so user has to change it
	  User usernamecheck = doCheck(username);
	  if (usernamecheck == null) {
		  try
		    {
			  //check if the username is empty
			  //check in db if there is a username with that name already
			  //checking the passwords are the same and the instructions have been met
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
								  //appropriate messages to display if password is not right
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
  
  /**
   * allows user to see all previous orders
   */
  public void doOrderCheck()
  {
	  String theAction = "";
	  
	  try {
		  ArrayList<Integer> listoforder = theStock.getOrderNums(currentUser.getUsername());
		  //get the list of every order number for the user
		  if (listoforder.size() != 0){
			  for (int ordernumberfromlist:listoforder) {
				  //for each order number, make the basket and get the details
				  Basket tempBasket = theStock.getOrder(ordernumberfromlist);
				  orderDetails = orderDetails+tempBasket.getDetails()+"\n";
			  }
			  theAction = "Orders:";
		  } else {
			  theAction = "no orders";
		  }
		  System.out.println(orderDetails);
	  } catch (Exception e) {
		  System.out.println(e);
	  }
	  setChanged(); notifyObservers(theAction);
  }
  public String getAllOrderDetails() {
	  return orderDetails;
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

