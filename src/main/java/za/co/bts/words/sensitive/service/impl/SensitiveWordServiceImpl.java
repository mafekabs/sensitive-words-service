package za.co.bts.words.sensitive.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import za.co.bts.words.sensitive.common.EnterpriseUtil;
import za.co.bts.words.sensitive.dto.*;
import za.co.bts.words.sensitive.exception.DuplicateResourceException;
import za.co.bts.words.sensitive.exception.ResourceNotFoundException;
import za.co.bts.words.sensitive.mapper.SensitiveWordMapper;
import za.co.bts.words.sensitive.repository.SensitiveWordRepository;
import za.co.bts.words.sensitive.service.SensitiveWordService;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SensitiveWordServiceImpl implements SensitiveWordService {
    private final SensitiveWordRepository repository;
    private final EnterpriseUtil enUtil;
    private final SensitiveWordMapper mapper;

    public SensitiveWordServiceImpl(SensitiveWordRepository repository, EnterpriseUtil enUtil, SensitiveWordMapper mapper) {
        this.repository = repository;
        this.enUtil = enUtil;
        this.mapper = mapper;
    }

    @Override
    public SensitiveWordResponse addSensitiveWord(SensitiveWordRequest request) {

        try {

            var entity = mapper.toEntity(request.getPayload().sensitiveWord());
            var saved = repository.save(entity);

            var dto = mapper.toDto(saved);

            return enUtil.createSensitiveWordResponse(
                    request.getHeader(),
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
            String timestamp,
            String id
    ) {
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

        return enUtil.createSensitiveWordResponse(
                enUtil.createResponseHeader(senderId, transactionId, messageId),
                List.of(dto),
                true,
                null
        );
    }

    @Override
    public SensitiveWordResponse getAllSensitiveWords(
            String senderId,
            String transactionId,
            String messageId,
            String timestamp
    ) {

        List<SensitiveWordDto> dtos = mapper.toDtoList(repository.findAll());

        return enUtil.createSensitiveWordResponse(
                enUtil.createResponseHeader(senderId, transactionId, messageId),
                dtos,
                true,
                null
        );
    }

    @Override
    public SensitiveWordResponse updateSensitiveWord(
            String id,
            SensitiveWordRequest request
    ) {

        try {
            UUID uuid = UUID.fromString(id);
            var entity = repository.findById(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Sensitive word not found for id: ", id)));

            entity.setWord(request.getPayload().sensitiveWord().word());
            SensitiveWordDto dto = mapper.toDto(repository.save(entity));

            return enUtil.createSensitiveWordResponse(
                    request.getHeader(),
                    List.of(dto),
                    true,
                    null
            );
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid UUID format: " + id);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to update sensitive word", ex);
        }
    }

    @Override
    public void deleteSensitiveWord(
            String senderId,
            String transationId,
            String messageId,
            String timestamp,
            String id) {

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