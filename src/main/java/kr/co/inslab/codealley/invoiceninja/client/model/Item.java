package kr.co.inslab.codealley.invoiceninja.client.model;

import mesosphere.marathon.client.utils.ModelUtils;

import com.google.gson.annotations.SerializedName;

public class Item {

	@SerializedName("product_key") private String product;
	@SerializedName("notes") private String description;
	private long cost;
	private int qty;

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCost() {
		return cost;
	}

	public void setCost(long cost) {
		this.cost = cost;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
