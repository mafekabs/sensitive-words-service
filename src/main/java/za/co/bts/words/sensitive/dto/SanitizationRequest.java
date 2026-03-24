package za.co.bts.words.sensitive.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SanitizationRequest {
   @Valid
   @JsonProperty(required = false)
   private EnterpriseHeaderRequest header;

   @NotNull @Valid
   private SanitizationPayload payload;
}
