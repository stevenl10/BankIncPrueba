package com.example.demo.dto.request;

import java.math.BigDecimal;

public class RechargeRequest {
	
    private String cardId;
    private BigDecimal amount;
    
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
    
    
}
