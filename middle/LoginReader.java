package middle;

import catalogue.User;

import java.sql.ResultSet;

import javax.swing.*;

/**
  * Interface for read access to the stock list.
  * @author  Mike Smith University of Brighton
  * @version 2.0
  */

public interface LoginReader
{

 /**
   * Checks if the product exits in the stock list
   * @param pNum Product nymber
   * @return true if exists otherwise false
   * @throws StockException if issue
   */
  boolean exists(String username) throws LoginException;
         
  
    
  /**
   * gets the password of a specified username
   * @param username
   * @return password
   * @throws LoginException
   */
  User getPassword(String username) throws LoginException;
  
  /**
   * returns current user from login details
   * @return currentuser
   */
  User getCurrentUser();
}