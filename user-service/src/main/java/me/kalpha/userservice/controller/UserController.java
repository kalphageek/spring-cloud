package me.kalpha.userservice.controller;

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
    public String welcome() {
        return greeting.getMessage();
    }

    @GetMapping("/health_check")
    public String check() {
        return String.format("It's working in User Service on PORT : %s!"
                , env.getProperty("local.server.port"));
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