package com.example.demo.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.response.CardResponse;
import com.example.demo.dto.response.TransactionResponse;
import com.example.demo.entity.Card;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.entity.Transaction;


@Service
public class TransactionService {
	
	private final CardRepository cardRepository;
	private final TransactionRepository transactionRepository;
	
	public TransactionService(CardRepository cardRepository,TransactionRepository transactionRepository) {
		this.cardRepository = cardRepository;
		this.transactionRepository = transactionRepository;
	}

	public CardResponse makePurchase(String cardId, BigDecimal price) {
	    if (price.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new IllegalArgumentException("El valor de la compra debe ser mayor que cero");
	    }

	    Card card = cardRepository.findById(cardId)
	            .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada: " + cardId));

	    if (card.isBlocked()) {
	        throw new RuntimeException("La tarjeta está bloqueada. No se puede realizar la compra.");
	    }

	    if (card.getBalance().compareTo(price) < 0) {
	        throw new RuntimeException("Saldo insuficiente para realizar la compra.");
	    }

	    // Descontar el valor de la compra
	    card.setBalance(card.getBalance().subtract(price));
	    cardRepository.save(card);
	    
	    Transaction tx = new Transaction();
        tx.setTransactionId(UUID.randomUUID().toString()); 
        tx.setCardId(card.getCardId());
        tx.setAmount(price);
        tx.setType("PURCHASE");
        tx.setTimestamp(LocalDateTime.now());
        transactionRepository.save(tx);

	    return mapToResponse(card);
	}
	
	/**
	 * Mapper para transformar entidad → DTO
	 */
	private CardResponse mapToResponse(Card card) {
		return new CardResponse(card.getCardId(), card.getHolderName(), card.isActive(), card.isBlocked(),
				card.getBalance(),card.getExpirationDate() .format(DateTimeFormatter.ofPattern("MM/yyyy")), card.getCurrency());
	}
	
	public TransactionResponse getTransactionById(String transactionId) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
        Transaction transaction = optionalTransaction
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada con ID: " + transactionId));

        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getCardId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getTimestamp()
        );
    }
	
	public TransactionResponse anulTransaction(String transactionId, String cardId) {

	    Transaction tx = transactionRepository.findById(transactionId)
	            .orElseThrow(() ->
	                    new RuntimeException("Transacción no encontrada con ID: " + transactionId));

	    if (tx.isAnnulled()) {
	        throw new RuntimeException("La transacción ya fue anulada");
	    }

	    // validar que pertenezca a la tarjeta
	    if (!tx.getCardId().equals(cardId)) {
	        throw new RuntimeException("La transacción no pertenece a la tarjeta");
	    }

	    // validar 24 horas
	    Duration duration = Duration.between(tx.getTimestamp(), LocalDateTime.now());
	    if (duration.toHours() > 24) {
	        throw new RuntimeException("La transacción supera las 24 horas y no puede anularse");
	    }

	    Card card = cardRepository.findById(cardId)
	            .orElseThrow(() ->
	                    new RuntimeException("Tarjeta no encontrada: " + cardId));

	    // devolver saldo
	    card.setBalance(card.getBalance().add(tx.getAmount()));
	    cardRepository.save(card);

	    // marcar como anulada
	    tx.setAnnulled(true);
	    tx.setType("ANNULLED");
	    transactionRepository.save(tx);

	    return new TransactionResponse(
	            tx.getTransactionId(),
	            tx.getCardId(),
	            tx.getAmount(),
	            tx.getType(),
	            tx.getTimestamp()
	    );
	}
}
