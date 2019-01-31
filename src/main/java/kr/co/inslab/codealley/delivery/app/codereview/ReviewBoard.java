package kr.co.inslab.codealley.delivery.app.codereview;

import kr.co.inslab.codealley.delivery.app.MarathonApp;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.Port;
import mesosphere.marathon.client.model.v2.Volume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 생성할 Reviewboard의 설정 정보를 관리하는 클래스
 */
public class ReviewBoard extends MarathonApp {

	private String hostPath;
	
	public ReviewBoard(String appId, String groupId, String hostPath, Double cores, Integer mem, String ldapHost, int ldapPort) {
		super(appId, cores, mem);
		Map<String, String> env = new HashMap<String, String>();
		env.put("LDAP_HOST", ldapHost);
		env.put("LDAP_PORT", String.valueOf(ldapPort));
		env.put("LDAP_BASE_DN", makeBaseDN(groupId));
		this.setEnv(env);
		this.hostPath = hostPath;
		this.setHealthChecks(makeDefaultHealthCheck("/reviewboard"));
	}
	
	@Override
	public Container makeContainer() {
		Docker docker = new Docker();
		docker.setImage("inslab/reviewboard");
		docker.setNetwork("BRIDGE");
		docker.setPortMappings(Arrays.asList(new Port(80), new Port(22)));
		
		Volume volume = new Volume();
		volume.setHostPath(hostPath + "/data");
		volume.setContainerPath("/srv/reviews.local/data");
		volume.setMode("RW");
		
		Container container = new Container();
		container.setType("DOCKER");
		container.setVolumes(Arrays.asList(volume));
		container.setDocker(docker);
		
		return container;
	}

}
