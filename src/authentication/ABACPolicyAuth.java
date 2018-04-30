package authentication;

import java.util.HashMap;

public class ABACPolicyAuth {
	public static final String CAN_ACCESS_CHOICE_AL = "authorList";
	public static final String CAN_ACCESS_CHOICE_AA = "addAuthor";
	public static final String CAN_ACCESS_CHOICE_AS = "saveAuthor";
	public static final String CAN_ACCESS_CHOICE_BL = "bookList";
	public static final String CAN_ACCESS_CHOICE_AB = "addBook";
	public static final String CAN_ACCESS_CHOICE_CR = "createReport";
	public static final String CAN_ACCESS_CHOICE_AD = "deleteAuthor";
	public static final String CAN_ACCESS_CHOICE_BD = "deleteBook";
	public static final String CAN_ACCESS_CHOICE_ABD = "deleteAuthorBook";
	public static final String CAN_ACCESS_CHOICE_ABA = "addAuthorBook";
	public static final String CAN_ACCESS_CHOICE_ABS = "saveAuthorBook";






	/**
	 * access control list for users and functions (see above)
	 */
	private HashMap<String, HashMap<String, Boolean>> acl;
	
	public ABACPolicyAuth() {
		//create default fall-through policy where person has no access
		acl = new HashMap<String, HashMap<String, Boolean>>();
		
		//always good to have a fall through ACL entry configured with default permissions
		createSimpleUserACLEntry("default", "unauthorized");
	}
	
	public void createSimpleUserACLEntry(String login, String role) {
		HashMap<String, Boolean> userTable = new HashMap<String, Boolean>();
		switch(role) {
		case "unauthorized":
			userTable.put(CAN_ACCESS_CHOICE_AL, false);
			userTable.put(CAN_ACCESS_CHOICE_AA, false);
			userTable.put(CAN_ACCESS_CHOICE_BL, false);
			userTable.put(CAN_ACCESS_CHOICE_AB, false);
			userTable.put(CAN_ACCESS_CHOICE_CR, false);
			userTable.put(CAN_ACCESS_CHOICE_AD, false);
			userTable.put(CAN_ACCESS_CHOICE_BD, false);
			//add user table to acl
			acl.put(login, userTable);
			break;
		case "Administrator":
			userTable.put(CAN_ACCESS_CHOICE_AL, true);
			userTable.put(CAN_ACCESS_CHOICE_AA, true);
			userTable.put(CAN_ACCESS_CHOICE_AS, true);
			userTable.put(CAN_ACCESS_CHOICE_BL, true);
			userTable.put(CAN_ACCESS_CHOICE_AB, true);
			userTable.put(CAN_ACCESS_CHOICE_CR, true);
			userTable.put(CAN_ACCESS_CHOICE_AD, true);
			userTable.put(CAN_ACCESS_CHOICE_BD, true);
			userTable.put(CAN_ACCESS_CHOICE_ABD, true);
			userTable.put(CAN_ACCESS_CHOICE_ABA, true);
			userTable.put(CAN_ACCESS_CHOICE_ABS, true);
			//add user table to acl
			acl.put(login, userTable);
			break;
		case "Data Entry":
			//cannot delete authors or books from their list views
			//can add, change, and delete AuthorBook associations
			userTable.put(CAN_ACCESS_CHOICE_AL, true);
			userTable.put(CAN_ACCESS_CHOICE_AA, true);
			userTable.put(CAN_ACCESS_CHOICE_AS, true);
			userTable.put(CAN_ACCESS_CHOICE_BL, true);
			userTable.put(CAN_ACCESS_CHOICE_AB, true);
			userTable.put(CAN_ACCESS_CHOICE_CR, true);
			userTable.put(CAN_ACCESS_CHOICE_AD, false);
			userTable.put(CAN_ACCESS_CHOICE_BD, false);
			userTable.put(CAN_ACCESS_CHOICE_ABD, true);
			userTable.put(CAN_ACCESS_CHOICE_ABA, true);
			userTable.put(CAN_ACCESS_CHOICE_ABS, true);
			//add user table to acl
			acl.put(login, userTable);
			break;
		case "Intern":
			//can view lists/details, cannot change any data or generate
			//spreadsheet, so all save/delete functions/buttons disabled
			userTable.put(CAN_ACCESS_CHOICE_AL, true);
			userTable.put(CAN_ACCESS_CHOICE_AA, false);
			userTable.put(CAN_ACCESS_CHOICE_AS, false);
			userTable.put(CAN_ACCESS_CHOICE_BL, true);
			userTable.put(CAN_ACCESS_CHOICE_AB, false);
			userTable.put(CAN_ACCESS_CHOICE_CR, false);
			userTable.put(CAN_ACCESS_CHOICE_AD, false);
			userTable.put(CAN_ACCESS_CHOICE_BD, false);
			userTable.put(CAN_ACCESS_CHOICE_ABD, false);
			userTable.put(CAN_ACCESS_CHOICE_ABA, false);
			userTable.put(CAN_ACCESS_CHOICE_ABS, false);
			//add user table to acl
			acl.put(login, userTable);
			break;
		}
	}
	
	/**
	 * Change user's permission for function f
	 * @param uName user whose permission will change
	 * @param f the application function
	 * @param val new permission 
	 */
	public void setUserACLEntry(String uName, String f, boolean val) throws LoginException {
		if(!acl.containsKey(uName))
			throw new LoginException(uName + " does not exist in ACL");
		//if it exists, access that
		HashMap<String, Boolean> userTable = acl.get(uName);
		if(!userTable.containsKey(f))
			throw new LoginException(f + " does not exist in user table");
		
		//change the permission
		userTable.put(f, val);
	}
	
	public boolean canUserAccessFunction(String userName, String functionName) {
		//make sure our ACL has a default entry and fail deny if default entry does not exist 
		if(!acl.containsKey("default"))
			return false;
		HashMap<String, Boolean> userTable = acl.get("default");
		
		//get user's table if it is in acl. otherwise, use default
		if(acl.containsKey(userName))
			userTable = acl.get(userName);
		
		//is permission for function in table? if so return that permission
		if(userTable.containsKey(functionName))
			return userTable.get(functionName);

		//otherwise return false (permission does not exist)
		return false;
	}
}
