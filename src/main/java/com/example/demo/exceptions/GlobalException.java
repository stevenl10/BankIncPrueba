package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

	 @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("timestamp", LocalDateTime.now());
	        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
	        errorResponse.put("error", "Bad Request");

	        // Aquí forzamos a que el mensaje aparezca incluso si es null
	        errorResponse.put("message", ex.getMessage() != null ? ex.getMessage() : "Error inesperado");

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	    }

	    @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("timestamp", LocalDateTime.now());
	        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
	        errorResponse.put("error", "Invalid Argument");
	        errorResponse.put("message", ex.getMessage() != null ? ex.getMessage() : "Argumento inválido");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	    }
}
