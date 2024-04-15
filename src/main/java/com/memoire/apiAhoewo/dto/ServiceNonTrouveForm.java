package com.memoire.apiAhoewo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceNonTrouveForm {
    private String nomAgence;
    private String nomDuService;
    private String descriptionDuService;
}
