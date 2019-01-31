package kr.co.inslab.codealley.rod.client.model;

import java.util.List;

import mesosphere.marathon.client.utils.ModelUtils;

public class VirtualMachine {

	private String operatingSystem;
	private String osImage;
	private String roleSize;
	private String virtualNetwork;
	private String subnet;
	private String userName;
	private String userPassword;
	private List<EndPoint> endPoints;
	private String storageName;
	private String storageAccessKey;

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getOsImage() {
		return osImage;
	}

	public void setOsImage(String osImage) {
		this.osImage = osImage;
	}

	public String getRoleSize() {
		return roleSize;
	}

	public void setRoleSize(String roleSize) {
		this.roleSize = roleSize;
	}

	public String getVirtualNetwork() {
		return virtualNetwork;
	}

	public void setVirtualNetwork(String virtualNetwork) {
		this.virtualNetwork = virtualNetwork;
	}

	public String getSubnet() {
		return subnet;
	}

	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public List<EndPoint> getEndPoints() {
		return endPoints;
	}

	public void setEndPoints(List<EndPoint> endPoints) {
		this.endPoints = endPoints;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getStorageAccessKey() {
		return storageAccessKey;
	}

	public void setStorageAccessKey(String storageAccessKey) {
		this.storageAccessKey = storageAccessKey;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
