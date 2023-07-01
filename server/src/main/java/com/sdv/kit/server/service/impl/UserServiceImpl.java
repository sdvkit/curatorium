package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.UserRegistrationDto;
import com.sdv.kit.server.mapper.UserMapper;
import com.sdv.kit.server.model.User;
import com.sdv.kit.server.model.enumerated.UserRole;
import com.sdv.kit.server.repository.UserRepository;
import com.sdv.kit.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    @Transactional
    @Cacheable(value = "users")
    @Override
    public Optional<User> register(UserRegistrationDto userRegistrationDto) {
        userRepository.findByUsername(userRegistrationDto.username())
                .ifPresent(user -> {
                    final String message = String.format("User with username %s already exists.", userRegistrationDto.username());
                    throw new ResponseStatusException(HttpStatus.CONFLICT, message);
                });

        final User user = USER_MAPPER.toEntity(userRegistrationDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.TEACHER);
        user.setIsLoggedIn(false);

        return Optional.of(userRepository.save(user));
    }
}
