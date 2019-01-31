package kr.co.inslab.codealley.rod.client.model;

import java.util.List;

import mesosphere.marathon.client.utils.ModelUtils;

public class EndPointResponse {

	private List<EndPoint> endPoints;

	public List<EndPoint> getEndPoints() {
		return endPoints;
	}

	public void setEndPoints(List<EndPoint> endPoints) {
		this.endPoints = endPoints;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
