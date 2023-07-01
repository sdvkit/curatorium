package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.UserAccessDto;
import com.sdv.kit.server.dto.UserDto;
import com.sdv.kit.server.dto.UserLoginDto;
import com.sdv.kit.server.dto.UserRegistrationDto;
import com.sdv.kit.server.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User toEntity(UserLoginDto userLoginDto);

    UserLoginDto toLoginDto(User user);

    User toEntity(UserRegistrationDto userRegistrationDto);

    UserRegistrationDto toRegistrationDto(User user);

    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    User toEntity(UserAccessDto userAccessDto);

    UserAccessDto toAccessDto(User user);
}
