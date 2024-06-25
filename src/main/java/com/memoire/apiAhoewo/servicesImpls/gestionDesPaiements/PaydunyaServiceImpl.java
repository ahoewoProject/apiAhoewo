package com.memoire.apiAhoewo.servicesImpls.gestionDesPaiements;

import com.memoire.apiAhoewo.services.gestionDesPaiements.PaydunyaService;
import org.springframework.stereotype.Service;

@Service
public class PaydunyaServiceImpl implements PaydunyaService {
//    private final PaydunyaSetup paydunyaSetup;
//    private final PaydunyaCheckoutStore paydunyaStore;
//    private final ComptePaiementService comptePaiementService;
//    private final Environment environment;
//    private final HttpClient httpClient = HttpClient.newHttpClient();
//
//    @Autowired
//    public PaydunyaServiceImpl(PaydunyaSetup paydunyaSetup, PaydunyaCheckoutStore paydunyaStore, ComptePaiementService comptePaiementService, Environment environment) {
//        this.paydunyaSetup = paydunyaSetup;
//        this.paydunyaStore = paydunyaStore;
//        this.comptePaiementService = comptePaiementService;
//        this.environment = environment;
//    }
//
//    @Override
//    public String createInvoiceAndGetRedirectUrl(Paiement paiement) {
//        PaydunyaCheckoutInvoice invoice = new PaydunyaCheckoutInvoice(paydunyaSetup, paydunyaStore);
//        invoice.setDescription(paiement.getPlanificationPaiement().getLibelle());
//        invoice.setTotalAmount(1500);
//
//        invoice.setReturnUrl("http://localhost:4040/api/paydunya-check-payment");
//        invoice.setCancelUrl("http://localhost:4200/#/client/planification-paiement/" + paiement.getPlanificationPaiement().getId());
//
//        try {
//            boolean created = invoice.create();
//
//            if (created) {
//                return invoice.getInvoiceUrl();
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            e.getMessage();
//            return null;
//        }
//    }
//
//    @Override
//    public Mono<String> checkPayement(String invoiceToken) throws JSONException {
//        PaydunyaCheckoutInvoice invoice = new PaydunyaCheckoutInvoice(paydunyaSetup, paydunyaStore);
//
//        if (invoice.confirm(invoiceToken)) {
//            String status = invoice.getStatus();
//
//            if ("completed".equals(status)) {
//                return processInvoice(invoice);
//            } else if ("pending".equals(status)) {
//                return Mono.just("Paiement en attente");
//            } else {
//                return Mono.just("Paiement annulé");
//            }
//        }
//        return Mono.just("Erreur lors de la vérification du paiement");
//    }
//
//    private Mono<String> processInvoice(PaydunyaCheckoutInvoice invoice) {
//        System.out.println(getInvoicePaydunya(invoice));
//        return getInvoicePaydunya(invoice);
////                .flatMap(response -> {
////                    JSONObject jsonResponse;
////                    try {
////                        jsonResponse = new JSONObject(response);
////                        String responseCode = jsonResponse.getString("response_code");
////                        String disburseToken = jsonResponse.getString("disburse_token");
////
////                        if ("00".equals(responseCode)) {
////                            return submitInvoice(disburseToken);
////                        } else {
////                            return Mono.error(new RuntimeException("Erreur lors de l'obtention de la réponse de Paydunya"));
////                        }
////                    } catch (JSONException e) {
////                        System.err.println("Failed to parse response: " + response);
////                        return Mono.error(new RuntimeException("Invalid JSON response", e));
////                    }
////                });
//    }
//
//    private Mono<String> getInvoicePaydunya(PaydunyaCheckoutInvoice invoice) {
//        return Mono.fromCallable(() -> {
//            String url = "https://app.paydunya.com/api/v2/disburse/get-invoice";
//            GetInvoicePaydunyaForm getInvoicePaydunyaForm = new GetInvoicePaydunyaForm();
//            getInvoicePaydunyaForm.setAccount_alias("97990638");
//            getInvoicePaydunyaForm.setAmount(1000);
//            getInvoicePaydunyaForm.setWithdraw_mode("t-money-togo");
//            getInvoicePaydunyaForm.setCallback_url("http://localhost:4040/api/paydunya-check-payment");
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Content-Type", "application/json");
//            headers.set("PAYDUNYA-MASTER-KEY", environment.getProperty("paydunya.masterKey"));
//            headers.set("PAYDUNYA-PRIVATE-KEY", environment.getProperty("paydunya.privateKey"));
//            headers.set("PAYDUNYA-TOKEN", environment.getProperty("paydunya.token"));
//
//            HttpEntity<GetInvoicePaydunyaForm> request = new HttpEntity<>(getInvoicePaydunyaForm, headers);
//
//            RestTemplate restTemplate = new RestTemplate();
//
//            try {
//                ResponseEntity<String> response = restTemplate.exchange(
//                        url,
//                        HttpMethod.POST,
//                        request,
//                        String.class
//                );
//                return response.getBody();
//            } catch (HttpClientErrorException e) {
//                System.err.println("Error response body: " + e.getResponseBodyAsString());
//                throw new RuntimeException("Failed to get invoice: " + e.getStatusCode());
//            } catch (RestClientException e) {
//                throw new RuntimeException("Failed to get invoice", e);
//            }
//        });
//    }
//
//
//    public Mono<String> submitInvoice(String disburseInvoice) {
//        return Mono.fromCallable(() -> {
//            String url = "https://app.paydunya.com/api/v2/disburse/submit-invoice";
//
//            SubmitInvoiceForm submitInvoiceForm = new SubmitInvoiceForm();
//            submitInvoiceForm.setDisburse_invoice(disburseInvoice);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Content-Type", "application/json");
//            headers.set("PAYDUNYA-MASTER-KEY", environment.getProperty("paydunya.masterKey"));
//            headers.set("PAYDUNYA-PRIVATE-KEY", environment.getProperty("paydunya.privateKey"));
//            headers.set("PAYDUNYA-TOKEN", environment.getProperty("paydunya.token"));
//
//            HttpEntity<SubmitInvoiceForm> request = new HttpEntity<>(submitInvoiceForm, headers);
//
//            RestTemplate restTemplate = new RestTemplate();
//
//            try {
//                ResponseEntity<String> response = restTemplate.exchange(
//                        url,
//                        HttpMethod.POST,
//                        request,
//                        String.class
//                );
//                return response.getBody();
//            } catch (HttpClientErrorException e) {
//                System.err.println("Error response body: " + e.getResponseBodyAsString());
//                throw new RuntimeException("Failed to submit invoice: " + e.getStatusCode());
//            } catch (RestClientException e) {
//                throw new RuntimeException("Failed to submit invoice", e);
//            }
//        });
//    }




}
