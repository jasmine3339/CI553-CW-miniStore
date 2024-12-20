package catalogue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;


/**
 * A collection of products,
 * used to record the products that are to be wished to be purchased.
 * @author  Mike Smith University of Brighton
 * @version 2.2
 *
 */
public class Basket extends ArrayList<Product> implements Serializable
{
  private static final long serialVersionUID = 1;
  private int    theOrderNum = 0;          // Order number
  
  /**
   * Constructor for a basket which is
   *  used to represent a customer order/ wish list
   */
  public Basket()
  {
    theOrderNum  = 0;
  }
  public Basket(int orderNum) {
	  theOrderNum = orderNum;
  }
  /**
   * Set the customers unique order number
   * Valid order Numbers 1 .. N
   * @param anOrderNum A unique order number
   */
  public void setOrderNum( int anOrderNum )
  {
    theOrderNum = anOrderNum;
  }

  /**
   * Returns the customers unique order number
   * @return the customers order number
   */
  public int getOrderNum()
  {
    return theOrderNum;
  }
  
  /**
   * Add a product to the Basket.
   * Product is appended to the end of the existing products
   * in the basket.
   * @param pr A product to be added to the basket
   * @return true if successfully adds the product
   */
  // Will be in the Java doc for Basket
  @Override
  public boolean add( Product pr )
	  {     
			boolean inList = false;
			for (Product x:this) {
				if (x.getProductNum().equals(pr.getProductNum())) {
					inList = true;
					x.setQuantity(x.getQuantity()+pr.getQuantity());
				}
			}
			if (inList == false) {
				super.add( pr );  
				Collections.sort(this);
				return true; // Call add in ArrayList
				
			} else {
				//pr.setQuantity(pr.getQuantity()+1);
				
				return true;
			}
			
	  }
  
  
  
 /**
  * returns true or false for weather the item has been removed from the basket 
  * @param pr, the product to be removed
  * @param removeNum, how many of the number to be removed
  * @return true/false, the item has been removed.
  */  
  public boolean doRemove(Product pr, int removeNum) {
	  	System.out.println("remove!");
		for (Product x:this) {
			//this checks every item in the pocket and sees if it is the same as the entered product number
			if (x.getProductNum().equals(pr.getProductNum())) { 
				//System.out.println(pr.getQuantity()+" quantity number of pr");
				int currentquant = x.getQuantity();
				//this sets the new quantity to the current quantity - the specified amount to remove
				if (currentquant - removeNum >-1) {
					x.setQuantity(currentquant - removeNum);
					//this checks if the quantity in the basket is 0, if it is it is removed from the basket	
					if (x.getQuantity()>0) {
						return true;
					} else {
						this.remove(x);
						return true;
					}
				}				
			}
		}
		return false;  
  }
  
 
  
  
  /**
   * Returns a description of the products in the basket suitable for printing.
   * @return a string description of the basket products
   */
  public String getDetails()
  {
    Locale uk = Locale.UK;
    StringBuilder sb = new StringBuilder(256);
    Formatter     fr = new Formatter(sb, uk);
    String csign = (Currency.getInstance( uk )).getSymbol();
    double total = 0.00;
    if ( theOrderNum != 0 )
      fr.format( "Order number: %03d\n", theOrderNum );
      
    
    if ( this.size() > 0 )
    {
      for ( Product pr: this )
      {
        int number = pr.getQuantity();
        fr.format("%-7s",       pr.getProductNum() );
        fr.format("%-14.14s ",  pr.getDescription() );
        fr.format("(%3d) ",     number );
        fr.format("%s%7.2f",    csign, pr.getPrice() * number );
        fr.format("\n");
        total += pr.getPrice() * number;
      }
      fr.format("----------------------------\n");
      fr.format("Total                       ");
      fr.format("%s%7.2f\n",    csign, total );
      fr.close();
    }
    return sb.toString();
  }


}
