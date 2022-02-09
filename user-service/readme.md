## Eureka 설정
> Server와 Client의 설정을 함께  조정해야 한다
1. 유레카 서버의 캐쉬 업데이트 주기 조정옵션 (Server에 설정)
```yml
eureka:
  server:
    response-cache-update-interval-ms: 3000
```
1. 유레카 클라이언트의 레지스트리를 받아오는 주기를 조정 (Client에 설정)
```yml
eureka:
  client:
    registry-fetch-interval-seconds: 30    
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
1. Parent와 Dependency 맞추기 위해 Spring Cloud 버전2020.0.1로 변경한다.
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
1. Json에서 Null값 속성 제외 - Response객체에 사용한다.
```java
@JsonInclude(JsonInclude.Include.NON_NULL)
```
1. RequestUser에서 NotNull을 사용하기 위한 dependency
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
http://localhost:8082/login
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
      uri: http://127.0.0.1:8888
      name: ecommerce
```
> config-service의 application.yml에 등록된 "http://127.0.0.1:8888/ecommerce/default"에 등록된 정보를 읽어온다.<br>
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