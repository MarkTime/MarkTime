package boar401s2.marktime.security;

public class Credentials {
	
	String email;
	String password;
	
	public Credentials(String email, String password){
		this.email = email;
		this.password = password;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getPassword(){
		return password;
	}

}
