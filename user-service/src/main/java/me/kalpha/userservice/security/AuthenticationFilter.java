package me.kalpha.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import me.kalpha.userservice.dto.UserDto;
import me.kalpha.userservice.service.UserService;
import me.kalpha.userservice.vo.RequestLogin;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


/**
 * 인증하고, 인증이 되면 이루어지는 일에 대해 정의
 */
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UserService userService;
    private Environment env;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
        super.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    /**
     * 사용자로부터 받은 ReuestLogin의 Username, Password를 통해 인증 작업 수행
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // Post로 보내는 경우 RequestParam을 받을 수 없기 때문에 request를 stream을 이용해 수작업으로 들어오는 데이터를 처리한다
            RequestLogin cred = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            // RequestLogin의 포함된 username, password, 권한목록을 전달해서 인증작업을 요청 한다
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            cred.getEmail(),
                            cred.getPwd(),
                            new ArrayList<>())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 인증이 성공했을때 사용자 Token을 생성한다
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // UserDetails 받아오기
        User user = (User) authResult.getPrincipal();
        UserDto userDto = userService.getUserByEmail(user.getUsername());

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDto.getUserId());
    }
}
