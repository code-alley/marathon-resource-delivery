package kr.co.inslab.codealley.invoiceninja.client.utils;

/**
 * Invoice Ninja의 REST API 호출시 예외를 정의한 클래스
 */
public class InvoiceNinjaException extends Exception {

	private static final long serialVersionUID = 1L;
	private int status;
	private String message;
	
	public InvoiceNinjaException(int status, String message) {
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
