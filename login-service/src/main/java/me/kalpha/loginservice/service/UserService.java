package me.kalpha.loginservice.service;

import me.kalpha.loginservice.dto.UserDto;
import me.kalpha.loginservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();

    UserDto getUserByEmail(String email);
}
