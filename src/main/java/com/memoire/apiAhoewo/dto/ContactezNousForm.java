package com.memoire.apiAhoewo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactezNousForm {
    private String nomPrenoms;
    private String telephone;
    private String emetteurEmail;
    private String recepteurEmail;
    private String message;
}
