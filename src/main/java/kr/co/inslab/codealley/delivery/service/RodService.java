package kr.co.inslab.codealley.delivery.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import kr.co.inslab.codealley.delivery.model.DeliveryProperty;
import kr.co.inslab.codealley.delivery.model.VirtualMachineVO;
import kr.co.inslab.codealley.rod.client.Rod;
import kr.co.inslab.codealley.rod.client.RodClient;
import kr.co.inslab.codealley.rod.client.model.EndPointResponse;
import kr.co.inslab.codealley.rod.client.model.RodResponse;
import kr.co.inslab.codealley.rod.client.model.VirtualMachine;
import kr.co.inslab.codealley.rod.client.model.VirtualMachineStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Rod service를 제어해서 VM 관리를 하기 위한 클래스
 */
@Service
public class RodService {

	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private DeliveryProperty property;
	@Autowired
	private DataService dataService;
	private Rod rod;
	private String endpoint;
	private String storageName;
	private String storageKey;

	/**
	 * 설정파일에 기록된 설정 정보로 초기 설정하는 함수
	 */
	@PostConstruct
	public void init() {
		this.endpoint = property.getRodEndPoint();
		this.rod = RodClient.getInstance(endpoint);
		this.storageName = property.getStorage();
		this.storageKey = property.getStorageKey();
		
		Unirest.setTimeouts(10000, 300000);
	}

	/**
	 * VM 정보를 가져오는 함수
	 * @param userId VM 사용자의 이름
	 * @return String VM 정보
	 */
	public String getVirtualMachineInfo(String userId) {
		List<VirtualMachineVO> vmList = dataService.selectVirtualMachines(userId);
		for(VirtualMachineVO vm : vmList) {
			logger.info("Check status of vm:" + vm.getVmName());
			VirtualMachineStatus s = rod.getVmStatus(vm.getVmName());
			logger.info("Check endpoints of vm:" + vm.getVmName());
			EndPointResponse r = rod.getEndPoints(vm.getVmName());
			vm.setStatus(s.getStatus());
			vm.setEndPoints(r.getEndPoints());
		}
		
		return vmList.toString();
	}

	/**
	 * VM을 추가하는 함수
	 * @param name VM 이름
	 * @param vm VM 생성에 필요한 정보
	 * @return boolean 생성 여부
	 * @throws UnirestException
	 */
	public boolean addVirtualMachine(String name, VirtualMachine vm) throws UnirestException {
		boolean created = false;
		
		if(vm != null) {
			vm.setStorageName(storageName);
			vm.setStorageAccessKey(storageKey);
			
			logger.info("Try to create vm:" + name + " data:" + vm.toString());
			//RodResponse response = rod.createVm(name, vm);
			RodResponse response = requestCreateVm(name, vm);
			logger.info("Rod response:" + response.toString());
			created = response.getStatusCode() == 200;
		}
		
		return created;
	}
	
	private RodResponse requestCreateVm(String name, VirtualMachine vm) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.post(this.endpoint + "/azure/vm/" + name)
				.body(vm.toString()).asJson();
		JSONObject object = response.getBody().getObject();
		int status = object.has("statusCode") ? object.getInt("statusCode") : 0;
		String error = object.has("error") ? object.getString("error") : "";
		
		return new RodResponse(status, error);
	}

	/**
	 * VM을 삭제하는 함수
	 * @param name 삭제할 VM의 이름
	 * @return boolean 삭제 여부
	 */
	public boolean deleteVirtualMachine(String name) {
		logger.info("Try to delete vm:" + name);
		RodResponse response = rod.deleteVm(name);
		logger.info("Rod response:" + response.toString());
		return response.getStatusCode() == 200;
	}
}
