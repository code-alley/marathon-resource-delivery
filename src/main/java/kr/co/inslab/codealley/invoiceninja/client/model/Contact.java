package kr.co.inslab.codealley.invoiceninja.client.model;

import mesosphere.marathon.client.utils.ModelUtils;

import com.google.gson.annotations.SerializedName;

public class Contact {

	private String email;
	@SerializedName("first_name") private String firstName;
	@SerializedName("last_name") private String lastName;
	private String phone;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
