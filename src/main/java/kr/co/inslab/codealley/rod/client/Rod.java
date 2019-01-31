package kr.co.inslab.codealley.rod.client;


import javax.inject.Named;

import kr.co.inslab.codealley.rod.client.model.EndPointResponse;
import kr.co.inslab.codealley.rod.client.model.RodResponse;
import kr.co.inslab.codealley.rod.client.model.VirtualMachineStatus;
import feign.RequestLine;

/**
 * Rod service의 REST API 호출을 위한 interface
 */
public interface Rod {

	@RequestLine("GET /azure/vm/status/{name}")
	VirtualMachineStatus getVmStatus(@Named("name") String name);

	@RequestLine("GET /azure/vm/{name}/endpoint")
	EndPointResponse getEndPoints(@Named("name") String name);
	
	//@RequestLine("POST /azure/vm/{name}")
	//RodResponse createVm(@Named("name") String name, VirtualMachine vm);
	
	@RequestLine("DELETE /azure/vm/{name}")
	RodResponse deleteVm(@Named("name") String name);
}
