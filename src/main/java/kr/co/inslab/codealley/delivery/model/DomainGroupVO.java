package kr.co.inslab.codealley.delivery.model;

import kr.co.inslab.codealley.delivery.util.ModelUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DB의 domain_groups table과 매핑되는 모델 클래스
 */
@Entity
@Table(name = "domain_groups")
public class DomainGroupVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	@ManyToOne
	@JoinColumn(name = "user_id", table = "user_info")
	private String userid;
	@ManyToOne
	@JoinColumn(name = "domain", table = "domains")
	private String domain;
	private String role;
	private int permCreate;
	private int permRead;
	private int permUpdate;
	private int permDelete;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "user_id")
	public String getUserId() {
		return this.userid;
	} 
	public void setUserId(String userid) {
		this.userid = userid;
	}
	
	@Column(name = "domain")
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "role")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "perm_create")
	public int getPermCreate() {
		return permCreate;
	}

	public void setPermCreate(int permCreate) {
		this.permCreate = permCreate;
	}

	@Column(name = "perm_read")
	public int getPermRead() {
		return permRead;
	}

	public void setPermRead(int permRead) {
		this.permRead = permRead;
	}

	@Column(name = "perm_update")
	public int getPermUpdate() {
		return permUpdate;
	}

	public void setPermUpdate(int permUpdate) {
		this.permUpdate = permUpdate;
	}

	@Column(name = "perm_delete")
	public int getPermDelete() {
		return permDelete;
	}

	public void setPermDelete(int permDelete) {
		this.permDelete = permDelete;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
