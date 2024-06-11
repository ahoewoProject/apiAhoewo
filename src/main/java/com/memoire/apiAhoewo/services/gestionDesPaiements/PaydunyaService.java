package com.memoire.apiAhoewo.services.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import org.json.JSONException;
import reactor.core.publisher.Mono;

public interface PaydunyaService {
    public String createInvoiceAndGetRedirectUrl(Paiement paiement);

    public Mono<String> checkPayement(String invoiceToken) throws JSONException;
}
