package com.memoire.apiAhoewo.services;

import com.memoire.apiAhoewo.models.Notification;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface NotificationService {
    public List<Notification> getNotificationsNonLuesByAdmin();

    public List<Notification> getNotificationsNonLuesByNotaire();

    public List<Notification> getNotificationsNonLuesByOwner(Principal principal);

    public List<Notification> getNotificationsListByAdmin();

    public List<Notification> getNotificationsListByNotaire();

    public List<Notification> getNotificationsListByOwner(Principal principal);

    public Page<Notification> getNotificationsByAdmin(int numeroDeLaPage, int elementsParPage);

    public Page<Notification> getNotificationsByNotaire(int numeroDeLaPage, int elementsParPage);

    public Page<Notification> getNotificationsByOwner(int numeroDeLaPage, int elementsParPage, Principal principal);

    public Notification findById(Long id);

    public Notification save(Notification notification);

    public void lireNotification(Long id);

    public void deleteById(Long id);
}
