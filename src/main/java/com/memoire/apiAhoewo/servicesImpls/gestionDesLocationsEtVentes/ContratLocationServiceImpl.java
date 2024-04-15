package com.memoire.apiAhoewo.servicesImpls.gestionDesLocationsEtVentes;

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
import com.memoire.apiAhoewo.models.MotifRejet;
import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratLocation;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeLocation;
import com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes.ContratLocationRepository;
import com.memoire.apiAhoewo.dto.MotifRejetForm;
import com.memoire.apiAhoewo.services.MotifRejetService;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.ContratLocationService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.DemandeLocationService;
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
import java.util.*;

@Service
public class ContratLocationServiceImpl implements ContratLocationService {
    @Autowired
    private ContratLocationRepository contratLocationRepository;
    @Autowired
    private DemandeLocationService demandeLocationService;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MotifRejetService motifRejetService;
    @Autowired
    private PublicationService publicationService;
    @Autowired
    private BienImmobilierService bienImmobilierService;

    @Override
    public Page<ContratLocation> getContratLocations(Principal principal, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        List<DemandeLocation> demandeLocationList = demandeLocationService.getDemandesLocations(principal);

        return contratLocationRepository.findByDemandeLocationInOrderByIdDesc(demandeLocationList, pageRequest);
    }

    @Override
    public List<ContratLocation> getContratLocations(Principal principal) {
        List<DemandeLocation> demandeLocationList = demandeLocationService.getDemandesLocations(principal);

        return contratLocationRepository.findByDemandeLocationIn(demandeLocationList);
    }

    private boolean verifyRoleCode(String roleCode) {
        return roleCode.equals("ROLE_PROPRIETAIRE") || roleCode.equals("ROLE_RESPONSABLE") ||
                roleCode.equals("ROLE_AGENTIMMOBILIER") || roleCode.equals("ROLE_DEMARCHEUR") ||
                roleCode.equals("ROLE_GERANT");
    }


    @Override
    public ContratLocation findById(Long id) {
        return contratLocationRepository.findById(id).orElse(null);
    }

    @Override
    public ContratLocation save(ContratLocation contratLocation, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        contratLocation.setCodeContrat("CONLOC" + UUID.randomUUID());
        contratLocation.setEtatContrat("En attente");
        contratLocation.setCreerPar(personne.getId());
        contratLocation.setCreerLe(new Date());

        contratLocation = contratLocationRepository.save(contratLocation);

        Notification notification1 = new Notification();
        notification1.setTitre("Proposition d'un contrat de location");
        notification1.setMessage("Vous avez reçu une proposition de contrat de location suite à la validation de votre demande de location " + contratLocation.getDemandeLocation().getCodeDemande());
        notification1.setSendTo(String.valueOf(contratLocation.getDemandeLocation().getClient().getId()));
        notification1.setDateNotification(new Date());
        notification1.setLu(false);
        notification1.setUrl("/contrats/locations/" + contratLocation.getId());
        notification1.setCreerPar(personne.getId());
        notification1.setCreerLe(new Date());
        notificationService.save(notification1);


        if (contratLocation.getBienImmobilier().getEstDelegue()) {
            if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                    personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {
                Notification notification2 = new Notification();
                notification2.setTitre("Proposition d'un contrat de location");
                notification2.setMessage("Une proposition de contrat a été faite sur le bien immobilier " + contratLocation.getBienImmobilier().getCodeBien() + " que vous avez délégué");
                notification2.setSendTo(String.valueOf(contratLocation.getBienImmobilier().getPersonne().getId()));
                notification2.setDateNotification(new Date());
                notification2.setLu(false);
                notification2.setUrl("/contrats/locations/" + contratLocation.getId());
                notification2.setCreerPar(personne.getId());
                notification2.setCreerLe(new Date());
                notificationService.save(notification2);
            }
        }

        contratLocation.setCodeContrat("CONLOC00" + contratLocation.getId());
        return contratLocationRepository.save(contratLocation);
    }

