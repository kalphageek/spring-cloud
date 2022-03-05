package me.kalpha.userservice.controller;

import io.micrometer.core.annotation.Timed;
import me.kalpha.userservice.dto.UserDto;
import me.kalpha.userservice.jpa.UserEntity;
import me.kalpha.userservice.service.UserService;
import me.kalpha.userservice.vo.Greeting;
import me.kalpha.userservice.vo.RequestUser;
import me.kalpha.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
    private Environment env;
    private UserService userService;
    private ModelMapper mapper;

    @Autowired
    private Greeting greeting;


    @Autowired
    public UserController(Environment env, ModelMapper mapper, UserService userService) {
        this.mapper = mapper;
        this.userService = userService;
        this.env = env;
    }

    @GetMapping("/welcome")
    @Timed(value = "user.welcome", longTask = true)
    public String welcome() {
        return greeting.getMessage();
    }

    @GetMapping("/health_check")
    @Timed(value = "user.status", longTask = true)
    public String status() {
        return String.format("It's working in User Service"
                        + ", port(local.server.port) = %s, port(server.port) = %s"
                        + ", with token secret = %s, with token time = %s"
                , env.getProperty("local.server.port"), env.getProperty("server.port")
                , env.getProperty("token.secret"), env.getProperty("token.expiration_time"));
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> userEntities = userService.getUserByAll();
        List<ResponseUser> result = new ArrayList<>();
        userEntities.forEach(v -> {
            result.add(mapper.map(v, ResponseUser.class));
        });

        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);

        ResponseUser result = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.ok(result);
    }

}
