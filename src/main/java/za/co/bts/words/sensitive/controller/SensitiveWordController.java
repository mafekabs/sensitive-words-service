package za.co.bts.words.sensitive.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.function.EntityResponse;
import za.co.bts.words.sensitive.dto.SensitiveWordRequest;
import za.co.bts.words.sensitive.dto.SensitiveWordResponse;
import za.co.bts.words.sensitive.service.SensitiveWordService;

import java.net.URI;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sensitive-words")
public class SensitiveWordController {
    private final SensitiveWordService sensitiveWordService;

    public SensitiveWordController(SensitiveWordService sensitiveWordService) {
        this.sensitiveWordService = sensitiveWordService;
    }

    @PostMapping
    public ResponseEntity<SensitiveWordResponse> addSensitiveWord(
            @RequestBody SensitiveWordRequest request) {

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
            @PathVariable String id) {
        return ResponseEntity.ok(sensitiveWordService.getSensitiveWordById(senderId, transactionId, messageId, id));
    }

    @GetMapping
    public ResponseEntity<SensitiveWordResponse> getAll(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId
    ) {
        return ResponseEntity.ok(
                sensitiveWordService.getAllSensitiveWords(senderId, transactionId, messageId)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SensitiveWordResponse> updateSensitiveWord(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            @PathVariable String id,
            @RequestBody SensitiveWordRequest request) {
        return ResponseEntity.ok(sensitiveWordService.updateSensitiveWord(senderId, transactionId, messageId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensitiveWord(
            @RequestHeader("senderId") String senderId,
            @RequestHeader(value = "transactionId", required = false) String transactionId,
            @RequestHeader("messageId") String messageId,
            @PathVariable String id
    ) {
        sensitiveWordService.deleteSensitiveWord(senderId, transactionId, messageId, id);
        return ResponseEntity.noContent().build();
    }
}