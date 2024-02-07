package com.memoire.apiAhoewo.serviceImpl;

import com.memoire.apiAhoewo.model.Notification;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.NotificationRepository;
import com.memoire.apiAhoewo.service.NotificationService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private PersonneService personneService;

    @Override
    public List<Notification> getNotificationsNonLuesByAdmin() {
        return notificationRepository.findBySendToAndLuAndStatut("ADMIN", false,true);
    }

    @Override
    public List<Notification> getNotificationsNonLuesByOwner(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return notificationRepository.findBySendToAndLuAndStatut(String.valueOf(personne.getId()), false, true);
    }

    @Override
    public Page<Notification> getNotificationsByAdmin(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return notificationRepository.findBySendToAndStatutOrderByIdDesc(pageRequest, "ADMIN", true);
    }

    @Override
    public Page<Notification> getNotificationsByOwner(int numeroDeLaPage, int elementsParPage, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return notificationRepository.findBySendToAndStatutOrderByIdDesc(pageRequest, String.valueOf(personne.getId()), true);
    }

    @Override
    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public Notification save(Notification notification) {
        notification.setCodeNotification("NOTIF" + UUID.randomUUID());
        Notification notificationAdd = notificationRepository.save(notification);
        notificationAdd.setCodeNotification("NOTIF00" + notificationAdd.getId());
        return notificationRepository.save(notificationAdd);
    }

    @Override
    public void lireNotification(Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        notification.setLu(true);
        notificationRepository.save(notification);
    }

    @Override
    public void deleteById(Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        notification.setStatut(false);
        notificationRepository.save(notification);
    }
}
