package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.user.UserDto;
import com.sdv.kit.server.dto.user.UserLoginDto;
import com.sdv.kit.server.dto.user.UserRegistrationDto;
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
}
