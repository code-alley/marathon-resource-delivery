package kr.co.inslab.codealley.delivery.service;

import kr.co.inslab.codealley.delivery.app.build.Jenkins;
import kr.co.inslab.codealley.delivery.app.build.JenkinsNode;
import kr.co.inslab.codealley.delivery.app.codereview.ReviewBoard;
import kr.co.inslab.codealley.delivery.app.dbms.MySql;
import kr.co.inslab.codealley.delivery.app.documentation.Xwiki;
import kr.co.inslab.codealley.delivery.app.ide.Codebox;
import kr.co.inslab.codealley.delivery.app.management.Redmine;
import kr.co.inslab.codealley.delivery.app.scm.Gitblit;
import kr.co.inslab.codealley.delivery.app.scm.Gitlab;
import kr.co.inslab.codealley.delivery.app.staticanalysis.Sonarqube;
import kr.co.inslab.codealley.delivery.app.testmanagement.TestLink;
import kr.co.inslab.codealley.delivery.model.DeliveryProperty;
import mesosphere.marathon.client.Marathon;
import mesosphere.marathon.client.MarathonClient;
import mesosphere.marathon.client.model.v2.*;
import mesosphere.marathon.client.utils.MarathonException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Marathon을 제어해서 application 관리를 하기 위한 서비스 클래스
 */
@Service
public class MarathonService {

	private Log logger = LogFactory.getLog(MarathonService.class);
	
	@Autowired
	private DeliveryProperty property;
	private Marathon marathon;
	private final String APPID_MYSQL = "mysql";
	private final String APPID_GITBLIT = "gitblit";
	private final String APPID_GITLAB = "gitlab";
	private final String APPID_REVIEWBOARD = "reviewboard";
	private final String APPID_JENKINS = "jenkins";
	private final String APPID_JENKINS_NODE = "jenkinsnode";
	private final String APPID_SONARQUBE = "sonarqube";
	private final String APPID_REDMINE = "redmine";
	private final String APPID_XWIKI = "xwiki";
	private final String APPID_TESTLINK = "testlink";
	private final String APPID_CODEBOX = "codebox";

	/**
	 * 설정파일에 기록된 설정들을 불러와 초기 설정하는 함수
	 */
	@PostConstruct
	public void init() {
		String endpoint = getLeader(property.getMarathons());
		String auth = property.getMarathonAuth();
		final byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
		
		//marathon = MarathonClient.getInstance(endpoint);
		marathon = MarathonClient.getInstance(endpoint,
					new HashMap<String, String>() {{ put("Authorization", "Basic " + new String(encodedAuth)); }});
		
		String callbackUrl = property.getCallback();
		Subscription subscription = marathon.getCallbacks();
		if(!subscription.getCallbackUrls().contains(callbackUrl)) {
			logger.info("Register callback: " + callbackUrl);
			marathon.registerCallback(callbackUrl);
		}
	}

	/**
	 * Marathon leader를 찾는 함수
	 * @param instances Marathon이 실행중인 instance list
	 * @return String Marathon leader의 endpoint
	 */
	public String getLeader(List<String> instances) {
		String leader = null;
		String auth = property.getMarathonAuth();
		final byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
		
		if(instances != null) {
			for(String instance : instances) {
				//Marathon m = MarathonClient.getInstance(instance);
				Marathon m = MarathonClient.getInstance(instance, new HashMap<String, String>() {
									{ put("Authorization", "Basic " + new String(encodedAuth)); }
								});
				GetServerInfoResponse res = m.getServerInfo();
				String l = res.getLeader();
				if(l != null) {
					leader = "http://" + l;
					// (for demo) dev에 있는 mesos, marathon 변경 후 제거할 것
					/*
					String[] toks = l.split(":");
					if(!toks[0].endsWith(".cloudapp.net")) {
						leader = "http://" + toks[0] + ".cloudapp.net:" + toks[1];
					} else {
						leader = l;
					}
					*/
					
					logger.info("Mesos leader:" + leader);
					break;
				}
			}
		}
		
		return leader;
	}

