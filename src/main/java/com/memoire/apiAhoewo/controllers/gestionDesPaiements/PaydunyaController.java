package com.memoire.apiAhoewo.controllers.gestionDesPaiements;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaydunyaService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class PaydunyaController {
    @Autowired
    private PaydunyaService paydunyaService;

    @RequestMapping(value = "/paydunya-initialize", method = {RequestMethod.GET, RequestMethod.POST})
    public String payDunyaInitialize(String paiementJson) throws JsonProcessingException {

        Paiement paiement = new ObjectMapper().readValue(paiementJson, Paiement.class);

        if (paiement == null) {
            return "http://localhost:4200/";
        }

        String redirectUrl = paydunyaService.createInvoiceAndGetRedirectUrl(paiement);

        System.out.println(redirectUrl);

        if (redirectUrl != null) {
            return redirectUrl;
        } else {
            return "http://localhost:4200/";
        }
    }

    @GetMapping("/paydunya-check-payment")
    public Mono<String> checkPayment(@RequestParam("token") String invoiceToken) throws JSONException {
        return paydunyaService.checkPayement(invoiceToken);
    }
}
