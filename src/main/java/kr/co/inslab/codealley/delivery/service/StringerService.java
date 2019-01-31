package kr.co.inslab.codealley.delivery.service;

import kr.co.inslab.codealley.delivery.model.DeliveryProperty;
import kr.co.inslab.codealley.stringer.client.Stringer;
import kr.co.inslab.codealley.stringer.client.StringerClient;
import kr.co.inslab.codealley.stringer.client.model.Record;
import kr.co.inslab.codealley.stringer.client.model.StringerResponse;
import kr.co.inslab.codealley.stringer.client.model.Tool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Stringer service를 제어해서 DNS record와 proxy 설정을 관리하기 위한 서비스 클래스
 */
@Service
public class StringerService {

	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private DeliveryProperty property;
	private Stringer stringer;
	private String domain;
	private String cname;
	private String address;
	private String appsHost;
	private String signpostHost;
	private int signpostPort;

	/**
	 * 설정파일에 기록된 정보로 초기 설정하기 위한 함수
	 */
	@PostConstruct
	public void init() {
		this.stringer = StringerClient.getInstance(property.getStringerEndPoint());
		this.domain = property.getDomain();
		this.cname = property.getCname();
		this.appsHost = property.getAppsHost();
		this.signpostHost = property.getSignpostHost();
		this.signpostPort = property.getSignpostPort();
		this.address = property.getAddress();
	}

	/**
	 * DNS record를 추가하는 함수
	 * @param record 추가할 record의 sub domain
	 * @return boolean 추가 여부
	 */
	// zone과 type, content 다양하게 설정하기 이전까지만 사용
	public boolean addRecord(String record) {
		//return addRecord(domain, record, "CNAME", cname);
		return addRecord(domain, record, "A", address);
	}

	/**
	 * DNS record를 삭제하는 함수
	 * @param record 삭제할 record의 sub domain
	 * @return boolean 삭제 여부
	 */
	public boolean deleteRecord(String record) {
		return deleteRecord(domain, record);
	}

	/**
	 * DNS record에 대한 proxy 설정을 추가하는 함수
	 * @param record Proxy 설정을 추가할 record의 sub domain
	 * @param toolName Proxy 설정을 추가할 application 종류
	 * @param port 연결할 port
	 * @return boolean 추가 여부
	 */
	public boolean addProxy(String record, String toolName, int port) {
		return addProxy(domain, record, toolName, appsHost, port);
	}

	/**
	 * Proxy 설정을 제거하는 함수
	 * @param record Proxy 설정을 제거할 record의 sub domain
	 * @param toolName Proxy 설정을 제거할 application 종류
	 * @return boolean 삭제 여부
	 */
	public boolean deleteProxy(String record, String toolName) {
		return deleteProxy(domain, record, toolName);
	}

	/**
	 * 사용자 dashboard에 대한 proxy 설정을 추가하는 함수
	 * @param record Proxy 설정을 추가할 sub domain
	 * @return boolean 추가 여부
	 */
	public boolean addDashboard(String record) {
		return addDashboard(domain, record, signpostHost, signpostPort);
	}
	// 임시 메소드 끝

	private boolean addRecord(String zone, String record, String type, String content) {
		logger.info("request to add record. zone:" + zone + " record:" + record + " type:" + type + " content:" + content);
		StringerResponse response = stringer.addRecord(zone, record, new Record(type, content));
		return response.getResult();
	}

	private boolean deleteRecord(String zone, String record) {
		logger.info("request to delete record. zone:" + zone + " record:" + record);
		StringerResponse response = stringer.deleteRecord(zone, record);
		return response.getResult();
	}

	// 한 record에 같은 tool이 여러개인 경우에 대해 처리되지 않음
	private boolean addProxy(String zone, String record, String toolName, String host, int port) {
		logger.info(String.format("request to add proxy setting. zone:%s record:%s tool:%s host:%s port:%d",
									zone, record, toolName, host, port));
		StringerResponse response = stringer.addProxy(zone, record, toolName, new Tool(host, port));
		return response.getResult();
	}
	
	// 한 record에 같은 tool이 여러개인 경우에 대해 처리되지 않음
	private boolean deleteProxy(String zone, String record, String toolName) {
		logger.info(String.format("request to delete proxy setting. zone:%s record:%s tool:%s",
									zone, record, toolName));
		StringerResponse response = stringer.deleteProxy(zone, record, toolName);
		return response.getResult();
	}

	private boolean addDashboard(String zone, String record, String host, int port) {
		logger.info(String.format("request to add dashboard setting. zone:%s record:%s host:%s port:%d",
				zone, record, host, port));
		StringerResponse response = stringer.addDashboard(zone, record, new Tool(host, port));
		return response.getResult();
	}

	/*
	public boolean isExistRecord(String zone, String record) {
		logger.info(String.format("request to get record. zone:%s record:%s", zone, record));
		StringerResponse response = stringer.getRecord(zone, record);
		return response.getResult();
	}
	
	public boolean isExistProxy(String zone, String record, String toolName) {
		logger.info(String.format("request to get a proxy setting. zone:%s record:%s tool:%s", zone, record, toolName));
		StringerResponse response = stringer.getProxy(zone, record, toolName);
		return response.getResult();
	}
	*/
}