    @Override
    public ContratLocation modifier(Principal principal, ContratLocation contratLocation) {

        Personne personne = personneService.findByUsername(principal.getName());
        contratLocation.setEtatContrat("Modifié");
        contratLocation.setModifierPar(personne.getId());
        contratLocation.setModifierLe(new Date());

        contratLocation = contratLocationRepository.save(contratLocation);

        Notification notification = new Notification();
        notification.setTitre("Modification d'une proposition de contrat de location");
        notification.setMessage("Votre proposition de contrat de location " + contratLocation.getCodeContrat() + " a été modifiée suite à votre demande de modification.");
        notification.setSendTo(String.valueOf(contratLocation.getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/contrats/locations/" + contratLocation.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (contratLocation.getBienImmobilier().getEstDelegue()) {
            if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                    personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {

                Notification notification2 = new Notification();
                notification2.setTitre("Modification d'une proposition de contrat de location");
                notification2.setMessage("La proposition de contrat faite sur le bien immobilier " + contratLocation.getBienImmobilier().getCodeBien() + " que vous avez délégué a été modifiée suite à une demande de modification");
                notification2.setSendTo(String.valueOf(contratLocation.getBienImmobilier().getPersonne().getId()));
                notification2.setDateNotification(new Date());
                notification2.setLu(false);
                notification2.setUrl("/contrats/locations/" + contratLocation.getId());
                notification2.setCreerPar(personne.getId());
                notification2.setCreerLe(new Date());
                notificationService.save(notification2);
            }
        }

        return contratLocation;
    }

    @Override
    public void valider(Principal principal, Long id) {
        ContratLocation contratLocation = contratLocationRepository.findById(id).orElse(null);
        Personne personne = personneService.findByUsername(principal.getName());

        if (contratLocation != null) {
            contratLocation.setDateSignature(new Date());
            contratLocation.setEtatContrat("En cours");

            Notification notification = new Notification();
            notification.setTitre("Validation d'une proposition de contrat de location");
            notification.setMessage("Votre proposition de contrat de location a été validée " + contratLocation.getCodeContrat() + ". Vous pouvez maintenant continuer les démarches pour la signature du contrat");
            notification.setSendTo(String.valueOf(contratLocation.getCreerPar()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/contrats/locations/" + contratLocation.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);

            if (contratLocation.getBienImmobilier().getEstDelegue()) {
                if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                        personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {
                    Notification notification2 = new Notification();
                    notification2.setTitre("Validation d'une proposition de contrat de location");
                    notification2.setMessage("La proposition de contrat faite sur le bien immobilier " + contratLocation.getBienImmobilier().getCodeBien() + " que vous avez délégué a été validée. Vous pouvez maintenant continuer les démarches pour la signature du contrat");
                    notification2.setSendTo(String.valueOf(contratLocation.getBienImmobilier().getPersonne().getId()));
                    notification2.setDateNotification(new Date());
                    notification2.setLu(false);
                    notification2.setUrl("/contrats/locations/" + contratLocation.getId());
                    notification2.setCreerPar(personne.getId());
                    notification2.setCreerLe(new Date());
                    notificationService.save(notification2);
                }
            }

            contratLocation.getDemandeLocation().getPublication().getBienImmobilier().setStatutBien("Loué");
            bienImmobilierService.setBienImmobilier(contratLocation.getDemandeLocation().getPublication().getBienImmobilier());

            publicationService.desactiverPublication(contratLocation.getDemandeLocation().getPublication().getId());

            contratLocationRepository.save(contratLocation);
        }
    }

    @Override
    public void mettreFin(Principal principal, Long id) {
        ContratLocation contratLocation = contratLocationRepository.findById(id).orElse(null);
        Personne personne = personneService.findByUsername(principal.getName());

        if (contratLocation != null) {
            contratLocation.setDateFin(new Date());
            contratLocation.setEtatContrat("Terminé");

            Notification notification = new Notification();
            notification.setTitre("Fin d'un contrat de location");
            notification.setMessage("Le contrat de location " + contratLocation.getCodeContrat() + " a pris fin");
            notification.setSendTo(String.valueOf(contratLocation.getClient().getId()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/contrats/locations/" + contratLocation.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);
        }

        contratLocation.getBienImmobilier().setStatutBien("Disponible");
        bienImmobilierService.setBienImmobilier(contratLocation.getBienImmobilier());

        publicationService.activerPublication(contratLocation.getDemandeLocation().getPublication().getId());

        contratLocationRepository.save(contratLocation);
    }

    @Override
    public void refuser(Principal principal, Long id, MotifRejetForm motifRejetForm) {
        Personne personne = personneService.findByUsername(principal.getName());

        ContratLocation contratLocation = contratLocationRepository.findById(id).orElse(null);

        if (contratLocation != null) {
            contratLocation.setEtatContrat("Refusé");
            contratLocation.setRefuserPar(personne.getId());
            contratLocation.setRefuserLe(new Date());

            Notification notification = new Notification();
            notification.setTitre("Refus d'une proposition de contrat de location");
            notification.setMessage("Votre proposition de contrat de location " + contratLocation.getCodeContrat() + " a été refusée.");
            notification.setSendTo(String.valueOf(contratLocation.getCreerPar()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/contrats/locations/" + contratLocation.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);

            if (contratLocation.getBienImmobilier().getEstDelegue()) {
                if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                        personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {

                    Notification notification2 = new Notification();
                    notification2.setTitre("Refus d'une proposition de contrat de location");
                    notification2.setMessage("La proposition de contrat faite sur le bien immobilier " + contratLocation.getBienImmobilier().getCodeBien() + " que vous avez délégué a été refusée.");
                    notification2.setSendTo(String.valueOf(contratLocation.getBienImmobilier().getPersonne().getId()));
                    notification2.setDateNotification(new Date());
                    notification2.setLu(false);
                    notification2.setUrl("/contrats/locations/" + contratLocation.getId());
                    notification2.setCreerPar(personne.getId());
                    notification2.setCreerLe(new Date());
                    notificationService.save(notification2);
                }
            }

            if (motifRejetForm != null) {
                MotifRejet motifRejet = new MotifRejet();
                motifRejet.setCode(contratLocation.getCodeContrat());
                motifRejet.setLibelle("Motif de refus de la proposition de contrat de location");
                motifRejet.setMotif(motifRejetForm.getMotif());
                motifRejetService.save(motifRejet, principal);
            }
            contratLocationRepository.save(contratLocation);
        }

    }

    @Override
    public void demandeModification(Principal principal, MotifRejetForm motifRejetForm, Long id) {
        Personne personne = personneService.findByUsername(principal.getName());

        ContratLocation contratLocation = contratLocationRepository.findById(id).orElse(null);
        if (contratLocation != null) {
            contratLocation.setEtatContrat("En attente");
            contratLocation.setModifierPar(personne.getId());
            contratLocation.setModifierLe(new Date());

            Notification notification = new Notification();
            notification.setTitre("Demande de modification d'une proposition de contrat de location");
            notification.setMessage("Votre proposition de contrat de location " + contratLocation.getCodeContrat() + " a été demandée pour modification.");
            notification.setSendTo(String.valueOf(contratLocation.getCreerPar()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/contrats/locations/" + contratLocation.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);

            if (motifRejetForm != null) {
                MotifRejet motifRejet = new MotifRejet();
                motifRejet.setCode(contratLocation.getCodeContrat());
                motifRejet.setLibelle("Motif de demande de modification de la proposition de contrat de location");
                motifRejet.setMotif(motifRejetForm.getMotif());
                motifRejetService.save(motifRejet, principal);
            }
            contratLocationRepository.save(contratLocation);
        }
    }

    @Override
    public boolean existingContratLocationByDemandeLocation(DemandeLocation demandeLocation) {
        return contratLocationRepository.existsByDemandeLocation(demandeLocation);
    }

    @Override
    public boolean existingContratLocationByBienImmobilierAndEtatContrat(BienImmobilier bienImmobilier, String etatContrat) {
        return contratLocationRepository.existsByBienImmobilierAndEtatContrat(bienImmobilier, etatContrat);
    }

    @Override
    public byte[] generateContratLocationPdf(Long id) throws IOException {
        ContratLocation contratLocation = contratLocationRepository.findById(id).orElse(null);

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

        Table table = new Table(twocolumnWidth);




        table.addCell(new Cell().add(new Paragraph("Contrat de location ").add(new Paragraph(contratLocation.getCodeContrat()))).setFont(poppinsRegular).setFontSize(14f).setBorder(Border.NO_BORDER).setBold());

//        Table nestedTable = new Table(new float[]{twocol/2, twocol/2});
//
//        nestedTable.addCell(getHeaderTextCell(new Paragraph("Date: ")));
//        nestedTable.addCell(getHeaderTextCellValue(new Paragraph(dateFormat.format(new Date()))));
//        nestedTable.addCell(getHeaderTextCell(new Paragraph("Code: ")));
//        nestedTable.addCell(getHeaderTextCellValue(new Paragraph(contratLocation.getCodeContrat())));
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

        Table TitleClient = new Table(twocolumnWidth);
        TitleClient.addCell(getHeadTitre(new Paragraph("Locataire")).setFontColor(color));

        document.add(TitleClient.setMarginBottom(12f));

        Table clientDetails = new Table(twocolumnWidth);

        clientDetails.addCell(getCell10fLeft(new Paragraph("Nom"), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph("Prénom(s)"), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getClient().getNom()), false));
        clientDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getClient().getPrenom()), false));

        clientDetails.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getClient().getEmail()), false));
        clientDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getClient().getTelephone()), false));

