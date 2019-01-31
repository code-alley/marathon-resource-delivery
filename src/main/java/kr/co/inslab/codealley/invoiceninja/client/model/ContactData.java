package kr.co.inslab.codealley.invoiceninja.client.model;

import java.util.Date;

import mesosphere.marathon.client.utils.ModelUtils;

import com.google.gson.annotations.SerializedName;

public class ContactData {

	@SerializedName("account_id") private long accountId;
	@SerializedName("user_id") private long userId;
	@SerializedName("client_id") private long clientId;
	@SerializedName("created_at") private Date createdAt;
	@SerializedName("updated_at") private Date updatedAt;
	@SerializedName("deleted_at") private Date deletedAt;
	@SerializedName("is_primary") private int isPrimary;
	@SerializedName("send_invoice") private int sendInvoice;
	@SerializedName("first_name") private String firstName;
	@SerializedName("last_name") private String lastName;
	@SerializedName("email") private String email;
	@SerializedName("phone") private String phone;
	@SerializedName("last_login") private Date lastLogin;
	@SerializedName("id") private long id;

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public int isPrimary() {
		return isPrimary;
	}

	public void setPrimary(int isPrimary) {
		this.isPrimary = isPrimary;
	}

	public int getSendInvoice() {
		return sendInvoice;
	}

	public void setSendInvoice(int sendInvoice) {
		this.sendInvoice = sendInvoice;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
