package com.memoire.apiAhoewo.controllers.gestionDesPaiements;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api")
public class PaypalController {
    @Autowired
    public PaypalService paypalService;
    @Autowired
    private Environment env;

    @RequestMapping(value = "/paypal-initialize", method = {RequestMethod.POST})
    public String payPalInitialize(@RequestBody Paiement paiement) {
        try {
            Payment payment = paypalService.createPayment(paiement);

            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return links.getHref();
                }
            }
        } catch (PayPalRESTException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return env.getProperty("client.web") + "client/planification-paiement/" + paiement.getPlanificationPaiement().getId() + "?paiementNonReussi=true";
    }

    @RequestMapping(value = "/payment/success", method = {RequestMethod.GET})
    public RedirectView paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Paiement paiement = paypalService.executePayment(paymentId, payerId);
            if (paiement != null && paiement.getId() != null) {
                return new RedirectView(env.getProperty("client.web") + "client/paiement/" + paiement.getId() + "?paiementReussi=true");
            } else if (paiement != null && paiement.getPlanificationPaiement() != null) {
                return new RedirectView(env.getProperty("client.web") + "client/planification-paiement/" + paiement.getPlanificationPaiement().getId() + "?paiementNonReussi=true");
            } else {
                return new RedirectView(env.getProperty("client.web") + "client/planification-paiement/?paiementNonReussi=true");
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
    }
}
