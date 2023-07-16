package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.UserDto;
import com.sdv.kit.server.dto.UserRegistrationDto;
import com.sdv.kit.server.dto.UserRenameDto;
import com.sdv.kit.server.mapper.UserMapper;
import com.sdv.kit.server.model.User;
import com.sdv.kit.server.model.UserRole;
import com.sdv.kit.server.repository.UserRepository;
import com.sdv.kit.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "users")
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        final String usernameNotFoundExceptionMessage = String.format("There's no such User with username %s", username);
        return userRepository.findByUsername(username)
                .map(com.sdv.kit.server.model.UserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(usernameNotFoundExceptionMessage));
    }

    @Async
    @Transactional
    @Cacheable(value = "users")
    @Override
    public CompletableFuture<UserDto> register(UserRegistrationDto userRegistrationDto) {
        userRepository.findByUsername(userRegistrationDto.username())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT);
                });

        final User user = USER_MAPPER.toEntity(userRegistrationDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.TEACHER);
        user.setIsLoggedIn(false);

        final UserDto registeredUserDto = USER_MAPPER.toDto(userRepository.save(user));
        return CompletableFuture.completedFuture(registeredUserDto);
    }

    @Async
    @CachePut(value = "users")
    @Transactional
    @Override
    public CompletableFuture<UserDto> rename(String username, UserRenameDto userRenameDto) {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("There's no user with this username"));

        user.setUsername(userRenameDto.username());
        user.setFullName(userRenameDto.fullName());

        final UserDto renamedUserDto = USER_MAPPER.toDto(userRepository.save(user));
        return CompletableFuture.completedFuture(renamedUserDto);
    }
}
