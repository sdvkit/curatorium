package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.UserDto;
import com.sdv.kit.server.dto.UserLoginDto;
import com.sdv.kit.server.dto.UserRegistrationDto;
import com.sdv.kit.server.model.User;
import com.sdv.kit.server.model.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "test")
public class UserMapperTest {

    private static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Test
    void fromLoginDtoToEntityTest() {
        final UserLoginDto userLoginDto = new UserLoginDto("test_username", "test_password");
        final User actualUser = USER_MAPPER.toEntity(userLoginDto);

        assertEquals(userLoginDto.username(), actualUser.getUsername());
        assertEquals(userLoginDto.password(), actualUser.getPassword());
    }

    @Test
    void fromEntityToLoginDtoTest() {
        final User user = User.builder()
                .username("test_username")
                .password("test_password")
                .build();
        final UserLoginDto actualLoginDto = USER_MAPPER.toLoginDto(user);

        assertEquals(user.getUsername(), actualLoginDto.username());
        assertEquals(user.getPassword(), actualLoginDto.password());
    }

    @Test
    void fromRegistrationDtoToEntityTest() {
        final UserRegistrationDto userRegistrationDto
                = new UserRegistrationDto("test_full_name", "test_username", "test_password");
        final User actualUser = USER_MAPPER.toEntity(userRegistrationDto);

        assertEquals(userRegistrationDto.username(), actualUser.getUsername());
        assertEquals(userRegistrationDto.password(), actualUser.getPassword());
        assertEquals(userRegistrationDto.fullName(), actualUser.getFullName());
    }

    @Test
    void fromEntityToRegistrationDtoTest() {
        final User user = User.builder()
                .username("test_username")
                .password("test_password")
                .fullName("test_full_name")
                .build();
        final UserRegistrationDto actualRegistrationDto = USER_MAPPER.toRegistrationDto(user);

        assertEquals(user.getUsername(), actualRegistrationDto.username());
        assertEquals(user.getPassword(), actualRegistrationDto.password());
        assertEquals(user.getFullName(), actualRegistrationDto.fullName());
    }

    @Test
    void fromDtoToEntityTest() {
        final UserDto userDto = new UserDto(1L, "test_full_name", "test_username", UserRole.TEACHER);
        final User actualUser = USER_MAPPER.toEntity(userDto);

        assertEquals(userDto.id(), actualUser.getId());
        assertEquals(userDto.username(), actualUser.getUsername());
        assertEquals(userDto.fullName(), actualUser.getFullName());
        assertEquals(userDto.role(), actualUser.getRole());
    }

    @Test
    void fromEntityToDtoTest() {
        final User user = User.builder()
                .id(1L)
                .username("test_username")
                .fullName("test_full_name")
                .role(UserRole.TEACHER)
                .build();
        final UserDto actualUserDto = USER_MAPPER.toDto(user);

        assertEquals(user.getId(), actualUserDto.id());
        assertEquals(user.getUsername(), actualUserDto.username());
        assertEquals(user.getFullName(), actualUserDto.fullName());
        assertEquals(user.getRole(), actualUserDto.role());
    }
}
