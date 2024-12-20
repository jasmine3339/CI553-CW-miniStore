package clients.customer;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dbAccess.StockR;
import middle.StockException;





class CustomerModelTest {

	@Test
	void testCheckByNameDB() {
		
		try {
			StockR stock = new StockR();
			ResultSet descriptions = stock.getAllDescription();
			System.out.println(descriptions);
			if (descriptions.next()) {
				System.out.println(descriptions.getString(0));
			}
			
				
		} catch (StockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		} catch (SQLException e) {
			fail("idk whats gouing on");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
