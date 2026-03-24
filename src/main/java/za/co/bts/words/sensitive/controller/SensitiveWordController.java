package za.co.bts.words.sensitive.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.bts.words.sensitive.common.EnterpriseUtil;
import za.co.bts.words.sensitive.dto.SensitiveWordRequest;
import za.co.bts.words.sensitive.dto.SensitiveWordResponse;
import za.co.bts.words.sensitive.service.SensitiveWordService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/sensitive-words")
public class SensitiveWordController {
    private final SensitiveWordService sensitiveWordService;
    private final EnterpriseUtil enUtil;

    public SensitiveWordController(SensitiveWordService sensitiveWordService, EnterpriseUtil enUtil) {
        this.sensitiveWordService = sensitiveWordService;
        this.enUtil = enUtil;
    }

    @PostMapping
    public ResponseEntity<SensitiveWordResponse> addSensitiveWord(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            @RequestHeader("timestamp") String timestamp,
            @Valid @RequestBody SensitiveWordRequest request) {

        if(request == null) {
            request.setHeader(enUtil.enrichSensitiveWordHeader(senderId, transactionId, messageId, timestamp));
        }

        SensitiveWordResponse response = sensitiveWordService.addSensitiveWord(request);

        URI wordURL = URI.create(
                "api/v1/sensitive-words/" +
                        response.payload().sensitiveWords().get(0).id()
        );

        return ResponseEntity
                .created(wordURL)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensitiveWordResponse> getSensitiveWordById(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            @RequestHeader("timestamp") String timestamp,
            @PathVariable String id) {

        return ResponseEntity.ok(
                sensitiveWordService.getSensitiveWordById(senderId, transactionId, messageId, timestamp, id)
        );
    }

    @GetMapping
    public ResponseEntity<SensitiveWordResponse> getAll(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            @RequestHeader("timestamp") String timestamp
    ) {

        return ResponseEntity.ok(
                sensitiveWordService.getAllSensitiveWords(senderId, transactionId, messageId, timestamp)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensitiveWordResponse> updateSensitiveWord(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            @RequestHeader("timestamp") String timestamp,
            @PathVariable String id,
            @Valid @RequestBody SensitiveWordRequest request) {

        if(request == null) {
            request.setHeader(enUtil.enrichSensitiveWordHeader(senderId, transactionId, messageId, timestamp));
        }

        return ResponseEntity.ok(sensitiveWordService.updateSensitiveWord(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensitiveWord(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            @RequestHeader("timestamp") String timestamp,
            @PathVariable String id
    ) {
        sensitiveWordService.deleteSensitiveWord(senderId, transactionId, messageId, timestamp, id);
        return ResponseEntity.noContent().build();
    }
}