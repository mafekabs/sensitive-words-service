package za.co.bts.words.sensitive.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.bts.words.sensitive.dto.SanitizationRequest;
import za.co.bts.words.sensitive.dto.SanitizationResponse;
import za.co.bts.words.sensitive.service.SanitizationService;

@RestController
@RequestMapping("/api/v1/sanitizations")
public class SanitazationController {
    private final SanitizationService sanitizationService;

    public SanitazationController(SanitizationService sanitizationService) {
        this.sanitizationService = sanitizationService;
    }

    @PostMapping
    public ResponseEntity<SanitizationResponse> sanitizeMessage(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            @RequestBody SanitizationRequest request
    ) {
        return ResponseEntity.ok(sanitizationService.sanitize(senderId, transactionId, messageId, request));
    }
}
