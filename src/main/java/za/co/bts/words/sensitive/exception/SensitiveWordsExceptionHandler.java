package za.co.bts.words.sensitive.exception;

import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import za.co.bts.words.sensitive.common.EnterpriseUtil;
import za.co.bts.words.sensitive.dto.EnterpriseHeaderRequest;
import za.co.bts.words.sensitive.dto.SensitiveWordResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class SensitiveWordsExceptionHandler {
    private final EnterpriseUtil enUtil;

    public SensitiveWordsExceptionHandler(EnterpriseUtil enUtil) {
        this.enUtil = enUtil;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<SensitiveWordResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        SensitiveWordResponse response = enUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<SensitiveWordResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        SensitiveWordResponse response = enUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, ex.getMessage());
        return ResponseEntity
                .status(404)
                .body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<SensitiveWordResponse> handleConflict(DuplicateResourceException ex, HttpServletRequest request) {
        SensitiveWordResponse response = enUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, ex.getMessage());
        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SensitiveWordResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        SensitiveWordResponse response = enUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SensitiveWordResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        SensitiveWordResponse response = enUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(response);

    }

    EnterpriseHeaderRequest getHeader(HttpServletRequest request) {
        String senderId = request.getHeader("senderId");
        String transactionId = request.getHeader("transactionId");
        String messageId = request.getHeader("messageId");

        return enUtil.createResponseHeader(senderId, transactionId, messageId);
    };

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SensitiveWordResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        String errorMsg = errors.values().stream().findFirst().orElse(null);
        SensitiveWordResponse response = enUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, errorMsg);
        return ResponseEntity.badRequest().body(response);
    }
}