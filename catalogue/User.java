package catalogue;

public class User 
{
	
	//to start with we just need a user to have a name and password
	private String username ;
	private String password ;
	
	//hopefully i can add more values like links to the orders 
	
	public User()
	{
		//use the db to initialise users to compare
	}
	/**
	 * 2 ways to make a user, if you know the username and password right away you can start with them initialkised.
	 */
	public User(String user, String pass)
	{
		username = user;
		password = pass;
	}
	public void setUsername(String name) 
	{
		username = name;
	}
	public String getUsername()
	{
		return username;
	}
	public void setPassword(String pass)
	{
		password = pass;
	}
	public String getPassword()
	{
		return password;
	}
	
}