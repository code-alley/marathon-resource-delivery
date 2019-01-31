package kr.co.inslab.codealley.invoiceninja.client.model;

import java.util.Date;

import mesosphere.marathon.client.utils.ModelUtils;

import com.google.gson.annotations.SerializedName;

public class InvoiceData {

	@SerializedName("client_id") private long clientId;
	@SerializedName("user_id") private long userId;
	@SerializedName("account_id") private long accountId;
	@SerializedName("invoice_status_id") private int invoiceStatusId;
	@SerializedName("created_at") private Date createdAt;
	@SerializedName("updated_at") private Date updatedAt;
	@SerializedName("invoice_number") private String invoiceNumber;
	@SerializedName("invoice_date") private String invoiceDate;
	@SerializedName("is_deleted") private int isDeleted;
	private String amount;
	private String balance;
	private long id;

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
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

	public int getInvoiceStatusId() {
		return invoiceStatusId;
	}

	public void setInvoiceStatusId(int invoiceStatusId) {
		this.invoiceStatusId = invoiceStatusId;
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

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
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
