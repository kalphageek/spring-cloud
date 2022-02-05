package me.kalpha.firstservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/first-service")
@Slf4j
public class WelcomeController {

    Environment env;

    @Autowired
    public WelcomeController(Environment env) {
        this.env = env;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "first-service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header) {
        log.info(header);
        return "Hello : " + header;
    }

    //Spring이 자동으로 request 채운다
    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server Port = {}", request.getServerPort());
        return String.format("First Service on PORT %s", env.getProperty("local.server.port"));
    }
}
