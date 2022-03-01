package me.kalpha.userservice.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import me.kalpha.userservice.client.OrderServiceClient;
import me.kalpha.userservice.dto.UserDto;
import me.kalpha.userservice.jpa.UserEntity;
import me.kalpha.userservice.jpa.UserRepository;
import me.kalpha.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ModelMapper mapper;
    private Environment env;
//    private RestTemplate restTemplate;
    private OrderServiceClient orderServiceClient;
    private CircuitBreakerFactory circuitBreakerFactory;


    @Autowired
    public UserServiceImpl(UserRepository userRepository
            , BCryptPasswordEncoder passwordEncoder
            , ModelMapper mapper
            , Environment env
//            , RestTemplate restTemplate
            , OrderServiceClient orderServiceClient
            , CircuitBreakerFactory circuitBreakerFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.env = env;
//        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }



    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found : " + userId);
        }
        UserDto userDto = mapper.map(userEntity, UserDto.class);

        /* Using a Resttemplate */
//        String orderUrl = String.format(env.getProperty("order-service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse =
//                restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                                    new ParameterizedTypeReference<List<ResponseOrder>>() {
//                });
//        List<ResponseOrder> orderList = orderListResponse.getBody();

        /* Using a FeignClient */
//        /* Feign exception handling */
//        List<ResponseOrder> orderList = null;
//        try {
//            orderServiceClient.getOrders(userId);
//        } catch (FeignException ex) {
//            log.error(ex.getMessage());
//        }

        /* Error exception handling by ErrorDecoder automatically. No need to inject FeiginErrorDecoder */
//        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        //getOrders가 에러나면 빈 ArrayList를 반환한다.
        List<ResponseOrder> orderList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>());
        userDto.setOrders(orderList);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }
}
