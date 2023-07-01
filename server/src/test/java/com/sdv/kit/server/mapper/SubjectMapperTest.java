package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.SubjectCreationDto;
import com.sdv.kit.server.dto.SubjectDto;
import com.sdv.kit.server.model.Subject;
import com.sdv.kit.server.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "test")
public class SubjectMapperTest {

    private static final SubjectMapper SUBJECT_MAPPER = Mappers.getMapper(SubjectMapper.class);

    @Test
    void fromDtoToEntityTest() {
        final SubjectDto subjectDto = new SubjectDto(1L, "test_name");
        final Subject actualSubject = SUBJECT_MAPPER.toEntity(subjectDto);

        assertEquals(subjectDto.id(), actualSubject.getId());
        assertEquals(subjectDto.name(), actualSubject.getName());
    }

    @Test
    void fromEntityToDtoTest() {
        final Subject subject = Subject.builder()
                .id(1L)
                .name("test_name")
                .build();
        final SubjectDto actualDto = SUBJECT_MAPPER.toDto(subject);

        assertEquals(subject.getId(), actualDto.id());
        assertEquals(subject.getName(), actualDto.name());
    }

    @Test
    void fromCreationDtoToEntityTest() {
        final SubjectCreationDto subjectCreationDto = new SubjectCreationDto("test_name", 1L);
        final Subject actualSubject = SUBJECT_MAPPER.toEntity(subjectCreationDto);

        assertEquals(subjectCreationDto.name(), actualSubject.getName());
        assertEquals(subjectCreationDto.userId(), actualSubject.getUser().getId());
    }

    @Test
    void fromEntityToCreationDtoTest() {
        final User user = User.builder().id(1L).build();
        final Subject subject = Subject.builder()
                .name("test_name")
                .user(user)
                .build();
        final SubjectCreationDto actualSubjectCreationDto = SUBJECT_MAPPER.toCreationDto(subject);

        assertEquals(subject.getName(), actualSubjectCreationDto.name());
        assertEquals(subject.getUser().getId(), actualSubjectCreationDto.userId());
    }
}
