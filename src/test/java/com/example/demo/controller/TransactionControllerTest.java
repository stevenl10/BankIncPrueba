package com.example.demo.controller;

import com.example.demo.dto.request.AnulationRequest;
import com.example.demo.dto.request.PurchaseRequest;
import com.example.demo.dto.response.CardResponse;
import com.example.demo.dto.response.TransactionResponse;
import com.example.demo.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    private TransactionService transactionService;
    private TransactionController controller;

    @BeforeEach
    void setUp() {
        transactionService = Mockito.mock(TransactionService.class);
        controller = new TransactionController(transactionService);
    }

    @Test
    void shouldReturnOkWithCardResponseWhenPurchaseSucceeds() {
        // Arrange
        PurchaseRequest request = new PurchaseRequest("card-123", new BigDecimal("50.00"));
        CardResponse expected = new CardResponse("card-123", "Felipe Ramirez", true, false, new BigDecimal("150.00"), "12/2030", "USD");
        when(transactionService.makePurchase("card-123", new BigDecimal("50.00"))).thenReturn(expected);
        ResponseEntity<CardResponse> response = controller.makePurchase(request);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(expected, response.getBody());
        verify(transactionService, times(1)).makePurchase("card-123", new BigDecimal("50.00"));
    }

    @Test
    void shouldReturnOkWithTransactionResponseWhenGetByIdSucceeds() {
        String txId = "tx-001";
        TransactionResponse expected = new TransactionResponse(txId, "card-1", new BigDecimal("10.00"), "PURCHASE", LocalDateTime.now());
        when(transactionService.getTransactionById(txId)).thenReturn(expected);

        ResponseEntity<TransactionResponse> response = controller.getTransaction(txId);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(expected, response.getBody());
        verify(transactionService, times(1)).getTransactionById(txId);
    }

    @Test
    void shouldReturnOkWithTransactionResponseWhenAnulationSucceeds() {
        AnulationRequest req = new AnulationRequest();
        req.setCardId("card-9");
        req.setTransactionId("tx-999");

        TransactionResponse expected = new TransactionResponse("tx-999", "card-9", new BigDecimal("25.00"), "ANNULLED", LocalDateTime.now());
        when(transactionService.anulTransaction("tx-999", "card-9")).thenReturn(expected);
        ResponseEntity<TransactionResponse> response = controller.anulTransaction(req);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(expected, response.getBody());
        verify(transactionService, times(1)).anulTransaction("tx-999", "card-9");
    }

    @Test
    void shouldPropagateServiceExceptionWhenPurchaseFails() {
        PurchaseRequest request = new PurchaseRequest("card-err", new BigDecimal("0.00"));
        when(transactionService.makePurchase(anyString(), any())).thenThrow(new IllegalArgumentException("El valor de la compra debe ser mayor que cero"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> controller.makePurchase(request));
        assertEquals("El valor de la compra debe ser mayor que cero", ex.getMessage());

        ArgumentCaptor<String> cardCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<BigDecimal> priceCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        verify(transactionService).makePurchase(cardCaptor.capture(), priceCaptor.capture());
        assertEquals("card-err", cardCaptor.getValue());
        assertEquals(new BigDecimal("0.00"), priceCaptor.getValue());
    }

    @Test
    void shouldReturnClientErrorWhenRequestBodyFieldsAreNull() {

        PurchaseRequest nullPurchase = new PurchaseRequest(null, null);
        when(transactionService.makePurchase(isNull(), isNull()))
                .thenThrow(new RuntimeException("Null campos"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.makePurchase(nullPurchase));
        assertEquals("Null campos", ex.getMessage());
        verify(transactionService, times(1)).makePurchase(null, null);

        AnulationRequest nullAnulation = new AnulationRequest();
        when(transactionService.anulTransaction(isNull(), isNull()))
                .thenThrow(new RuntimeException("Null campos anulation"));

        RuntimeException ex2 = assertThrows(RuntimeException.class, () -> controller.anulTransaction(nullAnulation));
        assertEquals("Null campos anulation", ex2.getMessage());
        verify(transactionService, times(1)).anulTransaction(null, null);
    }

    @Test
    void shouldPropagateErrorWhenTransactionNotFound() {
        String txId = "missing-id";
        when(transactionService.getTransactionById(txId))
                .thenThrow(new RuntimeException("Transacción no encontrada con ID: " + txId));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.getTransaction(txId));
        assertEquals("Transacción no encontrada con ID: missing-id", ex.getMessage());
        verify(transactionService, times(1)).getTransactionById(txId);
    }
}