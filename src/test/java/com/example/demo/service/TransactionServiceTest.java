package com.example.demo.service;

import com.example.demo.dto.response.TransactionResponse;
import com.example.demo.entity.Card;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Card card;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        card = new Card();
        card.setCardId("CARD123");
        card.setBalance(BigDecimal.valueOf(500));
        card.setBlocked(false);
        card.setActive(true);
        card.setHolderName("Steven");
        card.setCurrency("USD");
        card.setExpirationDate(LocalDate.now().plusYears(3));

        transaction = new Transaction();
        transaction.setTransactionId("TX123");
        transaction.setCardId("CARD123");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType("PURCHASE");
        transaction.setTimestamp(LocalDateTime.now().minusHours(1));
        transaction.setAnnulled(false);
    }

    @Test
    void makePurchase_invalidAmount_shouldThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.makePurchase("CARD123", BigDecimal.ZERO));

        assertEquals("El valor de la compra debe ser mayor que cero", ex.getMessage());
    }

    @Test
    void makePurchase_blockedCard_shouldThrowException() {
        card.setBlocked(true);

        when(cardRepository.findById("CARD123"))
                .thenReturn(Optional.of(card));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> transactionService.makePurchase("CARD123", BigDecimal.TEN));

        assertEquals("La tarjeta está bloqueada. No se puede realizar la compra.", ex.getMessage());
    }

    @Test
    void makePurchase_insufficientBalance_shouldThrowException() {
        when(cardRepository.findById("CARD123"))
                .thenReturn(Optional.of(card));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> transactionService.makePurchase("CARD123", BigDecimal.valueOf(1000)));

        assertEquals("Saldo insuficiente para realizar la compra.", ex.getMessage());
    }

    @Test
    void getTransactionById_existingTransaction_shouldReturnResponse() {
        when(transactionRepository.findById("TX123"))
                .thenReturn(Optional.of(transaction));

        TransactionResponse response = transactionService.getTransactionById("TX123");

        assertEquals("TX123", response.getTransactionId());
        assertEquals(BigDecimal.valueOf(100), response.getAmount());
    }

    @Test
    void getTransactionById_notFound_shouldThrowException() {
        when(transactionRepository.findById("TX404"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> transactionService.getTransactionById("TX404"));

        assertEquals("Transacción no encontrada con ID: TX404", ex.getMessage());
    }

    @Test
    void anulTransaction_validTransaction_shouldAnnulAndRefund() {
        when(transactionRepository.findById("TX123"))
                .thenReturn(Optional.of(transaction));

        when(cardRepository.findById("CARD123"))
                .thenReturn(Optional.of(card));

        TransactionResponse response =
                transactionService.anulTransaction("TX123", "CARD123");

        assertEquals("ANNULLED", response.getType());
        assertTrue(transaction.isAnnulled());
        assertEquals(BigDecimal.valueOf(600), card.getBalance());

        verify(cardRepository).save(card);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void anulTransaction_alreadyAnnulled_shouldThrowException() {
        transaction.setAnnulled(true);

        when(transactionRepository.findById("TX123"))
                .thenReturn(Optional.of(transaction));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> transactionService.anulTransaction("TX123", "CARD123"));

        assertEquals("La transacción ya fue anulada", ex.getMessage());
    }

    @Test
    void anulTransaction_wrongCard_shouldThrowException() {
        when(transactionRepository.findById("TX123"))
                .thenReturn(Optional.of(transaction));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> transactionService.anulTransaction("TX123", "OTHER_CARD"));

        assertEquals("La transacción no pertenece a la tarjeta", ex.getMessage());
    }

    @Test
    void anulTransaction_moreThan24Hours_shouldThrowException() {
        transaction.setTimestamp(LocalDateTime.now().minusHours(25));

        when(transactionRepository.findById("TX123"))
                .thenReturn(Optional.of(transaction));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> transactionService.anulTransaction("TX123", "CARD123"));

        assertEquals("La transacción supera las 24 horas y no puede anularse", ex.getMessage());
    }

    @Test
    void anulTransaction_cardNotFound_shouldThrowException() {
        when(transactionRepository.findById("TX123"))
                .thenReturn(Optional.of(transaction));

        when(cardRepository.findById("CARD123"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> transactionService.anulTransaction("TX123", "CARD123"));

        assertEquals("Tarjeta no encontrada: CARD123", ex.getMessage());
    }
}