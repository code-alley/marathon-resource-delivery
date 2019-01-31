package kr.co.inslab.codealley.rod.client.model;

import mesosphere.marathon.client.utils.ModelUtils;

public class RodResponse {

	private int statusCode;
	private String error;

	public RodResponse(int statusCode, String error) {
		this.statusCode = statusCode;
		this.error = error;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
