package kr.co.inslab.codealley.invoiceninja.client.model;

import java.util.Date;
import java.util.List;

import mesosphere.marathon.client.utils.ModelUtils;

import com.google.gson.annotations.SerializedName;

public class InvoiceResponse {

	@SerializedName("public_id") private int publicId;
	@SerializedName("user_id") private long userId;
	@SerializedName("account_id") private long accountId;
	@SerializedName("client_id") private long clientId;
	@SerializedName("invoice_number") private String invoiceNumber;
	@SerializedName("invoice_date") private String invoiceDate;
	private long balance;
	private long amount;
	@SerializedName("updated_at") private Date updatedAt;
	@SerializedName("created_at") private Date createdAt;
	@SerializedName("invoice_items") private List<ItemResponse> items;
	private String link;

	public int getPublicId() {
		return publicId;
	}

	public void setPublicId(int publicId) {
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

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
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

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
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

	public List<ItemResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemResponse> items) {
		this.items = items;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
