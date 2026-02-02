package com.example.demo.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

	private String transactionId;
    private String cardId;
    private BigDecimal amount;
    private String type;
    private LocalDateTime timestamp;
    
    public TransactionResponse(String transactionId, String cardId, BigDecimal amount, String type, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.cardId = cardId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }
    
    public String getTransactionId() {
        return transactionId;
    }

    public String getCardId() {
        return cardId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
