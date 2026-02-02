package com.example.demo.dto.response;

import java.math.BigDecimal;

public record CardResponse (String cardId,
        String holderName,
        boolean active,
        boolean blocked,
        BigDecimal balance,
        String expirationDate,
        String currency){

}
