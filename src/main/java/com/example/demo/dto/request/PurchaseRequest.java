package com.example.demo.dto.request;

import java.math.BigDecimal;

public class PurchaseRequest {
	
	private String cardId;
    private BigDecimal price;
    

	public PurchaseRequest(String cardId, BigDecimal price) {
		this.cardId = cardId;
		this.price = price;
	}

	public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
}
