package za.co.bts.words.sensitive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.bts.words.sensitive.dto.SanitizationRequest;
import za.co.bts.words.sensitive.dto.SanitizationResponse;
import za.co.bts.words.sensitive.model.SensitiveWord;

import java.util.UUID;

public interface SensitiveWordRepository extends JpaRepository<SensitiveWord, UUID> {
}