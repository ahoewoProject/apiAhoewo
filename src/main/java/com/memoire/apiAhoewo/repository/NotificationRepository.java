package com.memoire.apiAhoewo.repository;

import com.memoire.apiAhoewo.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findBySendToAndLuAndStatut(String sendTo, Boolean lu, Boolean statut);

    Page<Notification> findBySendToAndStatutOrderByIdDesc(Pageable pageable, String sendTo, Boolean statut);
}
