## Api Gateway
> Spring MVC로 구성하지 않음. 따라서 HttpServletRequest/HttpServletResponse를 사용할 수 없다. 
> 반면, Spring Webflux를 통한 비동기 처리를 한다.
## Eureka Client의 Hostname in Window
> Windows에서 localhost를 사용하면 PC명 형태의 도메인명이 나오는 경우가 있다. 이름 127.0.0.1로 변경해준다
```yaml
eureka:  
  instance:
    hostname: localhost
```
## Api Gateway에 JsonWebToken을 추가하는 이유
1. 사용자로 부터 요청된 작업에 포함된 token이 적절한지 확인한다.
   * AuthorizationHeaderFilter에서 token을 검사후 문제없으면 사용자 요청을 업무서비스에게 그대로 전달한다.
2. Session 없이 Loadbalencing된 서버들간에 데이터의 공유를 가능하게 한다. 이때 Jwt Token을 Api Gateway가 DB에 저장한다.
## DatatypeConverter 에러 fix
> Caused by: java.lang.ClassNotFoundException: javax.xml.bind.DatatypeConverter 예방
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
</dependency>
```
## user-service Routes
1. [POST] /user-service/users 는 인증 필요없음
2. [POST] /user-service/login 은 인증 필요없음
   * headers에 token 생성됨
3. [GET] /user-service/health_check,  /user-service/welcome 은 인증 필요없음
4. [GET] /user-service/users는  인증 필요 함 (spring.cloud.gateway.routes에 AuthorizationHeaderFilter 추가)
   1. /login에서 생성된 token을 복사
   2. headers의 Authorization > Bearer Token 에 복사한 token을 붙여넣는다.
   3. Request를 send한다.

## HttpTrace 설정
1. application.yml 추가
```yaml
management:
  endpoints:
    web:
      exposure:
        include: httptrace
```
2. Bean 등록
```java
 @Bean
 public HttpTraceRepository httpTraceRepository() {
     return new InMemoryHttpTraceRepository();
 }
```
4. Route 등록
```yaml
 - id: user-service
   uri: lb://USER-SERVICE
   predicates:
     - Path=/user-service/actuator/**
     - Method=GET, POST
   filters:
     - RemoveRequestHeader=Cookie #매번 Request Header정보 초기화
     - RewritePath=/user-service/(?<segment>.*), /$\{segment} #/user-service/actuator -> /actuator로 변환해준다.
```
5. 호출
```
http://localhost:8000/user-service/actuator/httptrace
```