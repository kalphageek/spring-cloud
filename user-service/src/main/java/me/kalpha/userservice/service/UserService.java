package me.kalpha.userservice.service;

import me.kalpha.userservice.dto.UserDto;
import me.kalpha.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();

    UserDto getUserByEmail(String email);
}
