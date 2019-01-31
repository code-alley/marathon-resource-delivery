package kr.co.inslab.codealley.stringer.client.model;

import mesosphere.marathon.client.utils.ModelUtils;

public class Record {

	private String type;
	private String content;

	public Record(String type, String content) {
		this.type = type;
		this.content = content;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
