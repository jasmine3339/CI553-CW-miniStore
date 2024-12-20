package clients.login;

import catalogue.User;
import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

//import clients.Main;

/**
 * Implements the Customer view.
 */

public class loginView implements Observer
{
  private static final String LOGIN  = "login";
  private static final String CREATE    = "Create";
  private static final String PROGRESS = "Progress";
  private static final String ORDERS = "Orders";
  private static final String RETURN = "Return";
 
  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels
  
  private Container rootWindow = null;

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JLabel 		username	   = new JLabel();
  private final JLabel 		password	   = new JLabel();
  private final JLabel 		passcheck	   = new JLabel();

  private final JTextArea   theInstructions  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtLogin = new JButton( LOGIN );
  private final JButton     theBtCreate = new JButton( CREATE );
  private final JButton     theBtProgress = new JButton( PROGRESS );
  private final JButton     theBtOrders = new JButton( ORDERS );
  private final JButton     theBtReturn = new JButton( RETURN );
  
  private final JTextField	theUsername = new JTextField();
  private final JTextField  thePassword = new JTextField();
  private final JTextField	thePassCheck = new JTextField();
  private final JTextField	theOrderNum = new JTextField();
  
  private User currentUser = null;
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
    pageTitle.setText( "Login or create account" );                        
    cp.add( pageTitle );
    
    theBtLogin.setBounds( 16, 25+60*0, 80, 40 );    // login button 
    theBtLogin.addActionListener(                   // Call back code
      e -> cont.doLogin( theUsername.getText(),thePassword.getText() ) );
    //theBtLogin.addActionListener( e -> removeItems());
    cp.add( theBtLogin );                           //  Add to canvas

    theBtCreate.setBounds( 16, 25+60*1, 80, 40 );   // create new account Button
    theBtCreate.addActionListener(                  // Call back code
      e -> cont.doCreate( theUsername.getText(),
                          thePassword.getText(),
                          thePassCheck.getText()) );
    cp.add( theBtCreate );                          //  Add to canvas
    
    //setting the buttons to hidden but in the right place for after they have logged in
    theBtProgress.setBounds( 16, 25+60*0, 80, 40 );    // login button 
    theBtProgress.addActionListener(                   // Call back code
      e -> cont.doProgress( ));
    theBtProgress.setVisible(false);
    cp.add( theBtProgress ); 
   
    theBtOrders.setBounds( 16, 25+60*1, 80, 40 );    // order check button 
    theBtOrders.addActionListener(                   // Call back code
    	      e -> cont.doOrderCheck( ));
    	    theBtOrders.setVisible(false);			//hide it for now
    	    cp.add( theBtOrders); 
    	    
    theBtReturn.setBounds( 16, 25+60*2, 80, 40 );    // return button 
    theBtReturn.addActionListener(                   // Call back code
    	      e -> cont.doReturn( ));
    	    theBtReturn.setVisible(false); 				//hide it for now
    	    cp.add( theBtReturn ); 
    	    
    theOrderNum.setBounds( 16, 25+60*3, 80, 40);	//input for order number to return?
    theOrderNum.setVisible(false);
    cp.add(theOrderNum);
    
    theAction.setBounds( 110, 25 , 270, 20 );       // Message area
    theAction.setText( "" );                        // Blank
    cp.add( theAction );                            //  Add to canvas

    theUsername.setBounds( 110, 50, 120, 40 );         // Input Area
    //theUsername.setText("username");                           // Blank
    cp.add( theUsername );                             //  Add to canvas
    
    username.setBounds( 110, 90 , 270, 20 );       
    username.setText( "username" );                        
    cp.add( username );
    
    password.setBounds( 260, 90 , 270, 20 );       
    password.setText( "password" );                        
    cp.add( password );
    
    thePassword.setBounds( 260, 50, 120, 40 );       // Input Area
    //thePassword.setText("password");                        
    cp.add( thePassword );                           //  Add to canvas
    
    thePassCheck.setBounds( 260, 120, 120, 40 );       // Input Area
    //thePassCheck.setText("passCheck");                        
    cp.add( thePassCheck );                           //  Add to canvas

    passcheck.setBounds( 260, 160 , 270, 20 );       
    passcheck.setText( "confirm password" );                        
    cp.add( passcheck );
    
    theSP.setBounds( 16, 145, 215, 110);
    theInstructions.setText( "password must be:\n- 8-12 characters\n- have at least:\n1 upper case letter\n1 lower case letter\n1 number" );                        //  Blank
    theInstructions.setFont( f );   
    cp.add(theSP);
    theSP.getViewport().add( theInstructions );           //  In TextArea
    rootWindow.setVisible( true ); 
    
  }
  
  public void setController( loginController c )
  {
    cont = c;
  }
  
  /**
   * this sets the user so only one is logged in at once.
   * @param addUser - user to be added as current user
   */
  public void setUser(User addUser) {
	  currentUser = addUser;
  }
  
  /**
   * this sets all initial j variables to be blank or not visible. and sets the scroll pane to be 
   * in a different place so all the buttons and boxes fit.
   */
  public void removeItems() {
	  theUsername.setVisible(false);
	  thePassword.setVisible(false);
	  thePassCheck.setVisible(false);
	  username.setText("");
	  password.setText("");
	  passcheck.setText("");
	  theBtCreate.setVisible(false);
	  theBtLogin.setVisible(false);
	  theInstructions.setText("");
	  theSP.setBounds(110, 55, 270, 205);
	  
  }
  
  /**
   * this sets all the previously hidden items to be visible so they can be used
   */
  public void addItems() {
	  theBtProgress.setVisible(true);
	  theBtOrders.setVisible(true);
	  theBtReturn.setVisible(true);
	  theOrderNum.setVisible(true);
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
    if (message.equals("adding your account") || message.equals("logging in") )
    {
    	//changing the window so after it has been logged in different buttons appear
    	removeItems();
    	addItems();   
    	getUserfromModel();
    	
    } else if (message.equals("Orders:")) {
    	//set the sp text to all the previous orders of the user 
    	theInstructions.setText(cont.getAllOrderDetails());
    }
  }
  
  /**
   * this connects to the model to get the user so the page title can be set to 
   * hello and their username
   */
	public void getUserfromModel() {
		cont.getUserfromModel();
		pageTitle.setText("hello "+currentUser.getUsername());
	}
  

}