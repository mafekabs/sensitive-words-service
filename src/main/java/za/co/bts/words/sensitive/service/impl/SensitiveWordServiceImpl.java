package za.co.bts.words.sensitive.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Internal;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import za.co.bts.words.sensitive.common.EnterpriseResponseUtil;
import za.co.bts.words.sensitive.dto.*;
import za.co.bts.words.sensitive.exception.DuplicateResourceException;
import za.co.bts.words.sensitive.exception.ResourceNotFoundException;
import za.co.bts.words.sensitive.mapper.SensitiveWordMapper;
import za.co.bts.words.sensitive.model.SensitiveWord;
import za.co.bts.words.sensitive.repository.SensitiveWordRepository;
import za.co.bts.words.sensitive.service.SensitiveWordService;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SensitiveWordServiceImpl implements SensitiveWordService {
    private final SensitiveWordRepository repository;

    final SensitiveWordMapper mapper;

    public SensitiveWordServiceImpl(SensitiveWordRepository repository, SensitiveWordMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public SensitiveWordResponse addSensitiveWord(SensitiveWordRequest request) {

        try {

            var entity = mapper.toEntity(request.payload().sensitiveWord());
            var saved = repository.save(entity);

            var dto = mapper.toDto(saved);

            return EnterpriseResponseUtil.createSensitiveWordResponse(
                    request.header(),
                    List.of(dto),
                    true,
                    null
            );

        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException("Sensitive word already exists", ex);

        } catch (Exception ex) {
            throw new RuntimeException("Failed to add sensitive word", ex);
        }
    }

    @Override
    public SensitiveWordResponse getSensitiveWordById(
            String senderId,
            String transactionId,
            String messageId,
            String id
    ) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID must not be null or blank");
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid UUID format: " + id);
        }

        var dto = repository.findById(uuid)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sensitive word not found for id: " + id
                ));

        return EnterpriseResponseUtil.createSensitiveWordResponse(
                EnterpriseResponseUtil.createResponseHeader(senderId, transactionId, messageId),
                List.of(dto),
                true,
                null
        );
    }

    @Override
    public SensitiveWordResponse getAllSensitiveWords(
            String senderId,
            String transactionId,
            String messageId
    ) {

        List<SensitiveWordDto> dtos = mapper.toDtoList(repository.findAll());

        if (dtos == null) {
            throw new RuntimeException("Failed to retrieve sensitive words");
        }

        return EnterpriseResponseUtil.createSensitiveWordResponse(
                EnterpriseResponseUtil.createResponseHeader(senderId, transactionId, messageId),
                dtos,
                true,
                null
        );
    }

    @Override
    public SensitiveWordResponse updateSensitiveWord(
            String senderId,
            String transactionId,
            String messageId,
            String id,
            SensitiveWordRequest request
    ) {

        try {

            var entity = repository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Sensitive word not found for id: ", id)));

            entity.setWord(request.payload().sensitiveWord().word());
            SensitiveWordDto dto = mapper.toDto(repository.save(entity));

            return EnterpriseResponseUtil.createSensitiveWordResponse(
                    request.header(),
                    List.of(dto),
                    true,
                    null
            );
        } catch (Exception ex) {
            throw new RuntimeException("Failed to update sensitive word", ex);
        }
    }

    @Override
    public void deleteSensitiveWord(
            String senderId,
            String transactionId,
            String messageId,
            String id
    ) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID must not be null or blank");
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid UUID format: " + id);
        }

        var entity = repository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sensitive word not found for id: " + id
                ));

        repository.delete(entity);
    }
}