package boar401s2.marktime.storage;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.AuthenticationException;

//TODO: Update this class to use OAuth2

/**
 * Used to autenticated services. At the moment it only authenticates
 * spreadsheet services.
 */
public class Authentication{
	
	private String email;
	private String password;
	
	public Authentication(String email, String password){
		this.email = email;
		this.password = password;
	}
	
	public void authenticateSpreadsheetService(SpreadsheetService service) throws AuthenticationException{
		service.setUserCredentials(email, password);
	}	
}