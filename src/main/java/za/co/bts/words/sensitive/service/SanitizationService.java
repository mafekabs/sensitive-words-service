package za.co.bts.words.sensitive.service;

import org.springframework.web.bind.annotation.RequestBody;
import za.co.bts.words.sensitive.dto.SanitizationRequest;
import za.co.bts.words.sensitive.dto.SanitizationResponse;

public interface SanitizationService {
    SanitizationResponse  sanitize(
            String senderId,
            String transactionId,
            String messageId,
            @RequestBody SanitizationRequest request
    );
}
