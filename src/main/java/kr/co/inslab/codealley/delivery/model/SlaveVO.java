package kr.co.inslab.codealley.delivery.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import mesosphere.marathon.client.utils.ModelUtils;

/**
 * DB의 slave table과 매핑되는 모델 클래스
 */
@Entity
@Table(name = "slave")
public class SlaveVO {

	private long id;
	private String hostname;
	private String publicIp;
	private String internalIp;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "hostname")
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@Column(name = "public_ip")
	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	@Column(name = "internal_ip")
	public String getInternalIp() {
		return internalIp;
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
