package dbAccess;

/**
 * Implements Read access to the stock list
 * The stock list is held in a relational DataBase
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

import catalogue.Product;
import debug.DEBUG;
import middle.StockException;
import middle.StockReader;
import catalogue.Basket;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

// mySQL
//    no spaces after SQL statement ;

/**
  * Implements read only access to the stock database.
  */
public class StockR implements StockReader
{
  private Connection theCon    = null;      // Connection to database
  private Statement  theStmt   = null;      // Statement object

  /**
   * Connects to database
   * Uses a factory method to help setup the connection
   * @throws StockException if problem
   */
  public StockR()
         throws StockException
  {
    try
    {
      DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
      dbDriver.loadDriver();
    
      theCon  = DriverManager.getConnection
                  ( dbDriver.urlOfDatabase(), 
                    dbDriver.username(), 
                    dbDriver.password() );

      theStmt = theCon.createStatement();
      theCon.setAutoCommit( true );
    }
    catch ( SQLException e )
    {
      throw new StockException( "SQL problem:" + e.getMessage() );
    }
    catch ( Exception e )
    {
      throw new StockException("Can not load database driver.");
    }
  }


  /**
   * Returns a statement object that is used to process SQL statements
   * @return A statement object used to access the database
   */
  
  protected Statement getStatementObject()
  {
    return theStmt;
  }

  /**
   * Returns a connection object that is used to process
   * requests to the DataBase
   * @return a connection object
   */

  protected Connection getConnectionObject()
  {
    return theCon;
  }

  /**
   * Checks if the product exits in the stock list
   * @param pNum The product number
   * @return true if exists otherwise false
   */
  public synchronized boolean exists( String pNum )
         throws StockException
  {
    
    try
    {
      ResultSet rs   = getStatementObject().executeQuery(
        "select price from ProductTable " +
        "  where  ProductTable.productNo = '" + pNum + "'"
      );
      boolean res = rs.next();
      DEBUG.trace( "DB StockR: exists(%s) -> %s", 
                    pNum, ( res ? "T" : "F" ) );
      return res;
    } catch ( SQLException e )
    {
      throw new StockException( "SQL exists: " + e.getMessage() );
    }
  }

  /**
   * Returns details about the product in the stock list.
   *  Assumed to exist in database.
   * @param pNum The product number
   * @return Details in an instance of a Product
   */
  public synchronized Product getDetails( String pNum )
         throws StockException
  {
    try
    {
      Product   dt = new Product( "0", "", 0.00, 0 );
      ResultSet rs = getStatementObject().executeQuery(
        "select description, price, stockLevel " +
        "  from ProductTable, StockTable " +
        "  where  ProductTable.productNo = '" + pNum + "' " +
        "  and    StockTable.productNo   = '" + pNum + "'"
      );
      if ( rs.next() )
      {
        dt.setProductNum( pNum );
        dt.setDescription(rs.getString( "description" ) );
        dt.setPrice( rs.getDouble( "price" ) );
        dt.setQuantity( rs.getInt( "stockLevel" ) );
      }
      rs.close();
      return dt;
    } catch ( SQLException e )
    {
      throw new StockException( "SQL getDetails: " + e.getMessage() );
    }
  }

  /**
   * Returns 'image' of the product
   * @param pNum The product number
   *  Assumed to exist in database.
   * @return ImageIcon representing the image
   */
  public synchronized ImageIcon getImage( String pNum )
         throws StockException
  {
    String filename = "default.jpg";  
    try
    {
      ResultSet rs   = getStatementObject().executeQuery(
        "select picture from ProductTable " +
        "  where  ProductTable.productNo = '" + pNum + "'"
      );
      
      boolean res = rs.next();
      if ( res )
        filename = rs.getString( "picture" );
      rs.close();
    } catch ( SQLException e )
    {
      DEBUG.error( "getImage()\n%s\n", e.getMessage() );
      throw new StockException( "SQL getImage: " + e.getMessage() );
    }
    
    //DEBUG.trace( "DB StockR: getImage -> %s", filename );
    return new ImageIcon( filename );
  }



