package com.memoire.apiAhoewo.repositories;

import com.memoire.apiAhoewo.models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findBySendToAndLuAndStatutOrderByIdDesc(String sendTo, Boolean lu, Boolean statut);

    List<Notification> findBySendToOrderByIdDesc(String sendTo);

    Page<Notification> findBySendToAndStatutOrderByIdDesc(Pageable pageable, String sendTo, Boolean statut);
}
