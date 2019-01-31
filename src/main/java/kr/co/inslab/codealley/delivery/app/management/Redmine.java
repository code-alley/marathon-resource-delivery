package kr.co.inslab.codealley.delivery.app.management;

import kr.co.inslab.codealley.delivery.app.MarathonApp;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.Port;
import mesosphere.marathon.client.model.v2.Volume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 생성할 Redmine의 설정 정보를 관리하는 클래스
 */
public class Redmine extends MarathonApp {

	private String hostPath;
	
	public Redmine(String appId, String groupId, String hostPath, String username, String password,
				   Double cores, Integer mem, String ldapHost, int ldapPort) {
		super(appId, cores, mem);
		Map<String, String> env = new HashMap<String, String>();
		env.put("DB_USER", username);
		env.put("DB_PASS", password);
		env.put("LDAP_HOST", ldapHost);
		env.put("LDAP_PORT", String.valueOf(ldapPort));
		env.put("LDAP_BASE_DN", makeBaseDN(groupId));
		this.setEnv(env);
		this.hostPath = hostPath;
		this.setHealthChecks(makeDefaultHealthCheck("HTTP", "/redmine",
				0, 10, 60, 2, 15, false));
	}

	@Override
	public Container makeContainer() {
		Docker docker = new Docker();
		docker.setImage("inslab/redmine");
		docker.setNetwork("BRIDGE");
		docker.setPortMappings(Arrays.asList(new Port(80)));
		
		Volume volume = new Volume();
		volume.setHostPath(hostPath + "/conf");
		volume.setContainerPath("/root/redmine/data");
		volume.setMode("RW");
		
		Volume dataDir = new Volume();
		dataDir.setHostPath(hostPath + "/data");
		dataDir.setContainerPath("/var/lib/mysql");
		dataDir.setMode("RW");
		
		Container container = new Container();
		container.setType("DOCKER");
		container.setVolumes(Arrays.asList(volume, dataDir));
		container.setDocker(docker);
		
		return container;
	}
}
