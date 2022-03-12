## Eureka 설정
> Server와 Client의 설정이 함께  조정해야 한다
- 유레카 서버의 캐쉬 업데이트 주기 조정옵션
```
eureka:
  server:
    response-cache-update-interval-ms: 3000
```
- 유레카 클라이언트의 레지스트리를 받아오는 주기를 조정
```
eureka:
  client:
    registry-fetch-interval-seconds: 30    
```
## Docker Image 생성
1. Dockerfile
```
FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/discovery-service-0.0.1-SNAPSHOT.jar discovery-service.jar
ENTRYPOINT ["java"]
CMD ["-jar", "discovery-service.jar"]
```
2. Compile & Docker샐행 Command
```sh
$ cd discovery-service
$ mvn clean compile package
$ docker build -t kalphageek/discovery-service:1.0 .
$ docker images
$ docker push kalphageek/discovery-service:1.0
# application.yml의 spring.cloud.config.uri 정보를 docker image이름으로 override한다.
$ docker run -d -p 8761:8761 --network ecommerce-network \
                -e "spring.cloud.config.uri=http://config-service:8888" \
                --name discovery-service kalphageek/discovery-service:1.0
```