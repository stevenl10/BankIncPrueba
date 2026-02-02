package com.example.demo.dto.response;

import java.math.BigDecimal;

public class BalanceResponse {

	private String cardId;
	private BigDecimal balance;
	private String currency;
	private String expirationDate;
	
	public BalanceResponse(String cardId, BigDecimal balance,String currency,String expirationDate) {
		this.cardId = cardId;
		this.balance = balance;
		this.currency = currency;
		this.expirationDate = expirationDate;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	
}
