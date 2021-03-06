## H2 Mariadb 접속
* Driver Class : org.mariadb.jdbc.Driver
* URL: jdbc:mysql://localhost:3306/mydb
* User : root
* Password : test1234
## Kafka sink connector for the orders table
1. db sink connect
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
2. orders json data format
```json
{
  "schema": {
    "type": "struct",
    "fields": [
      {"type": "string", "optional": true, "field": "order_id"},
      {"type": "string", "optional": true, "field": "user_id"},
      {"type": "string", "optional": true, "field": "product_id"},
      {"type": "int32", "optional": true, "field": "qty"},
      {"type": "int32", "optional": true, "field": "unit_price"},
      {"type": "int32", "optional": true, "field": "total_amount"}
    ],
    "opitonal": false,
    "name": "orders"
  },
  "payload": {
    "order_id": "10001",
    "user_id": "AA0001",
    "product_id": "P00001",
    "qty": 5,
    "unit_price": 1000,
    "total_amout": 5000
  }
}
```

## Docker Image 생성
1. KafkaProducerConfig에 정의된 broker의 ip를 docker ip로 변경한다.
  }
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
kalphageek/order-service:1.0.1

# 도커이미지 스냅샷 생성
$ docker commit -p 225d8afd1b13  kalphageek/order-service-2:1.0 
$ docker run -d --name order-service-2 --network ecommerce-network \
-e "spring.datasource.url=jdbc:mariadb://mariadb:3306/mydb" \
-e "spring.zipkin.base-url=http://zipkin:9411" \
-e "eureka.client.service-url.defaultZone=http://discovery-service:8761/eureka" \
-e "logging.file=/api-logs/order-ws.log" \
kalphageek/order-service-2:1.0

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
## Schema Registry + Avro Value
1. pom.xml
```xml
<project>
	<repositories>
		<repository>
			<id>confluent</id>
			<url>http://packages.confluent.io/maven/</url>
		</repository>
	</repositories>
  
    <dependencies>
        <dependency>
            <groupId>io.confluent</groupId>
            <artifactId>kafka-avro-serializer</artifactId>
            <version>7.1.0</version>
        </dependency>
    </dependencies>
  
    <build>
        <plugins>
            <plugin>
                <groupId>io.confluent</groupId>
                <artifactId>kafka-schema-registry-maven-plugin</artifactId>
                <version>7.1.0</version>
            </plugin>
        </plugins>
    </build>
</project>
```
2. AvroSchema 구조
```json
{
  "type" : record,
  "name" : orders,
  "fields" : [
    {
      "name" : order_id,
      "type" : string
    },
    {
      "name" : user_id,
      "type" : string
    },
    {
      "name" : product_id,
      "type" : string
    },
    {
      "name" : qty,
      "type" : int
    },
    {
      "name" : unit_price,
      "type" : int
    },
    {
      "name" : total_amount,
      "type" : int
    }
  ]
}
```
3. Jdbc sink connector
```json
{
    "name" : "orders-sink-connect",
    "config" : {
        "connector.class" : "io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url" : "jdbc:mysql://localhost:3306/mydb",
        "connection.user" : "root",
        "connection.password" : "test1234",
        "key.converter":"org.apache.kafka.connect.storage.StringConverter",
        "key.converter.schema.enable":"false",
        "value.converter":"io.confluent.connect.avro.AvroConverter",
        "value.converter.schema.registry.url":"http://127.0.0.1:8081",
        "auto.create" : "true",
        "auto.evolve" : "true",
        "delete.enabled" : "false",
        "topics" : "orders",
        "tasks.max" : "1"
    }
}
```
