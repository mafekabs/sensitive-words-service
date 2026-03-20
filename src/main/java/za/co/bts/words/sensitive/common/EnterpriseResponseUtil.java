package za.co.bts.words.sensitive.common;

import za.co.bts.words.sensitive.dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnterpriseResponseUtil {

    public EnterpriseResponseUtil() {}

    public static SanitizationResponse createSanitizationResponse(SanitizationRequest request, String sanitizedMessage, boolean isSuccess) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Result result = new Result(
            isSuccess ? 0 : 1,
            isSuccess ? "SUCCESS" : "FAILURE",
            isSuccess ? "Success" : "Failure"
        );

        EnterpriseHeaderResponse header = new EnterpriseHeaderResponse(
                SenderIdType.SWSERVICE_V1.name(),
                request.header().transactionId(),
                uuid.toString(),
                request.header().messageId(),
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

    public static SensitiveWordResponse createSensitiveWordResponse(EnterpriseHeaderRequest requestHeader, List<SensitiveWordDto> sensitiveWords, boolean isSuccess, String failureMessage) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Result result = new Result(
                isSuccess ? 0 : 1,
                isSuccess ? "SUCCESS" : "FAILURE",
                isSuccess ? "Success" : failureMessage
        );

        EnterpriseHeaderResponse header = new EnterpriseHeaderResponse(
                SenderIdType.SWSERVICE_V1.name(),
                requestHeader.transactionId(),
                uuid.toString(),
                requestHeader.messageId(),
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

    public static EnterpriseHeaderRequest createResponseHeader(String senderId, String transactionId, String messageId) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        EnterpriseHeaderRequest header = new EnterpriseHeaderRequest(
                SenderIdType.getSenderIdTypeByValue(senderId).getValue(),
                transactionId,
                messageId,
                now.toString()
        );

        return header;
    }
}
