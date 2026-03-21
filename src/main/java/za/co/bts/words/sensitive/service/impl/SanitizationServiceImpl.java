package za.co.bts.words.sensitive.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import za.co.bts.words.sensitive.common.EnterpriseResponseUtil;
import za.co.bts.words.sensitive.dto.*;
import za.co.bts.words.sensitive.model.SensitiveWord;
import za.co.bts.words.sensitive.repository.SensitiveWordRepository;
import za.co.bts.words.sensitive.service.SanitizationService;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static java.awt.SystemColor.text;

@Service
@Slf4j
public class SanitizationServiceImpl implements SanitizationService {
    private final SensitiveWordRepository repository;

    public SanitizationServiceImpl(SensitiveWordRepository repository) {
        this.repository = repository;
    }

    @Override
    public SanitizationResponse sanitize(
        String senderId,
        String transactionId,
        String messageId,
        SanitizationRequest request
    ) {

        try {
            if (request == null || request.payload() == null) {
                return EnterpriseResponseUtil.createSanitizationResponse(
                        request.header(),
                        null,
                        false,
                        "Payload is null"
                );
            }

            String message = request.payload().message();

            if (message == null || message.isBlank()) {
                return EnterpriseResponseUtil.createSanitizationResponse(
                        request.header(),
                        null,
                        false,
                        "Payload message is null"
                );
            }

            List<SensitiveWord> words = repository.findAll();

            if (words == null || words.isEmpty()) {
                return EnterpriseResponseUtil.createSanitizationResponse(
                        request.header(),
                        null,
                        false,
                        "No Sensitive Words found in the DB."
                );
            }

            String regex = words.stream()
                    .map(SensitiveWord::getWord)
                    .filter(Objects::nonNull)
                    .map(Pattern::quote)
                    .collect(Collectors.joining("|"));

            Pattern pattern = Pattern.compile("(?i)\\b(" + regex + ")\\b");

            String sanitized = pattern
                    .matcher(message)
                    .replaceAll(matchResult -> {
                        String word = matchResult.group();
                        return "*".repeat(word.length());
                    });

            return EnterpriseResponseUtil.createSanitizationResponse(
                    request.header(),
                    sanitized,
                    true,
                    null
            );

        } catch (PatternSyntaxException ex) {
            log.error("Invalid regex pattern for sensitive words.", ex);

            return EnterpriseResponseUtil.createSanitizationResponse(
                    request.header(),
                    null,
                    false,
                    "Invalid regex pattern for sensitive words."
        );

        } catch (Exception ex) {
            log.error("Unexpected error during sanitization.", ex);

            return EnterpriseResponseUtil.createSanitizationResponse(
                    request.header(),
                    null,
                    false,
                    "Unexpected error during sanitization."
            );
        }
    }
}