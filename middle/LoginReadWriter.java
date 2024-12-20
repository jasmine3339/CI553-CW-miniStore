package middle;

import catalogue.User;

public interface LoginReadWriter extends LoginReader
{
	/**
	 * calling loginRW? functino to add new user to db
	 * @param username
	 * @param password
	 * @throws LoginException
	 */
	void addUser(String username, String password) throws LoginException;
	
	
	
	
}