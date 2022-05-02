## Eureka 설정
> Server와 Client의 설정을 함께  조정해야 한다
1. 유레카 서버의 캐쉬 업데이트 주기 조정옵션 (Server에 설정)
```yml
eureka:
  server:
    response-cache-update-interval-ms: 3000
```
2. 유레카 클라이언트의 레지스트리를 받아오는 주기를 조정 (Client에 설정)
```yml
eureka:
  client:
    registry-fetch-interval-seconds: 30    
```
## Eureka Client의 Hostname in Window
> Windows에서 localhost를 사용하면 PC명 형태의 도메인명이 나오는 경우가 있다. 이름 127.0.0.1로 변경해준다
```yaml
eureka:  
  instance:
    hostname: localhost
```
## 버전특성 조정
1. H2는 1.4 에서는 자동으로 DB가 생성안된다. Test용이라 1.3을 그냥 사용한다.
```xml
		<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
    <version>1.3.176</version>
</dependency>
```
2. Parent와 Dependency 맞추기 위해 Spring Cloud 버전2020.0.1로 변경한다.
```xml
	<properties>
		<java.version>11</java.version>
		<spring-cloud.version>2020.0.1</spring-cloud.version>
	</properties>
```
## Annotation 설명
1. createdAt 자동생성 하기
```java
    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;
```
2. Json에서 Null값 속성 제외 - Response객체에 사용한다.
```java
@JsonInclude(JsonInclude.Include.NON_NULL)
```
3. RequestUser에서 NotNull을 사용하기 위한 dependency
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
## JPA설정
1. 초기 데이터 생성을 위해 resources/data.sql 자동실행
```yml
spring:
  jpa:
    generate-ddl: true
```
## Spring Security
* Web UI 적용
  1. https://bamdule.tistory.com/53
* Login 순서
    1. /login 호출
    2. AuthenticationFilter.attemptAuthentication 호출
       ( RequestLogin -> UsernamePasswordAuthenticationToken으로 변환 )
    3. UserService(UserDetailService).loadUserByUserName 호출
       ( UserEntity -> User로 변환 )
    4. AuthenticationFilter.successfulAuthentication 호출
       (User 참조)

1. Spring Security를 Import하면 자동으로 Login 엔드포인트를 제공한다
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
```html
http://localhost:18082/login
```
## Spring Cloud Config 추가
1. Dependency 추가
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```
2. bootstrap.yml 추가
> applicaiton.yml보다 먼저 loading된다.
```yaml
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8088
      name: ecommerce
