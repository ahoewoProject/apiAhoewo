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
import com.memoire.apiAhoewo.dto.MotifRejetForm;
import com.memoire.apiAhoewo.models.MotifRejet;
import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratVente;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeAchat;
import com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes.ContratVenteRepository;
import com.memoire.apiAhoewo.services.MotifRejetService;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.ContratVenteService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.DemandeAchatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContratVenteServiceImpl implements ContratVenteService {
    @Autowired
    private ContratVenteRepository contratVenteRepository;
    @Autowired
    private DemandeAchatService demandeAchatService;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MotifRejetService motifRejetService;

    @Override
    public Page<ContratVente> getContratVentes(Principal principal, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        List<DemandeAchat> demandeAchatList = demandeAchatService.getDemandesAchats(principal);

        return contratVenteRepository.findByDemandeAchatInOrderByIdDesc(demandeAchatList, pageRequest);
    }

    @Override
    public Page<ContratVente> getContratVentesByCodeBienAndEtatContrat(String codeBien, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);

        List<ContratVente> contratVenteList = contratVenteRepository.findByBienImmobilier_CodeBienOrderByIdDesc(codeBien);

        List<ContratVente> contratVenteList1 =  contratVenteList.stream()
                .filter(contratVente -> "Confirmé".equals(contratVente.getEtatContrat()))
                .collect(Collectors.toList());

        List<ContratVente> contratVenteList2 = contratVenteList.stream()
                .filter(contratVente -> "Modifié".equals(contratVente.getEtatContrat()))
                .collect(Collectors.toList());

        List<ContratVente> contratVenteList3 = contratVenteList.stream()
                .filter(contratVente -> "En attente".equals(contratVente.getEtatContrat()))
                .collect(Collectors.toList());

        List<ContratVente> contratVenteList4 = contratVenteList.stream()
                .filter(contratVente -> "Validé".equals(contratVente.getEtatContrat()))
                .collect(Collectors.toList());

        List<ContratVente> contratVenteList5 =  contratVenteList.stream()
                .filter(contratVente -> "Refusé".equals(contratVente.getEtatContrat()))
                .collect(Collectors.toList());

        List<ContratVente> contratVenteArrayList = new ArrayList<>();
        contratVenteArrayList.addAll(contratVenteList1);
        contratVenteArrayList.addAll(contratVenteList2);
        contratVenteArrayList.addAll(contratVenteList3);
        contratVenteArrayList.addAll(contratVenteList4);
        contratVenteArrayList.addAll(contratVenteList5);

        int start = numeroDeLaPage * elementsParPage;
        int end = Math.min(start + elementsParPage, contratVenteArrayList.size());
        List<ContratVente> paginatedContrats = contratVenteArrayList.subList(start, end);

        return new PageImpl<>(paginatedContrats, pageRequest, contratVenteArrayList.size());
    }

    @Override
    public List<ContratVente> getContratVentes(Principal principal) {
        List<DemandeAchat> demandeAchatList = demandeAchatService.getDemandesAchats(principal);

        return contratVenteRepository.findByDemandeAchatIn(demandeAchatList);
    }

    @Override
    public List<ContratVente> getContratVentesEnAttente() {
        return contratVenteRepository.findByEtatContrat("En attente");
    }

    @Override
    public ContratVente findById(Long id) {
        return contratVenteRepository.findById(id).orElse(null);
    }

    @Override
    public ContratVente save(ContratVente contratVente, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        contratVente.setCodeContrat("CONACH" + UUID.randomUUID());
        contratVente.setEtatContrat("En attente");
        contratVente.setCreerPar(personne.getId());
        contratVente.setCreerLe(new Date());

        contratVente = contratVenteRepository.save(contratVente);

        Notification notification1 = new Notification();
        notification1.setTitre("Proposition d'un contrat de vente");
        notification1.setMessage("Vous avez reçu une proposition de contrat de vente suite à la validation de votre demande d'achat " + contratVente.getDemandeAchat().getCodeDemande());
        notification1.setSendTo(String.valueOf(contratVente.getDemandeAchat().getClient().getId()));
        notification1.setDateNotification(new Date());
        notification1.setLu(false);
        notification1.setUrl("/contrats/ventes/" + contratVente.getId());
        notification1.setCreerPar(personne.getId());
        notification1.setCreerLe(new Date());
        notificationService.save(notification1);

        if (contratVente.getBienImmobilier().getEstDelegue()) {
            if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                    personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {
                Notification notification2 = new Notification();
                notification2.setTitre("Proposition d'un contrat de vente");
                notification2.setMessage("Une proposition de contrat a été faite sur le bien immobilier " + contratVente.getBienImmobilier().getCodeBien() + " que vous avez délégué");
                notification2.setSendTo(String.valueOf(contratVente.getBienImmobilier().getPersonne().getId()));
                notification2.setDateNotification(new Date());
                notification2.setLu(false);
                notification2.setUrl("/contrats/ventes/" + contratVente.getId());
                notification2.setCreerPar(personne.getId());
                notification2.setCreerLe(new Date());
                notificationService.save(notification2);
            }
        }

        contratVente.setCodeContrat("CONACH00" + contratVente.getId());
        return contratVenteRepository.save(contratVente);
    }

    @Override
    public ContratVente modifier(Principal principal, ContratVente contratVente) {

        Personne personne = personneService.findByUsername(principal.getName());
        contratVente.setEtatContrat("Modifié");
        contratVente.setModifierPar(personne.getId());
        contratVente.setModifierLe(new Date());

        contratVente = contratVenteRepository.save(contratVente);

        Notification notification = new Notification();
        notification.setTitre("Modification d'une proposition de contrat de vente");
        notification.setMessage("Votre proposition de contrat de vente " + contratVente.getCodeContrat() + " a été modifiée suite à votre demande de modification.");
        notification.setSendTo(String.valueOf(contratVente.getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/contrats/ventes/" + contratVente.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (contratVente.getBienImmobilier().getEstDelegue()) {
            if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                    personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {

                Notification notification2 = new Notification();
                notification2.setTitre("Modification d'une proposition de contrat de vente");
                notification2.setMessage("La proposition de contrat faite sur le bien immobilier " + contratVente.getBienImmobilier().getCodeBien() + " que vous avez délégué a été modifiée suite à une demande de modification");
                notification2.setSendTo(String.valueOf(contratVente.getBienImmobilier().getPersonne().getId()));
                notification2.setDateNotification(new Date());
                notification2.setLu(false);
                notification2.setUrl("/contrats/ventes/" + contratVente.getId());
                notification2.setCreerPar(personne.getId());
                notification2.setCreerLe(new Date());
                notificationService.save(notification2);
            }
        }

        return contratVente;
    }

    @Override
    public ContratVente setEtatContrat(ContratVente contratVente) {
        return contratVenteRepository.save(contratVente);
    }

    @Override
    public void valider(Principal principal, Long id) {
        ContratVente contratVente = contratVenteRepository.findById(id).orElse(null);
        Personne personne = personneService.findByUsername(principal.getName());

        if (contratVente != null) {
            contratVente.setDateSignature(new Date());
            contratVente.setEtatContrat("Validé");

            Notification notification = new Notification();
            notification.setTitre("Validation d'une proposition de contrat de vente");
            notification.setMessage("Votre proposition de contrat de vente a été validée " + contratVente.getCodeContrat() + ". Vous pouvez maintenant continuer les démarches pour la signature du contrat");
            notification.setSendTo(String.valueOf(contratVente.getCreerPar()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/contrats/ventes/" + contratVente.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);

            if (contratVente.getBienImmobilier().getEstDelegue()) {
                if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                        personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {
                    Notification notification2 = new Notification();
                    notification2.setTitre("Validation d'une proposition de contrat de vente");
                    notification2.setMessage("La proposition de contrat faite sur le bien immobilier " + contratVente.getBienImmobilier().getCodeBien() + " que vous avez délégué a été validée. Vous pouvez maintenant continuer les démarches pour la signature du contrat");
                    notification2.setSendTo(String.valueOf(contratVente.getBienImmobilier().getPersonne().getId()));
                    notification2.setDateNotification(new Date());
                    notification2.setLu(false);
                    notification2.setUrl("/contrats/ventes/" + contratVente.getId());
                    notification2.setCreerPar(personne.getId());
                    notification2.setCreerLe(new Date());
                    notificationService.save(notification2);
                }
            }

            contratVenteRepository.save(contratVente);
        }
    }

    @Override
    public void refuser(Principal principal, Long id, MotifRejetForm motifRejetForm) {
        ContratVente contratVente = contratVenteRepository.findById(id).orElse(null);
        Personne personne = personneService.findByUsername(principal.getName());

        if (contratVente != null) {
            contratVente.setEtatContrat("Refusé");
            contratVente.setRefuserPar(personne.getId());
            contratVente.setRefuserLe(new Date());

            Notification notification = new Notification();
            notification.setTitre("Refus d'une proposition de contrat de vente");
            notification.setMessage("Votre proposition de contrat de vente " + contratVente.getCodeContrat() + " a été refusée.");
            notification.setSendTo(String.valueOf(contratVente.getCreerPar()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/contrats/ventes/" + contratVente.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);

            if (contratVente.getBienImmobilier().getEstDelegue()) {
                if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                        personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {
                    Notification notification2 = new Notification();
                    notification2.setTitre("Refus d'une proposition de contrat de vente");
                    notification2.setMessage("La proposition de contrat faite sur le bien immobilier " + contratVente.getBienImmobilier().getCodeBien() + " que vous avez délégué a été refusée.");
                    notification2.setSendTo(String.valueOf(contratVente.getBienImmobilier().getPersonne().getId()));
                    notification2.setDateNotification(new Date());
                    notification2.setLu(false);
                    notification2.setUrl("/contrats/ventes/" + contratVente.getId());
                    notification2.setCreerPar(personne.getId());
                    notification2.setCreerLe(new Date());
                    notificationService.save(notification2);
                }
            }

            if (motifRejetForm != null) {
                MotifRejet motifRejet = new MotifRejet();
                motifRejet.setCode(contratVente.getCodeContrat());
                motifRejet.setLibelle("Motif de refus de la proposition de contrat de vente");
                motifRejet.setMotif(motifRejetForm.getMotif());
                motifRejetService.save(motifRejet, principal);
            }
            contratVenteRepository.save(contratVente);
        }
    }

    @Override
    public void demandeModification(Principal principal, Long id, MotifRejetForm motifRejetForm) {
        Personne personne = personneService.findByUsername(principal.getName());

        ContratVente contratVente = contratVenteRepository.findById(id).orElse(null);
        if (contratVente != null) {
            contratVente.setEtatContrat("En attente");
            contratVente.setModifierPar(personne.getId());
            contratVente.setModifierLe(new Date());

            Notification notification = new Notification();
            notification.setTitre("Demande de modification d'une proposition de contrat de vente");
            notification.setMessage("Votre proposition de contrat de vente " + contratVente.getCodeContrat() + " a été demandée pour modification.");
            notification.setSendTo(String.valueOf(contratVente.getCreerPar()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/contrats/ventes/" + contratVente.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);

            if (motifRejetForm != null) {
                MotifRejet motifRejet = new MotifRejet();
                motifRejet.setCode(contratVente.getCodeContrat());
                motifRejet.setLibelle("Motif de demande de modification de la proposition de contrat de vente");
                motifRejet.setMotif(motifRejetForm.getMotif());
                motifRejetService.save(motifRejet, principal);
            }
            contratVenteRepository.save(contratVente);
        }
    }

    @Override
    public boolean existingContratLocationByDemandeAchat(DemandeAchat demandeAchat) {
        return this.contratVenteRepository.existsByDemandeAchat(demandeAchat);
    }

    @Override
    public boolean existingContratLocationByBienImmobilierAndEtatContrat(BienImmobilier bienImmobilier, String etatContrat) {
        return this.contratVenteRepository.existsByBienImmobilierAndEtatContrat(bienImmobilier, etatContrat);
    }

    @Override
    public byte[] generateContratVentePdf(Long id) throws IOException {
        ContratVente contratVente = contratVenteRepository.findById(id).orElse(null);

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


        table.addCell(new Cell().add(new Paragraph("Contrat de vente ").add(new Paragraph(contratVente.getCodeContrat()))).setFontSize(14f).setBorder(Border.NO_BORDER).setBold());

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

        Table titleAcquereur = new Table(twocolumnWidth);
        titleAcquereur.addCell(getHeadTitre(new Paragraph("Acquéreur")).setFontColor(color));

        document.add(titleAcquereur.setMarginBottom(12f));

        Table detailAcquereurTwoColumn = new Table(twocolumnWidth);

        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph("Nom"), true));
        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph("Prénom(s)"), true));
        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getClient().getNom()), false));
        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getClient().getPrenom()), false));

        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getClient().getEmail()), false));
        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getClient().getTelephone()), false));

        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph("Numero du contrat").setMarginTop(10), true));
        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph("Prix de vente").setMarginTop(10), true));
        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getCodeContrat()), false));
        detailAcquereurTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getPrixVente()) + " FCFA"), false));

        document.add(detailAcquereurTwoColumn);

        float oneColumnWidth[] = { twocol150 };

        Table detailAcquereurOneColumn = new Table(oneColumnWidth);
        detailAcquereurOneColumn.addCell(getCell10fLeft(new Paragraph("Nombre de tranches du paiement").setMarginTop(10), true));
        detailAcquereurOneColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getNombreDeTranche()) + " tranches"), false));

        document.add(detailAcquereurOneColumn.setMarginBottom(12f));

        if (contratVente.getBienImmobilier().getEstDelegue()) {
            Table titleVendeur = new Table(twocolumnWidth);
            titleVendeur.addCell(getHeadTitre(new Paragraph("Vendeur")).setFontColor(color));

            document.add(titleVendeur.setMarginBottom(12f));

            Table detailVendeurTwoColumn = new Table(twocolumnWidth);

            detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Nom"), true));
            detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Prénom(s)"), true));
            detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getProprietaire().getNom()), false));
            detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getProprietaire().getPrenom()), false));

            detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
            detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
            detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getProprietaire().getEmail()), false));
            detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getProprietaire().getTelephone()), false));

            document.add(detailVendeurTwoColumn);

            if (contratVente.getAgenceImmobiliere() != null) {
                Table titleAgence = new Table(twocolumnWidth);
                titleAgence.addCell(getHeadTitre(new Paragraph("Agence").setMarginTop(10)).setFontColor(color));

                document.add(titleAgence.setMarginBottom(12f));

                Table detailAgence = new Table(twocolumnWidth);

                detailAgence.addCell(getCell10fLeft(new Paragraph("Nom").setMarginTop(10), true));
                detailAgence.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                detailAgence.addCell(getCell10fLeft(new Paragraph(contratVente.getAgenceImmobiliere().getNomAgence()), false));
                detailAgence.addCell(getCell10fLeft(new Paragraph(contratVente.getAgenceImmobiliere().getAdresseEmail()), false));

                detailAgence.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                detailAgence.addCell(getCell10fLeft(new Paragraph("Adresse").setMarginTop(10), true));
                detailAgence.addCell(getCell10fLeft(new Paragraph(contratVente.getAgenceImmobiliere().getTelephone()), false));
                detailAgence.addCell(getCell10fLeft(new Paragraph(contratVente.getAgenceImmobiliere().getAdresse()), false));

                detailAgence.addCell(getCell10fLeft(new Paragraph("Commission").setMarginTop(10), true));
                detailAgence.addCell(getCell10fLeft(new Paragraph("Frais de visite").setMarginTop(10), true));
                detailAgence.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getCommission()) + " mois"), false));
                detailAgence.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getFraisDeVisite()) + " FCFA"), false));

                document.add(detailAgence);
            }

            if (contratVente.getDemarcheur() != null) {
                Table titleDemarcheur = new Table(twocolumnWidth);
                titleDemarcheur.addCell(getHeadTitre(new Paragraph("Vendeur")).setFontColor(color));

                document.add(titleDemarcheur.setMarginBottom(12f));

                Table detailDemarcheurTwoColumn = new Table(twocolumnWidth);

                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph("Nom"), true));
                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph("Prénom(s)"), true));
                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getDemarcheur().getNom()), false));
                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getDemarcheur().getPrenom()), false));

                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getDemarcheur().getEmail()), false));
                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getDemarcheur().getTelephone()), false));

                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph("Commission").setMarginTop(10), true));
                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph("Frais de visite").setMarginTop(10), true));
                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getCommission()) + " mois"), false));
                detailDemarcheurTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getFraisDeVisite()) + " FCFA"), false));

                document.add(detailDemarcheurTwoColumn);
            }

            if (contratVente.getGerant() != null) {
                Table titleGerant = new Table(twocolumnWidth);
                titleGerant.addCell(getHeadTitre(new Paragraph("Vendeur")).setFontColor(color));

                document.add(titleGerant.setMarginBottom(12f));

                Table detailGerantTwoColumn = new Table(twocolumnWidth);

                detailGerantTwoColumn.addCell(getCell10fLeft(new Paragraph("Nom"), true));
                detailGerantTwoColumn.addCell(getCell10fLeft(new Paragraph("Prénom(s)"), true));
                detailGerantTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getGerant().getNom()), false));
                detailGerantTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getGerant().getPrenom()), false));

                detailGerantTwoColumn.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                detailGerantTwoColumn.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                detailGerantTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getGerant().getEmail()), false));
                detailGerantTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getGerant().getTelephone()), false));

                document.add(detailGerantTwoColumn);
            }

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

            document.add(divider);

            Table textPiedPage = new Table(fullWidth);
            textPiedPage.setMarginTop(20);

            textPiedPage.addCell(new Cell().add(new Paragraph("Fait à                , le                 ce contrat est délivré à l'acquéreur pour servir et valoir ce que de droit.").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));
            textPiedPage.setMarginBottom(30);
            textPiedPage.addCell(new Cell().add(new Paragraph("ONT SIGNÉS").setMarginTop(10).setFontSize(10f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
            document.add(textPiedPage);

            Table piedDePageDetails1 = new Table(twocolumnWidth);

            piedDePageDetails1.addCell(getCell10fLeft(new Paragraph("LE VENDEUR").setMarginTop(10), true));
            piedDePageDetails1.addCell(getCell10fLeft(new Paragraph("L'ACQUÉREUR").setMarginTop(10), true));
            piedDePageDetails1.addCell(getCell10fLeft(new Paragraph(contratVente.getProprietaire().getNom() + " " + contratVente.getProprietaire().getPrenom() + " " + contratVente.getProprietaire().getTelephone()).setMarginTop(10), false));
            piedDePageDetails1.addCell(getCell10fLeft(new Paragraph(contratVente.getClient().getNom() + " " + contratVente.getClient().getPrenom() + " " + contratVente.getClient().getTelephone()).setMarginTop(10), false));

            document.add(piedDePageDetails1);

            Table piedDePageDetails2 = new Table(twocolumnWidth);

            piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("TÉMOINS VENDEUR").setMarginTop(20), true));
            piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("TÉMOINS ACQUÉREUR").setMarginTop(20), true));
            piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("1."  + contratVente.getNomPrenomTemoin1Vendeur() + " "+ contratVente.getContactTemoin1Vendeur()).setMarginTop(10), false));
            piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("1."  + contratVente.getNomPrenomTemoin1Acheteur() + " "+ contratVente.getContactTemoin1Acheteur()).setMarginTop(10), false));

            if (contratVente.getNomPrenomTemoin2Vendeur() != null && contratVente.getContactTemoin2Vendeur() != null) {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("2. " + contratVente.getNomPrenomTemoin2Vendeur() + " " + contratVente.getContactTemoin2Vendeur()).setMarginTop(10), false));
            } else {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("2. ").setMarginTop(10), false));
            }

            if (contratVente.getNomPrenomTemoin2Acheteur() != null && contratVente.getContactTemoin2Acheteur() != null) {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("2. " + contratVente.getNomPrenomTemoin2Acheteur() + " " + contratVente.getContactTemoin2Acheteur()).setMarginTop(10), false));
            } else {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("2. ").setMarginTop(10), false));
            }

            if (contratVente.getNomPrenomTemoin3Vendeur() != null && contratVente.getContactTemoin3Vendeur() != null) {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("3. " + contratVente.getNomPrenomTemoin3Vendeur() + " " + contratVente.getContactTemoin3Vendeur()).setMarginTop(10), false));
            } else {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("3. ").setMarginTop(10), false));
            }

            if (contratVente.getNomPrenomTemoin3Acheteur() != null && contratVente.getContactTemoin3Acheteur() != null) {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("3. " + contratVente.getNomPrenomTemoin3Acheteur() + " " + contratVente.getContactTemoin3Acheteur()).setMarginTop(10), false));
            } else {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("3. ").setMarginTop(10), false));
            }


            document.add(piedDePageDetails2);

            Table autreDetail = new Table(oneColumnWidth);

            if (contratVente.getAgenceImmobiliere() != null) {
                autreDetail.addCell(getCell10fLeft(new Paragraph("Agence").setMarginTop(20), true));
                autreDetail.addCell(getCell10fLeft(new Paragraph(contratVente.getAgenceImmobiliere().getNomAgence() + " " + contratVente.getAgenceImmobiliere().getTelephone()).setMarginTop(10), false));
            }

            if (contratVente.getDemarcheur() != null) {
                autreDetail.addCell(getCell10fLeft(new Paragraph("Démarcheur").setMarginTop(20), true));
                autreDetail.addCell(getCell10fLeft(new Paragraph(contratVente.getDemarcheur().getNom() + " " + contratVente.getDemarcheur().getPrenom() + " " + contratVente.getDemarcheur().getTelephone()).setMarginTop(10), false));
            }

            if (contratVente.getGerant() != null) {
                autreDetail.addCell(getCell10fLeft(new Paragraph("Gérant").setMarginTop(20), true));
                autreDetail.addCell(getCell10fLeft(new Paragraph(contratVente.getGerant().getNom() + " " + contratVente.getGerant().getPrenom() + " " + contratVente.getGerant().getTelephone()).setMarginTop(10), false));
            }
            document.add(autreDetail);
        } else {
            if (contratVente.getProprietaire() != null) {
                Table titleVendeur = new Table(twocolumnWidth);
                titleVendeur.addCell(getHeadTitre(new Paragraph("Vendeur")).setFontColor(color));

                document.add(titleVendeur.setMarginBottom(12f));

                Table detailVendeurTwoColumn = new Table(twocolumnWidth);

                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Nom"), true));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Prénom(s)"), true));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getProprietaire().getNom()), false));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getProprietaire().getPrenom()), false));

                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getProprietaire().getEmail()), false));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getProprietaire().getTelephone()), false));

                document.add(detailVendeurTwoColumn);
            }

            if (contratVente.getDemarcheur() != null) {
                Table titleVendeur = new Table(twocolumnWidth);
                titleVendeur.addCell(getHeadTitre(new Paragraph("Vendeur")).setFontColor(color));

                document.add(titleVendeur.setMarginBottom(12f));

                Table detailVendeurTwoColumn = new Table(twocolumnWidth);

                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Nom"), true));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Prénom(s)"), true));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getDemarcheur().getNom()), false));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getDemarcheur().getPrenom()), false));

                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getDemarcheur().getEmail()), false));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(contratVente.getDemarcheur().getTelephone()), false));

                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Commission").setMarginTop(10), true));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph("Frais de visite").setMarginTop(10), true));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getCommission()) + " mois"), false));
                detailVendeurTwoColumn.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getFraisDeVisite()) + " FCFA"), false));

                document.add(detailVendeurTwoColumn);
            }

            if (contratVente.getAgenceImmobiliere() != null) {
                Table titleVendeur = new Table(twocolumnWidth);
                titleVendeur.addCell(getHeadTitre(new Paragraph("Vendeur").setMarginTop(10)).setFontColor(color));

                document.add(titleVendeur.setMarginBottom(12f));

                Table detailVendeur = new Table(twocolumnWidth);

                detailVendeur.addCell(getCell10fLeft(new Paragraph("Nom").setMarginTop(10), true));
                detailVendeur.addCell(getCell10fLeft(new Paragraph("Email").setMarginTop(10), true));
                detailVendeur.addCell(getCell10fLeft(new Paragraph(contratVente.getAgenceImmobiliere().getNomAgence()), false));
                detailVendeur.addCell(getCell10fLeft(new Paragraph(contratVente.getAgenceImmobiliere().getAdresseEmail()), false));

                detailVendeur.addCell(getCell10fLeft(new Paragraph("Téléphone").setMarginTop(10), true));
                detailVendeur.addCell(getCell10fLeft(new Paragraph("Adresse").setMarginTop(10), true));
                detailVendeur.addCell(getCell10fLeft(new Paragraph(contratVente.getAgenceImmobiliere().getTelephone()), false));
                detailVendeur.addCell(getCell10fLeft(new Paragraph(contratVente.getAgenceImmobiliere().getAdresse()), false));

                detailVendeur.addCell(getCell10fLeft(new Paragraph("Commission").setMarginTop(10), true));
                detailVendeur.addCell(getCell10fLeft(new Paragraph("Frais de visite").setMarginTop(10), true));
                detailVendeur.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getCommission()) + " mois"), false));
                detailVendeur.addCell(getCell10fLeft(new Paragraph(String.valueOf(contratVente.getFraisDeVisite()) + " FCFA"), false));

                document.add(detailVendeur);

            }

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

            document.add(divider);

            Table textPiedPage = new Table(fullWidth);
            textPiedPage.setMarginTop(20);

            textPiedPage.addCell(new Cell().add(new Paragraph("Fait à                , le                 ce contrat est délivré à l'acquéreur pour servir et valoir ce que de droit.").setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED)).setBorder(Border.NO_BORDER));
            textPiedPage.setMarginBottom(30);
            textPiedPage.addCell(new Cell().add(new Paragraph("ONT SIGNÉS").setMarginTop(10).setFontSize(10f).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
            document.add(textPiedPage);

            Table piedDePageDetails1 = new Table(twocolumnWidth);

            piedDePageDetails1.addCell(getCell10fLeft(new Paragraph("LE VENDEUR").setMarginTop(10), true));
            piedDePageDetails1.addCell(getCell10fLeft(new Paragraph("L'ACQUÉREUR").setMarginTop(10), true));
            if (contratVente.getAgenceImmobiliere() != null) {
                piedDePageDetails1.addCell(getCell10fLeft(new Paragraph(contratVente.getAgenceImmobiliere().getNomAgence() + " " + contratVente.getAgenceImmobiliere().getTelephone()).setMarginTop(10), false));
            }
            if (contratVente.getDemarcheur() != null) {
                piedDePageDetails1.addCell(getCell10fLeft(new Paragraph(contratVente.getDemarcheur().getNom() + " " + contratVente.getDemarcheur().getPrenom() + " " + contratVente.getDemarcheur().getTelephone()).setMarginTop(10), false));
            }

            if (contratVente.getProprietaire() != null) {
                piedDePageDetails1.addCell(getCell10fLeft(new Paragraph(contratVente.getProprietaire().getNom() + " " + contratVente.getProprietaire().getPrenom() + " " + contratVente.getProprietaire().getTelephone()).setMarginTop(10), false));
            }
            piedDePageDetails1.addCell(getCell10fLeft(new Paragraph(contratVente.getClient().getNom() + " " + contratVente.getClient().getPrenom() + " " + contratVente.getClient().getTelephone()).setMarginTop(10), false));

            document.add(piedDePageDetails1);

            Table piedDePageDetails2 = new Table(twocolumnWidth);

            piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("TÉMOINS VENDEUR").setMarginTop(20), true));
            piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("TÉMOINS ACQUÉREUR").setMarginTop(20), true));
            piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("1."  + contratVente.getNomPrenomTemoin1Vendeur() + " "+ contratVente.getContactTemoin1Vendeur()).setMarginTop(10), false));
            piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("1."  + contratVente.getNomPrenomTemoin1Acheteur() + " "+ contratVente.getContactTemoin1Acheteur()).setMarginTop(10), false));

            if (contratVente.getNomPrenomTemoin2Vendeur() != null && contratVente.getContactTemoin2Vendeur() != null) {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("2. " + contratVente.getNomPrenomTemoin2Vendeur() + " " + contratVente.getContactTemoin2Vendeur()).setMarginTop(10), false));
            } else {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("2. ").setMarginTop(10), false));
            }

            if (contratVente.getNomPrenomTemoin2Acheteur() != null && contratVente.getContactTemoin2Acheteur() != null) {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("2. " + contratVente.getNomPrenomTemoin2Acheteur() + " " + contratVente.getContactTemoin2Acheteur()).setMarginTop(10), false));
            } else {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("2. ").setMarginTop(10), false));
            }

            if (contratVente.getNomPrenomTemoin3Vendeur() != null && contratVente.getContactTemoin3Vendeur() != null) {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("3. " + contratVente.getNomPrenomTemoin3Vendeur() + " " + contratVente.getContactTemoin3Vendeur()).setMarginTop(10), false));
            } else {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("3. ").setMarginTop(10), false));
            }

            if (contratVente.getNomPrenomTemoin3Acheteur() != null && contratVente.getContactTemoin3Acheteur() != null) {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("3. " + contratVente.getNomPrenomTemoin3Acheteur() + " " + contratVente.getContactTemoin3Acheteur()).setMarginTop(10), false));
            } else {
                piedDePageDetails2.addCell(getCell10fLeft(new Paragraph("3. ").setMarginTop(10), false));
            }

            document.add(piedDePageDetails2);

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

    public boolean afficherCategorie(ContratVente contratVente) {
        return contratVente.getBienImmobilier().getTypeDeBien().getDesignation().equals("Maison") ||
                contratVente.getBienImmobilier().getTypeDeBien().getDesignation().equals("Villa") ||
                contratVente.getBienImmobilier().getTypeDeBien().getDesignation().equals("Immeuble") ||
                contratVente.getBienImmobilier().getTypeDeBien().getDesignation().equals("Appartement") ||
                contratVente.getBienImmobilier().getTypeDeBien().getDesignation().equals("Chambre salon") ||
                contratVente.getBienImmobilier().getTypeDeBien().getDesignation().equals("Chambre") ||
                contratVente.getBienImmobilier().getTypeDeBien().getDesignation().equals("Bureau");
    }
}
