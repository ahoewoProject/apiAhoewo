package com.memoire.apiAhoewo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitInvoiceForm {
    private String disburse_invoice;
    private String disburse_id;
}
