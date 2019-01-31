package kr.co.inslab.codealley.invoiceninja.client;

import kr.co.inslab.codealley.invoiceninja.client.utils.InvoiceNinjaException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

/**
 * Invoice Ninja의 REST API 호출을 대행할 수 있는 client 클래스
 */
public class InvoiceNinjaClient {

	static class InvoiceNinjaErrorDecoder implements ErrorDecoder {
		@Override
		public Exception decode(String methodKey, Response response) {
			return new InvoiceNinjaException(response.status(), response.reason());
		}
	}
	
	static class InvoiceNinjaHeaderInterceptor implements RequestInterceptor {
		private String token;
		public InvoiceNinjaHeaderInterceptor(String token) {
			this.token = token;
		}
		
		@Override
		public void apply(RequestTemplate template) {
			template.header("Accept", "application/json");
			template.header("Content-Type", "application/json");
			//template.header("X-Ninja-Token", "RBwzZ8WEam13XHyozUsbc8Tym28ixXZ7");
			template.header("X-Ninja-Token", this.token);
		}
	}
	
	public static InvoiceNinja getInstance(String endpoint, String token) {
		Gson customDateGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.setPrettyPrinting().disableHtmlEscaping().create();
		
		GsonDecoder decoder = new GsonDecoder(customDateGson);
		GsonEncoder encoder = new GsonEncoder(customDateGson);
		return Feign.builder().encoder(encoder).decoder(decoder)
				.errorDecoder(new InvoiceNinjaErrorDecoder())
				.requestInterceptor(new InvoiceNinjaHeaderInterceptor(token))
				.target(InvoiceNinja.class, endpoint);
	}
}