	/**
	 * Marathon에 그룹을 생성하는 함수
	 * @param groupId 생성할 그룹 이름
	 * @return boolean 생성 여부
	 * @throws MarathonException
	 */
	public boolean createGroup(String groupId) throws MarathonException {
		boolean created = false;
		
		Group group = new Group();
		group.setId(groupId);
		
		Result result = marathon.createGroup(group);
		if(result.getVersion() != null && !result.getVersion().isEmpty()) {
			created = true;
		}
		
		return created;
	}

	/**
	 * 그룹을 삭제하는 함수
	 * @param groupId 삭제할 그룹 이름
	 * @return boolean 삭제 여부
	 * @throws MarathonException
	 */
	public boolean deleteGroup(String groupId) throws MarathonException {
		boolean deleted = false;

		Result result = marathon.deleteGroup(groupId);
		//deleteDeployments(groupId); // group 제거 후 deployment 제거하는 구문 (version up으로 필요없을 것으로 보임)
		
		if(result.getVersion() != null && !result.getVersion().isEmpty()) {
			deleted = true;
		}
		
		return deleted;
	}

	/*
	public String getGroup(String groupId) {
		String group = null;
		try {
			group = marathon.getGroup(groupId).toString();
		} catch (MarathonException e) {
			if(e.getStatus() == 404) {
				group = "{}";
				logger.error(e.getMessage());
			} else {
				logger.error(e.getMessage(), e);
			}
		}
		
		return group;
	}
	*/

	/**
	 * Application이 running 상태인지 확인하는 함수
	 * @param app 상태를 확인할 application instance
	 * @return boolean 상태가 RUNNING인지 여부
	 */
	public boolean isRunning(App app) {
		return app.getTasksRunning() >= 1;
	}
	
	public Integer getServicePort(App app, Integer containerPort) {
		Integer servicePort = 0;
		Container container = app.getContainer();
		if(container != null) {
			Docker docker = container.getDocker();
			if(docker != null) {
				Collection<Port> ports = docker.getPortMappings();
				for(Port port : ports) {
					//Integer tmpPort = port.getContainerPort();
					if(port.getContainerPort().equals(containerPort)) {
						servicePort = port.getServicePort();
						break;
					}
				}
			}
		}
		
		return servicePort;
	}

	/**
	 * 그룹에 속한 application list를 가져오는 함수
	 * @param groupId 그룹 이름
	 * @return Collection Application list
	 */
	public Collection<App> getApplications(String groupId) {
		Collection<App> apps = new ArrayList<App>();
		try {
			apps = marathon.getGroup(groupId).getApps();
		} catch (MarathonException e) {
			if(e.getStatus() == 404) {
				logger.error(e.getMessage());
			} else {
				logger.error(e.getMessage(), e);
			}
		}
		
		return apps;
	}

	/**
	 * Application의 정보를 가져오는 함수
	 * @param appId Marathon에서의 app id
	 * @return App Application의 정보
	 */
	public App getApplication(String appId) {
		App app = null;
		try {
			app = marathon.getApp(appId).getApp();
		} catch (MarathonException e) {
			if(e.getStatus() == 404) {
				logger.error(e.getMessage());
			} else {
				logger.error(e.getMessage(), e);
			}
		}
		
		return app;
	}

	/**
	 * Application을 restart 하는 함수
	 * @param appId Restart 할 application의 id
	 * @return boolean Restart 여부
	 * @throws MarathonException
	 */
	public boolean restartApp(String appId) throws MarathonException {
		boolean restarted = false;
		
		Result result = marathon.restartApp(appId);

		String version = result.getVersion();
		if(version != null && !version.isEmpty()) {
			restarted = true;
		}
		
		return restarted;
	}

