package za.co.bts.words.sensitive.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import za.co.bts.words.sensitive.dto.SensitiveWordDto;
import za.co.bts.words.sensitive.model.SensitiveWord;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface SensitiveWordMapper {
    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    SensitiveWordDto toDto(SensitiveWord entity);

    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUuid")
    SensitiveWord toEntity(SensitiveWordDto dto);

    List<SensitiveWordDto> toDtoList(List<SensitiveWord> entities);

    List<SensitiveWord> toEntityList(List<SensitiveWordDto> dtos);

    @Named("uuidToString")
    static String uuidToString(UUID id) {
        return id != null ? id.toString() : null;
    }

    @Named("stringToUuid")
    static UUID stringToUuid(String id) {
        return (id != null && !id.isBlank()) ? UUID.fromString(id) : null;
    }
}