package com.memoire.apiAhoewo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInvoicePaydunyaForm {
    private String account_alias;
    private int amount;
    private String withdraw_mode;
    private String callback_url;
}
