package kr.co.inslab.codealley.delivery.app.testmanagement;

import kr.co.inslab.codealley.delivery.app.MarathonApp;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.Port;
import mesosphere.marathon.client.model.v2.Volume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 생성할 Testlink의 설정 정보를 관리하는 클래스
 */
public class TestLink extends MarathonApp {

	private String hostPath;
	
	public TestLink(String appId, String groupId, String password, String hostPath,
					Double cores, Integer mem, String ldapHost, int ldapPort) {
		super(appId, cores, mem);
		Map<String, String> env = new HashMap<String, String>();
		env.put("MYSQL_PASS", password);
		env.put("LDAP_HOST", ldapHost);
		env.put("LDAP_PORT", String.valueOf(ldapPort));
		env.put("LDAP_BASE_DN", makeBaseDN(groupId));
		this.setEnv(env);
		this.hostPath = hostPath;
		this.setHealthChecks(makeDefaultHealthCheck("/testlink"));
	}
	
	@Override
	public Container makeContainer() {
		Docker docker = new Docker();
		docker.setImage("inslab/testlink");
		docker.setNetwork("BRIDGE");
		docker.setPortMappings(Arrays.asList(new Port(80), new Port(3306)));

		// configuration volume 지정 필요
		Volume volume = new Volume();
		volume.setHostPath(hostPath + "/data");
		volume.setContainerPath("/var/lib/mysql");
		volume.setMode("RW");
		
		Container container = new Container();
		container.setType("DOCKER");
		container.setVolumes(Arrays.asList(volume));
		container.setDocker(docker);
		
		return container;
	}

}
