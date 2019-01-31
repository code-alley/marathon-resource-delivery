package kr.co.inslab.codealley.invoiceninja.client.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class ContactResponse extends Contact {

	@SerializedName("public_id") private long publicId;
	@SerializedName("account_id") private long accountId;
	@SerializedName("user_id") private long userId;
	@SerializedName("client_id") private long clientId;
	@SerializedName("created_at") private Date createdAt;
	@SerializedName("updated_at") private Date updatedAt;
	@SerializedName("deleted_at") private Date deletedAt;

	public long getPublicId() {
		return publicId;
	}

	public void setPublicId(long publicId) {
		this.publicId = publicId;
	}

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
}
