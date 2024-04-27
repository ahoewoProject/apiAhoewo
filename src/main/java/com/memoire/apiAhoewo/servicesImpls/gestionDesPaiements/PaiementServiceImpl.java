package com.memoire.apiAhoewo.servicesImpls.gestionDesPaiements;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.Contrat;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratLocation;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratVente;
import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.memoire.apiAhoewo.models.gestionDesPaiements.PlanificationPaiement;
import com.memoire.apiAhoewo.repositories.gestionDesPaiements.PaiementRepository;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.ContratLocationService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.ContratVenteService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaiementService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PlanificationPaiementService;
import com.memoire.apiAhoewo.services.gestionDesPublications.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class PaiementServiceImpl implements PaiementService {
    @Autowired
    private PaiementRepository paiementRepository;
    @Autowired
    private PlanificationPaiementService planificationPaiementService;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private PublicationService publicationService;
    @Autowired
    private ContratVenteService contratVenteService;
    @Autowired
    private ContratLocationService contratLocationService;

    @Override
    public Page<Paiement> getPaiements(Principal principal, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);

        List<PlanificationPaiement> planificationPaiementList = planificationPaiementService.getPlanificationsPaiement(principal);

        return paiementRepository.findByPlanificationPaiementInOrderByIdDesc(planificationPaiementList, pageRequest);
    }

    @Override
    public Paiement findById(Long id) {
        return paiementRepository.findById(id).orElse(null);
    }

    @Override
    public List<Paiement> dernierePaiement(String codeContrat) {
        return paiementRepository.findByPlanificationPaiement_Contrat_CodeContrat(codeContrat);
    }

    @Override
    public Paiement savePaiementLocation(Paiement paiement, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        paiement.setCodePaiement("PAIEME" + UUID.randomUUID());
        paiement.setStatutPaiement("Effectué");
        paiement.setDatePaiement(new Date());
        paiement.setCreerLe(new Date());
        paiement.setCreerPar(personne.getId());
        paiement.setStatut(true);

        paiement =  paiementRepository.save(paiement);

        Notification notification = new Notification();
        notification.setTitre("Nouveau paiement");
        notification.setMessage("Un paiement vient d'être effectué pour la planification de paiement " + paiement.getPlanificationPaiement().getCodePlanification());
        notification.setSendTo(String.valueOf(paiement.getPlanificationPaiement().getCreerLe()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/paiements/" + paiement.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (paiement.getPlanificationPaiement().getContrat().getBienImmobilier().getEstDelegue()) {
            if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                    personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {
                Notification notification1 = new Notification();
                notification1.setTitre("Nouveau paiement");
                notification1.setMessage("Un paiement vient d'être effectué pour la planification de paiement " + paiement.getPlanificationPaiement().getCodePlanification());
                notification1.setSendTo(String.valueOf(paiement.getPlanificationPaiement().getContrat().getBienImmobilier().getPersonne().getId()));
                notification1.setDateNotification(new Date());
                notification1.setLu(false);
                notification1.setUrl("/paiements/" + paiement.getId());
                notification1.setCreerPar(personne.getId());
                notification1.setCreerLe(new Date());
                notificationService.save(notification1);
            }
        }

        if (paiement.getModePaiement().equals("Manuel") || paiement.getModePaiement().equals("Espèce")) {
            if (paiement.getPlanificationPaiement().getLibelle().equals("Avance/Caution")) {
                paiement.getPlanificationPaiement().getContrat().getBienImmobilier().setStatutBien("Loué");
                bienImmobilierService.setBienImmobilier(paiement.getPlanificationPaiement().getContrat().getBienImmobilier());

                publicationService.desactiverPublicationParBienImmobilier(paiement.getPlanificationPaiement().getContrat().getBienImmobilier().getId());
            }
        } else {

        }

        paiement.getPlanificationPaiement().setStatutPlanification("Payé");
        planificationPaiementService.setStatutPlanification(paiement.getPlanificationPaiement());

        paiement.getPlanificationPaiement().getContrat().setEtatContrat("En cours");
        contratLocationService.setEtatContrat((ContratLocation) paiement.getPlanificationPaiement().getContrat());

        paiement.setCodePaiement("PAIEME00" + paiement.getId());
        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement savePaiementAchat(Paiement paiement, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        paiement.setCodePaiement("PAIEME" + UUID.randomUUID());
        paiement.setStatutPaiement("Effectué");
        paiement.setDatePaiement(new Date());
        paiement.setCreerLe(new Date());
        paiement.setCreerPar(personne.getId());
        paiement.setStatut(true);

        paiement = paiementRepository.save(paiement);

        ContratVente contratVente = contratVenteService.findById(paiement.getPlanificationPaiement().getContrat().getId());

        Notification notification = new Notification();
        notification.setTitre("Nouveau paiement");
        notification.setMessage("Un paiement vient d'être effectué pour la planification de paiement " + paiement.getPlanificationPaiement().getCodePlanification());
        notification.setSendTo(String.valueOf(paiement.getPlanificationPaiement().getCreerLe()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/paiements/" + paiement.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (paiement.getPlanificationPaiement().getContrat().getBienImmobilier().getEstDelegue()) {
            if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                    personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {
                Notification notification1 = new Notification();
                notification1.setTitre("Nouveau paiement");
                notification1.setMessage("Un paiement vient d'être effectué pour la planification de paiement " + paiement.getPlanificationPaiement().getCodePlanification());
                notification1.setSendTo(String.valueOf(paiement.getPlanificationPaiement().getContrat().getBienImmobilier().getPersonne().getId()));
                notification1.setDateNotification(new Date());
                notification1.setLu(false);
                notification1.setUrl("/paiements/" + paiement.getId());
                notification1.setCreerPar(personne.getId());
                notification1.setCreerLe(new Date());
                notificationService.save(notification1);
            }
        }

        if (paiement.getModePaiement().equals("Manuel") || paiement.getModePaiement().equals("Espèce")) {
            List<Paiement> lastPaiements = dernierePaiement(paiement.getPlanificationPaiement().getContrat().getCodeContrat());

            if (!lastPaiements.isEmpty()) {
                double montantTotal = 0;
                for (Paiement paiement1: lastPaiements) {
                    montantTotal += paiement1.getMontant();
                }

                if (contratVente.getPrixVente().equals(montantTotal)) {
                    contratVente.getBienImmobilier().setStatutBien("Vendu");
                    bienImmobilierService.setBienImmobilier(contratVente.getBienImmobilier());

                    publicationService.desactiverPublicationParBienImmobilier(contratVente.getBienImmobilier().getId());

                }
            } else {
                contratVente.getBienImmobilier().setStatutBien("Vendu");
                bienImmobilierService.setBienImmobilier(contratVente.getBienImmobilier());

                publicationService.desactiverPublicationParBienImmobilier(contratVente.getBienImmobilier().getId());
            }
        } else {

        }

        paiement.getPlanificationPaiement().setStatutPlanification("Payé");
        planificationPaiementService.setStatutPlanification(paiement.getPlanificationPaiement());

        paiement.getPlanificationPaiement().getContrat().setEtatContrat("Confirmé");
        contratVenteService.setEtatContrat((ContratVente) paiement.getPlanificationPaiement().getContrat());

        paiement.setCodePaiement("PAIEME00" + paiement.getId());
        return paiementRepository.save(paiement);
    }

    @Override
    public byte[] generatePdf(Long id) throws IOException {
        Paiement paiement = paiementRepository.findById(id).orElse(null);

        if (paiement.getPlanificationPaiement().getTypePlanification().equals("Paiement de location")) {
            return this.generatePaiementLocationPdf(paiement.getId());
        } else {
            return this.generatePaiementAchatPdf(paiement.getId());
        }
    }

    public byte[] generatePaiementAchatPdf(Long id) throws IOException {
        Paiement paiement = paiementRepository.findById(id).orElse(null);
        ContratVente contratVente =  new ContratVente();
        if (paiement != null) {
            contratVente = contratVenteService.findById(paiement.getPlanificationPaiement().getContrat().getId());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Le' d MMMM yyyy, 'à' HH 'heures' mm 'minutes'", Locale.FRENCH);

        // Formater la date actuelle selon le format spécifié
        String formattedDate = now.format(formatter);

        String fontFilePoRegular = "src/main/resources/fonts/Poppins/Poppins-Regular.ttf";

        // Charger la police à partir du fichier
        PdfFont poppinsRegular = PdfFontFactory.createFont(fontFilePoRegular);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);

        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument).setFont(poppinsRegular);

        Color color = new DeviceRgb(0x3B, 0x82, 0xF6);

        float threecol = 190f;
        float twocol = 285f;
        float twocol150 = twocol + 150f;
        float twocolumnWidth[] = {twocol150, twocol};
        float fullWidth[] = {threecol*3};

        float oneColumnWidth[] = { twocol150 };

        Table table = new Table(twocolumnWidth);


        table.addCell(new Cell().add(new Paragraph("Paiement ").add(new Paragraph(paiement.getCodePaiement()))).setFontSize(14f).setBorder(Border.NO_BORDER).setBold());

//        Table nestedTable = new Table(new float[]{twocol/2, twocol/2});
//
//        nestedTable.addCell(getHeaderTextCell(new Paragraph("Date: ")));
//        nestedTable.addCell(getHeaderTextCellValue(new Paragraph(dateFormat.format(new Date()))));
//        nestedTable.addCell(getHeaderTextCell(new Paragraph("Code: ")));
//        nestedTable.addCell(getHeaderTextCellValue(new Paragraph(contratVente.getCodeContrat())));
//
//        table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));

        document.add(table);

        Table date = new Table(twocolumnWidth);
        date.addCell(getHeaderTextCellValue(new Paragraph(formattedDate)).setBorder(Border.NO_BORDER));

        document.add(date);

        Border border = new SolidBorder(ColorConstants.GRAY, 2f);

        Table divider = new Table(fullWidth);
        divider.setBorder(border);
        document.add(divider);

        Table titlePaiement = new Table(twocolumnWidth);
        titlePaiement.addCell(getHeadTitre(new Paragraph("Paiement")).setFontColor(color));

        document.add(titlePaiement.setMarginBottom(12f));

        Table detailPaiementTwoColumn = new Table(twocolumnWidth);

        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Code"), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Mode de paiement"), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(paiement.getCodePaiement()), false));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(paiement.getModePaiement()), false));

        if (paiement.getModePaiement().equals("Manuel")) {
            detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Numero compte de paiement"), true));
            detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Référence de la transaction"), true));
            detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(paiement.getNumeroComptePaiement()), false));
            detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(paiement.getReferenceTransaction()), false));
        }

        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Libelle").setMarginTop(10), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Montant dû").setMarginTop(10), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(paiement.getPlanificationPaiement().getLibelle()), false));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(paiement.getPlanificationPaiement().getMontantDu()) + " FCFA"), false));

        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Montant payé").setMarginTop(10), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Montant restant").setMarginTop(10), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(paiement.getMontant()) + " FCFA"), false));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(paiement.getPlanificationPaiement().getRestePaye()) + " FCFA"), false));

        document.add(detailPaiementTwoColumn);

        Table detailPaiementOneColumn = new Table(oneColumnWidth);
        detailPaiementOneColumn.addCell(getCell10fLeft(new Paragraph("Date paiement").setMarginTop(10), true));
        detailPaiementOneColumn.addCell(getCell10fLeft(new Paragraph(dateFormat.format(paiement.getDatePaiement())), false));

        document.add(detailPaiementOneColumn.setMarginBottom(12f));

        Table titleBienImmobilier = new Table(twocolumnWidth);
        titleBienImmobilier.addCell(getHeadTitre(new Paragraph("Bien immobilier").setMarginTop(10)).setFontColor(color));

        document.add(titleBienImmobilier.setMarginBottom(12f));


        Table description = new Table(fullWidth);
        description.addCell(new Cell().add(new Paragraph("Description").setFontSize(10f)).setBold().setBorder(Border.NO_BORDER));

        description.addCell(new Cell().add(new Paragraph(contratVente.getBienImmobilier().getDescription()).setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));

        document.add(description);

        Table bienImmobilierDetails = new Table(twocolumnWidth);

        bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Code du bien").setMarginTop(10), true));
        bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Type de bien").setMarginTop(10), true));
        bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getCodeBien()), false));
        bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getTypeDeBien().getDesignation()), false));

        if (afficherCategorie(contratVente)) {
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Catégorie").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Pays").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getCategorie()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getQuartier().getVille().getRegion().getPays().getLibelle()), false));

            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Région").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Ville").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getQuartier().getVille().getRegion().getLibelle()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getQuartier().getVille().getLibelle()), false));

            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Quartier").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Adresse").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getQuartier().getLibelle()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getQuartier().getLibelle()), false));

            document.add(bienImmobilierDetails);

            Table surfaceTable = new Table(oneColumnWidth);
            surfaceTable.addCell(getCell10fLeft(new Paragraph("Surface").setMarginTop(10), true));
            surfaceTable.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getBienImmobilier().getSurface() + " m²")), false));
            document.add(surfaceTable);

        } else {
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Pays").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Région").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getQuartier().getVille().getRegion().getPays().getLibelle()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getQuartier().getVille().getRegion().getLibelle()), false));

            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Ville").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Quartier").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getQuartier().getVille().getLibelle()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getQuartier().getLibelle()), false));

            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Adresse").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Surface").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratVente.getBienImmobilier().getAdresse()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getBienImmobilier().getSurface() + " m²")), false));

            document.add(bienImmobilierDetails);
        }

        if (paiement.getPlanificationPaiement().getContrat().getBienImmobilier().getEstDelegue()) {
            if (paiement.getPlanificationPaiement().getContrat().getAgenceImmobiliere() != null) {
                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("L'Agence").setMarginTop(50), true));

                document.add(piedDePageDetails);

            }
            if (paiement.getPlanificationPaiement().getContrat().getDemarcheur() !=  null) {

                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Démarcheur").setMarginTop(50), true));

                document.add(piedDePageDetails);

            }

            if (paiement.getPlanificationPaiement().getContrat().getGerant() != null) {

                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Gérant").setMarginTop(50), true));

                document.add(piedDePageDetails);
            }

        } else {
            if (paiement.getPlanificationPaiement().getContrat().getAgenceImmobiliere() != null) {

                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("L'Agence").setMarginTop(10), true));

                document.add(piedDePageDetails);

            }

            if (paiement.getPlanificationPaiement().getContrat().getProprietaire() != null) {

                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                document.add(piedDePageDetails);
            }

            if (paiement.getPlanificationPaiement().getContrat().getDemarcheur() != null) {

                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Démarcheur").setMarginTop(10), true));

                document.add(piedDePageDetails);

            }
        }

        document.close();
        return outputStream.toByteArray();
    }

    public byte[] generatePaiementLocationPdf(Long id) throws IOException {
        Paiement paiement = paiementRepository.findById(id).orElse(null);
        ContratLocation contratLocation =  new ContratLocation();
        if (paiement != null) {
            contratLocation = contratLocationService.findById(paiement.getPlanificationPaiement().getContrat().getId());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Le' d MMMM yyyy, 'à' HH 'heures' mm 'minutes'", Locale.FRENCH);

        // Formater la date actuelle selon le format spécifié
        String formattedDate = now.format(formatter);

        String fontFilePoRegular = "src/main/resources/fonts/Poppins/Poppins-Regular.ttf";

        // Charger la police à partir du fichier
        PdfFont poppinsRegular = PdfFontFactory.createFont(fontFilePoRegular);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);

        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument).setFont(poppinsRegular);

        Color color = new DeviceRgb(0x3B, 0x82, 0xF6);

        float threecol = 190f;
        float twocol = 285f;
        float twocol150 = twocol + 150f;
        float twocolumnWidth[] = {twocol150, twocol};
        float fullWidth[] = {threecol*3};

        float oneColumnWidth[] = { twocol150 };

        Table table = new Table(twocolumnWidth);


        table.addCell(new Cell().add(new Paragraph("Paiement ").add(new Paragraph(paiement.getCodePaiement()))).setFontSize(14f).setBorder(Border.NO_BORDER).setBold());

        document.add(table);

        Table date = new Table(twocolumnWidth);
        date.addCell(getHeaderTextCellValue(new Paragraph(formattedDate)).setBorder(Border.NO_BORDER));

        document.add(date);

        Border border = new SolidBorder(ColorConstants.GRAY, 2f);

        Table divider = new Table(fullWidth);
        divider.setBorder(border);
        document.add(divider);

        Table titlePaiement = new Table(twocolumnWidth);
        titlePaiement.addCell(getHeadTitre(new Paragraph("Paiement")).setFontColor(color));

        document.add(titlePaiement.setMarginBottom(12f));

        Table detailPaiementTwoColumn = new Table(twocolumnWidth);

        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Code"), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Mode de paiement"), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(paiement.getCodePaiement()), false));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(paiement.getModePaiement()), false));

        if (paiement.getModePaiement().equals("Manuel")) {
            detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Numero compte de paiement"), true));
            detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Référence de la transaction"), true));
            detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(paiement.getNumeroComptePaiement()), false));
            detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(paiement.getReferenceTransaction()), false));
        }

        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Libelle").setMarginTop(10), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Montant dû").setMarginTop(10), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(paiement.getPlanificationPaiement().getLibelle()), false));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(paiement.getPlanificationPaiement().getMontantDu()) + " FCFA"), false));

        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Montant payé").setMarginTop(10), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph("Montant restant").setMarginTop(10), true));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(paiement.getMontant()) + " FCFA"), false));
        detailPaiementTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(paiement.getPlanificationPaiement().getRestePaye()) + " FCFA"), false));

        document.add(detailPaiementTwoColumn);

        Table detailPaiementOneColumn = new Table(oneColumnWidth);
        detailPaiementOneColumn.addCell(getCell10fLeft(new Paragraph("Date paiement").setMarginTop(10), true));
        detailPaiementOneColumn.addCell(getCell10fLeft(new Paragraph(dateFormat.format(paiement.getDatePaiement())), false));

        document.add(detailPaiementOneColumn.setMarginBottom(12f));

        Table titleBienImmobilier = new Table(twocolumnWidth);
        titleBienImmobilier.addCell(getHeadTitre(new Paragraph("Bien immobilier").setMarginTop(10)).setFontColor(color));

        document.add(titleBienImmobilier.setMarginBottom(12f));


        Table description = new Table(fullWidth);
        description.addCell(new Cell().add(new Paragraph("Description").setFontSize(10f)).setBold().setBorder(Border.NO_BORDER));

        description.addCell(new Cell().add(new Paragraph(contratLocation.getBienImmobilier().getDescription()).setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));

        document.add(description);

        Table bienImmobilierDetails = new Table(twocolumnWidth);

        bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Code du bien").setMarginTop(10), true));
        bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Type de bien").setMarginTop(10), true));
        bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getCodeBien()), false));
        bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getTypeDeBien().getDesignation()), false));

        if (afficherCategorie(contratLocation)) {
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Catégorie").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Pays").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getCategorie()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getQuartier().getVille().getRegion().getPays().getLibelle()), false));

            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Région").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Ville").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getQuartier().getVille().getRegion().getLibelle()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getQuartier().getVille().getLibelle()), false));

            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Quartier").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Adresse").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getQuartier().getLibelle()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getQuartier().getLibelle()), false));

            document.add(bienImmobilierDetails);

            Table surfaceTable = new Table(oneColumnWidth);
            surfaceTable.addCell(getCell10fLeft(new Paragraph("Surface").setMarginTop(10), true));
            surfaceTable.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getBienImmobilier().getSurface() + " m²")), false));
            document.add(surfaceTable);

        } else {
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Pays").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Région").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getQuartier().getVille().getRegion().getPays().getLibelle()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getQuartier().getVille().getRegion().getLibelle()), false));

            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Ville").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Quartier").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getQuartier().getVille().getLibelle()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getQuartier().getLibelle()), false));

            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Adresse").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph("Surface").setMarginTop(10), true));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getBienImmobilier().getAdresse()), false));
            bienImmobilierDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getBienImmobilier().getSurface() + " m²")), false));

            document.add(bienImmobilierDetails);
        }

        if (paiement.getPlanificationPaiement().getContrat().getBienImmobilier().getEstDelegue()) {
            if (paiement.getPlanificationPaiement().getContrat().getAgenceImmobiliere() != null) {
                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("L'Agence").setMarginTop(50), true));

                document.add(piedDePageDetails);

            }
            if (paiement.getPlanificationPaiement().getContrat().getDemarcheur() !=  null) {

                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Démarcheur").setMarginTop(50), true));

                document.add(piedDePageDetails);

            }

            if (paiement.getPlanificationPaiement().getContrat().getGerant() != null) {

                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Gérant").setMarginTop(50), true));

                document.add(piedDePageDetails);
            }

        } else {
            if (paiement.getPlanificationPaiement().getContrat().getAgenceImmobiliere() != null) {

                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("L'Agence").setMarginTop(10), true));

                document.add(piedDePageDetails);

            }

            if (paiement.getPlanificationPaiement().getContrat().getProprietaire() != null) {

                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                document.add(piedDePageDetails);
            }

            if (paiement.getPlanificationPaiement().getContrat().getDemarcheur() != null) {

                document.add(divider);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Démarcheur").setMarginTop(10), true));

                document.add(piedDePageDetails);

            }
        }

        document.close();
        return outputStream.toByteArray();
    }

    static Cell getHeaderTextCell(Paragraph paragraph) {
        return new Cell().add(paragraph).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getHeaderTextCellValue(Paragraph paragraph) {
        return new Cell().add(paragraph).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }


    static Cell getHeadTitre(Paragraph paragraph) {
        return new Cell().add(paragraph).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getCell10fLeft(Paragraph paragraph, Boolean isBold) {
        Cell myCell = new Cell().add(paragraph).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ? myCell.setBold() : myCell;
    }

    public boolean afficherCategorie(Contrat contrat) {
        return contrat.getBienImmobilier().getTypeDeBien().getDesignation().equals("Maison") ||
                contrat.getBienImmobilier().getTypeDeBien().getDesignation().equals("Villa") ||
                contrat.getBienImmobilier().getTypeDeBien().getDesignation().equals("Immeuble") ||
                contrat.getBienImmobilier().getTypeDeBien().getDesignation().equals("Appartement") ||
                contrat.getBienImmobilier().getTypeDeBien().getDesignation().equals("Chambre salon") ||
                contrat.getBienImmobilier().getTypeDeBien().getDesignation().equals("Chambre") ||
                contrat.getBienImmobilier().getTypeDeBien().getDesignation().equals("Bureau");
    }
}
