package com.example.demo.controller;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.response.BalanceResponse;
import com.example.demo.dto.response.CardNumberResponse;
import com.example.demo.dto.response.CardResponse;
import com.example.demo.dto.request.EnrollRequest;
import com.example.demo.dto.request.RechargeRequest;
import com.example.demo.entity.Card;
import com.example.demo.service.CardService;

@RestController
@RequestMapping("/api/cards")
public class CardController {
	
	 private final CardService cardService;

	    public CardController(CardService cardService) {
	        this.cardService = cardService;
	    }

	    	    
	    /**
	     * Generar número de tarjeta basado en el productId
	     */
	    @GetMapping("/{productId}/number")
	    public ResponseEntity<CardNumberResponse> generateCardNumber(@PathVariable String productId) {
	        String cardNumber = cardService.generateCardNumber(productId);
	        return ResponseEntity.ok(new CardNumberResponse(
	                productId,
	                cardNumber,
	                "Numero de tarjeta generado exitosamente!!"
	        ));
	    }

	    /**
	     * Enroll / Activar tarjeta
	     */
	    @PostMapping("/enroll")
	    public ResponseEntity<CardResponse> enrollCard(@Validated @RequestBody EnrollRequest request) {
	        return ResponseEntity.ok(cardService.enrollCard(request.getCardId(), request.getHolderName()));
	    }

	    /**
	     * Bloquear tarjeta
	     */
	    @DeleteMapping("/block/{cardId}")
	    public ResponseEntity<String> blockCard(@PathVariable String cardId) {
	        cardService.blockCard(cardId);
	        return ResponseEntity.ok("Tarjeta No: " + cardId + " ha sido bloqueada exitosamente!!");
	    }
	    
	    /**
	     * Desbloquear tarjeta
	     */
	    @PutMapping("/{cardId}/activate")
	    public ResponseEntity<String> activateCard(@PathVariable String cardId) {
	        cardService.activateCard(cardId);
	        return ResponseEntity.ok("Tarjeta " + cardId + " Activada Existosamente!!");
	    }

	    /**
	     * Recargar tarjeta
	     */
	    @PostMapping("/recharge")
	    public ResponseEntity<CardResponse> rechargeCard(@Validated @RequestBody RechargeRequest request) {
	        return ResponseEntity.ok(cardService.rechargeCard(request.getCardId(), request.getAmount()));
	    }

	    /**
	     * Consultar saldo de la tarjeta
	     */
	    @GetMapping("/balance/{cardId}")
	    public ResponseEntity<BalanceResponse> getBalance(@PathVariable String cardId) {
	        BigDecimal balance = cardService.getBalance(cardId);
	        Card card = cardService.getCard(cardId);
	        return ResponseEntity.ok(new BalanceResponse(cardId, balance, "USD",card.getExpirationDate().format(DateTimeFormatter.ofPattern("MM/yyyy"))));
	    }

}
