package clients.login;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone login Client
 */


public class loginClient
{
   public static void main (String args[])
   {
     String stockURL = args.length < 1     // URL of stock RW
                     ? Names.STOCK_RW      //  default  location
                     : args[0];            //  supplied location
     String orderURL = args.length < 2     // URL of order
                     ? Names.ORDER         //  default  location
                     : args[1];            //  supplied location
     
    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRWInfo( stockURL );
    mrf.setOrderInfo  ( orderURL );        //
    displayGUI(mrf);                       // Create GUI
  }
  
  private static void displayGUI(MiddleFactory mf)
  {     
    JFrame  window = new JFrame();
     
    window.setTitle( "BackDoor Client (MVC RMI)");
    window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    
    loginModel      model = new loginModel(mf);
    loginView       view  = new loginView( window, mf, 0, 0 );
    loginController cont  = new loginController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model - view is observer, model is Observable
    window.setVisible(true);         // Display Screen
  }
}
