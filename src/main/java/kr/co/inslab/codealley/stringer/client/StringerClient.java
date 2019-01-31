package kr.co.inslab.codealley.stringer.client;

import kr.co.inslab.codealley.stringer.client.utils.StringerException;
import mesosphere.marathon.client.utils.ModelUtils;
import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

/**
 * Stringer service의 REST API 호출을 대행하는 client 클래스
 */
public class StringerClient {

	static class StringerErrorDecoder implements ErrorDecoder {
		@Override
		public Exception decode(String methodKey, Response response) {
			return new StringerException(response.status(), response.reason());
		}
	}
	
	static class StringerHeaderInterceptor implements RequestInterceptor {
		@Override
		public void apply(RequestTemplate template) {
			template.header("Accept", "application/json");
			template.header("Content-Type", "application/json");
		}
	}
	
	public static Stringer getInstance(String endpoint) {
		GsonDecoder decoder = new GsonDecoder(ModelUtils.GSON);
		GsonEncoder encoder = new GsonEncoder(ModelUtils.GSON);
		return Feign.builder().encoder(encoder).decoder(decoder)
				.errorDecoder(new StringerErrorDecoder())
				.requestInterceptor(new StringerHeaderInterceptor())
				.target(Stringer.class, endpoint);
	}
}
