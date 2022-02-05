package me.kalpha.secondservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second-service")
@Slf4j
public class WelcomeController {
    @GetMapping("/welcome")
    public String welcome() {
        return "second-service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request") String header) {
        log.info(header);
        return "Hello : " + header;
    }
    @GetMapping("/check")
    public String check() {
        log.info("Hi there. this is a message from second message");
        return "Hi there. this is a message from second message";
    }
}
