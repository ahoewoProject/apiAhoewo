package com.memoire.apiAhoewo.servicesImpls.gestionDesPaiements;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.memoire.apiAhoewo.models.gestionDesPaiements.ComptePaiement;
import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.memoire.apiAhoewo.models.gestionDesPaiements.PlanificationPaiement;
import com.memoire.apiAhoewo.services.gestionDesPaiements.ComptePaiementService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaiementService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaypalService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PlanificationPaiementService;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {

    private final APIContext apiContext;
    private final PlanificationPaiementService planificationPaiementService;
    private final PaiementService paiementService;
    private final ComptePaiementService comptePaiementService;
    private final Environment env;

    @Override
    public Payment createPayment(Paiement paiement) throws PayPalRESTException {

        String cancelUrl = env.getProperty("client.web") + "client/planification-paiement/" + paiement.getPlanificationPaiement().getId();
        String successUrl = "http://localhost:4040/api/payment/success";

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format(Locale.forLanguageTag("USD"), "%.2f", 1000.0));

        Transaction transaction = new Transaction();
        transaction.setDescription(paiement.getPlanificationPaiement().getLibelle());
        transaction.setAmount(amount);

        JsonObject customData = new JsonObject();
        customData.addProperty("planificationPaiementId", paiement.getPlanificationPaiement().getId());
        customData.addProperty("modePaiement", paiement.getModePaiement());

        transaction.setCustom(customData.toString());

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        WebProfile webProfile = new WebProfile();
        webProfile.setName("My_Profile_123");
        Presentation presentation = new Presentation();
        presentation.setBrandName("ahoewo");
        presentation.setLocaleCode("US");
        webProfile.setPresentation(presentation);

        InputFields inputFields = new InputFields();
        inputFields.setNoShipping(1);
        webProfile.setInputFields(inputFields);

        FlowConfig flowConfig = new FlowConfig();
        flowConfig.setLandingPageType("Billing");
        webProfile.setFlowConfig(flowConfig);

        payment.setExperienceProfileId(webProfile.getId());

        return payment.create(apiContext);
    }

    public Paiement executePayment(
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executed =  payment.execute(apiContext, paymentExecution);

        Paiement paiement = new Paiement();

        if (executed.getState().equals("approved")) {
            Amount amount = executed.getTransactions().get(0).getAmount();
            String total = amount.getTotal();
            String currency = amount.getCurrency();

            String customDataString = executed.getTransactions().get(0).getCustom();
            JsonObject customData = JsonParser.parseString(customDataString).getAsJsonObject();
            Long planificationPaiementId = customData.get("planificationPaiementId").getAsLong();
            String modePaiement = customData.get("modePaiement").getAsString();

            PlanificationPaiement planificationPaiement = planificationPaiementService.findById(planificationPaiementId);

            paiement.setMontant(planificationPaiement.getMontantPaye());
            paiement.setPlanificationPaiement(planificationPaiement);
            paiement.setModePaiement(modePaiement);

            String recipientEmail = determineRecipientEmail(planificationPaiement);

            PayoutBatch payoutBatch = sendPayout(recipientEmail, total, currency);

            paiement.setPayoutBatchId(payoutBatch.getBatchHeader().getPayoutBatchId());
            if (payoutBatch.getBatchHeader().getBatchStatus().equals("PENDING")) {
                paiement = paiementService.savePaiementByClientIfStatutPaiementIsPending(paiement);
            }
        }
        return paiement;
    }

    public PayoutBatch sendPayout(String recipientEmail, String amount, String currency) throws PayPalRESTException {
        PayoutSenderBatchHeader senderBatchHeader = new PayoutSenderBatchHeader()
                .setSenderBatchId("batch_" + System.currentTimeMillis())
                .setEmailSubject("Vous avez reçu un nouveau de paiement");

        Currency payoutAmount = new Currency()
                .setValue(amount)
                .setCurrency(currency);

        PayoutItem senderItem = new PayoutItem()
                .setRecipientType("EMAIL")
                .setReceiver(recipientEmail)
                .setAmount(payoutAmount)
                .setSenderItemId("item_" + System.currentTimeMillis())
                .setNote("Voici votre paiement.");

        List<PayoutItem> items = new ArrayList<>();
        items.add(senderItem);

        Payout payout = new Payout()
                .setSenderBatchHeader(senderBatchHeader)
                .setItems(items);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("sync_mode", "false");

        // Créer le payout en utilisant l'API PayPal
        PayoutBatch payoutBatch = payout.create(apiContext, parameters);

        // Vérifiez et enregistrez le statut de la requête
        if (payoutBatch != null && payoutBatch.getBatchHeader() != null) {
            String payoutStatus = payoutBatch.getBatchHeader().getBatchStatus();
            System.out.println("Payout batch status: " + payoutStatus);
        }

        return payoutBatch;
    }

    public void updatePayoutStatus(String payoutBatchId) {
        try {
            PayoutBatch payoutBatch = getPayoutBatchDetails(payoutBatchId);
            String payoutStatus = payoutBatch.getBatchHeader().getBatchStatus();

            System.out.println(payoutStatus);

            if (payoutStatus.equals("SUCCESS")) {
                Paiement paiement = paiementService.findByPayoutBatchId(payoutBatchId);
                paiementService.savePaiementIsCompleted(paiement.getId());
            }

            System.out.println("Payout batch status: " + payoutStatus);
        } catch (PayPalRESTException e) {
            System.err.println("Erreur lors de la récupération des détails du payout batch: " + e.getMessage());
        }
    }

    private PayoutBatch getPayoutBatchDetails(String payoutBatchId) throws PayPalRESTException {
        return Payout.get(apiContext, payoutBatchId);
    }

    private String determineRecipientEmail(PlanificationPaiement planificationPaiement) {
        ComptePaiement comptePaiement = null;

        if (planificationPaiement.getContrat().getBienImmobilier().getEstDelegue()) {
            if (planificationPaiement.getContrat().getAgenceImmobiliere() != null) {
                comptePaiement = comptePaiementService.findByTypeAndAgenceAndEtat("PayPal", planificationPaiement.getContrat().getAgenceImmobiliere(), true);
            } else if (planificationPaiement.getContrat().getDemarcheur() != null) {
                comptePaiement = comptePaiementService.findByTypeAndPersonneAndEtat("PayPal", planificationPaiement.getContrat().getDemarcheur(), true);
            } else {
                comptePaiement = comptePaiementService.findByTypeAndPersonneAndEtat("PayPal", planificationPaiement.getContrat().getGerant(), true);
            }
        } else {
            if (planificationPaiement.getContrat().getAgenceImmobiliere() != null) {
                comptePaiement = comptePaiementService.findByTypeAndAgenceAndEtat("PayPal", planificationPaiement.getContrat().getAgenceImmobiliere(), true);
            } else if (planificationPaiement.getContrat().getDemarcheur() != null) {
                comptePaiement = comptePaiementService.findByTypeAndPersonneAndEtat("PayPal", planificationPaiement.getContrat().getDemarcheur(), true);
            } else {
                comptePaiement = comptePaiementService.findByTypeAndPersonneAndEtat("PayPal", planificationPaiement.getContrat().getProprietaire(), true);
            }
        }

        return comptePaiement != null ? comptePaiement.getContact() : null;
    }
}
