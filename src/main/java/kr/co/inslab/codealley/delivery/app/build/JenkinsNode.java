package kr.co.inslab.codealley.delivery.app.build;

import kr.co.inslab.codealley.delivery.app.MarathonApp;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.Port;
import mesosphere.marathon.client.model.v2.Volume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 생성할 Jenkins Node의 설정 정보를 관리하는 클래스
 */
public class JenkinsNode extends MarathonApp {

private String hostPath;
	
	public JenkinsNode(String appId, String scriptUrl, String scriptFile, String sshId, String sshPassword, String hostPath,
					   Double cores, Integer mem) {
		super(appId, cores, mem);
		Map<String, String> env = new HashMap<String, String>();
		env.put("SCRIPT_REPO_URL", scriptUrl);
		env.put("SCRIPT_FILE", scriptFile);
		env.put("USER_ID", sshId);
		env.put("USER_PASSWORD", sshPassword);
		this.setEnv(env);
		this.hostPath = hostPath;
	}
	
	@Override
	public Container makeContainer() {
		Docker docker = new Docker();
		docker.setImage("inslab/jenkins-node");
		docker.setNetwork("BRIDGE");
		
		docker.setPortMappings(Arrays.asList(new Port(22)));
		
		Volume volume = new Volume();
		volume.setHostPath(hostPath + "/data");
		volume.setContainerPath("/home");
		volume.setMode("RW");
		
		Container container = new Container();
		container.setType("DOCKER");
		container.setVolumes(Arrays.asList(volume));
		container.setDocker(docker);
		
		return container;
	}
}
