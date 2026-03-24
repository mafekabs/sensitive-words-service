package za.co.bts.words.sensitive.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import za.co.bts.words.sensitive.TestUtil;
import za.co.bts.words.sensitive.dto.SanitizationRequest;
import za.co.bts.words.sensitive.dto.SanitizationResponse;
import za.co.bts.words.sensitive.dto.SensitiveWordRequest;
import za.co.bts.words.sensitive.dto.SensitiveWordResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class SanitizationControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUtil testUtil;

    HttpHeaders headers;
    SanitizationRequest request;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
    }

    @Test
    void should_SanitizeMessage() throws JsonProcessingException {
        HttpEntity<?> request = testUtil.createRequest(headers, testUtil.createSanitizationRequest("SELECT * from sensitive_words."), null);
        var response = restTemplate.exchange(
                "/api/v1/sanitizations",
                HttpMethod.POST,
                request,
                SanitizationResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        SanitizationResponse castedRes = (SanitizationResponse)response.getBody();
        String message = castedRes.payload().message();


        assertEquals(0,  castedRes.result().resultCode());
        assertEquals("SUCCESS",  castedRes.result().resultMsgCode());
        assertEquals("Success",  castedRes.result().resultMsg());
        assertEquals("****** * from sensitive_words.", message);
    }
}