```
> config-service의 application.yml에 등록된 "http://127.0.0.1:8088/ecommerce/default"에 등록된 정보를 읽어온다.<br>
> 이는 결국 git-local-rep/ecommerce.yml 읽게 된다.
3. git-local-rep/ecommerce.yml 변경 후 재 적용 방법
    1. user-service 재기동
    2. Actuator refresh
    3. Spring cloud bus
## git-local-rep/ecommerce.yml 변경 후 재 적용 (Actuator refresh)
1. Dependency 추가
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-actuator</artifactId>
</dependency>
```
2. Actuator endpoints API 추가
> application.yml에 추가
```yaml
management:
  endpoints:
    web:
      exposure:
        include: refresh
```
3. Actuator refresh API 호출
```
[POST] http://localhost:8082/actuator/refesh
```
## 대칭키를 이용한 암호화
1. Git Local Repository에 추가
> '{cipher}...' 는 암화화된 텍스트로 식해서 bootstrap.yml에 등록된 http://localhost:8088/decrypt API를 통해 복호화한다.
* user-service.yml
```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:testdb
    username: sa
    password: '{cipher}38e40f741d0d517c386a29bcd575bac6394a2a4eb4983b9292c770b147a30e92'
```
2. Spring Config Service의 Profile 정보 확인
```html
http://localhost:8088/user-service/default
```
## IP -> Service Name 변경
1. UserServiceApplication.java
```java
@Bean
@LoadBalanced  #<--추가
public RestTemplate getRestTemplate() {
    return new RestTemplate();
}
```
2. native-local-repo/user-service.yml
```yaml
order-service:
  # url: http://127.0.0.1:8000/order-service/%s/orders
  url: http://ORDER-SERVICE/order-service/%s/orders
```
3. busrefresh
```
http://127.0.0.1:8000/actuatory/busrefresh
```
## FeignClient 사용
1. Dependency 추가
```yaml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
2. Main Class
```java
@EnableFeignClients
public class UserServiceApplication {    
}
```
3. Interface 생성
```java
@FeignClient(name = "order-service")
public interface OrderServiceClient {
    @GetMapping("/order-service/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable String userId);
}
```
4. Call API
```java
@Autowired
private OrderServiceClient orderServiceClient;
...
List<ResponseOrder> orderList=orderServiceClient.getOrders(userId);
```
## CircuitBreaker 적용
1. Dependency
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```
2. Defaoult 적용
```java
CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
//getOrders가 에러나면 빈 ArrayList를 반환한다.
List<ResponseOrder> orderList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
        throwable -> new ArrayList<>());
```
3. CircuitBreakerFactory 커스터마이징
```java
@Configuration
public class Resilience4jConfig {
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfig() {
        // TODO
    }
}
```
## Sleuth & Zipkin 적용
1. Dependency
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```
2. Application.yml 설정
```yaml
spring:
  zipkin:
    based-url: http://localhost:9411
    enabled: true
  sleuth:
    sampler:
    probability: 1.0 # zipkin 서버에 100% 전달한다는 의미
```
3. 필요한 곳에 log 적용
```java
@GetMapping("/{userId}/orders/{orderId}")
public ResponseEntity<ResponseOrder> getOrder(@PathVariable("userId") String userId,
                                              @PathVariable("orderId") String orderId) {
    log.info("Before call order-service  microservice (getOrder)");
    OrderDto orderDto = orderService.getOrderByOrderId(orderId);
    // TODO
    log.info("After call order-service  microservice (getOrder)");
    return ResponseEntity.ok(result);
}
```
```shell
2022-03-01 20:56:20.164  INFO [order-service,efda907f96b9bbc8,efda907f96b9bbc8] 111754 --- [io-18084-exec-2] m.k.o.controller.OrderController         : Before call order-service microservice (getOrders)
2022-03-01 20:56:38.944  INFO [order-service,cb2948cb9721e5c4,881dbc61ff033c29] 111754 --- [io-18084-exec-3] m.k.o.controller.OrderController         : After call order-service  microservice (getOrders)
```
> log에 자동적용된 trace id (efda907f96b9bbc8) 를 복사해서 zipkin 화면에서 조회 <br>
> http://localhost:9411
## Micrometer 적용
1. Dependency
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```
2. Application.yml
```yaml
management:
  endpoints:
    web:
      exposure:
        include: info, metrics, prometheus
```
3. 사용자 정의 정보 표시
```java
@Timed(value = "user.status", longTask = true)
public String status() {
    // TODO
}
```
## Docker Image 생성
1. Dockerfile
```
FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/user-service-0.0.1-SNAPSHOT.jar user-service.jar
ENTRYPOINT ["java"]
CMD ["-jar", "user-service.jar"]
```
2. Compile & Docker샐행 Command
```sh
$ cd user-service
$ mvn clean compile package
$ docker build -t kalphageek/login-service:1.0 .
$ docker images
# apigateway를 통해 접속하기 때문에 port forwading을 지정할 필요 없다.
# application.yml의 rabbitmq host정보를 rabbitmq image이름으로 override한다.
$ docker run -d --name login-service --network ecommerce-network \
-e "spring.cloud.config.uri=http://config-service:8888" \
-e "spring.rabbitmq.host=rabbitmq" \
-e "spring.zipkin.base-url=http://zipkin:9411" \
-e "eureka.client.service-url.defaultZone=http://discovery-service:8761/eureka" \
-e "logging.file=/api-logs/users-ws.log" \
kalphageek/login-service:1.0
$ docker push kalphageek/login-service:1.0
$ docker logs -f login-service
```