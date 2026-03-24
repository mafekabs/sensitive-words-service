package za.co.bts.words.sensitive.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import za.co.bts.words.sensitive.TestUtil;
import za.co.bts.words.sensitive.dto.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class SensitiveWordControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUtil  testUtil;

    HttpHeaders headers;
    SensitiveWordRequest request;

    List<String> testWordsIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
    }

    @Test
    void should_AddASensitiveWord() {
        HttpEntity<?> request = testUtil.createRequest(headers, testUtil.createSensitiveWordRequest("TEST1"), null);
        var response = restTemplate.exchange(
                "/api/v1/sensitive-words",
                HttpMethod.POST,
                request,
                SensitiveWordResponse.class
        );

        SensitiveWordResponse castedRes = (SensitiveWordResponse)response.getBody();
        String id = castedRes.payload().sensitiveWords().get(0).id();
        testWordsIds.add(id);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(0,  castedRes.result().resultCode());
        assertEquals("SUCCESS",  castedRes.result().resultMsgCode());
        assertEquals("Success",  castedRes.result().resultMsg());
        assertEquals("TEST1", castedRes.payload().sensitiveWords().get(0).word());
    }

    @Test
    void should_GetAllSensitiveWords() {
        HttpEntity<?> request = testUtil.createRequest(headers, null, null);
        var response = restTemplate.exchange(
                "/api/v1/sensitive-words",
                HttpMethod.GET,
                request,
                SensitiveWordResponse.class
                );

        SensitiveWordResponse castedRes = (SensitiveWordResponse)response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0,  castedRes.result().resultCode());
        assertEquals("SUCCESS",  castedRes.result().resultMsgCode());
        assertEquals("Success",  castedRes.result().resultMsg());
    }

    @Test
    void should_GetSensitiveWordById() {
        HttpEntity<?> request = testUtil.createRequest(headers, testUtil.createSensitiveWordRequest("TEST2"), null);
        var response = restTemplate.exchange(
                "/api/v1/sensitive-words",
                HttpMethod.POST,
                request,
                SensitiveWordResponse.class
        );

        SensitiveWordResponse castedRes = (SensitiveWordResponse)response.getBody();
        testWordsIds.add(castedRes.payload().sensitiveWords().get(0).id());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        HttpEntity<?> getRequest = testUtil.createRequest(headers, null, null);
        String benId = castedRes.payload().sensitiveWords().get(0).id();
        var getResponse = restTemplate.exchange(
                String.format("/api/v1/sensitive-words/%s",benId),
                HttpMethod.GET,
                request,
                SensitiveWordResponse.class
        );

        SensitiveWordResponse castedGetRes = (SensitiveWordResponse)response.getBody();

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(0,  castedGetRes.result().resultCode());
        assertEquals("SUCCESS",  castedGetRes.result().resultMsgCode());
        assertEquals("Success",  castedGetRes.result().resultMsg());
        assertEquals("TEST2",  castedGetRes.payload().sensitiveWords().get(0).word());
    }

    @Test
    void should_UpdateSensitiveWord() {
        HttpEntity<?> request = testUtil.createRequest(headers, testUtil.createSensitiveWordRequest("TEST4"), null);
        var response = restTemplate.exchange(
                "/api/v1/sensitive-words",
                HttpMethod.POST,
                request,
                SensitiveWordResponse.class
        );

        SensitiveWordResponse castedRes = (SensitiveWordResponse)response.getBody();
        String toBeUpdated = castedRes.payload().sensitiveWords().get(0).id();
        testWordsIds.add(toBeUpdated);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        SensitiveWordRequest updateBody = testUtil.createSensitiveWordRequest("TEST4_UPDATE");

        HttpEntity<?> updateRequest = testUtil.createRequest(headers, updateBody, null);

        var updateResponse = restTemplate.exchange(
                String.format("/api/v1/sensitive-words/%s",toBeUpdated),
                HttpMethod.PUT,
                updateRequest,
                SensitiveWordResponse.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());

        SensitiveWordResponse updatedRes = (SensitiveWordResponse)updateResponse.getBody();
        String updateId = updatedRes.payload().sensitiveWords().get(0).id();
        testWordsIds.add(updateId);
    }

    @Test
    void should_DeleteSensitiveWordById() {
        HttpEntity<?> request = testUtil.createRequest(headers, testUtil.createSensitiveWordRequest("TEST3"), null);
        var response = restTemplate.exchange(
                "/api/v1/sensitive-words",
                HttpMethod.POST,
                request,
                SensitiveWordResponse.class
        );

        SensitiveWordResponse castedRes = (SensitiveWordResponse)response.getBody();
        String toDeleteId = castedRes.payload().sensitiveWords().get(0).id();
        testWordsIds.add(toDeleteId);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        HttpEntity<?> deleteRequest = testUtil.createRequest(headers, null, null);

        var deleteResponse = restTemplate.exchange(
                String.format("/api/v1/sensitive-words/%s",toDeleteId),
                HttpMethod.DELETE,
                deleteRequest,
                SensitiveWordResponse.class
        );
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }

    /*
    * ++++++++++++++++++++++++++++++++++++ Negative Tests +++++++++++++++++++++++++++++++++++++
    * */

    @Test
    void should_Throw_DuplicateResourceException_AddASensitiveWord(){
        SensitiveWordRequest swRequest = testUtil.createSensitiveWordRequest("TEST1");
        HttpEntity<?> request = testUtil.createRequest(headers, swRequest, null);
        var response = restTemplate.exchange(
                "/api/v1/sensitive-words",
                HttpMethod.POST,
                request,
                SensitiveWordResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        SensitiveWordResponse castedRes = (SensitiveWordResponse)response.getBody();
        String id = castedRes.payload().sensitiveWords().get(0).id();
        testWordsIds.add(id);

        var response2 = restTemplate.exchange(
                "/api/v1/sensitive-words",
                HttpMethod.POST,
                request,
                SensitiveWordResponse.class
        );

        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
    }

    @Test
    void should_ResourceNotFoundException_getSensitiveWordById_404() {
        HttpEntity<?> deleteRequest = testUtil.createRequest(headers, null, null);

        String id = "d71ef2d4-030e-4e28-81f2-3e49039903f2";
        var getResponse = restTemplate.exchange(
                "/api/v1/sensitive-words/" + id,
                HttpMethod.GET,
                deleteRequest,
                SensitiveWordResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void should_Throw_IllegalArgumentException_GetSensitiveWordById_400() {
        HttpEntity<?> deleteRequest = testUtil.createRequest(headers, null, null);

        String id = "wrong uuid format";
        var getResponse = restTemplate.exchange(
                "/api/v1/sensitive-words/" + id,
                HttpMethod.GET,
                deleteRequest,
                SensitiveWordResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, getResponse.getStatusCode());
    }

    @Test
    void should_Throw_ResourceNotFoundException_UpdateSensitiveWord_404() {
        HttpEntity<?> request = testUtil.createRequest(headers, testUtil.createSensitiveWordRequest("TEST4"), null);
        var response = restTemplate.exchange(
                "/api/v1/sensitive-words",
                HttpMethod.POST,
                request,
                SensitiveWordResponse.class
        );

        SensitiveWordResponse castedRes = (SensitiveWordResponse)response.getBody();
        String toBeUpdatedId = castedRes.payload().sensitiveWords().get(0).id();
        testWordsIds.add(toBeUpdatedId);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        SensitiveWordRequest updateBody = testUtil.createSensitiveWordRequest("TEST4_UPDATE");

        HttpEntity<?> updateRequest = testUtil.createRequest(headers, updateBody, null);

        var updateResponse = restTemplate.exchange(
                "/api/v1/sensitive-words/d71ef2d4-030e-4e28-81f2-3e49039903f2",
                HttpMethod.PUT,
                updateRequest,
                SensitiveWordResponse.class
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, updateResponse.getStatusCode());
    }

    @Test
    void should_Throw_IllegalArgumentException_DeleteSensitiveWordById_400() {
        HttpEntity<?> request = testUtil.createRequest(headers, testUtil.createSensitiveWordRequest("TEST3"), null);
        var response = restTemplate.exchange(
                "/api/v1/sensitive-words",
                HttpMethod.POST,
                request,
                SensitiveWordResponse.class
        );

        SensitiveWordResponse castedRes = (SensitiveWordResponse)response.getBody();
        String toDeleteId = castedRes.payload().sensitiveWords().get(0).id();
        testWordsIds.add(toDeleteId);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        HttpEntity<?> deleteRequest = testUtil.createRequest(headers, null, null);

        var deleteResponse = restTemplate.exchange(
                "/api/v1/sensitive-words/1",
                HttpMethod.DELETE,
                deleteRequest,
                SensitiveWordResponse.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, deleteResponse.getStatusCode());
    }

    @Test
    void should_Throw_ResourceNotFoundException_DeleteSensitiveWordById_404() {
        HttpEntity<?> request = testUtil.createRequest(headers, testUtil.createSensitiveWordRequest("TEST3"), null);
        var response = restTemplate.exchange(
                "/api/v1/sensitive-words",
                HttpMethod.POST,
                request,
                SensitiveWordResponse.class
        );

        SensitiveWordResponse castedRes = (SensitiveWordResponse)response.getBody();
        String toDeleteId = castedRes.payload().sensitiveWords().get(0).id();
        testWordsIds.add(toDeleteId);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        HttpEntity<?> deleteRequest = testUtil.createRequest(headers, null, null);

        var deleteResponse = restTemplate.exchange(
                "/api/v1/sensitive-words/d71ef2d4-030e-4e28-81f2-3e49039903f2",
                HttpMethod.DELETE,
                deleteRequest,
                SensitiveWordResponse.class
        );
        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
    }

    @AfterEach
    void tearDown() {
        testUtil.clearTestWords(testWordsIds);
    }
}