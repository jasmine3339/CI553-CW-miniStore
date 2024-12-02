package clients.login;

import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class loginView implements Observer
{
  private static final String LOGIN  = "login";
  private static final String CREATE    = "Create";
 
  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  
  //private final JTextArea   theOutput  = new JTextArea();
  //private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtLogin = new JButton( LOGIN );
  private final JButton     theBtCreate = new JButton( CREATE );
  
  private final JTextField	theUsername = new JTextField();
  private final JTextField  thePassword = new JTextField();
  private final JTextField	thePassCheck = new JTextField();
  
  
  private StockReadWriter theStock     = null;
  //this will need to change to be the login data base i will make eventually
  private loginController cont= null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  public loginView(  RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    try                                             // 
    {      
      theStock = mf.makeStockReadWriter();          // Database access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    Container cp         = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );
    
    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

    pageTitle.setBounds( 110, 0 , 270, 20 );       
    pageTitle.setText( "Staff check and manage stock" );                        
    cp.add( pageTitle );
    
    theBtLogin.setBounds( 16, 25+60*0, 80, 40 );    // login button 
    theBtLogin.addActionListener(                   // Call back code
      e -> cont.doLogin( theUsername.getText(),thePassword.getText() ) );
    cp.add( theBtLogin );                           //  Add to canvas

    theBtCreate.setBounds( 16, 25+60*1, 80, 40 );   // create new account Button
    theBtCreate.addActionListener(                  // Call back code
      e -> cont.doCreate( theUsername.getText(),
                          thePassword.getText(),
                          thePassCheck.getText()) );
    cp.add( theBtCreate );                          //  Add to canvas

    
 
    theAction.setBounds( 110, 25 , 270, 20 );       // Message area
    theAction.setText( "" );                        // Blank
    cp.add( theAction );                            //  Add to canvas

    theUsername.setBounds( 110, 50, 120, 40 );         // Input Area
    theUsername.setText("username");                           // Blank
    cp.add( theUsername );                             //  Add to canvas
    
    thePassword.setBounds( 260, 50, 120, 40 );       // Input Area
    thePassword.setText("password");                        // 0
    cp.add( thePassword );                           //  Add to canvas
    
    thePassCheck.setBounds( 260, 100, 120, 40 );       // Input Area
    thePassCheck.setText("passCheck");                        // 0
    cp.add( thePassCheck );                           //  Add to canvas

    
  }
  
  public void setController( loginController c )
  {
    cont = c;
  }

  /**
   * Update the view, called by notifyObservers(theAction) in model,
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg )  
  {
    loginModel model  = (loginModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );
    
    //theUsername.setText( model.getBasket().getDetails() );
    //thePasswored.requestFocus();
  }

}