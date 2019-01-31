package kr.co.inslab.codealley.stringer.client.model;

import mesosphere.marathon.client.utils.ModelUtils;

public class StringerResponse {

	private boolean result;
	private String error;

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
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
