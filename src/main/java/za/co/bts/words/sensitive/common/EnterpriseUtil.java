package za.co.bts.words.sensitive.common;

import org.springframework.stereotype.Component;
import za.co.bts.words.sensitive.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class EnterpriseUtil {

    public EnterpriseUtil() {}

    public SensitiveWordResponse createSensitiveWordResponse(EnterpriseHeaderRequest requestHeader, List<SensitiveWordDto> sensitiveWords, boolean isSuccess, String failureMessage) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Result result = new Result(
                isSuccess ? 0 : 1,
                isSuccess ? "SUCCESS" : "FAILURE",
                isSuccess ? "Success" : failureMessage
        );

        EnterpriseHeaderResponse header = new EnterpriseHeaderResponse(
                SenderIdType.SWSERVICE_V1.name(),
                requestHeader.getTransactionId(),
                uuid.toString(),
                requestHeader.getMessageId(),
                now.toString()
        );

        SensitiveWordResponsePayload payload = null;
        if(isSuccess) {
            payload =  new SensitiveWordResponsePayload(sensitiveWords);
        }

        SensitiveWordResponse res = new SensitiveWordResponse(
                header,
                result,
                payload
        );

        return res;
    }

    public SanitizationResponse createSanitizationResponse(EnterpriseHeaderRequest requestHeader, String sanitizedMessage, boolean isSuccess, String failureMessage) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Result result = new Result(
                isSuccess ? 0 : 1,
                isSuccess ? "SUCCESS" : "FAILURE",
                isSuccess ? "Success" : failureMessage
        );

        EnterpriseHeaderResponse header = new EnterpriseHeaderResponse(
                SenderIdType.getSenderIdTypeByValue(requestHeader.getSenderId()).name(),
                requestHeader.getTransactionId(),
                uuid.toString(),
                requestHeader.getMessageId(),
                now.toString()
        );

        SanitizationPayload payload = null;
        if(isSuccess) {
            payload =  new SanitizationPayload(sanitizedMessage);
        }

        SanitizationResponse res = new SanitizationResponse(
                header,
                result,
                payload
        );

        return res;
    }


    public EnterpriseHeaderRequest createResponseHeader(String senderId, String transactionId, String messageId) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        EnterpriseHeaderRequest header = new EnterpriseHeaderRequest(
                SenderIdType.getSenderIdTypeByValue(senderId).name(),
                transactionId,
                messageId,
                now.toString()
        );

        return header;
    }

    public EnterpriseHeaderRequest enrichSensitiveWordHeader(String senderId,  String transactionId, String messageId, String timestamp) {
        return EnterpriseHeaderRequest.builder()
                .senderId(senderId)
                .transactionId(transactionId)
                .messageId(messageId)
                .timestamp(timestamp)
                .build();
    }
}
