package kr.co.inslab.codealley.stringer.client.model;

import mesosphere.marathon.client.utils.ModelUtils;

public class RecordInfo {

	private int id;
	private int domain_id;
	private String name;
	private String type;
	private String content;
	private int ttl;
	private int prio;
	private int change_date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(int domain_id) {
		this.domain_id = domain_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public int getPrio() {
		return prio;
	}

	public void setPrio(int prio) {
		this.prio = prio;
	}

	public int getChange_date() {
		return change_date;
	}

	public void setChange_date(int change_date) {
		this.change_date = change_date;
	}
	
	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
