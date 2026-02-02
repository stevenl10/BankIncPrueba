package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.dto.response.CardResponse;
import com.example.demo.entity.Card;
import com.example.demo.repository.CardRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CardService {
	private final CardRepository cardRepository;

	public CardService(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	/**
	 * Generar número de tarjeta único basado en productId
	 */
	public String generateCardNumber(String productId) {
		if (productId == null)
			productId = "";
		String digitsOnly = productId.replaceAll("\\D", "");

		final String prefix;
		if (digitsOnly.length() >= 6) {
			prefix = digitsOnly.substring(0, 6);
		} else {
			prefix = String.format("%6s", digitsOnly).replace(' ', '0');
		}
		// Generar los 10 dígitos aleatorios
		StringBuilder sb = new StringBuilder(16);
		sb.append(prefix);
		ThreadLocalRandom rnd = ThreadLocalRandom.current();
		for (int i = 0; i < 10; i++) {
			sb.append(rnd.nextInt(0, 10));
		}
		return sb.toString();
	}

	/**
	 * Activar o enrolar una tarjeta
	 */
	public CardResponse enrollCard(String cardId, String holderName) {
		Optional<Card> existingCard = cardRepository.findById(cardId);

		if (existingCard.isPresent()) {
			Card card = existingCard.get();
			if (card.isBlocked()) {
				throw new RuntimeException("La tarjeta está bloqueada, No se puede enrolar.");
			}
			card.setHolderName(holderName);
			card.setActive(true);
			cardRepository.save(card);
			return mapToResponse(card);
		} else {
			// Si no existe, se crea una nueva tarjeta
			Card newCard = new Card();
			newCard.setCardId(cardId);
			newCard.setHolderName(holderName);
			newCard.setBalance(BigDecimal.ZERO);
			newCard.setCurrency("USD");
			newCard.setActive(true);
			newCard.setBlocked(false);
			LocalDate expirationDate = LocalDate.now()
			        .plusYears(3)
			        .withDayOfMonth(1)
			        .plusMonths(1)
			        .minusDays(1);
			newCard.setExpirationDate(expirationDate);
			cardRepository.save(newCard);
			return mapToResponse(newCard);
		}
	}

	/**
	 * Bloquear tarjeta
	 */
	public void blockCard(String cardId) {
		Card card = cardRepository.findById(cardId)
				.orElseThrow(() -> new RuntimeException("Tarjeta no encontrada " + cardId));
		card.setBlocked(true);
		card.setActive(false);
		cardRepository.save(card);
	}

	/**
	 * Desbloquear tarjeta
	 */
	public void activateCard(String cardId) {
		Card card = cardRepository.findById(cardId)
				.orElseThrow(() -> new RuntimeException("Tarjeta no encontrada " + cardId));

		if (!card.isBlocked()) {
			throw new RuntimeException("La tarjeta ya está activa.");
		}

		card.setBlocked(false);
		card.setActive(true);
		cardRepository.save(card);
	}

	/**
	 * Recargar tarjeta
	 */
	public CardResponse rechargeCard(String cardId, BigDecimal amount) {

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("El monto de recarga debe ser mayor que cero");
		}

		Card card = cardRepository.findById(cardId)
				.orElseThrow(() -> new RuntimeException("Tarjeta no encontrada " + cardId));

		if (card.isBlocked()) {
			throw new RuntimeException("La tarjeta esta bloqueada, No se puede recargar.");
		}

		card.setBalance(card.getBalance().add(amount));
		cardRepository.save(card);
		return mapToResponse(card);
	}

	/**
	 * Consultar saldo de tarjeta
	 */
	public BigDecimal getBalance(String cardId) {
		Card card = cardRepository.findById(cardId)
				.orElseThrow(() -> new RuntimeException("Tarjeta no encontrada para consultar saldo " + cardId));
		return card.getBalance();
	}

	/**
	 * Mapper para transformar entidad → DTO
	 */
	private CardResponse mapToResponse(Card card) {
		return new CardResponse(card.getCardId(), card.getHolderName(), card.isActive(), card.isBlocked(),
				card.getBalance(),card.getExpirationDate() .format(DateTimeFormatter.ofPattern("MM/yyyy")), card.getCurrency());
	}
	
	public Card getCard(String cardId) {
	    return cardRepository.findById(cardId)
	        .orElseThrow(() -> new RuntimeException("Card not found: " + cardId));
	}
	
	
}
