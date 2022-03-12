## Kafka sink connector for the orders table
```json
{
  "name" : "orders-sink-connect",
  "config" : {
    "connector.class" : "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.url" : "jdbc:mysql://localhost:3306/mydb",
    "connection.user" : "root",
    "connection.password" : "test1234",
    "auto.create" : "true",
    "auto.evolve" : "true",
    "delete.enabled" : "false",
    "topics" : "orders",
    "tasks.max" : "1"
  }
}
```

## Docker Image 생성
1. KafkaProducerConfig에 정의된 broker의 ip를 docker ip로 변경한다.
2. Dockerfile
```
FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/order-service-0.0.1-SNAPSHOT.jar order-service.jar
ENTRYPOINT ["java"]
CMD ["-jar", "user-service.jar"]
```
3. Compile & Docker샐행 Command
```sh
$ cd order-service
$ mvn clean compile package -DskipTests=true
$ docker build -t kalphageek/order-service:1.0 .
$ docker images
# application.yml의 rabbitmq host정보를 rabbitmq image이름으로 override한다.
$ docker run -d --name order-service --network ecommerce-network \
-e "spring.datasource.url=jdbc:mariadb://mariadb:3306/mydb" \
-e "spring.zipkin.base-url=http://zipkin:9411" \
-e "eureka.client.service-url.defaultZone=http://discovery-service:8761/eureka" \
-e "logging.file=/api-logs/order-ws.log" \
kalphageek/order-service:1.0

# privileges 에러나는 경우
$ docker exec -it mariadb /bin/bash
$ mysql -u root -p
Enter password : test1234
none> use mysql
mysql> grant all privileges on *.* to 'root'@'%' identified by 'test1234';
mysql> flush privileges;
exit
exit
# docker run 재실행

$ docker push kalphageek/order-service:1.0
$ docker logs -f order-service
```