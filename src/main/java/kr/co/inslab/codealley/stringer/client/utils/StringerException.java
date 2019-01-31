package kr.co.inslab.codealley.stringer.client.utils;

public class StringerException extends Exception {

	private static final long serialVersionUID = 1L;
	private int status;
	private String message;
	
	public StringerException(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}
	
	@Override
	public String getMessage() {
		return message + " (http status: " + status + ")";
	}

	@Override
	public String toString() {
		return message + " (http status: " + status + ")";
	}
}
