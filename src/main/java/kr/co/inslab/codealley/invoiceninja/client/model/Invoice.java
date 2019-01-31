package kr.co.inslab.codealley.invoiceninja.client.model;

import java.util.List;

import mesosphere.marathon.client.utils.ModelUtils;

import com.google.gson.annotations.SerializedName;

public class Invoice {

	@SerializedName("client_id") private long clientId;
	@SerializedName("invoice_items") private List<Item> items;

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