        clientDetails.addCell(getCell10fLeft(new Paragraph("Numero du contrat").setMarginTop(10), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph("Type de contrat").setMarginTop(10), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getCodeContrat()), false));
        clientDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getTypeContrat()), false));

        clientDetails.addCell(getCell10fLeft(new Paragraph("Loyer").setMarginTop(10), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph("Avance").setMarginTop(10), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getLoyer()) + " FCFA"), false));
        clientDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getAvance()) + " mois"), false));

        clientDetails.addCell(getCell10fLeft(new Paragraph("Caution").setMarginTop(10), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph("Date début contrat").setMarginTop(10), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getCaution()) + " mois"), false));
        clientDetails.addCell(getCell10fLeft(new Paragraph(dateFormat.format(contratLocation.getDateDebut())), false));

        clientDetails.addCell(getCell10fLeft(new Paragraph("Date fin contrat").setMarginTop(10), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph("Date prochain paiement").setMarginTop(10), true));
        clientDetails.addCell(getCell10fLeft(new Paragraph((contratLocation.getDateFin() != null) ? dateFormat.format(contratLocation.getDateFin()) : "Non connu"), false));
        clientDetails.addCell(getCell10fLeft(new Paragraph(dateFormat.format(calculerProchainPaiement(contratLocation))), false));
        document.add(clientDetails);

        if (contratLocation.getBienImmobilier().getEstDelegue()) {
            Table TitleProprietaire = new Table(twocolumnWidth).setMarginTop(10);
            TitleProprietaire.addCell(getHeadTitre(new Paragraph("Propriétaire")).setFontColor(color));

            document.add(TitleProprietaire.setMarginBottom(12f));

            Table proprietaireDetails = new Table(twocolumnWidth);

            proprietaireDetails.addCell(getCell10fLeft(new Paragraph("Nom"), true));
            proprietaireDetails.addCell(getCell10fLeft(new Paragraph("Prénom(s)"), true));
            proprietaireDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getProprietaire().getNom()), false));
            proprietaireDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getProprietaire().getPrenom()), false));

            proprietaireDetails.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
            proprietaireDetails.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
            proprietaireDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getProprietaire().getEmail()), false));
            proprietaireDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getProprietaire().getTelephone()), false));

            document.add(proprietaireDetails);

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

                float oneColumnWidth[] = { twocol150 };

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

            if (contratLocation.getAgenceImmobiliere() != null) {
                Table titleAgence = new Table(twocolumnWidth);
                titleAgence.addCell(getHeadTitre(new Paragraph("Agence").setMarginTop(10)).setFontColor(color));

                document.add(titleAgence.setMarginBottom(12f));

                Table agenceDetails = new Table(twocolumnWidth);

                agenceDetails.addCell(getCell10fLeft(new Paragraph("Nom").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getAgenceImmobiliere().getNomAgence()), false));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getAgenceImmobiliere().getAdresseEmail()), false));

                agenceDetails.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph("Adresse").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getAgenceImmobiliere().getTelephone()), false));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getAgenceImmobiliere().getAdresse()), false));

                agenceDetails.addCell(getCell10fLeft(new Paragraph("Commission").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph("Frais de visite").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getCommission()) + " mois"), false));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getFraisDeVisite()) + " FCFA"), false));

                document.add(agenceDetails);

                document.add(divider);

                Table piedPage = new Table(fullWidth);

                piedPage.setMarginTop(20);
                piedPage.addCell(new Cell().add(new Paragraph("Fait à                , le                   autant d'originaux que de parties, dont un remis à chaque client.").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));
                piedPage.addCell(new Cell().add(new Paragraph("Signatures précédées de la mention « Lu et approuvé » ").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));

                document.add(piedPage);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("L'Agence").setMarginTop(50), true));

                document.add(piedDePageDetails);

            }

            if (contratLocation.getDemarcheur() !=  null) {
                Table titleDemarcheur = new Table(twocolumnWidth);
                titleDemarcheur.addCell(getHeadTitre(new Paragraph("Démarcheur").setMarginTop(10)).setFontColor(color));

                document.add(titleDemarcheur.setMarginBottom(12f));

                Table demarcheurDetails = new Table(twocolumnWidth);
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Nom").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Prénom(s)").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemarcheur().getNom()), false));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemarcheur().getPrenom()), false));

                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemarcheur().getEmail()), false));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemarcheur().getTelephone()), false));

                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Commission").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Frais de visite").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getCommission()) + " mois"), false));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getFraisDeVisite()) + " FCFA"), false));

                document.add(demarcheurDetails);

                document.add(divider);

                Table piedPage = new Table(fullWidth);

                piedPage.setMarginTop(20);
                piedPage.addCell(new Cell().add(new Paragraph("Fait à                , le                   autant d'originaux que de parties, dont un remis à chaque client.").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));
                piedPage.addCell(new Cell().add(new Paragraph("Signatures précédées de la mention « Lu et approuvé » ").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));

                document.add(piedPage);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Démarcheur").setMarginTop(50), true));

                document.add(piedDePageDetails);

            }

            if (contratLocation.getGerant() != null) {
                Table titleGerant = new Table(twocolumnWidth);
                titleGerant.addCell(getHeadTitre(new Paragraph("Gérant").setMarginTop(10)).setFontColor(color));

                document.add(titleGerant);

                Table gerantDetails = new Table(twocolumnWidth);
                gerantDetails.addCell(getCell10fLeft(new Paragraph("Nom").setMarginTop(10), true));
                gerantDetails.addCell(getCell10fLeft(new Paragraph("Prénom(s)").setMarginTop(10), true));
                gerantDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getGerant().getNom()), false));
                gerantDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getGerant().getPrenom()), false));

                gerantDetails.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                gerantDetails.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                gerantDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getGerant().getEmail()), false));
                gerantDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getGerant().getTelephone()), false));

                document.add(gerantDetails);

                document.add(divider);

                Table piedPage = new Table(fullWidth);

                piedPage.setMarginTop(20);
                piedPage.addCell(new Cell().add(new Paragraph("Fait à                , le                   autant d'originaux que de parties, dont un remis à chaque client.").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));
                piedPage.addCell(new Cell().add(new Paragraph("Signatures précédées de la mention « Lu et approuvé » ").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));

                document.add(piedPage);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Gérant").setMarginTop(50), true));

                document.add(piedDePageDetails);
            }

        } else if (!contratLocation.getBienImmobilier().getEstDelegue()) {
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

                float oneColumnWidth[] = { twocol150 };

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

            if (contratLocation.getDemandeLocation().getPublication().getAgenceImmobiliere() != null) {
                Table titleAgence = new Table(twocolumnWidth);
                titleAgence.addCell(getHeadTitre(new Paragraph("Agence").setMarginTop(10)).setFontColor(color));

                document.add(titleAgence.setMarginBottom(12f));

                Table agenceDetails = new Table(twocolumnWidth);

                agenceDetails.addCell(getCell10fLeft(new Paragraph("Nom").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemandeLocation().getPublication().getAgenceImmobiliere().getNomAgence()), false));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemandeLocation().getPublication().getAgenceImmobiliere().getAdresseEmail()), false));

                agenceDetails.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph("Adresse").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemandeLocation().getPublication().getAgenceImmobiliere().getTelephone()), false));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemandeLocation().getPublication().getAgenceImmobiliere().getAdresse()), false));

                agenceDetails.addCell(getCell10fLeft(new Paragraph("Commission").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph("Frais de visite").setMarginTop(10), true));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getCommission()) + " mois"), false));
                agenceDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getFraisDeVisite()) + " FCFA"), false));

                document.add(agenceDetails);

                document.add(divider);

                Table piedPage = new Table(fullWidth);

                piedPage.setMarginTop(20);
                piedPage.addCell(new Cell().add(new Paragraph("Fait à                , le                   autant d'originaux que de parties, dont un remis à chaque client.").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));
                piedPage.addCell(new Cell().add(new Paragraph("Signatures précédées de la mention « Lu et approuvé » ").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));

                document.add(piedPage);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("L'Agence").setMarginTop(10), true));

                document.add(piedDePageDetails);

            }

            if (contratLocation.getProprietaire() != null) {
                Table titleGerant = new Table(twocolumnWidth);
                titleGerant.addCell(getHeadTitre(new Paragraph("Propriétaire").setMarginTop(10)).setFontColor(color));

                document.add(titleGerant);

                Table gerantDetails = new Table(twocolumnWidth);
                gerantDetails.addCell(getCell10fLeft(new Paragraph("Nom").setMarginTop(10), true));
                gerantDetails.addCell(getCell10fLeft(new Paragraph("Prénom(s)").setMarginTop(10), true));
                gerantDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getProprietaire().getNom()), false));
                gerantDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getProprietaire().getPrenom()), false));

                gerantDetails.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                gerantDetails.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                gerantDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getProprietaire().getEmail()), false));
                gerantDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getProprietaire().getTelephone()), false));

                document.add(gerantDetails);

                document.add(divider);

                Table piedPage = new Table(fullWidth);

                piedPage.setMarginTop(20);
                piedPage.addCell(new Cell().add(new Paragraph("Fait à                , le                   autant d'originaux que de parties, dont un remis à chaque client.").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));
                piedPage.addCell(new Cell().add(new Paragraph("Signatures précédées de la mention « Lu et approuvé » ").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));

                document.add(piedPage);

                Table piedDePageDetails = new Table(twocolumnWidth);

                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Locataire").setMarginTop(10), true));
                piedDePageDetails.addCell(getCell10fLeft(new Paragraph("Le Propriétaire").setMarginTop(10), true));

                document.add(piedDePageDetails);
            }

            if (contratLocation.getDemarcheur() != null) {
                Table titleDemarcheur = new Table(twocolumnWidth);
                titleDemarcheur.addCell(getHeadTitre(new Paragraph("Démarcheur").setMarginTop(10)).setFontColor(color));

                document.add(titleDemarcheur.setMarginBottom(12f));

                Table demarcheurDetails = new Table(twocolumnWidth);
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Nom").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Prénom(s)").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemarcheur().getNom()), false));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemarcheur().getPrenom()), false));

                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemarcheur().getEmail()), false));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(contratLocation.getDemarcheur().getTelephone()), false));

                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Commission").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph("Frais de visite").setMarginTop(10), true));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getCommission()) + " mois"), false));
                demarcheurDetails.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratLocation.getFraisDeVisite()) + " FCFA"), false));

                document.add(demarcheurDetails);

                document.add(divider);

                Table piedPage = new Table(fullWidth);

                piedPage.setMarginTop(20);
                piedPage.addCell(new Cell().add(new Paragraph("Fait à                , le                   autant d'originaux que de parties, dont un remis à chaque client.").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));
                piedPage.addCell(new Cell().add(new Paragraph("Signatures précédées de la mention « Lu et approuvé » ").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));

                document.add(piedPage);

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

    public boolean afficherCategorie(ContratLocation contratLocation) {
        return contratLocation.getBienImmobilier().getTypeDeBien().getDesignation().equals("Maison") ||
                contratLocation.getBienImmobilier().getTypeDeBien().getDesignation().equals("Villa") ||
                contratLocation.getBienImmobilier().getTypeDeBien().getDesignation().equals("Immeuble") ||
                contratLocation.getBienImmobilier().getTypeDeBien().getDesignation().equals("Appartement") ||
                contratLocation.getBienImmobilier().getTypeDeBien().getDesignation().equals("Chambre salon") ||
                contratLocation.getBienImmobilier().getTypeDeBien().getDesignation().equals("Chambre") ||
                contratLocation.getBienImmobilier().getTypeDeBien().getDesignation().equals("Bureau");
    }

    public Date calculerProchainPaiement(ContratLocation contratLocation) {

        Date dateDebut = contratLocation.getDateDebut();
        int debutPaiement = contratLocation.getDebutPaiement();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateDebut);

        if (contratLocation.getJourSupplementPaiement() != null) {
            calendar.add(Calendar.DAY_OF_MONTH, contratLocation.getJourSupplementPaiement());
        }

        calendar.add(Calendar.MONTH, debutPaiement);

        return calendar.getTime();
    }

}