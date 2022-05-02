package me.kalpha.loginservice.controller;

import me.kalpha.loginservice.dto.UserDto;
import me.kalpha.loginservice.service.UserService;
import me.kalpha.loginservice.vo.RequestUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private Environment env;
    private UserService userService;
    private ModelMapper mapper;

    @Autowired
    public HomeController(Environment env, UserService userService, ModelMapper mapper) {
        this.env = env;
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/")
    public String homeView() {
        return "pages/home";
    }

    @GetMapping("/login")
    public String loginView() {
        return "pages/login";
    }

    @GetMapping("/signup")
    public String signupView() {
        return "pages/signup";
    }

    @PostMapping("/signup")
    public String signup(RequestUser requestUser) {
        UserDto userDto = mapper.map(requestUser, UserDto.class);
        userService.createUser(userDto);

        return "redirect:/login";
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/member/info")
    public String userInfoView() {
        return "pages/user_info";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminView() {
        return "pages/admin";
    }

    @GetMapping("/denied")
    public String deniedView() {
        return "pages/denied";
    }
}
