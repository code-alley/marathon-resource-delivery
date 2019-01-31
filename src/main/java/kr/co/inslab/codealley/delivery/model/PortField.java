package kr.co.inslab.codealley.delivery.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 각 application 별로 외부에서 연결이 되어야 하는 port 정보를 저장하는 모델 클래스
 */
public class PortField {

	private Map<String, Integer> mysqlPortMap;
	private Map<String, Integer> gitblitPortMap;
	private Map<String, Integer> gitlabPortMap;
	private Map<String, Integer> jenkinsPortMap;
	private Map<String, Integer> jenkinsNodePortMap;
	private Map<String, Integer> webAppPortMap;
	private Map<String, Integer> redminePortMap;
	private Map<String, Integer> xwikiPortMap;
	private Map<String, Integer> testlinkPortMap;
	private Map<String, Integer> reviewBoardPortMap;
	private Map<String, Integer> sonarQubePortMap;
	private Map<String, Integer> codeboxPortMap;
	
	public PortField() {
		mysqlPortMap = new HashMap<String, Integer>() {{ put("port", 3306); }};
		gitblitPortMap = new HashMap<String, Integer>() {{
			put("http_port", 80); put("https_port", 443); put("git_port", 9418); put("ssh_port", 29418); }};
		gitlabPortMap = new HashMap<String, Integer>() {{ put("http_port", 80); }};
		jenkinsPortMap = new HashMap<String, Integer>() {{ put("http_port", 8080); }};
		jenkinsNodePortMap = new HashMap<String, Integer>() {{ put("ssh_port", 22); }};
		webAppPortMap = new HashMap<String, Integer>() {{ put("http_port", 5000); }};
		redminePortMap = new HashMap<String, Integer>() {{ put("http_port", 80); }};
		xwikiPortMap = new HashMap<String, Integer>() {{ put("http_port", 8080); }};
		testlinkPortMap = new HashMap<String, Integer>() {{ put("http_port", 80); }};
		reviewBoardPortMap = new HashMap<String, Integer>() {{ put("http_port", 80); put("ssh_port", 22); }};
		sonarQubePortMap = new HashMap<String, Integer>() {{ put("http_port", 9000); }};
		codeboxPortMap = new HashMap<String, Integer>() {{ put("http_port", 80); }};
	}

	public Map<String, Integer> getMysqlPortMap() {
		return mysqlPortMap;
	}

	public void setMysqlPortMap(Map<String, Integer> mysqlPortMap) {
		this.mysqlPortMap = mysqlPortMap;
	}

	public Map<String, Integer> getGitblitPortMap() {
		return gitblitPortMap;
	}

	public void setGitblitPortMap(Map<String, Integer> gitblitPortMap) {
		this.gitblitPortMap = gitblitPortMap;
	}
	
	public Map<String, Integer> getGitlabPortMap() {
		return gitlabPortMap;
	}

	public void setGitlabPortMap(Map<String, Integer> gitlabPortMap) {
		this.gitlabPortMap = gitlabPortMap;
	}

	public Map<String, Integer> getJenkinsPortMap() {
		return jenkinsPortMap;
	}
	
	public void setJenkinsPortMap(Map<String, Integer> jenkinsPortMap) {
		this.jenkinsPortMap = jenkinsPortMap;
	}
	
	public Map<String, Integer> getJenkinsNodePortMap() {
		return jenkinsNodePortMap;
	}

	public void setJenkinsNodePortMap(Map<String, Integer> jenkinsNodePortMap) {
		this.jenkinsNodePortMap = jenkinsNodePortMap;
	}
	
	public Map<String, Integer> getWebAppPortMap() {
		return webAppPortMap;
	}
	
	public void setWebAppPortMap(Map<String, Integer> webAppPortMap) {
		this.webAppPortMap = webAppPortMap;
	}

	public Map<String, Integer> getRedminePortMap() {
		return redminePortMap;
	}

	public void setRedminePortMap(Map<String, Integer> redminePortMap) {
		this.redminePortMap = redminePortMap;
	}

	public Map<String, Integer> getXwikiPortMap() {
		return xwikiPortMap;
	}

	public void setXwikiPortMap(Map<String, Integer> xwikiPortMap) {
		this.xwikiPortMap = xwikiPortMap;
	}

	public Map<String, Integer> getTestlinkPortMap() {
		return testlinkPortMap;
	}

	public void setTestlinkPortMap(Map<String, Integer> testlinkPortMap) {
		this.testlinkPortMap = testlinkPortMap;
	}

	public Map<String, Integer> getReviewBoardPortMap() {
		return reviewBoardPortMap;
	}

	public void setReviewBoardPortMap(Map<String, Integer> reviewBoardPortMap) {
		this.reviewBoardPortMap = reviewBoardPortMap;
	}

	public Map<String, Integer> getSonarQubePortMap() {
		return sonarQubePortMap;
	}

	public void setSonarQubePortMap(Map<String, Integer> sonarQubePortMap) {
		this.sonarQubePortMap = sonarQubePortMap;
	}

	public Map<String, Integer> getCodeboxPortMap() {
		return codeboxPortMap;
	}

	public void setCodeboxPortMap(Map<String, Integer> codeboxPortMap) {
		this.codeboxPortMap = codeboxPortMap;
	}
}
