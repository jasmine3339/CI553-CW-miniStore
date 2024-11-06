package catalogue;

import java.io.Serializable;
import java.util.Collections;

/**
 * Write a description of class BetterBasket here.
 * 
 * @author  Your Name 
 * @version 1.0
 */
public class BetterBasket extends Basket implements Serializable
{
  private static final long serialVersionUID = 1L;
  

  // You need to add code here
  // merge the items for same product,
  // or sort the item based on the product number
  
  //overriding the add function to check weather there is already that product in the list and adding 1 to quantity if 
  public boolean add( Product pr )
  {     
	boolean inList = false;
	for (Product x:this) {
		if (x.equals(pr)) {
			inList = true;
			x.setQuantity(x.getQuantity()+1);
			
		}
		
	}
	if (inList == false) {
		return super.add( pr );     // Call add in ArrayList
	} else {
		//pr.setQuantity(pr.getQuantity()+1);
		
		return true;
	}
    
  }
}
