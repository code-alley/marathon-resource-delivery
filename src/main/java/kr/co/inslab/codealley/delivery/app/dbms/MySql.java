package kr.co.inslab.codealley.delivery.app.dbms;

import kr.co.inslab.codealley.delivery.app.MarathonApp;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.Port;
import mesosphere.marathon.client.model.v2.Volume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 생성할 MySQL의 설정 정보를 관리하는 클래스
 */
public class MySql extends MarathonApp {

	private String hostPath;
	
	public MySql(String appId, String username, String password, String hostPath, Double cores, Integer mem) {
		super(appId, cores, mem);
		Map<String, String> env = new HashMap<String, String>();
		env.put("MYSQL_USER", username);
		env.put("MYSQL_PASS", password);
		this.setEnv(env);
		this.hostPath = hostPath;
	}
	
	@Override
	public Container makeContainer() {
		Docker docker = new Docker();
		docker.setImage("inslab/mysql");
		docker.setNetwork("BRIDGE");
		docker.setPortMappings(Arrays.asList(new Port(3306)));

		// configuration volume 지정 필요 (나중에)
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
