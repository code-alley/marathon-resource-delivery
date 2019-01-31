package kr.co.inslab.codealley.delivery.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * application.properties에 설정된 설정값들을 관리하기 위한 모델 클래스
 */
@Component
public class DeliveryProperty {

	private List<String> marathons = new ArrayList<String>();
	private String stringerEndPoint;
	private String domain;
	private String cname;
	private String callback;	// 구조변경하면서 제거할 수 있으면 제거
	private String appsHost;	// 구조변경하면서 제거할 수 있으면 제거
	private String rodEndPoint;			// rod service endpoint
	private String storage;		// rod service azure storage name
	private String storageKey;	// rod service azure storage key
	private String invoiceEndPoint;	// invoice ninja endpoint
	private String invoiceToken;	// invoice ninja token
	private String signpostHost;
	private int signpostPort;
	private String address;
	private String marathonAuth;
	private String ldapHost;
	private int ldapPort;
	
	@Autowired
	public DeliveryProperty(@Value("${marathon.instances}") String marathonInstances,
			@Value("${stringer.endpoint}") String stringer,
			@Value("${stringer.domain}") String domain,
			@Value("${stringer.cname.content}") String cname,
			@Value("${stringer.address.content}") String address,
			@Value("${marathon.callback}") String callback,
			@Value("${stringer.apps.host}") String appsHost,
			@Value("${rod.endpoint}") String rod,
			@Value("${rod.storageName}") String storage,
			@Value("${rod.storageAccessKey}") String storageKey,
			@Value("${invoice.endpoint}") String invoiceEndPoint,
			@Value("${invoice.token}") String invoiceToken,
			@Value("${signpost.host}") String signpostHost,
			@Value("${signpost.port}") String signpostPort,
			@Value("${marathon_basic_auth}") String marathonAuth,
			@Value("${ldap.host}") String ldapHost,
			@Value("${ldap.port}") String ldapPort) {
		if(marathonInstances != null) {
			String[] tokens = marathonInstances.split(",");
			for(int i = 0; i < tokens.length; i++) {
				marathons.add("http://" + tokens[i]);
			}
		}
		
		this.stringerEndPoint = stringer;
		this.domain = domain;
		this.cname = cname;
		this.callback = callback;
		this.appsHost = appsHost;
		
		this.rodEndPoint = rod;
		this.storage = storage;
		this.storageKey = storageKey;
		
		this.invoiceEndPoint = invoiceEndPoint;
		this.invoiceToken = invoiceToken;
		this.signpostHost = signpostHost;
		this.signpostPort = Integer.parseInt(signpostPort);
		this.address = address;
		this.marathonAuth = marathonAuth;
		this.ldapHost = ldapHost;
		this.ldapPort = Integer.parseInt(ldapPort);
	}
	
	public List<String> getMarathons() {
		return marathons;
	}
	
	public String getStringerEndPoint() {
		return stringerEndPoint;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public String getCname() {
		return cname;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getCallback() {
		return callback;
	}
	
	public String getAppsHost() {
		return appsHost;
	}

	public String getRodEndPoint() {
		return rodEndPoint;
	}

	public String getStorage() {
		return storage;
	}

	public String getStorageKey() {
		return storageKey;
	}

	public String getInvoiceEndPoint() {
		return invoiceEndPoint;
	}

	public void setInvoiceEndPoint(String invoiceEndPoint) {
		this.invoiceEndPoint = invoiceEndPoint;
	}

	public String getInvoiceToken() {
		return invoiceToken;
	}

	public void setInvoiceToken(String invoiceToken) {
		this.invoiceToken = invoiceToken;
	}

	public String getSignpostHost() {
		return signpostHost;
	}

	public void setSignpostHost(String signpostHost) {
		this.signpostHost = signpostHost;
	}

	public int getSignpostPort() {
		return signpostPort;
	}

	public void setSignpostPort(int signpostPort) {
		this.signpostPort = signpostPort;
	}

	public String getMarathonAuth() {
		return marathonAuth;
	}

	public void setMarathonAuth(String marathonAuth) {
		this.marathonAuth = marathonAuth;
	}

	public String getLdapHost() {
		return ldapHost;
	}

	public void setLdapHost(String ldapHost) {
		this.ldapHost = ldapHost;
	}

	public int getLdapPort() {
		return ldapPort;
	}

	public void setLdapPort(int ldapPort) {
		this.ldapPort = ldapPort;
	}
}
