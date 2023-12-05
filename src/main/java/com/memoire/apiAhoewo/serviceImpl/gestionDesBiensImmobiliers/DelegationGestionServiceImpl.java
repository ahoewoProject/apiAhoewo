package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.DelegationGestionRepository;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.DelegationGestionService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.service.gestionDesComptes.ResponsableAgenceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DelegationGestionServiceImpl implements DelegationGestionService {
    @Autowired
    private DelegationGestionRepository delegationGestionRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;
    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public List<DelegationGestion> getDelegationsByProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return delegationGestionRepository.findByBienImmobilier_Personne(personne);
    }

    @Override
    public List<DelegationGestion> getDelegationsByGestionnaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return delegationGestionRepository.findByGestionnaire(personne);
    }

    @Override
    public List<DelegationGestion> getDelegationsOfAgencesByResponsable(Principal principal) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByResponsable(principal);

        return delegationGestionRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public List<DelegationGestion> getDelegationsOfAgencesByAgent(Principal principal) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByAgent(principal);

        return delegationGestionRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public DelegationGestion findById(Long id) {
        return delegationGestionRepository.findById(id).orElse(null);
    }

    @Override
    public DelegationGestion save(DelegationGestion delegationGestion, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        delegationGestion.setDateDelegation(new Date());
        delegationGestion.setStatutDelegation(0);
        delegationGestion.setCreerLe(new Date());
        delegationGestion.setCreerPar(personne.getId());
        delegationGestion.setStatut(true);

        String delegationGestionLink = "";
        String contenu = "";

        if (delegationGestion.getAgenceImmobiliere() == null) {

            if (delegationGestion.getGestionnaire().getRole().getCode() == "ROLE_DEMARCHEUR") {
                delegationGestionLink = "http://localhost:4200/#/demarcheur/delegations-gestions";
            } else {
                delegationGestionLink = "http://localhost:4200/#/gerant/delegations-gestions";
            }


            contenu = "Bonjour M/Mlle " + delegationGestion.getGestionnaire().getPrenom() + delegationGestion.getGestionnaire().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que M/Mlle " + personne.getPrenom() + personne.getNom() + " vient de vous déléguer la gestion d'un de ses biens.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";

        } else {
            delegationGestionLink = "http://localhost:4200/#/responsable/agences-immobilieres/delegations-gestions";
            contenu = "Bonjour Agence immobilière " + delegationGestion.getAgenceImmobiliere().getNomAgence() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que M/Mlle " + personne.getPrenom() + personne.getNom() + " vient de vous déléguer la gestion d'un de ses biens.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";


        }

        String finalContenu = contenu;
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(delegationGestion.getGestionnaire().getEmail(), "Délégation de la gestion de bien", finalContenu);
        });

        return delegationGestionRepository.save(delegationGestion);

    }

    @Override
    public void accepterDelegationGestion(Long id) {
        DelegationGestion delegationGestion = delegationGestionRepository.findById(id).orElse(null);
        delegationGestion.setStatutDelegation(1);

        String delegationGestionLink = "http://localhost:4200/#/proprietaire/delegations-gestions";
        String contenu = "";

        if (delegationGestion.getAgenceImmobiliere() == null) {
             contenu = "Bonjour M/Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + delegationGestion.getBienImmobilier().getPersonne().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que M/Mlle " + delegationGestion.getGestionnaire().getPrenom() + delegationGestion.getGestionnaire().getNom() + " vient d'accepter la gestion de votre bien que vous lui avez délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";
        } else {
             contenu = "Bonjour M/Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + delegationGestion.getBienImmobilier().getPersonne().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que l'agence immobilière " + delegationGestion.getAgenceImmobiliere().getNomAgence() + " vient d'accepter la gestion de votre bien que vous lui avez délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";
        }


        String finalContenu = contenu;
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(delegationGestion.getGestionnaire().getEmail(), "Acceptation de la délégation de gestion de bien", finalContenu);
        });

        delegationGestionRepository.save(delegationGestion);
    }

    @Override
    public void refuserDelegationGestion(Long id) {
        DelegationGestion delegationGestion = delegationGestionRepository.findById(id).orElse(null);
        delegationGestion.setStatutDelegation(2);

        String delegationGestionLink = "http://localhost:4200/#/proprietaire/delegations-gestions";
        String contenu = "";

        if (delegationGestion.getAgenceImmobiliere() == null) {
            contenu = "Bonjour M/Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + delegationGestion.getBienImmobilier().getPersonne().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que M/Mlle " + delegationGestion.getGestionnaire().getPrenom() + delegationGestion.getGestionnaire().getNom() + " vient de rejeter la gestion de votre bien que vous lui avez délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";
        } else {
            contenu = "Bonjour M/Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + delegationGestion.getBienImmobilier().getPersonne().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que l'agence immobilière " + delegationGestion.getAgenceImmobiliere().getNomAgence() + " vient de rejeter la gestion de votre bien que vous lui avez délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";
        }


        String finalContenu = contenu;
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(delegationGestion.getGestionnaire().getEmail(), "Refus de la délégation de gestion de bien", finalContenu);
        });

        delegationGestionRepository.save(delegationGestion);
    }

    @Override
    public boolean bienImmobilierAndStatutDelegationExists(BienImmobilier bienImmobilier, Integer statutDelegation) {
        return delegationGestionRepository.existsByBienImmobilierAndStatutDelegation(bienImmobilier, statutDelegation);
    }

    @Override
    public void supprimerDelegationGestion(Long id) {
        delegationGestionRepository.deleteById(id);
    }
}
