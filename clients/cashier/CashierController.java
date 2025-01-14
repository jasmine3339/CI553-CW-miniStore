package clients.cashier;


/**
 * The Cashier Controller
 */

public class CashierController
{
  private CashierModel model = null;
  private CashierView  view  = null;

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public CashierController( CashierModel model, CashierView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * Check interaction from view
   * @param pn The product number to be checked
   */
  public void doCheck( String pn, String theQuantity )
  {
    model.doCheck(pn, theQuantity);
  }
  
  /**
   * remove item from basket, from view to model
   * @param pn is the product number
   * @param theQuantity how many items to remove
   */
  public void doRemove( String pn, String theQuantity )
  {
	//System.out.println("remove in the controller");
    model.doRemove(pn, theQuantity);
  }

   /**
   * Buy interaction from view
   */
  public void doBuy()
  {
    model.doBuy();
  }
  
   /**
   * Bought interaction from view
   */
  public void doBought()
  {
    model.doBought();
  }
}
