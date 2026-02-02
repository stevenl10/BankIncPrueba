package com.example.demo.dto.request;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseRequestTest {

    @Test
    void testConstructorSetsFields() {
        String cardId = "CARD123";
        BigDecimal price = new BigDecimal("19.99");

        PurchaseRequest request = new PurchaseRequest(cardId, price);

        assertEquals(cardId, request.getCardId());
        assertEquals(price, request.getPrice());
    }

    @Test
    void testSetAndGetCardId() {
        PurchaseRequest request = new PurchaseRequest("OLD", new BigDecimal("10.00"));
        String newCardId = "NEW123";

        request.setCardId(newCardId);

        assertEquals(newCardId, request.getCardId());
    }

    @Test
    void testSetAndGetPrice() {
        PurchaseRequest request = new PurchaseRequest("CARD", new BigDecimal("1.00"));
        BigDecimal newPrice = new BigDecimal("25.50");

        request.setPrice(newPrice);

        assertEquals(newPrice, request.getPrice());
    }

    @Test
    void testNullCardIdHandling() {
        PurchaseRequest request = new PurchaseRequest(null, new BigDecimal("5.00"));

        assertNull(request.getCardId());
    }

    @Test
    void testNullPriceHandling() {
        PurchaseRequest request = new PurchaseRequest("CARD", null);

        assertNull(request.getPrice());
    }

    @Test
    void testBigDecimalPrecisionAndScalePreserved() {
        PurchaseRequest request = new PurchaseRequest("CARD", new BigDecimal("0.0001"));
        BigDecimal precise = new BigDecimal("123.4500"); // scale 4

        request.setPrice(precise);

        assertEquals(precise, request.getPrice());
        assertEquals(4, request.getPrice().scale());
        assertEquals(precise.stripTrailingZeros(), request.getPrice().stripTrailingZeros());
    }

    @Test
    void testConstructorInitializesFields() {
        String cardId = "INIT123";
        BigDecimal price = new BigDecimal("0.99");

        PurchaseRequest request = new PurchaseRequest(cardId, price);

        assertEquals(cardId, request.getCardId());
        assertEquals(price, request.getPrice());
    }

    @Test
    void testGetCardIdAfterConstruction() {
        String cardId = "AFTERCONSTRUCT";
        PurchaseRequest request = new PurchaseRequest(cardId, new BigDecimal("100.00"));

        assertEquals(cardId, request.getCardId());
    }

    @Test
    void testSetCardIdToNull() {
        PurchaseRequest request = new PurchaseRequest("CARD", new BigDecimal("10.00"));

        request.setCardId(null);

        assertNull(request.getCardId());
    }

    @Test
    void testSetPriceToNull() {
        PurchaseRequest request = new PurchaseRequest("CARD", new BigDecimal("10.00"));

        request.setPrice(null);

        assertNull(request.getPrice());
    }

    @Test
    void testPricePrecisionAndScalePreserved() {
        BigDecimal priceWithScale = new BigDecimal("42.3000"); // scale 4
        PurchaseRequest request = new PurchaseRequest("CARD", priceWithScale);

        assertEquals(priceWithScale, request.getPrice());
        assertEquals(4, request.getPrice().scale());
    }
}