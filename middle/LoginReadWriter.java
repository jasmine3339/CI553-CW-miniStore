package middle;

import catalogue.User;

public interface LoginReadWriter extends LoginReader
{
	void addUser(String username, String password) throws LoginException;
	
	
	
	
}