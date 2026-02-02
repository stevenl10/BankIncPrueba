package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.AnulationRequest;
import com.example.demo.dto.response.CardResponse;
import com.example.demo.dto.request.PurchaseRequest;
import com.example.demo.dto.response.TransactionResponse;
import com.example.demo.service.TransactionService;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin(origins = "*")
public class TransactionController {
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping("/purchase")
	public ResponseEntity<CardResponse> makePurchase(@RequestBody PurchaseRequest request) {
		CardResponse response = transactionService.makePurchase(request.getCardId(), request.getPrice());
		return ResponseEntity.ok(response);
	}
	
	/**
     * Consultar transacción por ID
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable String transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }
    
    @PostMapping("/transaction/anulation")
    public ResponseEntity<TransactionResponse> anulTransaction(
            @RequestBody AnulationRequest request) {

        return ResponseEntity.ok(
                transactionService.anulTransaction(
                        request.getTransactionId(),
                        request.getCardId()
                )
        );
    }
}
