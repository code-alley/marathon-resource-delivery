package kr.co.inslab.codealley.delivery.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.inslab.codealley.delivery.model.DomainVO;
import kr.co.inslab.codealley.delivery.model.PortField;
import kr.co.inslab.codealley.delivery.service.*;
import kr.co.inslab.codealley.delivery.util.ModelUtils;
import kr.co.inslab.codealley.delivery.util.Response;
import mesosphere.marathon.client.model.v2.*;
import mesosphere.marathon.client.utils.MarathonException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * REST API endpoint 정의한 클래스
 */
@RequestMapping("/v1")
@Controller
public class MainController {

	private Log logger = LogFactory.getLog(MainController.class);
	@Autowired
	private MarathonService service;
	@Autowired
	private DataService dataService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private StringerService stringerService;
	@Autowired
	private RodService rodService;
	@Autowired
	private InvoiceNinjaService invoiceService;
	private final String STATUS_UPDATE_EVENT = "status_update_event";
	private final String UNHEALTHY_EVENT = "failed_health_check_event";
	private final String HEALTHY_CHANGED_EVENT = "health_status_changed_event";

	/**
	 * Marathon callback event 등록하는 API
	 * @param callbackUrl Event 발생시 호출될 callback url
	 * @return String 등록된 callback 정보
	 */
	@RequestMapping(value = "/events", method = RequestMethod.POST)
	@ResponseBody
	public String registerEvent(@RequestParam("callback") String callbackUrl) {
		logger.info("Register event requested.");
		return service.registerCallback(callbackUrl);
	}

	/**
	 * Marathon callback event를 해제하는 API
	 * @param callbackUrl 해제할 callback url
	 * @return String 해제된 callback 정보
	 */
	@RequestMapping(value = "/events", method = RequestMethod.DELETE)
	@ResponseBody
	public String unregisterEvent(@RequestParam("callback") String callbackUrl) {
		logger.info("Unregister event requested.");
		return service.unregisterCallback(callbackUrl);
	}

	/**
	 * 현재 Marathon에 등록된 event callback 정보를 얻어오는 API
	 * @return String 현재 등록된 callback 정보
	 */
	@RequestMapping(value = "/events", method = RequestMethod.GET)
	@ResponseBody
	public String getEvent() {
		logger.info("Get event requested.");
		return service.getCallbacks();
	}

