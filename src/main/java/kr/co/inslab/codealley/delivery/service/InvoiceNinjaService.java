package kr.co.inslab.codealley.delivery.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.inslab.codealley.delivery.model.DeliveryProperty;
import kr.co.inslab.codealley.invoiceninja.client.InvoiceNinja;
import kr.co.inslab.codealley.invoiceninja.client.InvoiceNinjaClient;
import kr.co.inslab.codealley.invoiceninja.client.model.Client;
import kr.co.inslab.codealley.invoiceninja.client.model.ClientResponse;
import kr.co.inslab.codealley.invoiceninja.client.model.Invoice;
import kr.co.inslab.codealley.invoiceninja.client.model.InvoiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Invoice Ninja를 제어하는 서비스 클래스
 */
@Service
public class InvoiceNinjaService {

	private Log logger = LogFactory.getLog(this.getClass());
	private InvoiceNinja invoiceNinja;
	
	@Autowired
	private DeliveryProperty property;

	/**
	 * 설정파일에 기록된 설정으로 초기 설정하는 함수
	 */
	@PostConstruct
	public void init() {
		String endpoint = property.getInvoiceEndPoint();
		String token = property.getInvoiceToken();
		this.invoiceNinja = InvoiceNinjaClient.getInstance(endpoint, token);
	}

	/**
	 * Invoice Ninja의 client 정보들을 가져오는 함수
	 * @return String Client 정보
	 */
	public String getClients() {
		return invoiceNinja.getClients().toString();
	}

	/**
	 * Client를 생성하는 함수
	 * @param raw Client 생성에 필요한 정보
	 * @return String 생성된 결과
	 * @throws Exception
	 */
	public String createClient(String raw) throws Exception {
		Gson gson = new GsonBuilder().create();
		Client client = gson.fromJson(raw, Client.class);
		logger.info("Client to create:" + client.toString());
		ClientResponse response = invoiceNinja.createClient(client);
		
		return response.toString();
	}

	/**
	 * Invoice 정보들을 가져오는 함수
	 * @return String Invoice 정보
	 */
	public String getInvoices() {
		return invoiceNinja.getInvoices().toString();
	}

	/**
	 * Invoice를 생성하는 함수
	 * @param raw Invoice 생성에 필요한 정보
	 * @return String 생성된 결과
	 * @throws Exception
	 */
	public String createInvoice(String raw) throws Exception {
		Gson gson = new GsonBuilder().create();
		Invoice invoice = gson.fromJson(raw, Invoice.class);
		logger.info("Invoice to create:" + invoice.toString());
		InvoiceResponse response = invoiceNinja.createInvoice(invoice);
		
		return response.toString();
	}
}
