package middle;

import catalogue.Basket;
import catalogue.Product;

import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.*;

/**
  * Interface for read access to the stock list.
  * @author  Mike Smith University of Brighton
  * @version 2.0
  */

public interface StockReader
{

 /**
   * Checks if the product exits in the stock list
   * @param pNum Product nymber
   * @return true if exists otherwise false
   * @throws StockException if issue
   */
  boolean exists(String pNum) throws StockException;
         
  /**
   * Returns details about the product in the stock list
   * @param pNum Product nymber
   * @return StockNumber, Description, Price, Quantity
   * @throws StockException if issue
   */
  
  Product getDetails(String pNum) throws StockException;
  
  
  /**
   * Returns an image of the product in the stock list
   * @param pNum Product nymber
   * @return Image
   * @throws StockException if issue
   */
  
  ImageIcon getImage(String pNum) throws StockException;
  
  
  
  /**
   * get list of descriptions to search by
   * @return list of descriptinos from db
   */
  ResultSet getAllDescription( ) throws StockException;
  
  /**
   * reutns the last order numebr so the next one can be one bigger
   * @return
   * @throws StockException
   */
  int getNextOrderNum() throws StockException;
  
  /**
   * gets list of all users order numbers 
   * @param username
   * @return arraylist of each order number
   * @throws StockException
   */
  public ArrayList<Integer> getOrderNums (String username) throws StockException;
  
  /**
   * gets basket for a specified order number
   * @param orderNum
   * @return basket of the order specified
   * @throws StockException
   */
  public Basket getOrder(int orderNum) throws StockException;
  
  /**
   * adds to the linking table, only used if someone is logged in when order is made.
   * @param username
   * @param orderNum
   * @throws OrderException
   */
  public void addUserAndOrder(String username, int orderNum) throws OrderException;
	  
  }
  