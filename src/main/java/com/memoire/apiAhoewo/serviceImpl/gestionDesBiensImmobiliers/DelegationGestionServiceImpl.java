package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.DelegationGestionRepository;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.DelegationGestionService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
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
    private EmailSenderService emailSenderService;

    @Override
    public List<DelegationGestion> getAllByProprietaire(Principal principal) {
        Personne proprietaire = personneService.findByUsername(principal.getName());
        return delegationGestionRepository.findDelegationGestionByBienImmobilier_Personne(proprietaire);
    }

    @Override
    public List<DelegationGestion> getAllByGestionnaire(Principal principal) {
        Personne gestionnaire = personneService.findByUsername(principal.getName());
        return delegationGestionRepository.findDelegationGestionByGestionnaire(gestionnaire);
    }

    @Override
    public DelegationGestion findById(Long id) {
        return delegationGestionRepository.findById(id).orElse(null);
    }

    @Override
    public DelegationGestion save(DelegationGestion delegationGestion, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        delegationGestion.setDateDelegation(new Date());
        delegationGestion.setStatutDelegation(false);
        delegationGestion.setCreerLe(new Date());
        delegationGestion.setCreerPar(personne.getId());
        delegationGestion.setStatut(true);

        String delegationGestionLink = "http://localhost:4200/delegations-gestions";

        String contenu = "Bonjour M/Mlle " + delegationGestion.getGestionnaire().getPrenom() + ",\n\n" +
                "Nous avons le plaisir de vous informer que M/Mlle " + personne.getPrenom() + " vient de vous déléguer la gestion d'un de ses biens.\n" +
                "\n\n" +
                "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                "\n\n" +
                "Cordialement,\n" +
                "\nL'équipe de support technique - ahoewo !";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(delegationGestion.getGestionnaire().getEmail(), "Délégation de la gestion de bien", contenu);
        });

        return delegationGestionRepository.save(delegationGestion);

    }

    @Override
    public void accepterDelegationGestion(Long id) {
        DelegationGestion delegationGestion = delegationGestionRepository.findById(id).orElse(null);
        delegationGestion.setStatutDelegation(true);

        String delegationGestionLink = "http://localhost:4200/delegations-gestions";

        String contenu = "Bonjour M/Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + ",\n\n" +
                "Nous avons le plaisir de vous informer que M/Mlle " + delegationGestion.getGestionnaire().getPrenom() + " vient d'accepter la délégation de la gestion de votre bien.\n" +
                "\n\n" +
                "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                "\n\n" +
                "Cordialement,\n" +
                "\nL'équipe de support technique - ahoewo !";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(delegationGestion.getGestionnaire().getEmail(), "Acceptation de la délégation de gestion de bien", contenu);
        });

        delegationGestionRepository.save(delegationGestion);
    }

    @Override
    public void supprimerDelegationGestion(Long id) {
        delegationGestionRepository.deleteById(id);
    }

    @Override
    public int countDelegationGestionProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<DelegationGestion> delegationGestionList = delegationGestionRepository.findDelegationGestionByBienImmobilier_Personne(personne);
        int count = delegationGestionList.size();
        return count;
    }

    @Override
    public int countDelegationGestionGestionnaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<DelegationGestion> delegationGestions = delegationGestionRepository.findDelegationGestionByGestionnaire(personne);
        int count = delegationGestions.size();
        return count;
    }
}
