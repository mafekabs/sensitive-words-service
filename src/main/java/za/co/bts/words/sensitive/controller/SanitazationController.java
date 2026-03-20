package za.co.bts.words.sensitive.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import za.co.bts.words.sensitive.dto.SanitizationRequest;
import za.co.bts.words.sensitive.dto.SanitizationResponse;
import za.co.bts.words.sensitive.service.SanitizationService;

@RestController("/sanitizations")
@ControllerAdvice
public class SanitazationController {
    private final SanitizationService sanitizationService;

    public SanitazationController(SanitizationService sanitizationService) {
        this.sanitizationService = sanitizationService;
    }

    @PostMapping
    public ResponseEntity<SanitizationResponse> sanitizeMessage(@RequestBody SanitizationRequest request) {
        return ResponseEntity.ok(null);
    }
}
