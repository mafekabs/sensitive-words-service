package za.co.bts.words.sensitive.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.co.bts.words.sensitive.common.EnterpriseUtil;
import za.co.bts.words.sensitive.dto.SanitizationRequest;
import za.co.bts.words.sensitive.dto.SanitizationResponse;
import za.co.bts.words.sensitive.service.SanitizationService;

@RestController
@RequestMapping("/api/v1/sanitizations")
public class SanitazationController {
    private final SanitizationService sanitizationService;
    private final EnterpriseUtil enUtil;

    public SanitazationController(SanitizationService sanitizationService, EnterpriseUtil enUtil) {
        this.sanitizationService = sanitizationService;
        this.enUtil = enUtil;
    }

    @PostMapping
    public ResponseEntity<SanitizationResponse> sanitizeMessage(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            @RequestHeader("timestamp") String timestamp,
            @Valid @RequestBody SanitizationRequest request
    ) {
        if(request == null) {
            request.setHeader(enUtil.enrichSensitiveWordHeader(senderId, transactionId, messageId, timestamp));
        }
        return ResponseEntity.ok(sanitizationService.sanitize(request));
    }
}
