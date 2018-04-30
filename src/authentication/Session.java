package authentication;

public class Session {

	public static int nextId = 1;

	/**
	 * unique session identifier
	 */
	private int sessionId;

	/**
	 * user who is involved in the session
	 */
	private User sessionUser;

	public Session(User user) {
		sessionUser = user;
		sessionId = nextId++;
	}

	public User getSessionUser() {
		return sessionUser;
	}

	public void setSessionUser(User sessionUser) {
		this.sessionUser = sessionUser;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
}
