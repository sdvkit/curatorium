package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.user.UserRegistrationDto;
import com.sdv.kit.server.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> register(UserRegistrationDto userRegistrationDto);
}
