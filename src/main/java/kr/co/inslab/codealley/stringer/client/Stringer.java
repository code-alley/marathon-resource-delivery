package kr.co.inslab.codealley.stringer.client;

import javax.inject.Named;

import kr.co.inslab.codealley.stringer.client.model.Record;
import kr.co.inslab.codealley.stringer.client.model.StringerResponse;
import kr.co.inslab.codealley.stringer.client.model.Tool;
import feign.RequestLine;

/**
 * Stringer service의 REST API 호출을 위한 interface
 */
public interface Stringer {

	@RequestLine("GET /v1/zone/{zoneName:.+}/record/{id}")
	StringerResponse getRecord(@Named("zoneName:.+") String zone, @Named("id") String id);
	
	@RequestLine("POST /v1/zone/{zoneName:.+}/record/{id}")
	StringerResponse addRecord(@Named("zoneName:.+") String zone, @Named("id") String id, Record record);
	
	@RequestLine("DELETE /v1/zone/{zoneName:.+}/record/{id}")
	StringerResponse deleteRecord(@Named("zoneName:.+") String zone, @Named("id") String id);
	
	@RequestLine("GET /v1/zone/{zoneName:.+}/record/{id}/{tool}")
	StringerResponse getProxy(@Named("zoneName:.+") String zone, @Named("id") String id, @Named("tool") String tool);
	
	@RequestLine("POST /v1/zone/{zoneName:.+}/record/{id}/{tool}")
	StringerResponse addProxy(@Named("zoneName:.+") String zone, @Named("id") String id, @Named("tool") String toolName, Tool tool);
	
	@RequestLine("DELETE /v1/zone/{zoneName:.+}/record/{id}/{tool}")
	StringerResponse deleteProxy(@Named("zoneName:.+") String zone, @Named("id") String id, @Named("tool") String toolName);
	
	@RequestLine("POST /v1/zone/{zoneName:.+}/record/{id}/signpost")
	StringerResponse addDashboard(@Named("zoneName:.+") String zone, @Named("id") String id, Tool tool);
}