	/**
	 * Application을 제거하는 함수
	 * @param appId 제거할 application의 id
	 * @return boolean 삭제 여부
	 * @throws MarathonException
	 */
	public boolean deleteApplication(String appId) throws MarathonException {
		boolean deleted = false;
		
		Result result = marathon.deleteApp(appId);
		String version = result.getVersion();
		if(version != null && !version.isEmpty()) {
			deleted = true;
		}
		
		return deleted;
	}

	/**
	 * MySql을 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name MySql 이름
	 * @param username 사용자 이름
	 * @param password 패스워드
	 * @param hostPath 파일들이 저장될 경로
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createMySql(String groupId, String name, String username, String password,
							  String hostPath, Double cores, Integer memory) throws MarathonException {
		String result = null;
		MySql mysql = new MySql("/" + groupId + "/" + APPID_MYSQL + "-" + name,
								username, password, hostPath, cores, memory);
		App app = marathon.createApp(mysql.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Gitblit을 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name Gitblit 이름
	 * @param hostPath 파일이 저장될 경로
	 * @param cores 할당될 cpu
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createGitblit(String groupId, String name, String hostPath,
								Double cores, Integer memory) throws MarathonException {
		String result = null;
		Gitblit gitblit = new Gitblit("/" + groupId + "/" + APPID_GITBLIT + "-" + name,
										groupId, hostPath, cores, memory,
										property.getLdapHost(), property.getLdapPort());
		App app = marathon.createApp(gitblit.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Gitlab을 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name Gitlab 이름
	 * @param hostPath 파일들이 저장될 경로
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createGitlab(String groupId, String name, String hostPath,
							   Double cores, Integer memory) throws MarathonException {
		String result = null;
		Gitlab gitlab = new Gitlab("/" + groupId + "/" + APPID_GITLAB + "-" + name,
									groupId, hostPath, cores, memory,
									property.getLdapHost(), property.getLdapPort());
		App app = marathon.createApp(gitlab.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Reviewboard를 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name Reviewboard 이름
	 * @param hostPath 파일들이 저장될 경로
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createReviewBoard(String groupId, String name, String hostPath,
									Double cores, Integer memory) throws MarathonException {
		String result = null;
		ReviewBoard reviewBoard = new ReviewBoard("/" + groupId + "/" + APPID_REVIEWBOARD + "-" + name,
													groupId, hostPath, cores, memory,
													property.getLdapHost(), property.getLdapPort());
		App app = marathon.createApp(reviewBoard.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Jenkins를 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name Jenkins 이름
	 * @param hostPath 파일들이 저장될 경로
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createJenkins(String groupId, String name, String hostPath,
								Double cores, Integer memory) throws MarathonException {
		String result = null;
		Jenkins jenkins = new Jenkins("/" + groupId + "/" + APPID_JENKINS + "-" + name,
										groupId, hostPath, cores, memory,
										property.getLdapHost(), property.getLdapPort());
		App app = marathon.createApp(jenkins.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Jenkins node를 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name Jenkins node 이름
	 * @param scriptUrl Script url
	 * @param scriptFile Script 파일명
	 * @param sshId SSH 사용자 ID
	 * @param sshPassword SSH 패스워드
	 * @param hostPath 파일들이 저장될 경로
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createJenkinsNode(String groupId, String name, String scriptUrl, String scriptFile,
			String sshId, String sshPassword, String hostPath, Double cores, Integer memory) throws MarathonException {
		String result = null;
		JenkinsNode jenkinsNode = new JenkinsNode("/" + groupId + "/" + APPID_JENKINS_NODE + "-" + name,
				scriptUrl, scriptFile, sshId, sshPassword, hostPath, cores, memory);
		App app = marathon.createApp(jenkinsNode.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Sonarqube를 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name Sonarqube 이름
	 * @param hostPath 파일들이 저장될 경로
	 * @param username DB 사용자 ID
	 * @param password DB 패스워드
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createSonarqube(String groupId, String name, String hostPath, String username,
								  String password, Double cores, Integer memory) throws MarathonException {
		String result = null;
		Sonarqube sonarqube = new Sonarqube("/" + groupId + "/" + APPID_SONARQUBE + "-" + name,
											groupId, hostPath, username, password, cores, memory,
											property.getLdapHost(), property.getLdapPort());
		App app = marathon.createApp(sonarqube.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Redmine을 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name Redmine 이름
	 * @param hostPath 파일들이 저장될 경로
	 * @param username DB 사용자 ID
	 * @param password DB 패스워드
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createRedmine(String groupId, String name, String hostPath, String username,
								String password, Double cores, Integer memory) throws MarathonException {
		String result = null;
		Redmine redmine = new Redmine("/" + groupId + "/" + APPID_REDMINE + "-" + name,
										groupId, hostPath, username, password, cores, memory,
										property.getLdapHost(), property.getLdapPort());
		App app = marathon.createApp(redmine.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Xwiki를 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name Xwiki 이름
	 * @param hostPath 파일들이 저장될 경로
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createXwiki(String groupId, String name, String hostPath,
							  Double cores, Integer memory) throws MarathonException {
		String result = null;
		Xwiki xwiki = new Xwiki("/" + groupId + "/" + APPID_XWIKI + "-" + name,
								groupId, hostPath, cores, memory,
								property.getLdapHost(), property.getLdapPort());
		App app = marathon.createApp(xwiki.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Testlink를 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name Testlink 이름
	 * @param password DB 패스워드
	 * @param hostPath 파일들이 저장될 경로
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createTestLink(String groupId, String name, String password, String hostPath,
								 Double cores, Integer memory) throws MarathonException {
		String result = null;
		TestLink testlink = new TestLink("/" + groupId + "/" + APPID_TESTLINK + "-" + name,
											groupId, password, hostPath, cores, memory,
											property.getLdapHost(), property.getLdapPort());
		App app = marathon.createApp(testlink.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Codebox를 생성하는 함수
	 * @param groupId 그룹 이름
	 * @param name Codebox 이름
	 * @param username 사용자 이름
	 * @param password 사용자 패스워드
	 * @param hostPath 파일들이 저장될 경로
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @return String 생성된 정보
	 * @throws MarathonException
	 */
	public String createCodebox(String groupId, String name, String username, String password,
								String hostPath, Double cores, Integer memory) throws MarathonException {
		String result = null;
		Codebox codebox = new Codebox("/" + groupId + "/" + APPID_CODEBOX + "-" + name,
										username, password, hostPath, cores, memory);
		App app = marathon.createApp(codebox.makeApp());
		if(app != null) {
			String id = app.getId();
			if(id != null && !id.isEmpty()) {
				result = app.toString();
			}
		}
		
		return result;
	}

	/**
	 * Marathon에 callback url을 등록하는 함수
	 * @param callbackUrl 등록할 callback url
	 * @return String 등록된 결과
	 */
	public String registerCallback(String callbackUrl) {
		String result = null;
		SubscriptionResponse response = marathon.registerCallback(callbackUrl);
		if(response != null) {
			result = response.toString();
		}
		
		return result;
	}

	/**
	 * Marathon에 등록된 callback url을 제거하는 함수
	 * @param callbackUrl 제거할 callback url
	 * @return String 제거 결과
	 */
	public String unregisterCallback(String callbackUrl) {
		String result = null;
		SubscriptionResponse response = marathon.unregisterCallback(callbackUrl);
		if(response != null) {
			result = response.toString();
		}
		
		return result;
	}

	/**
	 * Marathon에 등록되어 있는 callback url을 가져오는 함수
	 * @return String 등록된 callback url 정보
	 */
	public String getCallbacks() {
		String result = null;
		Subscription callbacks = marathon.getCallbacks();
		if(callbacks != null) {
			result = callbacks.toString();
		}
		
		return result;
	}
}
