package database;

@SuppressWarnings("serial")
public class AppException extends RuntimeException {

	public AppException(Exception e) {
		super(e);
	}
	
	public AppException(String msg) {
		super(msg);
	}
}