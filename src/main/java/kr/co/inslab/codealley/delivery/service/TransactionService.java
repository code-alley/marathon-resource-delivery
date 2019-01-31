package kr.co.inslab.codealley.delivery.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.inslab.codealley.delivery.model.VirtualMachineVO;
import kr.co.inslab.codealley.rod.client.model.VirtualMachine;
import kr.co.inslab.codealley.stringer.client.utils.StringerException;
import mesosphere.marathon.client.utils.MarathonException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Map;

/**
 * 여러 서비스들을 호출해서 특정 기능들을 수행할 수 있도록 구현한 서비스 클래스
 */
@Service
public class TransactionService {

	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private DataService dataService;
	@Autowired
	private MarathonService marathonService;
	@Autowired
	private StringerService stringerService;
	@Autowired
	private RodService rodService;

	/**
	 * 그룹을 생성하는 함수. DB와 Marathon에 해당 그룹을 추가하고 DNS record와 proxy 설정을 추가.
	 * @param groupId 생성할 그룹 이름
	 * @param userId 생성하는 사용자 ID
	 * @return boolean 생성 여부
	 * @throws SQLException
	 */
	@Transactional(rollbackFor = {SQLException.class, MarathonException.class}, propagation = Propagation.REQUIRES_NEW)
	public boolean createGroup(String groupId, String userId) throws SQLException {
		boolean created = false;
		
		if(!stringerService.addRecord(groupId) || !stringerService.addDashboard(groupId)) {
			throw new SQLException(new StringerException(500, "Add dns record failed."));
		}
		
		try {
			dataService.insertDomain(groupId, userId);
			dataService.insertDomainGroup(groupId, userId);
			marathonService.createGroup(groupId);
			created = true;
		} catch(Exception e) {
			stringerService.deleteRecord(groupId);
			throw new SQLException(e);
		}
		
		return created;
	}

	/**
	 * 그룹을 제거하기 위한 함수. DNS record와 proxy 설정, DB와 Marathon에 생성된 내용을 삭제
	 * @param groupId 삭제할 그룹 이름
	 * @return boolean 삭제 여부
	 * @throws SQLException
	 */
	@Transactional(rollbackFor = {SQLException.class, MarathonException.class}, propagation = Propagation.REQUIRES_NEW)
	public boolean deleteGroup(String groupId) throws SQLException {
		boolean deleted = false;
		
		if(!stringerService.deleteRecord(groupId)) {
			throw new SQLException(new StringerException(500, "Delete dns record failed."));
		}
		
		try {
			dataService.deleteDomain(groupId);
			marathonService.deleteGroup(groupId);
			deleted = true;
		} catch(MarathonException e) {
			if(e.getStatus() == 404) {	//404인 경우 이미 삭제된 상태이므로 DB에서도 rollback할 필요가 없음
				deleted = true;
			} else {
				stringerService.addRecord(groupId);
				throw new SQLException(e);
			}
		} catch(Exception e) {
			stringerService.addRecord(groupId);
			throw new SQLException(e);
		}
		
		return deleted;
	}

	/**
	 * Application을 생성하는 함수. DB와 Marathon에 application을 추가
	 * @param groupId 그룹 이름
	 * @param appId Application 종류
	 * @param name Application 이름
	 * @param cores 할당될 core
	 * @param memory 할당될 memory
	 * @param params Application 생성에 필요한 정보
	 * @return String 생성된 application 정보
	 * @throws SQLException
	 */
	@Transactional(rollbackFor = {SQLException.class, MarathonException.class}, propagation = Propagation.REQUIRES_NEW)
	public String createApplication(String groupId, String appId, String name, Double cores, Integer memory,
									Map<String, String> params) throws SQLException {
		logger.info("Create application requested.");

		String response = null;
		try {
			String dataPath = String.format("/opt/data/%s/%s/%s", groupId, appId, name);
			String description = params.get("description");
			dataService.insertApplication(groupId, appId, name, dataPath, description, cores, memory, params);
		
			switch (appId) {
			case "mysql":
				response = marathonService.createMySql(groupId, name, params.get("username"),
						params.get("password"), dataPath, cores, memory);
				break;
			case "jenkins":
				response = marathonService.createJenkins(groupId, name, dataPath, cores, memory);
				break;
			case "jenkinsnode":
				response = marathonService.createJenkinsNode(groupId, name,
							params.get("scripturl"), params.get("scriptfile"),
							params.get("sshid"), params.get("sshpassword"), dataPath,
							cores, memory);
				break;
			case "gitblit":
				response = marathonService.createGitblit(groupId, name, dataPath, cores, memory);
				break;
			case "gitlab":
				response = marathonService.createGitlab(groupId, name, dataPath, cores, memory);
				break;
			case "redmine":
				response = marathonService.createRedmine(groupId, name, dataPath, 
						params.get("username"), params.get("password"), cores, memory);
				break;
			case "xwiki":
				response = marathonService.createXwiki(groupId, name, dataPath, cores, memory);
				break;
			case "testlink":
				response = marathonService.createTestLink(groupId, name,
							params.get("password"), dataPath, cores, memory);
				break;
			case "sonarqube":
				response = marathonService.createSonarqube(groupId, name, dataPath,
						params.get("username"), params.get("password"), cores, memory);
				break;
			case "reviewboard":
				response = marathonService.createReviewBoard(groupId, name, dataPath, cores, memory);
				break;
			case "codebox":
				response = marathonService.createCodebox(groupId, name,
							params.get("username"), params.get("password"), dataPath, cores, memory);
			default:
			}
		} catch(Exception e) {
			throw new SQLException(e);
		}

		if(response == null) {
			throw new SQLException();
		}
		
		return response;
	}

