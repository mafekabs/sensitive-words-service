package za.co.bts.words.sensitive.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnterpriseHeaderRequest {
    @NotBlank(message = "Send ID is required.")
    private String senderId;
    private String transactionId;

    @NotBlank(message = "Message ID is required.")
    private String messageId;

    @NotBlank(message = "Timestamp is required.")
    private String timestamp;
}