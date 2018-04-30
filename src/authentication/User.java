package authentication;

//import misc.CryptoStuff;

public class User {
	private String login;
	private String passwordHash; //hash of user's pw
	private String role;

	public User(String login, String pw, String role) {
		this.login = login;
		this.role = role;
		passwordHash = CryptoStuff.sha256(pw);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String userName) {
		this.role = role;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

}
