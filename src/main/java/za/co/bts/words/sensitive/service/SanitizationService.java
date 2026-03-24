package za.co.bts.words.sensitive.service;

import za.co.bts.words.sensitive.dto.SanitizationRequest;
import za.co.bts.words.sensitive.dto.SanitizationResponse;

public interface SanitizationService {
    SanitizationResponse  sanitize(SanitizationRequest request);
}