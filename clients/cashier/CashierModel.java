package clients.cashier;

import catalogue.Basket;
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

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum, int quantity )
  {
    String theAction = "";
    theState  = State.process;                  // State process
    pn  = productNum.trim();                    // Product no.
    //int    amount  = quantity;                         //  & quantity
    try
    {
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
    } catch( StockException e )
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
  public void doRemove(String pn, int theQuantity) {
	  //System.out.println("remove in the model");
	  String theAction = "";
	  try {
		  if ( theState != State.checked )  
		  {
			  theAction = "please check its availablity";
		  }
		  else {	
			Product pr = theStock.getDetails(pn);
			int removenum = theQuantity;
			if (removenum!=0) {
				System.out.println(removenum);
				boolean removed = theBasket.doRemove(pr,removenum);
				//use removed to added error message i think, if not removed then change action
				theAction = "removed";
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
      if ( theBasket != null &&
           theBasket.size() >= 1 )            // items > 1
      {                                       // T
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
  
