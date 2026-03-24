package za.co.bts.words.sensitive.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.bts.words.sensitive.common.EnterpriseUtil;
import za.co.bts.words.sensitive.dto.*;
import za.co.bts.words.sensitive.model.SensitiveWord;
import za.co.bts.words.sensitive.repository.SensitiveWordRepository;
import za.co.bts.words.sensitive.service.SanitizationService;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SanitizationServiceImpl implements SanitizationService {
    private final SensitiveWordRepository repository;
    private final EnterpriseUtil enUtil;

    public SanitizationServiceImpl(SensitiveWordRepository repository, EnterpriseUtil enUtil) {
        this.repository = repository;
        this.enUtil = enUtil;
    }

    @Override
    public SanitizationResponse sanitize(SanitizationRequest request) {

        try {

            String message = request.getPayload().message();

            List<SensitiveWord> words = repository.findAll();

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

            return enUtil.createSanitizationResponse(
                    request.getHeader(),
                    sanitized,
                    true,
                    null
            );

        } catch (Exception ex) {
            log.error("Unexpected error during sanitization.", ex);

            return enUtil.createSanitizationResponse(
                    request.getHeader(),
                    null,
                    false,
                    "Unexpected error during sanitization."
            );
        }
    }
}