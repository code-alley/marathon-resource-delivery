package kr.co.inslab.codealley.delivery.app;

import mesosphere.marathon.client.model.v2.App;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.HealthCheck;

import java.util.*;

/**
 * Marathon으로 생성할 application에 공통으로 사용할 정보를 관리하거나 처리하는 클래스
 */
public abstract class MarathonApp {

	private String appId;
	private Integer instances;
	private Double cpus;
	private Integer mem;
	private Map<String, String> env;
	private Map<String, String> labels;
	private List<HealthCheck> healthChecks;
	//protected final String LDAP_URI = "ldap://codealley-ldap.cloudapp.net:389";
	//protected final String LDAP_HOST = "codealley-ldap.cloudapp.net";
	//protected final int LDAP_PORT = 389;

	protected String makeBaseDN(String groupId) {
		return String.format("ou=%s,ou=groups,dc=codealley,dc=co", groupId);
	}

	public MarathonApp(String appId, Double cores, Integer mem) {
		this.appId = appId;
		this.instances = 1;
		this.cpus = cores;
		this.mem = mem;
		this.labels = makeHaProxyGroupLabel();
	}
	
	private Map<String, String> makeHaProxyGroupLabel() {
		return new HashMap<String, String>() {{ put("HAPROXY_GROUP", "external"); }};
	}

	protected List<HealthCheck> makeDefaultHealthCheck(String path) {
		return makeDefaultHealthCheck("HTTP", path, 0, 10, 30, 2, 5, false);
	}

	protected List<HealthCheck> makeDefaultHealthCheck(String protocol, String path, int portIndex, int gracePeriod,
													   int interval, int timeout, int maxFailures, boolean ignoreHttp1xx) {
		HealthCheck health = new HealthCheck();
		health.setProtocol(protocol);
		health.setPath(path);
		health.setPortIndex(portIndex);
		health.setGracePeriodSeconds(gracePeriod);
		health.setIntervalSeconds(interval);
		health.setTimeoutSeconds(timeout);
		health.setMaxConsecutiveFailures(maxFailures);
		health.setIgnoreHttp1xx(ignoreHttp1xx);

		return new ArrayList<HealthCheck>(Arrays.asList(health));
	}

	/**
	 * 설정된 정보를 바탕으로 Marathon App instance를 생성하는 함수
	 * @return App Marathon App instance
	 */
	public App makeApp() {
		if(this.appId == null || this.appId.length() == 0) {
			throw new IllegalArgumentException("App ID:" + this.appId);
		}
		
		App app = new App();
		app.setId(this.appId);
		app.setInstances(this.instances);
		app.setCpus(this.cpus);
		// marathon client의 memory가 Double type으로 되어있음
		app.setMem((double)this.mem.intValue());
		app.setEnv(env);
		app.setContainer(makeContainer());
		app.setLabels(this.labels);
		app.setHealthChecks(this.healthChecks);
		return app;
	}

	/**
	 * Container instance를 생성하는 abstract 함수
	 * @return Container
	 */
	public abstract Container makeContainer();

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getInstance() {
		return instances;
	}

	public void setInstance(Integer instances) {
		this.instances = instances;
	}

	public Double getCpus() {
		return cpus;
	}

	public void setCpus(Double cpus) {
		this.cpus = cpus;
	}

	public Integer getMem() {
		return mem;
	}

	public void setMem(Integer mem) {
		this.mem = mem;
	}

	public Map<String, String> getEnv() {
		return env;
	}

	public void setEnv(Map<String, String> env) {
		this.env = env;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public List<HealthCheck> getHealthChecks() {
		return healthChecks;
	}

	public void setHealthChecks(List<HealthCheck> healthChecks) {
		this.healthChecks = healthChecks;
	}
}
