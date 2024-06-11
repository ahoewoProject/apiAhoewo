package com.memoire.apiAhoewo.services.gestionDesPaiements;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaypalService {
    Payment createPayment(Paiement paiement) throws PayPalRESTException, JsonProcessingException;

    public Paiement executePayment(
            String paymentId,
            String payerId
    ) throws PayPalRESTException;

    void updatePayoutStatus(String payoutBatchId);
}
