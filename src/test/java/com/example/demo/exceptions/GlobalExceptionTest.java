package com.example.demo.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class GlobalExceptionTest {

    private GlobalException globalException;

    @BeforeEach
    void setUp() {
        globalException = new GlobalException();
    }

    @Test
    void shouldPopulateRecentTimestampInErrorResponse() {
        RuntimeException ex = new RuntimeException("time check");

        LocalDateTime fixedNow = LocalDateTime.of(2026, 2, 1, 12, 0, 0);

        try (MockedStatic<LocalDateTime> mockedTime = mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(fixedNow);
            ResponseEntity<Map<String, Object>> response = globalException.handleRuntimeException(ex);

            Map<String, Object> body = response.getBody();
            assertNotNull(body);
            Object ts = body.get("timestamp");
            assertTrue(ts instanceof LocalDateTime);
            assertEquals(fixedNow, ts);
        }
    }
}