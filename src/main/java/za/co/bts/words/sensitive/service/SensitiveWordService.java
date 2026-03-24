package za.co.bts.words.sensitive.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import za.co.bts.words.sensitive.dto.SensitiveWordRequest;
import za.co.bts.words.sensitive.dto.SensitiveWordResponse;

public interface SensitiveWordService {
    SensitiveWordResponse addSensitiveWord(
            SensitiveWordRequest request);
    SensitiveWordResponse getSensitiveWordById(
            String senderId,
            String transactionId,
            String messageId,
            String timestamp,
            String id);
    SensitiveWordResponse getAllSensitiveWords(
            String senderId,
            String transactionId,
            String messageId,
            String timestamp
    );
    SensitiveWordResponse updateSensitiveWord(String id, SensitiveWordRequest request);
    void deleteSensitiveWord(
        String senderId,
        String transactionId,
        String messageId,
        String timestamp,
        String id);
    }