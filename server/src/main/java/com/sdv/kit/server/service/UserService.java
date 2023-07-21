package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.UserDto;
import com.sdv.kit.server.dto.UserRegistrationDto;
import com.sdv.kit.server.dto.UserRenameDto;

import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<UserDto> register(UserRegistrationDto userRegistrationDto);

    CompletableFuture<UserDto> rename(String username, UserRenameDto userRenameDto);

    CompletableFuture<UserDto> getUserInfo(String username);
}
