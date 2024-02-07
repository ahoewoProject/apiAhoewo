package com.memoire.apiAhoewo.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "notifications")
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

    public Notification() {
    }

    public Notification(Long id, String codeNotification, String titre, String message, String url, String sendTo, Boolean lu, Date dateNotification) {
        this.id = id;
        this.codeNotification = codeNotification;
        this.titre = titre;
        this.message = message;
        this.url = url;
        this.sendTo = sendTo;
        this.lu = lu;
        this.dateNotification = dateNotification;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCodeNotification() {
        return codeNotification;
    }

    public void setCodeNotification(String codeNotification) {
        this.codeNotification = codeNotification;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public Boolean getLu() {
        return lu;
    }

    public void setLu(Boolean lu) {
        this.lu = lu;
    }

    public Date getDateNotification() {
        return dateNotification;
    }

    public void setDateNotification(Date dateNotification) {
        this.dateNotification = dateNotification;
    }
}
