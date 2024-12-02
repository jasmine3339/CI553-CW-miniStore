package clients.login;

import clients.login.loginModel;
import clients.login.loginView;

/**
 * The BackDoor Controller
 */

public class loginController
{
  private loginModel model = null;
  private loginView  view  = null;
  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public loginController( loginModel model, loginView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * Query interaction from view
   * @param pn The product number to be checked
   */
  public void doLogin( String username, String password )
  {
    model.doLogin(username, password);
  }
  
  /**
   * RStock interaction from view
   * @param pn       The product number to be re-stocked
   * @param quantity The quantity to be re-stocked
   */
  public void doCreate( String username, String password, String passcheck )
  {
    model.doCreate(username, password, passcheck);
  }

  /**
   * Clear interaction from view
   */
  public void doClear()
  {
    model.doClear();
  }

  
}

