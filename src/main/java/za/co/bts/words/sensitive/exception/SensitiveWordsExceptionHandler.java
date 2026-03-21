package za.co.bts.words.sensitive.exception;

import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import za.co.bts.words.sensitive.common.EnterpriseResponseUtil;
import za.co.bts.words.sensitive.dto.EnterpriseHeaderRequest;
import za.co.bts.words.sensitive.dto.SensitiveWordResponse;

import java.util.List;

@ControllerAdvice
public class SensitiveWordsExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<SensitiveWordResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        SensitiveWordResponse response = EnterpriseResponseUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<SensitiveWordResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        SensitiveWordResponse response = EnterpriseResponseUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, ex.getMessage());
        return ResponseEntity
                .status(404)
                .body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<SensitiveWordResponse> handleConflict(DuplicateResourceException ex, HttpServletRequest request) {
        SensitiveWordResponse response = EnterpriseResponseUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, ex.getMessage());
        return ResponseEntity.status(409).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SensitiveWordResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        SensitiveWordResponse response = EnterpriseResponseUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SensitiveWordResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        SensitiveWordResponse response = EnterpriseResponseUtil.createSensitiveWordResponse(getHeader(request), List.of(), false, ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(response);

    }

    EnterpriseHeaderRequest getHeader(HttpServletRequest request) {
        String senderId = request.getHeader("senderId");
        String transactionId = request.getHeader("transactionId");
        String messageId = request.getHeader("messageId");

        return EnterpriseResponseUtil.createResponseHeader(senderId, transactionId, messageId);
    };
}