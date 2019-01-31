package kr.co.inslab.codealley.invoiceninja.client;

import java.util.List;

import kr.co.inslab.codealley.invoiceninja.client.model.Client;
import kr.co.inslab.codealley.invoiceninja.client.model.ClientData;
import kr.co.inslab.codealley.invoiceninja.client.model.ClientResponse;
import kr.co.inslab.codealley.invoiceninja.client.model.Invoice;
import kr.co.inslab.codealley.invoiceninja.client.model.InvoiceData;
import kr.co.inslab.codealley.invoiceninja.client.model.InvoiceResponse;
import feign.RequestLine;

/**
 * Invoice Ninja의 REST API interface
 */
public interface InvoiceNinja {

	@RequestLine("GET /api/v1/clients")
	List<ClientData> getClients();
	
	@RequestLine("POST /api/v1/clients")
	ClientResponse createClient(Client client);
	
	@RequestLine("GET /api/v1/invoices")
	List<InvoiceData> getInvoices();
	
	@RequestLine("POST /api/v1/invoices")
	InvoiceResponse createInvoice(Invoice invoice);
}
