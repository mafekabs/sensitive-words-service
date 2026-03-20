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
    public SanitizationResponse sanitize(SanitizationRequest request) {

        try {
            if (request == null || request.payload() == null) {
                return EnterpriseResponseUtil.createSanitizationResponse(
                        request,
                        null,
                        false
                );
            }

            String message = request.payload().message();

            if (message == null || message.isBlank()) {
                return EnterpriseResponseUtil.createSanitizationResponse(
                        request,
                        message,
                        true
                );
            }

            List<SensitiveWord> words = repository.findAll();

            if (words == null || words.isEmpty()) {
                return EnterpriseResponseUtil.createSanitizationResponse(
                        request,
                        "No sensitive words found from the DB",
                        false
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
                    .replaceAll("****");

            return EnterpriseResponseUtil.createSanitizationResponse(
                    request,
                    sanitized,
                    true
            );

        } catch (PatternSyntaxException ex) {
            log.error("Invalid regex pattern for sensitive words.", ex);

            return EnterpriseResponseUtil.createSanitizationResponse(
                request,
                ex.getMessage(),
                false
        );

        } catch (Exception ex) {
            log.error("Unexpected error during sanitization.", ex);

            return EnterpriseResponseUtil.createSanitizationResponse(
                    request,
                    ex.getMessage(),
                    false
            );
        }
    }
}