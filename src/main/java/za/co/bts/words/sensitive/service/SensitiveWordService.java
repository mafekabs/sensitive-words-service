package za.co.bts.words.sensitive.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import za.co.bts.words.sensitive.dto.SensitiveWordRequest;
import za.co.bts.words.sensitive.dto.SensitiveWordResponse;

public interface SensitiveWordService {
    SensitiveWordResponse addSensitiveWord(@RequestBody SensitiveWordRequest request);
    SensitiveWordResponse getSensitiveWordById(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            String id
    );
    SensitiveWordResponse getAllSensitiveWords(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId
    );
    SensitiveWordResponse updateSensitiveWord(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            String id, SensitiveWordRequest request);
    void deleteSensitiveWord(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            String id);
}