package kr.co.inslab.codealley.rod.client.model;

import mesosphere.marathon.client.utils.ModelUtils;

public class VirtualMachineStatus {

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
