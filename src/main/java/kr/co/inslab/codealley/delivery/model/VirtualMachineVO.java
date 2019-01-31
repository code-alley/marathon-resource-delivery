package kr.co.inslab.codealley.delivery.model;

import kr.co.inslab.codealley.rod.client.model.EndPoint;
import mesosphere.marathon.client.utils.ModelUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * DB의 conf_vm table과 매핑되는 모델 클래스
 */
@Entity
@Table(name = "conf_vm")
public class VirtualMachineVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private long id;
	private String vmName;
	@ManyToOne
	@JoinColumn(name = "user_id", table = "user_info")
	private String userId;
	private String osType;
	private String osImage;
	private String size;
	private String vnet;
	private String subnet;
	private String adminId;
	private String adminPassword;
	
	private String status;				// DB에는 존재하지 않는 멤버
	private List<EndPoint> endPoints;	// DB에는 존재하지 않는 멤버

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "vm_name")
	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "os_type")
	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	@Column(name = "os_image")
	public String getOsImage() {
		return osImage;
	}

	public void setOsImage(String osImage) {
		this.osImage = osImage;
	}

	@Column(name = "size")
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Column(name = "vnet")
	public String getVnet() {
		return vnet;
	}

	public void setVnet(String vnet) {
		this.vnet = vnet;
	}

	@Column(name = "subnet")
	public String getSubnet() {
		return subnet;
	}

	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}

	@Column(name = "admin_id")
	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	@Column(name = "admin_password")
	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	@Transient
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Transient
	public List<EndPoint> getEndPoints() {
		return endPoints;
	}

	public void setEndPoints(List<EndPoint> endPoints) {
		this.endPoints = endPoints;
	}
	
	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
