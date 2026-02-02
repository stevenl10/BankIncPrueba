package com.example.demo.dto.response;



public class CardNumberResponse {
	
		private String productId;
		private String cardNumber;
		private String message;
		
		public CardNumberResponse(String productId, String cardNumber, String message) {
			this.productId = productId;
			this.cardNumber = cardNumber;
			this.message = message;
		}
		
		public String getProductId() {
			return productId;
		}
		public void setProductId(String productId) {
			this.productId = productId;
		}
		public String getCardNumber() {
			return cardNumber;
		}
		public void setCardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		

}
