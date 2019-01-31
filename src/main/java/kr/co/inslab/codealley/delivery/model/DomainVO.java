package kr.co.inslab.codealley.delivery.model;

import kr.co.inslab.codealley.delivery.util.ModelUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DB의 domains table과 매핑되는 모델 클래스
 */
@Entity
@Table(name = "domains")
public class DomainVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "user_id", table = "user_info")
	private String userId;
	private String domain;
	private Timestamp createdAt;

	@Column(name="user_id")
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Id
	@Column(name = "domain")
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name="created_at")
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
