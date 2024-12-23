package clients.cashier;

import catalogue.Basket;
import catalogue.User;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import java.util.Observable;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel extends Observable
{
  private enum State { process, checked }

  private State       theState   = State.process;   // Current state
  private Product     theProduct = null;            // Current product
  private Basket      theBasket  = null;            // Bought items

  private String      pn = "";                      // Product being processed

  private StockReadWriter theStock     = null;
  private OrderProcessing theOrder     = null;
  private LoginReader theLogin = null;
  private User currentUser = null;

  /**
   * Construct the model of the Cashier
   * @param mf The factory to create the connection objects
   */

  public CashierModel(MiddleFactory mf)
  {
    try                                           // 
    {      
      theStock = mf.makeStockReadWriter();        // Database access
      theOrder = mf.makeOrderProcessing();        // Process order
      theLogin = mf.makeLoginReader(); 				//so we can check if the user is logged in
    } catch ( Exception e )
    {
      DEBUG.error("CashierModel.constructor\n%s", e.getMessage() );
    }
    theState   = State.process;                  // Current state
  }
  
  /**
   * Get the Basket of products
   * @return basket
   */
  public Basket getBasket()
  {
    return theBasket;
  }
  public void setUser(User passedInUser) {
	  currentUser = passedInUser;
  }
  public void removeUser() {
	  currentUser = null;
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum, String strquantity )
  {
    String theAction = "";
    theState  = State.process;                  // State process
    pn  = productNum.trim();  // Product no.
    //int    amount  = quantity;                         //  & quantity
    try
    {
      int quantity = Integer.parseInt(strquantity); //try to put the quantity as an integer
      if ( theStock.exists( pn ) )              // Stock Exists?
      {                                         // T
        Product pr = theStock.getDetails(pn);   //  Get details
        if ( pr.getQuantity() >= quantity )       //  In stock?
        {                                       //  T
          theAction =                           //   Display 
            String.format( "%s : %7.2f (%2d) ", //
              pr.getDescription(),              //    description
              pr.getPrice(),                    //    price
              pr.getQuantity() );               //    quantity     
          theProduct = pr;                      //   Remember prod.
          theProduct.setQuantity( quantity );     //    & quantity
          theState = State.checked;             //   OK await BUY 
        } else {                                //  F
          theAction =                           //   Not in Stock
            pr.getDescription() +" not in stock";
        }
      } else {                                  // F Stock exists
        theAction =                             //  Unknown
          "Unknown product number " + pn;       //  product no.
      }
    } catch (NumberFormatException e) { //catch if the numbers arent in the right format
    	theAction = "Unknown quantity number " + strquantity;
    }
    catch( StockException e ) //catch for any stock exceptions 
    {
      DEBUG.error( "%s\n%s", 
            "CashierModel.doCheck", e.getMessage() );
      theAction = e.getMessage();
    }
    setChanged(); notifyObservers(theAction);
  }

  /**
   * Buy the product
   */
  public void doBuy()
  {
    String theAction = "";
    //int    amount  = 1;                         //  & quantity
    try
    {
      if ( theState != State.checked )          // Not checked
      {                                         //  with customer
        theAction = "please check its availablity";
      } else {
        boolean stockBought =                   // Buy
          theStock.buyStock(                    //  however
            theProduct.getProductNum(),         //  may fail              
            theProduct.getQuantity() );         //
        if ( stockBought )                      // Stock bought
        {                                       // T
          makeBasketIfReq();                    //  new Basket ?
          theBasket.add( theProduct  );          //  Add to bought
          theAction = "Purchased " +            //    details
                  theProduct.getDescription();  //
          
        } else {                                // F
          theAction = "!!! Not in stock";       //  Now no stock
        }
      }
    } catch( StockException e )
    {
      DEBUG.error( "%s\n%s", 
            "CashierModel.doBuy", e.getMessage() );
      theAction = e.getMessage();
    }
    theState = State.process;                   // All Done
    setChanged(); notifyObservers(theAction);
  }
  
  
  /**
   * removing items from the basket
   * @param pn, the product number to find the product object
   * @param theQuantity, how many of the item they want to remove.
   */
  public void doRemove(String pn, String strQuantity) {
	  //System.out.println("remove in the model");
	  String theAction = "";
	  try { //in a try catch incase the integer cant be parsed or errors occur with the db
		  if ( theState != State.checked )  //checked the user entered the correct format
		  {  theAction = "please check its availablity";
		  }  else {	 		// if the item has been checked
			int theQuantity =  Integer.parseInt(strQuantity); //get the quantity from the input
			Product pr = theStock.getDetails(pn); //the product details
			int removenum = theQuantity; //and the quantity is how many to be removed
			if (removenum!=0) { //checking the user isnt removing 0 items
				System.out.println(removenum);
				boolean removed = theBasket.doRemove(pr,removenum); //calls the basket function 
				if (removed) {
					theStock.addStock(pn, theQuantity); 
					//we then add back the stock we removed so the availability is right
					theAction = "removed";
				} else {
					theAction = "unable to remove";
				}
				//use removed to added error message i think, if not removed then change action
				
			}	
		  }
	  } 
	  catch(Exception e) {
		  System.out.print("error "+e);
	  }
	  setChanged(); notifyObservers(theAction);
  }
  
  
  /**
   * Customer pays for the contents of the basket
   */
  public void doBought() 
  {
    String theAction = "";
    int    amount  = 1;                       //  & quantity
    
    try
    {
//    	System.out.println("ordernum in basket "+theBasket.getOrderNum());
//    	System.out.println("the product num"+theBasket.getFirst().getProductNum());
//    	boolean addtodb = theStock.addOrder(theBasket.getOrderNum(), theBasket.getFirst().getProductNum(),2);
      if ( theBasket != null &&
           theBasket.size() >= 1 )            // items > 1
      {                                       // T
    	  //check if the user is logged in, if yes add to the database, if not just add order details.
    	  if (theLogin.getCurrentUser() != null ) {
    		  theStock.addUserAndOrder(theLogin.getCurrentUser().getUsername(), theBasket.getOrderNum());
    	  }
    	  
    		//adding the order details to the orders database.  
    	  for (Product eachProduct:theBasket) {
    		  boolean addtodb = theStock.addOrder(theBasket.getOrderNum(),eachProduct.getProductNum(),eachProduct.getQuantity());
    		  if (addtodb == false) {
    			  System.out.println("not added");
    		  }
    	  }
        theOrder.newOrder( theBasket );       //  Process order
        theBasket = null;                     //  reset
      }                                       //
      theAction = "Start New Order";            // New order
      theState = State.process;               // All Done
       theBasket = null;
    } catch( OrderException e )
    {
      DEBUG.error( "%s\n%s", 
            "CashierModel.doCancel", e.getMessage() );
      theAction = e.getMessage();
    }
    catch (Exception e) {
    	System.out.println(e);
    }
    theBasket = null;
    setChanged(); notifyObservers(theAction); // Notify
  }

  /**
   * ask for update of view callled at start of day
   * or after system reset
   */
  public void askForUpdate()
  {
    setChanged(); notifyObservers("Welcome");
  }
  
  /**
   * make a Basket when required
   */
  private void makeBasketIfReq()
  {
    if ( theBasket == null )
    {
      try
      {
        int uon   = theOrder.uniqueNumber();     // Unique order num.
        theBasket = makeBasket();                //  basket list
        theBasket.setOrderNum( uon );            // Add an order number
      } catch ( OrderException e )
      {
        DEBUG.error( "Comms failure\n" +
                     "CashierModel.makeBasket()\n%s", e.getMessage() );
      }
    }
  }

  /**
   * return an instance of a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket()
  {
    return new Basket();
  }
  
}
  
