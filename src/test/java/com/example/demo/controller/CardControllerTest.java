package com.example.demo.controller;

import com.example.demo.dto.request.EnrollRequest;
import com.example.demo.dto.request.RechargeRequest;
import com.example.demo.dto.response.CardResponse;
import com.example.demo.entity.Card;
import com.example.demo.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardControllerTest {

    private CardService cardService;
    private CardController cardController;

    @BeforeEach
    void setup() {
        cardService = Mockito.mock(CardService.class);
        cardController = new CardController(cardService);
    }

    @Test
    void shouldGenerateCardNumberAndReturnOk() {
        String productId = "PROD-123456";
        String generatedNumber = "1234567890123456";
        when(cardService.generateCardNumber(productId)).thenReturn(generatedNumber);

        ResponseEntity<?> response = cardController.generateCardNumber(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // Validate body via reflection-free approach using toString checks or casting
        // Since CardNumberResponse is a POJO with getters, we can cast by package access.
        Object body = response.getBody();
        assertTrue(body instanceof com.example.demo.dto.response.CardNumberResponse);
        com.example.demo.dto.response.CardNumberResponse resp = (com.example.demo.dto.response.CardNumberResponse) body;
        assertEquals(productId, resp.getProductId());
        assertEquals(generatedNumber, resp.getCardNumber());
        assertNotNull(resp.getMessage());
        assertEquals(16, resp.getCardNumber().length());
        verify(cardService).generateCardNumber(productId);
    }

    @Test
    void shouldEnrollCardAndReturnCardResponse() {
        EnrollRequest request = new EnrollRequest();
        request.setCardId("CARD-1");
        request.setHolderName("John Doe");

        CardResponse serviceResponse = new CardResponse("CARD-1", "John Doe", true, false, BigDecimal.ZERO, "12/2030", "USD");
        when(cardService.enrollCard("CARD-1", "John Doe")).thenReturn(serviceResponse);

        ResponseEntity<CardResponse> response = cardController.enrollCard(request);

        assertEquals(200, response.getStatusCodeValue());
        CardResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("CARD-1", body.cardId());
        assertEquals("John Doe", body.holderName());
        assertTrue(body.active());
        assertFalse(body.blocked());
        verify(cardService).enrollCard("CARD-1", "John Doe");
    }

    @Test
    void shouldGetBalanceWithFormattedExpiration() {
        String cardId = "CARD-2";
        BigDecimal balance = new BigDecimal("150.75");
        LocalDate expDate = LocalDate.of(2031, 7, 31);

        Card card = new Card();
        card.setCardId(cardId);
        card.setExpirationDate(expDate);

        when(cardService.getBalance(cardId)).thenReturn(balance);
        when(cardService.getCard(cardId)).thenReturn(card);

        ResponseEntity<?> response = cardController.getBalance(cardId);

        assertEquals(200, response.getStatusCodeValue());
        Object body = response.getBody();
        assertTrue(body instanceof com.example.demo.dto.response.BalanceResponse);
        com.example.demo.dto.response.BalanceResponse br = (com.example.demo.dto.response.BalanceResponse) body;
        assertEquals(cardId, br.getCardId());
        assertEquals(balance, br.getBalance());
        assertEquals("USD", br.getCurrency());
        assertEquals("07/2031", br.getExpirationDate());
        verify(cardService).getBalance(cardId);
        verify(cardService).getCard(cardId);
    }

    @Test
    void shouldBlockCardAndReturnConfirmation() {
        String cardId = "CARD-3";

        ResponseEntity<String> response = cardController.blockCard(cardId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Tarjeta No: " + cardId + " ha sido bloqueada exitosamente!!", response.getBody());
        verify(cardService).blockCard(cardId);
    }

    @Test
    void shouldActivateCardAndReturnConfirmation() {
        String cardId = "CARD-4";

        ResponseEntity<String> response = cardController.activateCard(cardId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Tarjeta " + cardId + " Activada Existosamente!!", response.getBody());
        verify(cardService).activateCard(cardId);
    }

    @Test
    void shouldRechargeCardAndReturnUpdatedResponse() {
        RechargeRequest request = new RechargeRequest();
        request.setCardId("CARD-5");
        request.setAmount(new BigDecimal("50.00"));

        CardResponse updated = new CardResponse("CARD-5", "Alice", true, false, new BigDecimal("150.00"), "05/2032", "USD");
        when(cardService.rechargeCard("CARD-5", new BigDecimal("50.00"))).thenReturn(updated);

        ResponseEntity<CardResponse> response = cardController.rechargeCard(request);

        assertEquals(200, response.getStatusCodeValue());
        CardResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("CARD-5", body.cardId());
        assertEquals(new BigDecimal("150.00"), body.balance());
        assertEquals("USD", body.currency());

        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        verify(cardService).rechargeCard(idCaptor.capture(), amountCaptor.capture());
        assertEquals("CARD-5", idCaptor.getValue());
        assertEquals(new BigDecimal("50.00"), amountCaptor.getValue());
    }
}