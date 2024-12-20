package middle;

/**
  * Exception throw if there is an error in accessing the stock list
  * @author  Mike Smith University of Brighton
  * @version 2.0
  */
 
public class LoginException extends Exception
{
  private static final long serialVersionUID = 1;
  public LoginException( String s )
  {
    super(s);
  }
}