  public synchronized ResultSet getAllDescription( ) {
	  
	try {
		ResultSet rs = getStatementObject().executeQuery(
		        "select description" +
		        "  from ProductTable "
		        		);
		return rs;
	} catch (Exception e){
		System.out.println(e);
		return null;
	}
  }
  public synchronized int getNextOrderNum() {
	  try {
		  int biggestNum = 95;
		  ResultSet rs = getStatementObject().executeQuery(
				  "select orderNum"+
				  " from Orders"+
				  " order by orderNum desc");
		  System.out.println("test1");
		  if (rs.next()) {
		  biggestNum = rs.getInt("orderNum");
		  }
		  System.out.println("test2");
		  rs.close();
		  System.out.println(biggestNum);
		  biggestNum++;
		  System.out.println(biggestNum);
		  return biggestNum;
		  
	  } catch (Exception e) {
		  System.out.println(e);
		  return 36;
	  }
  }
  public synchronized boolean addOrder(int orderNum, String productNo, int quantity) {
	  try {
		  getStatementObject().executeUpdate(
				  "insert into Orders values ( "+orderNum+", '"+productNo+"', "+quantity+")" );
		  
		  System.out.println("added into orders");
		  return true;
	  } catch (Exception e) {
		  System.out.println(e);
		  return false;
	  }
  }
  public synchronized Basket getOrder(int orderNum) {
	  System.out.println("test1 "+ orderNum);
	  
	  
	  Basket theBasket = new Basket(orderNum);
	  try {
		  ResultSet rs = getStatementObject().executeQuery(
				 "select * from Orders");
		  while (rs.next()) { System.out.println(rs.getInt("Quantity"));}
		  rs.close();
		  //theCon.setAutoCommit(false);
		  int counter = 0;
		  ResultSet counters = getStatementObject().executeQuery(
				  "select count(productNo) as counter from Orders where orderNum = "+ orderNum);
		  if (counters.next()) {
			  System.out.println(counters.getInt("counter")+"total hopefully");
			  counter = counters.getInt("counter");
		  }
		  counters.close();
		  //for (int i = 0; i < counter;i++) {
			   rs = getStatementObject().executeQuery(
				  "select productNo, Quantity from Orders where orderNum = "+orderNum);
		  //for (int i = 0; i < counter;i++) {
			   ArrayList<Integer> quants = new ArrayList<Integer>();
			   ArrayList<String> pns = new ArrayList<String>();
			   //ArrayList<Product> products = new ArrayList<Product>();
			  while (rs.next()) {
		  	//for (int i = 0; i < counter;i++) {
				  System.out.println("test3.5");
				  //int quant = rs.getInt("Quantity");
				  quants.add(rs.getInt("Quantity"));
				  //String pn = rs.getString("productNo");
				  //products.add(getDetails(rs.getString("productNo")));
				  pns.add(rs.getString("productNo"));
				  //Product pr = getDetails(pn);
			  //rs.next();
				  //Product tempProduct = pr;
			  //int quant = rs.getInt("Quantity");
				  //tempProduct.setQuantity(quant);
				  //theBasket.add(tempProduct);
				  //System.out.println(quant+pn+"doisj");
			  
		  	}
			  for (int j = 0; j < pns.size(); j++) {
				  
				  Product pr = getDetails(pns.get(j));
				  pr.setQuantity(quants.get(0));
				  theBasket.add(pr);
			  }
			  rs.close();
		  //}
		 
		  
		  //theCon.setAutoCommit(true);
	  } catch (Exception e) {
		  System.out.println(e);
	  }
	  
	  return theBasket;
  }
  public synchronized ArrayList<Integer> getOrderNums (String username) {
	  System.out.println("test4");
	  
	  ArrayList<Integer> orders = new ArrayList<Integer>();
	  try {
		  ResultSet rs = getStatementObject().executeQuery(
				  "select orderNum from UOLink where username = '"+username+"'");
		  while (rs.next()) {
			 orders.add(rs.getInt("orderNum"));
		  }
		  rs.close();
	  } catch (Exception e) {
		  System.out.println(e);
	  }
	  return orders;
	  }
  
  public synchronized void addUserAndOrder(String username, int orderNum) {
	  try {
		  getStatementObject().executeUpdate(
				  "insert into UOLink values ( "+orderNum+", '"+username+"')" );
		  
		  System.out.println("added into UOLink");
		  
	  } catch (Exception e) {
		  System.out.println(e);
	  }
  }
}