/**
 * 
 */
package clients.login;


import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JFrame;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import middle.LocalMiddleFactory;
import middle.MiddleFactory;

/**
 * 
 */
class loginTest {
	JFrame  window = new JFrame();
	MiddleFactory mlf = new LocalMiddleFactory();
	loginModel model      = new loginModel(mlf);
    loginView view        = new loginView( window, mlf, 100, 100 );
    loginController cont  = new loginController( model, view );
    

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//view.setController( cont );
	    
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void test() {
		//assertEquals(view.doLogin("jas","00000000"));
		fail("Not yet implemented");
	}

}
