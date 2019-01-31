package kr.co.inslab.codealley.stringer.client.model;

import mesosphere.marathon.client.utils.ModelUtils;

public class Tool {

	private String host;
	private int port;

	public Tool(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
