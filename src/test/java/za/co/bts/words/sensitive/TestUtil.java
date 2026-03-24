package za.co.bts.words.sensitive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import za.co.bts.words.sensitive.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class TestUtil {
    @Autowired
    private TestRestTemplate restTemplate;

    public void addToTestWordIdList(String id, List<String> ids) {
        ids.add(id);
    }

    public void clearTestWords(List<String> ids) {
        ids.stream().forEach((id) -> {
            removeSensitiveWordById(id);
        });
    }

    private void removeSensitiveWordById(String id) {
        HttpEntity<?> request = createRequest(new HttpHeaders(), null, null);
        var getResponse = restTemplate.exchange(
                String.format("/api/v1/sensitive-words/%s",id),
                HttpMethod.DELETE,
                request,
                SensitiveWordResponse.class
        );
    }

    public SensitiveWordRequest createSensitiveWordRequest(String word) {
        EnterpriseHeaderRequest header = new EnterpriseHeaderRequest(SenderIdType.SWSERVICE_V1.name(), null, UUID.randomUUID().toString(), LocalDateTime.now().toString());
        SensitiveWordDto sensitiveWord = new SensitiveWordDto(null, word);
        SensitiveWordRequest request = new SensitiveWordRequest(
                header,
                new SensitiveWordRequestPayload(sensitiveWord)
        );

        return  request;
    }

    public SanitizationRequest createSanitizationRequest(String message) {
        EnterpriseHeaderRequest header = new EnterpriseHeaderRequest(SenderIdType.SWSERVICE_V1.name(), null, UUID.randomUUID().toString(), LocalDateTime.now().toString());
        SanitizationPayload payload = new SanitizationPayload(message);
        SanitizationRequest request = new SanitizationRequest(
                header,
                payload
        );

        return  request;
    }

    public HttpEntity<?> createRequest(HttpHeaders headers, Object body, String token) {
        if(token != null && !token.isEmpty()) {
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }

        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set("senderId", "SWSERVICE_V1");
        headers.set("messageId", UUID.randomUUID().toString());
        headers.set("timestamp", LocalDateTime.now().toString());

        return new HttpEntity<>(body, headers);
    }
}
