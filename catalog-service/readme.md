## Eureka 설정
> Server와 Client의 설정이 함께  조정해야 한다
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
1. Spring Cloud 버전 이슈로 Java 버전을 조정한다.
```xml
	<properties>
		<java.version>11</java.version>
		<spring-cloud.version>2020.0.5</spring-cloud.version>
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
## JPA설정
1. 초기 데이터 생성을 위해 resources/data.sql 자동실행
```yml
spring:
  jpa:
    generate-ddl: true
```

## Docker Image 생성
1. KafkaPConfumerConfig에 정의된 broker의 ip를 docker ip로 변경한다.
2. Dockerfile
```
FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/catalog-service-0.0.1-SNAPSHOT.jar catalog-service.jar
ENTRYPOINT ["java"]
CMD ["-jar", "catalog-service.jar"]
```
3. Compile & Docker샐행 Command
```sh
$ cd catalog-service
$ mvn clean compile package -DskipTests=true
$ docker build -t kalphageek/catalog-service:1.0 .
$ docker images
# application.yml의 rabbitmq host정보를 rabbitmq image이름으로 override한다.
$ docker run -d --name catalog-service --network ecommerce-network \
-e "eureka.client.service-url.defaultZone=http://discovery-service:8761/eureka" \
-e "logging.file=/api-logs/catalog-ws.log" \
kalphageek/catalog-service:1.0
$ docker push kalphageek/catalog-service:1.0
$ docker logs -f catalog-service
```
