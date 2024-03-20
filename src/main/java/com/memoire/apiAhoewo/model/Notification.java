package com.memoire.apiAhoewo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_notification", nullable = false, unique = true)
    private String codeNotification;

    @Column(name = "titre")
    private String titre;

    @Column(name = "message")
    private String message;

    @Column(name = "url")
    private String url;

    @Column(name = "send_to")
    private String sendTo;

    @Column(name = "lu")
    private Boolean lu;

    @Column(name = "date_notification")
    private Date dateNotification;
}
