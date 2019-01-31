package kr.co.inslab.codealley.delivery.app.staticanalysis;

import kr.co.inslab.codealley.delivery.app.MarathonApp;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.Port;
import mesosphere.marathon.client.model.v2.Volume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 생성할 Sonarqube의 설정 정보를 관리하는 클래스
 */
public class Sonarqube extends MarathonApp {

	private String hostPath;
	
	public Sonarqube(String appId, String groupId, String hostPath, String username, String password,
					 Double cores, Integer mem, String ldapHost, int ldapPort) {
		super(appId, cores, mem);
		Map<String, String> env = new HashMap<String, String>();
		env.put("LDAP_HOST", ldapHost);
		env.put("LDAP_PORT", String.valueOf(ldapPort));
		env.put("LDAP_BASE_DN", makeBaseDN(groupId));
		env.put("DB_USER", username);
		env.put("DB_PASS", password);
		this.setEnv(env);
		this.hostPath = hostPath;
		this.setHealthChecks(makeDefaultHealthCheck("/sonarqube"));
	}

	@Override
	public Container makeContainer() {
		Docker docker = new Docker();
		docker.setImage("inslab/sonarqube");
		docker.setNetwork("BRIDGE");
		docker.setPortMappings(Arrays.asList(new Port(9000), new Port(3306)));
		
		Volume confDir = new Volume();
		confDir.setHostPath(hostPath + "/conf");
		confDir.setContainerPath("/opt/sonar/conf");
		confDir.setMode("RW");

		Volume extensionsDir = new Volume();
		extensionsDir.setHostPath(hostPath + "/conf/extensions");
		extensionsDir.setContainerPath("/opt/sonar/extensions");
		extensionsDir.setMode("RW");

		Volume logDir = new Volume();
		logDir.setHostPath(hostPath + "/logs");
		logDir.setContainerPath("/opt/sonar/logs");
		logDir.setMode("RW");
		
		Volume dataDir = new Volume();
		dataDir.setHostPath(hostPath + "/data");
		dataDir.setContainerPath("/var/lib/mysql");
		dataDir.setMode("RW");
		
		Container container = new Container();
		container.setType("DOCKER");
		container.setVolumes(Arrays.asList(confDir, extensionsDir, logDir, dataDir));
		container.setDocker(docker);
		
		return container;
	}
}
