package com.example.demo.dto.request;

public class AnulationRequest {
	
	private String cardId;
    private String transactionId;
    
    
	public AnulationRequest() {
		
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	
    
    

}
