package kr.co.inslab.codealley.delivery.app.ide;

import kr.co.inslab.codealley.delivery.app.MarathonApp;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.Port;
import mesosphere.marathon.client.model.v2.Volume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 생성할 Codebox의 설정 정보를 관리하는 클래스
 */
public class Codebox extends MarathonApp {

	private String hostPath;
	
	public Codebox(String appId, String username, String password, String hostPath,
				   Double cores, Integer mem) {
		super(appId, cores, mem);
		Map<String, String> env = new HashMap<String, String>();
		env.put("CODEBOX_USER", username);
		env.put("CODEBOX_PASS", password);
		this.setEnv(env);
		this.hostPath = hostPath;
		this.setHealthChecks(makeDefaultHealthCheck("/codebox"));
	}
	
	@Override
	public Container makeContainer() {
		Docker docker = new Docker();
		docker.setImage("inslab/codebox");
		docker.setNetwork("BRIDGE");
		docker.setPortMappings(Arrays.asList(new Port(80)));
		
		Volume volume = new Volume();
		volume.setHostPath(hostPath + "/data");
		volume.setContainerPath("/opt/codebox_data");
		volume.setMode("RW");
		
		Container container = new Container();
		container.setType("DOCKER");
		container.setVolumes(Arrays.asList(volume));
		container.setDocker(docker);
		
		return container;
	}

}
