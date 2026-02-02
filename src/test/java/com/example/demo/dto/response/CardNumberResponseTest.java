package com.example.demo.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardNumberResponseTest {

    @Test
    void elConstructorDebeInicializarLosCampos() {
        CardNumberResponse response =
                new CardNumberResponse("producto-123", "4111111111111111", "creado");

        assertEquals("producto-123", response.getProductId());
        assertEquals("4111111111111111", response.getCardNumber());
        assertEquals("creado", response.getMessage());
    }

    @Test
    void losGettersDebenRetornarLosValoresAsignados() {
        CardNumberResponse response =
                new CardNumberResponse("producto-1", "1234", "ok");

        assertAll(
                () -> assertEquals("producto-1", response.getProductId()),
                () -> assertEquals("1234", response.getCardNumber()),
                () -> assertEquals("ok", response.getMessage())
        );
    }

    @Test
    void losSettersDebenActualizarLosCampos() {
        CardNumberResponse response =
                new CardNumberResponse("producto-inicial", "0000", "inicio");

        response.setProductId("producto-actualizado");
        response.setCardNumber("5678");
        response.setMessage("actualizado");

        assertEquals("producto-actualizado", response.getProductId());
        assertEquals("5678", response.getCardNumber());
        assertEquals("actualizado", response.getMessage());
    }

    @Test
    void elConstructorDebePermitirValoresNulos() {
        CardNumberResponse response =
                new CardNumberResponse(null, null, null);

        assertNull(response.getProductId());
        assertNull(response.getCardNumber());
        assertNull(response.getMessage());
    }

    @Test
    void losSettersDebenPermitirValoresNulos() {
        CardNumberResponse response =
                new CardNumberResponse("producto", "1", "mensaje");

        response.setProductId(null);
        response.setCardNumber(null);
        response.setMessage(null);

        assertNull(response.getProductId());
        assertNull(response.getCardNumber());
        assertNull(response.getMessage());
    }

    @Test
    void debeMantenerCadenasVacias() {
        CardNumberResponse response =
                new CardNumberResponse("", "", "");

        assertEquals("", response.getProductId());
        assertEquals("", response.getCardNumber());
        assertEquals("", response.getMessage());
    }

    @Test
    void elConstructorDebeAsignarTodosLosCamposCorrectamente() {
        String productId = "producto-x";
        String cardNumber = "9999888877776666";
        String message = "exitoso";

        CardNumberResponse response =
                new CardNumberResponse(productId, cardNumber, message);

        assertEquals(productId, response.getProductId());
        assertEquals(cardNumber, response.getCardNumber());
        assertEquals(message, response.getMessage());
    }

    @Test
    void losGettersDebenReflejarLosValoresAsignados() {
        CardNumberResponse response =
                new CardNumberResponse("A", "B", "C");

        assertEquals("A", response.getProductId());
        assertEquals("B", response.getCardNumber());
        assertEquals("C", response.getMessage());
    }

    @Test
    void losSettersDebenReflejarLosCambiosEnLosGetters() {
        CardNumberResponse response =
                new CardNumberResponse("producto-inicial", "1111", "inicio");

        response.setProductId("producto-final");
        response.setCardNumber("2222");
        response.setMessage("finalizado");

        assertAll(
                () -> assertEquals("producto-final", response.getProductId()),
                () -> assertEquals("2222", response.getCardNumber()),
                () -> assertEquals("finalizado", response.getMessage())
        );
    }

    @Test
    void debeManejarAsignacionesParcialesConValoresNulos() {
        CardNumberResponse response =
                new CardNumberResponse(null, "123", "mensaje");

        assertNull(response.getProductId());
        assertEquals("123", response.getCardNumber());
        assertEquals("mensaje", response.getMessage());

        response.setCardNumber(null);
        response.setMessage(null);

        assertNull(response.getCardNumber());
        assertNull(response.getMessage());
    }

    @Test
    void debeConservarCadenasVaciasYEspacios() {
        CardNumberResponse response =
                new CardNumberResponse("", " ", "");

        assertEquals("", response.getProductId());
        assertEquals(" ", response.getCardNumber());
        assertEquals("", response.getMessage());

        response.setProductId("");
        response.setCardNumber("");
        response.setMessage(" ");

        assertEquals("", response.getProductId());
        assertEquals("", response.getCardNumber());
        assertEquals(" ", response.getMessage());
    }

    @Test
    void debeConservarCaracteresEspecialesYEspacios() {
        String productId = " producto\tid-★ ";
        String cardNumber = "4111-1111-1111-1111\n";
        String message = "Creado ✔ con notas: áéíóú ñ !@#$%";

        CardNumberResponse response =
                new CardNumberResponse(productId, cardNumber, message);

        assertEquals(productId, response.getProductId());
        assertEquals(cardNumber, response.getCardNumber());
        assertEquals(message, response.getMessage());

        String nuevoProductId = "\t\n★NUEVO★\n\t";
        String nuevoCardNumber = " 0000 0000 0000 0000 ";
        String nuevoMessage = "Actualizado ✓ ";

        response.setProductId(nuevoProductId);
        response.setCardNumber(nuevoCardNumber);
        response.setMessage(nuevoMessage);

        assertEquals(nuevoProductId, response.getProductId());
        assertEquals(nuevoCardNumber, response.getCardNumber());
        assertEquals(nuevoMessage, response.getMessage());
    }
}