package kr.co.inslab.codealley.invoiceninja.client.model;

import java.util.Date;
import java.util.List;

import mesosphere.marathon.client.utils.ModelUtils;

import com.google.gson.annotations.SerializedName;

public class ClientResponse {

	@SerializedName("public_id") private long publicId;
	@SerializedName("user_id") private long userId;
	@SerializedName("account_id") private long accountId;
	private String name;
	@SerializedName("id_number") private String idNumber;
	@SerializedName("vat_number") private String vatNumber;
	@SerializedName("work_phone") private String workPhone;
	private String address1;
	private String address2;
	private String city;
	private String state;
	@SerializedName("postal_code") private String postalCode;
	@SerializedName("country_id") private int countryId;
	private String website;
	@SerializedName("updated_at") private Date updatedAt;
	@SerializedName("created_at") private Date createdAt;
	
	private List<ContactResponse> contacts;

	public long getPublicId() {
		return publicId;
	}

	public void setPublicId(long publicId) {
		this.publicId = publicId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<ContactResponse> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactResponse> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
