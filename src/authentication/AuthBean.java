package authentication;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.Stateful;

/**
 * Session Bean implementation class AuthBean
 */
@Singleton
public class AuthBean implements AuthBeanRemote {

private ABACPolicyAuth accessPolicy;
	
	/**
	 * fake server session states
	 */
	private List<Session> sessions;
	
	/**
	 * users authenticator knows about
	 */
	private List<User> credentials;
	
	public AuthBean() {
		//init the session list and credentials list
		sessions = new ArrayList<Session>();
		credentials = new ArrayList<User>();
		
		//create a default ABAC policy and add some permissions for user Wilma Williams
		accessPolicy = new ABACPolicyAuth();

		//create Wilma as valid user
		//NOTE: we don't store the user's password in our credential store
		//User hashes the password in its constructor
		User u = new User("wilma", "arugula", "Administrator");
		credentials.add(u);
		//Wilma can access everything
		accessPolicy.createSimpleUserACLEntry(u.getLogin(), u.getRole());

		//create Leroy Jenkins as valid user
		u = new User("leroy", "wipeout", "Data Entry");
		credentials.add(u);
		accessPolicy.createSimpleUserACLEntry(u.getLogin(),u.getRole());

		//create Sasquatch Jones as valid user
		u = new User("sasquatch", "jerky", "Intern");
		credentials.add(u);
		accessPolicy.createSimpleUserACLEntry(u.getLogin(), u.getRole());
		
	}
		
	/**
	 * determine if session user has access to function f 
	 * @param sessionId id of session to lookup in user reference identifier in policy
	 * @param f function for which permission is being asked
	 * @return
	 * @throws LoginException
	 */
	public boolean hasAccess(int sessionId, String f) {
		for(Session s : sessions) {
			if(s.getSessionId() == sessionId) {
				//session id matches, use serverSession for user object for access control
				return accessPolicy.canUserAccessFunction(s.getSessionUser().getLogin(), f);
			}
		}
		return false;
	}
		
	/**
	 * login and create a new session if credentials match using Sha-256 hash of user's password
	 * @param l
	 * @param pwHash Sha256 hash of password
	 * @return id of newly created session
	 * @throws LoginException
	 */
	public int loginSha256(String l, String pwHash) throws LoginException {
		//iterate through user credentials and see if l and pwHash match. if so, make a session and returns its id
		for(User u : credentials) {
			if(u.getLogin().equals(l) && u.getPasswordHash().equals(pwHash)) {
				//create a fake server-side session from the user object given to us
				//in reality this makes no sense. authentication happens on the server side
				Session s = new Session(u);
				sessions.add(s);
				return s.getSessionId();
			}
		}
		throw new LoginException("Authentication failed");
	}
	
	/**
	 * remove the session object with id of sessionId
	 * @param sessionId
	 */
	public void logout(int sessionId) {
		for(int i = sessions.size() - 1; i >= 0; i--) {
			Session s = sessions.get(i);
			if(s.getSessionId() == sessionId) {
				sessions.remove(i);
			}
		}
	}
	
	


}