	/**
	 * Event 발생시 호출될 callback API
	 * @param body Event에 대한 정보
	 * @return String Event 처리한 결과
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	@ResponseBody
	public String callback(@RequestBody String body) {
		logger.info("Event callback.");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Event ev = gson.fromJson(body, Event.class);
		logger.info("##### Event scrap start #####");
		logger.info(ModelUtils.toString(ev));
		logger.info("##### Event scrap end ####");

		String groupId = null;
		String appKind = null;
		String appName = null;
		String appId = ev.getAppId();

		if(appId != null && !appId.isEmpty()) {
			if (appId.startsWith("/")) appId = appId.substring(1, appId.length());
			String[] toks = appId.split("/");
			if (toks.length == 2) {
				groupId = toks[0];
				int firstDash = toks[1].indexOf("-");
				appKind = toks[1].substring(0, firstDash);
				appName = toks[1].substring(firstDash + 1);
			}
		}

		if(ev.getEventType().equals(STATUS_UPDATE_EVENT)) {
			logger.info("status update event");
			StatusUpdateEvent event = gson.fromJson(body, StatusUpdateEvent.class);
			logger.info("StatusUpdateEvent:\r\n");
			logger.info(event.toString());

			if(groupId != null && appKind != null && appName != null) {
				// mysql은 proxy 설정할 필요가 없으므로 callback 종료
				if (appKind.equals("mysql")) return new Response(true).toString();

				// 이미 running 중인데도 running에 대한 status update event가 발생하는 경우가 있음 (임시 조치)
				String dbStatus = dataService.selectApplicationStatus(groupId, appKind, appName);
				logger.info(String.format("DB status of app %s : %s", appName, dbStatus));

				// scaling 하는 경우 proxy 설정할 필요 없음
				App app = service.getApplication(appId);
				if (app != null) {
					logger.info(String.format("Instances of app %s : %d", appId, app.getInstances()));
				}

				// TASK_RUNNING, TASK_FAILED, TASK_KILLED
				switch (event.getTaskStatus()) {
					// TASK_RUNNING (DB에 STAGING 상태인 경우만 proxy 설정 추가)
					case "TASK_RUNNING":
						if (dbStatus == null || dbStatus.equals("STAGING")) {
							int entities = dataService.updateStatus(groupId, appKind, appName, "RUNNING");
							if (entities == 0 || (app != null && app.getInstances() > 1)) break;
							int httpPort = checkServicePort(groupId, appKind, appName);
							// slave id update는 구조변경 후 작업
							//dataService.updateSlaveId(groupId, appKind, appName, event.getHost());
							if (httpPort != 0) stringerService.addProxy(groupId, appKind, httpPort);
						}
						break;
					// TASK_FAILED (DB에 RUNNING인 경우에만 proxy 제거)
					case "TASK_FAILED":
						if (dbStatus == null || dbStatus.equals("RUNNING")) {
							int entities = dataService.updateStatus(groupId, appKind, appName, "STAGING");
							if (entities == 0 || (app != null && app.getInstances() >= 1)) break;
							stringerService.deleteProxy(groupId, appKind);
						}
						break;
					// TASK_KILLED (DB에 data가 없어도 proxy 제거)
					case "TASK_KILLED":
						if (app == null || app.getInstances() < 1) {
							stringerService.deleteProxy(groupId, appKind);
						}
						break;
					default:
				}
			}
		} else if(ev.getEventType().equals(UNHEALTHY_EVENT)) {
			logger.info("##### unhealthy event #####");
			FailedHealthCheckEvent event = gson.fromJson(body, FailedHealthCheckEvent.class);
			logger.info("FailedHealthCheckEvent:\r\n");
			logger.info(event.toString());
			if(groupId != null && appKind != null && appName != null) dataService.updateHealthy(groupId, appKind, appName, 0);
		} else if(ev.getEventType().equals(HEALTHY_CHANGED_EVENT)) {
			logger.info("##### healthy changed event #####");
			HealthStatusChangedEvent event = gson.fromJson(body, HealthStatusChangedEvent.class);
			logger.info("HealthStatusChangedEvent:\r\n");
			logger.info(event.toString());
			if(groupId != null && appKind != null && appName != null) dataService.updateHealthy(groupId, appKind, appName, event.isAlive() ? 1 : 0);
		} else {
			logger.info("other event. skipped.");
		}
		
		return new Response(true).toString();
	}

	/**
	 * Sub domain을 생성하고 Marathon에 그룹을 생성하는 API
	 * @param groupId 생성할 그룹명
	 * @param userId 생성하는 유저
	 * @return String 처리 결과
	 */
	@RequestMapping(value = "/group/{groupId}", method = RequestMethod.POST)
	@ResponseBody
	public String createGroup(@PathVariable("groupId") String groupId, @RequestParam("userId") String userId) {
		logger.info("Create group requested.");
		groupId = groupId.toLowerCase();
		boolean created = false;
		
		try {
			created = transactionService.createGroup(groupId, userId);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		
		if(created) {
			return new Response(true).toString();
		} else {
			return new Response(false).toString();
		}
	}

	/**
	 * Sub domain을 삭제하고 Marathon에서 그룹을 제거하는 API
	 * @param groupId 제거할 그룹명
	 * @return String 처리 결과
	 */
	@RequestMapping(value = "/group/{groupId}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteGroup(@PathVariable("groupId") String groupId) {
		logger.info("Delete group requested. Group ID:" + groupId);
		groupId = groupId.toLowerCase();
		boolean deleted = false;
		
		try {
			deleted = transactionService.deleteGroup(groupId);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		
		if(deleted) {
			return new Response(true).toString();
		} else {
			return new Response(false).toString();
		}
	}

	/**
	 * Sub domain(그룹) 정보를 가져오는 API
	 * @param groupId 가져올 그룹명
	 * @return String 처리 결과와 그룹 정보
	 */
	@RequestMapping(value = "/group/{groupId}", method = RequestMethod.GET)
	@ResponseBody
	public String getGroup(@PathVariable("groupId") String groupId) {
		logger.info("Get group requested.");
		groupId = groupId.toLowerCase();
		DomainVO vo = dataService.selectDomain(groupId);
		String domain = vo == null ? null : vo.toString();
		if(domain == null) {
			return new Response(false).toString();
		}
		
		return new Response("domain", domain).toString();
	}

	/**
	 * Application 생성하는 API
	 * @param groupId 그룹명
	 * @param appId Application 종류
	 * @param name Application 이름
	 * @param params 기타 생성에 필요한 parameters
	 * @return String 생성 결과 및 정보
	 */
	@RequestMapping(value = "/app/{groupId}/{appId}/{name}", method = RequestMethod.POST)
	@ResponseBody
	public String createApplication(@PathVariable("groupId") String groupId, @PathVariable("appId") String appId,
									@PathVariable("name") String name, @RequestParam Map<String, String> params) {
		logger.info("Create application requested.");
		groupId = groupId.toLowerCase();
		appId = appId.toLowerCase();
		name = name.toLowerCase();
		Double cores = 0.0;
		Integer memory = 0;
		
		if(name.isEmpty()) return new Response(false).toString();
		try {
			cores = Double.parseDouble(params.get("cores"));
			memory = Integer.parseInt(params.get("memory"));
			logger.info(String.format("Input cores:%s memory:%s", cores, memory));
			if(cores <= 0 || memory <= 0) {
				return new Response(false).toString();
			}
		} catch(Exception e) {
			logger.error(String.format("Invalid parameters. cores:%s memory:%s",
							params.get("cores"), params.get("memory")));
			return new Response(false).toString();
		}

		String response = null;
		try {
			response = transactionService.createApplication(groupId, appId, name, cores, memory, params);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		
		if(response != null) {
			return new Response("request_app", response).toString();
		} else {
			return new Response(false).toString();
		}
	}

	/**
	 * Application을 제거하는 API
	 * @param groupId 그룹 이름
	 * @param appId Application 종류
	 * @param name Application 이름
	 * @return String 삭제 결과
	 */
	@RequestMapping(value = "/app/{groupId}/{appId}/{name}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteApplication(@PathVariable("groupId") String groupId, @PathVariable("appId") String appId, @PathVariable("name") String name) {
		logger.info("Delete application requested.");
		groupId = groupId.toLowerCase();
		appId = appId.toLowerCase();
		name = name.toLowerCase();
		
		boolean deleted = false;
		try {
			deleted = transactionService.deleteApplication(groupId, appId, name);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		
		if(deleted) {
			return new Response(true).toString();
		} else {
			return new Response(false).toString();
		}
	}

	/**
	 * 설치된 모든 application 정보를 가져오는 API
	 * @param groupId 그룹 이름
	 * @return String Application 정보
	 */
	@RequestMapping(value = "/app/{groupId}", method = RequestMethod.GET)
	@ResponseBody
	public String getAllApplications(@PathVariable("groupId") String groupId) {
		logger.info("Get all applications requested.");
		groupId = groupId.toLowerCase();
		
		Collection<App> apps = service.getApplications(groupId);
		for(App app : apps) {
			String marathonId = app.getId();
			if(marathonId.startsWith("/")) marathonId = marathonId.substring(1, marathonId.length());
			String[] toks = marathonId.split("/");
			if(toks.length == 2) {
				int firstDash = toks[1].indexOf("-");
				String appKind = toks[1].substring(0, firstDash);
				String appName = toks[1].substring(firstDash + 1);
				checkServicePort(groupId, appKind, appName);
				checkStatus(groupId, appKind, appName);
			}
		}
		
		String applications = dataService.selectAllApplicationsInfo(groupId);
		if(applications == null) {
			return new Response(false).toString();
		}
		
		return new Response("apps", applications).toString();
	}

	/**
	 * Application 정보를 가져오는 API
	 * @param groupId 그룹 이름
	 * @param appId Application 종류
	 * @param name Application 이름
	 * @return String Application 정보
	 */
	@RequestMapping(value = "/app/{groupId}/{appId}/{name}", method = RequestMethod.GET)
	@ResponseBody
	public String getApplication(@PathVariable("groupId") String groupId, @PathVariable("appId") String appId, @PathVariable("name") String name) {
		logger.info("Get an application requested.");
		groupId = groupId.toLowerCase();
		appId = appId.toLowerCase();
		name = name.toLowerCase();
		
		checkServicePort(groupId, appId, name);
		checkStatus(groupId, appId, name);
		String application = dataService.selectApplication(groupId, appId, name);
		if(application == null) {
			return new Response(false).toString();
		}
		
		return new Response("app", application).toString();
	}

	/**
	 * Application에 필요한 token 값을 갱신하는 API
	 * @param groupId 그룹 이름
	 * @param appId Application 종류
	 * @param name Application 이름
	 * @param token 토큰값
	 * @return String 갱신 결과
	 */
	@RequestMapping(value = "/app/{groupId}/{appId}/{name}/{token}", method = RequestMethod.POST)
	@ResponseBody
	public String updateApplicationToken(@PathVariable("groupId") String groupId, @PathVariable("appId") String appId,
			@PathVariable("name") String name, @PathVariable("token") String token) {
		logger.info("Update applincation token requested.");
		groupId = groupId.toLowerCase();
		appId = appId.toLowerCase();
		name = name.toLowerCase();

		int entities = dataService.updateToken(groupId, appId, name, token);

		if (entities != 0) {
			return new Response(true).toString();
		} else {
			return new Response(false).toString();
		}
	}

	/**
	 * Application을 restart 하는 API
	 * @param groupId 그룹 이름
	 * @param appId Application 종류
	 * @param name Application 이름
	 * @return String Restart 결과
	 */
	@RequestMapping(value = "/app/restart/{groupId}/{appId}/{name}", method = RequestMethod.POST)
	@ResponseBody
	public String restartApp(@PathVariable("groupId") String groupId, @PathVariable("appId") String appId, @PathVariable("name") String name) {
		logger.info("App restart requested.");
		groupId = groupId.toLowerCase();
		appId = appId.toLowerCase();
		name = name.toLowerCase();
		boolean restarted = false;
		
		try {
			restarted = service.restartApp("/" + groupId + "/" + appId + "-" + name);
		} catch (MarathonException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
		
		return new Response(restarted).toString();
	}

	/**
	 * VM 정보를 가져오는 API
	 * @param userId 사용자 ID
	 * @return String VM 정보
	 */
	@RequestMapping(value = "/vms", method = RequestMethod.GET)
	@ResponseBody
	public String getVirtualMachines(@RequestParam("userId") String userId) {
		logger.info("Getting virtual machines requested.");
		String vms = rodService.getVirtualMachineInfo(userId);
		logger.info("Getting virtual machines completed.");
		if(vms == null) {
			return new Response(false).toString();
		}
		
		return new Response("vms", vms).toString();
	}

	/**
	 * VM을 생성하는 API
	 * @param name VM 이름
	 * @param body VM에 생성에 필요한 정보
	 * @return String 생성 결과
	 */
	@RequestMapping(value = "/vms/{name}", method = RequestMethod.POST)
	@ResponseBody
	public String createVirtualMachine(@PathVariable("name") String name, @RequestBody String body) {
		logger.info("Creating virtual machine requested.");

		boolean created = false;
		try {
			created = transactionService.createVirtualMachineTransaction(name, body);
			logger.info("Creating virtual machine completed.");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		
		return new Response(created).toString();
	}

	/**
	 * VM을 제거하는 API
	 * @param name 제거할 VM의 이름
	 * @return String 삭제 결과
	 */
	@RequestMapping(value = "/vms/{name}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteVirtualMachine(@PathVariable("name") String name) {
		logger.info("Deleting virtual machine requested.");
		
		boolean deleted = false;
		try {
			deleted = transactionService.deleteVirtualMachine(name);
			logger.info("Deleting virtual machine completed.");
		} catch(SQLException e) {
			logger.error(e.getMessage(), e);
		}
		
		return new Response(deleted).toString();
	}

	/**
	 * Invoice client 정보를 가져오는 API
	 * @return String Invoice clients 정보
	 */
	@RequestMapping(value = "/invoice/clients", method = RequestMethod.GET)
	@ResponseBody
	public String getInvoiceClients() {
		logger.info("Getting invoice clients requested.");
		
		String clients = invoiceService.getClients();
		logger.info("Getting invoice clients completed.");
		if(clients != null) {
			return new Response("clients", clients).toString();
		} else {
			return new Response(false).toString();
		}
	}

	/**
	 * Invoice client를 생성하는 API
	 * @param body 생성에 필요한 정보
	 * @return String 생성 결과
	 */
	@RequestMapping(value = "/invoice/clients", method = RequestMethod.POST)
	@ResponseBody
	public String createInvoiceClient(@RequestBody String body) {
		logger.info("Creating client requested.");
		
		Response response = new Response(false);
		try {
			String created = invoiceService.createClient(body);
			response = new Response("client", created);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new Response(false, e.getMessage());
		}
		
		logger.info("Creating client completed.");
		
		return response.toString();
	}

	/**
	 * Invoice 정보를 가져오는 API
	 * @return String Invoice 정보
	 */
	@RequestMapping(value = "/invoice/invoices", method = RequestMethod.GET)
	@ResponseBody
	public String getInvoices() {
		logger.info("Getting invoices requested.");
		
		String invoices = invoiceService.getInvoices();
		logger.info("Getting invoices completed.");
		if(invoices != null) {
			return new Response("invoices", invoices).toString();
		} else {
			return new Response(false).toString();
		}
	}

	/**
	 * Invoice를 생성하는 API
	 * @param body Invoice 생성에 필요한 정보
	 * @return String 생성 결과
	 */
	@RequestMapping(value = "/invoice/invoices", method = RequestMethod.POST)
	@ResponseBody
	public String createInvoice(@RequestBody String body) {
		logger.info("Creating invoice requested.");

		Response response = new Response(false);
		try {
			String created = invoiceService.createInvoice(body);
			response = new Response("invoice", created);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new Response(false, e.getMessage());
		}
		
		logger.info("Creating invoice completed.");
		
		return response.toString();
	}

	/**
	 * Marathon에 생성된 application의 상태 정보를 확인하고 갱신하는 함수
	 * @param groupId 그룹 이름
	 * @param appId Application 종류
	 * @param name Application 이름
	 * @return String Application을 상태 (STAGING / RUNNING)
	 */
	private String checkStatus(String groupId, String appId, String name) {
		String status = "STAGING";
		App app = service.getApplication("/" + groupId + "/" + appId + "-" + name);
		if(app != null) {
			if(service.isRunning(app))	status = "RUNNING";
			dataService.updateStatus(groupId, appId, name, status);
		}
		
		return status;
	}

	/**
	 * Marathon에 올라가 있는 application의 service port를 확ㅇ니하고 갱신하는 함수
	 * @param groupId 그룹 이름
	 * @param appId Application 종류
	 * @param name Application 이름
	 * @return int HTTP port가 service port로 등록되어 있는 경우 port 번호
	 */
	private int checkServicePort(String groupId, String appId, String name) {
		int httpPort = 0;
		App app = service.getApplication("/" + groupId + "/" + appId + "-" + name);
		if(app == null) return httpPort;
		
		PortField portField = new PortField();
		Map<String, Integer> port = null;
		switch(appId) {
		case "mysql":
			port = portField.getMysqlPortMap();
			break;
		case "jenkins":
			port = portField.getJenkinsPortMap();
			break;
		case "jenkinsnode":
			port = portField.getJenkinsNodePortMap();
			break;
		case "simplewebapp":
			port = portField.getWebAppPortMap();
			break;
		case "gitblit":
			port = portField.getGitblitPortMap();
			break;
		case "gitlab":
			port = portField.getGitlabPortMap();
			break;
		case "redmine":
			port = portField.getRedminePortMap();
			break;
		case "xwiki":
			port = portField.getXwikiPortMap();
			break;
		case "testlink":
			port = portField.getTestlinkPortMap();
			break;
		case "sonarqube":
			port = portField.getSonarQubePortMap();
			break;
		case "reviewboard":
			port = portField.getReviewBoardPortMap();
			break;
		case "codebox":
			port = portField.getCodeboxPortMap();
		default:
		}
		
		if(port != null) {
			Iterator<String> it = port.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				Integer servicePort = service.getServicePort(app, port.get(key));
				port.put(key, servicePort);
			}
			
			dataService.updatePort(groupId, appId, name, port);
			if(port.containsKey("http_port")) httpPort = port.get("http_port");
		}
		
		return httpPort;
	}
}