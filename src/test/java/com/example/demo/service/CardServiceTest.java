package com.example.demo.service;

import com.example.demo.entity.Card;
import com.example.demo.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private Card card;

    @BeforeEach
    void setUp() {
        card = new Card();
        card.setCardId("1234567890123456");
        card.setHolderName("Steven");
        card.setBalance(BigDecimal.valueOf(100));
        card.setCurrency("USD");
        card.setActive(true);
        card.setBlocked(false);
        card.setExpirationDate(LocalDate.now().plusYears(3));
    }

    @Test
    void generateCardNumber_shouldReturn16Digits() {
        String cardNumber = cardService.generateCardNumber("PROD123456");

        assertNotNull(cardNumber);
        assertEquals(16, cardNumber.length());
        assertTrue(cardNumber.matches("\\d{16}"));
    }


    @Test
    void enrollCard_blockedCard_shouldThrowException() {
        card.setBlocked(true);

        when(cardRepository.findById(card.getCardId()))
                .thenReturn(Optional.of(card));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cardService.enrollCard(card.getCardId(), "Steven"));

        assertEquals("La tarjeta está bloqueada, No se puede enrolar.", ex.getMessage());
        verify(cardRepository, never()).save(any());
    }

    @Test
    void blockCard_shouldBlockAndDeactivate() {
        when(cardRepository.findById(card.getCardId()))
                .thenReturn(Optional.of(card));

        cardService.blockCard(card.getCardId());

        assertTrue(card.isBlocked());
        assertFalse(card.isActive());
        verify(cardRepository).save(card);
    }

    @Test
    void activateCard_blockedCard_shouldActivate() {
        card.setBlocked(true);
        card.setActive(false);

        when(cardRepository.findById(card.getCardId()))
                .thenReturn(Optional.of(card));

        cardService.activateCard(card.getCardId());

        assertFalse(card.isBlocked());
        assertTrue(card.isActive());
        verify(cardRepository).save(card);
    }

    @Test
    void activateCard_notBlocked_shouldThrowException() {
        when(cardRepository.findById(card.getCardId()))
                .thenReturn(Optional.of(card));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cardService.activateCard(card.getCardId()));

        assertEquals("La tarjeta ya está activa.", ex.getMessage());
    }

    @Test
    void rechargeCard_negativeAmount_shouldThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cardService.rechargeCard(card.getCardId(), BigDecimal.ZERO));

        assertEquals("El monto de recarga debe ser mayor que cero", ex.getMessage());
    }

    @Test
    void rechargeCard_blockedCard_shouldThrowException() {
        card.setBlocked(true);

        when(cardRepository.findById(card.getCardId()))
                .thenReturn(Optional.of(card));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cardService.rechargeCard(card.getCardId(), BigDecimal.TEN));

        assertEquals("La tarjeta esta bloqueada, No se puede recargar.", ex.getMessage());
    }

    @Test
    void getBalance_shouldReturnBalance() {
        when(cardRepository.findById(card.getCardId()))
                .thenReturn(Optional.of(card));

        BigDecimal balance = cardService.getBalance(card.getCardId());

        assertEquals(BigDecimal.valueOf(100), balance);
    }

    @Test
    void getCard_existingCard_shouldReturnCard() {
        when(cardRepository.findById(card.getCardId()))
                .thenReturn(Optional.of(card));

        Card result = cardService.getCard(card.getCardId());

        assertEquals(card, result);
    }

}