package kr.co.inslab.codealley.delivery.app.scm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kr.co.inslab.codealley.delivery.app.MarathonApp;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.Port;
import mesosphere.marathon.client.model.v2.Volume;

/**
 * 생성할 Gitlab의 설정 정보를 관리하는 클래스
 */
public class Gitlab extends MarathonApp {

	private String hostPath;
	
	public Gitlab(String appId, String groupId, String hostPath, Double cores, Integer mem, String ldapHost, int ldapPort) {
		super(appId, cores, mem);
		Map<String, String> env = new HashMap<String, String>();
		env.put("LDAP_HOST", ldapHost);
		env.put("LDAP_PORT", String.valueOf(ldapPort));
		env.put("LDAP_BASE_DN", makeBaseDN(groupId));
		this.setEnv(env);
		this.hostPath = hostPath;
		this.setHealthChecks(makeDefaultHealthCheck("HTTP", "/gitlab",
				0, 10, 60, 2, 10, false));
	}
	
	@Override
	public Container makeContainer() {
		Docker docker = new Docker();
		docker.setImage("inslab/gitlab");
		docker.setNetwork("BRIDGE");
		docker.setPortMappings(Arrays.asList(new Port(80)));
		
		Volume confDir = new Volume();
		confDir.setHostPath(hostPath + "/conf");
		confDir.setContainerPath("/etc/gitlab");
		confDir.setMode("RW");
		
		Volume dataDir = new Volume();
		dataDir.setHostPath(hostPath + "/data");
		dataDir.setContainerPath("/var/opt/gitlab");
		dataDir.setMode("RW");
		
		Container container = new Container();
		container.setType("DOCKER");
		container.setVolumes(Arrays.asList(confDir, dataDir));
		container.setDocker(docker);
		
		return container;
	}

}
