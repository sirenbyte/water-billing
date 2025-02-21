package org.example.waterbilling.model.dto;

import lombok.Data;

@Data
public class CardDto {
    private String cardNumber;
    private String cardHolder;
    private String expirationDate;
    private String cvv;
}
