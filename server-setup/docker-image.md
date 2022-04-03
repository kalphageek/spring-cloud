## Rabbitmq
> 추가 작업이 없기 때문에 Dockerfile은 생성하지 않는다
```sh
$ docker run -d --name rabbitmq --network ecommerce-network \
-p 15672:15672 -p 5672:5672 -p 15671:15671 -p 5671:5671 -p 4369:4369 \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin \
rabbitmq:3-management
```
## Mariadb
```sh
$ docker run -d --name mariadb --network ecommerce-network \
-p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=test1234 \
-e MYSQL_DATABASE=mydb \
mariadb:10.7.3
``` 
## Kafka
```sh
$ docker-compose -f docker-compose-single-broker-connect.yml up -d
```
## Zipkin
```sh
$ docker run -d --name zipkin --network ecommerce-network \
-p 9411:9411 \
openzipkin/zipkin
```
## Prometheus
```sh
# -v : host의 dir이나 file을 docker에서 공유할 수 있도록 해준다.
$ docker run -d --name prometheus --network ecommerce-network \
-p 9090:9090 \
-v /home/jjd/workspace/prometheus-2.34.0/prometheus.yml \
prom/prometheus
```
## Grafana
```sh
$ docker run -d --name grafana --network ecommerce-network \
-p 3000:3000 \
grafana/grafana
```