	/**
	 * Application을 삭제하는 함수. DB와 Marathon에서 application을 제거하고 proxy 설정도 제거
	 * @param groupId 그룹 이름
	 * @param appId Application 종류
	 * @param name Application 이름
	 * @return boolean 삭제 여부
	 * @throws SQLException
	 */
	@Transactional(rollbackFor = {SQLException.class, MarathonException.class}, propagation = Propagation.REQUIRES_NEW)
	public boolean deleteApplication(String groupId, String appId, String name) throws SQLException {
		boolean deleted = false;
		stringerService.deleteProxy(groupId, appId);
		/*
		if(!stringerService.deleteProxy(groupId, appId)) {
			throw new SQLException(new StringerException(500, "Delete proxy configuration failed."));
		}
		*/

		try {
			dataService.deleteConfigurationData(groupId, appId, name);
			deleted = marathonService.deleteApplication("/" + groupId + "/" + appId + "-" + name);
		} catch(MarathonException e) {
			if(e.getStatus() == 404) {
				deleted = true;
			} else {
				throw new SQLException(e);
			}
		} catch(Exception e) {
			// 예외 발생시 삭제된 proxy 정보를 되돌릴 적당한 방법이 없음
			throw new SQLException(e);
		}
		
		return deleted;
	}

	/**
	 * VM을 생성하는 함수. DB에 row를 추가하고 VM 생성
	 * @param name VM 이름
	 * @param raw VM 생성에 필요한 정보
	 * @return boolean 생성 여부
	 * @throws SQLException
	 */
	@Transactional(rollbackFor = {SQLException.class}, propagation = Propagation.REQUIRES_NEW)
	public boolean createVirtualMachineTransaction(String name, String raw) throws SQLException {
		
		Gson gson = new GsonBuilder().create();
		VirtualMachineVO vo = gson.fromJson(raw, VirtualMachineVO.class);
		vo.setVmName(name);
		
		boolean created = false;
		
		try {
			dataService.insertVirtualMachine(vo);
			created = rodService.addVirtualMachine(name, makeVirtualMachine(vo));
		} catch(Exception e) {
			throw new SQLException(e);
		}
		
		if(!created && name != null && !name.isEmpty()) {
			dataService.deleteVirtualMachine(name);
		}
		
		return created;
	}
	
	private VirtualMachine makeVirtualMachine(VirtualMachineVO vo) {
		VirtualMachine vm = new VirtualMachine();
		vm.setOperatingSystem(vo.getOsType());
		vm.setOsImage(vo.getOsImage());
		vm.setRoleSize(vo.getSize());
		vm.setVirtualNetwork(vo.getVnet());
		vm.setSubnet(vo.getSubnet());
		vm.setUserName(vo.getAdminId());
		vm.setUserPassword(vo.getAdminPassword());
		vm.setEndPoints(vo.getEndPoints());
		
		return vm;
	}

	/**
	 * VM을 삭제하는 함수. DB에서 삭제하고 VM 삭제
	 * @param name 삭제할 VM 이름
	 * @return boolean 삭제 여부
	 * @throws SQLException
	 */
	@Transactional(rollbackFor = {SQLException.class}, propagation = Propagation.REQUIRES_NEW)
	public boolean deleteVirtualMachine(String name) throws SQLException {
		boolean deleted = false;
		
		try {
			dataService.deleteVirtualMachine(name);
			deleted = rodService.deleteVirtualMachine(name);
		} catch(Exception e) {
			throw new SQLException(e);
		}
		
		return deleted;
	}
}
