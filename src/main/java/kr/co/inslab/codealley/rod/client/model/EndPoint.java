package kr.co.inslab.codealley.rod.client.model;

import mesosphere.marathon.client.utils.ModelUtils;

public class EndPoint {

	private String name;
	private int port;
	private int localPort;
	private String protocol;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
