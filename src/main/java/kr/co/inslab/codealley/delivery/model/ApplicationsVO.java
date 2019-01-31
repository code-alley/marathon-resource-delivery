package kr.co.inslab.codealley.delivery.model;

import kr.co.inslab.codealley.delivery.util.ModelUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DB의 applications table과 매핑하기 위한 클래스
 */
@Entity
@Table(name = "applications")
public class ApplicationsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    @ManyToOne
    @JoinColumn(name = "domain", table = "domains")
    private String domain;
    private String type;
    private String name;
    @ManyToOne
    @JoinColumn(name = "slave_id", table = "slave")
    private Long slaveId;
    private Integer tcpPort;
    private Integer httpPort;
    private Integer httpsPort;
    private Integer gitPort;
    private Integer sshPort;
    private String dataPath;
    private String description;
    private String dbId;
    private String dbPassword;
    private String status;
    private int healthy;
    private String token;
    private Double cores;
    private Integer memory;
    private Timestamp createdAt;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(Long slaveId) {
        this.slaveId = slaveId;
    }

    @Column(name = "tcp_port")
    public Integer getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(Integer tcpPort) {
        this.tcpPort = tcpPort;
    }

    @Column(name = "http_port")
    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }

    @Column(name = "https_port")
    public Integer getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(Integer httpsPort) {
        this.httpsPort = httpsPort;
    }

    @Column(name = "git_port")
    public Integer getGitPort() {
        return gitPort;
    }

    public void setGitPort(Integer gitPort) {
        this.gitPort = gitPort;
    }

    @Column(name = "ssh_port")
    public Integer getSshPort() {
        return sshPort;
    }

    public void setSshPort(Integer sshPort) {
        this.sshPort = sshPort;
    }

    @Column(name = "data_path")
    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "db_id")
    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    @Column(name = "db_password")
    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "healthy")
    public int getHealthy() {
        return healthy;
    }

    public void setHealthy(int healthy) {
        this.healthy = healthy;
    }

    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "cores")
    public Double getCores() {
        return cores;
    }

    public void setCores(Double cores) {
        this.cores = cores;
    }

    @Column(name = "memory")
    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    @Column(name = "created_at")
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
