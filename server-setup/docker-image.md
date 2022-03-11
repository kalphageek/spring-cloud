## Rabbitmq
> 추가 작업이 없기 때문에 Dockerfile은 생성하지 않는다
```sh
$ docker run -d --name rabbitmq --network ecommerce-network \
-p 15672:15672 -p 5672:5672 -p 15671:15671 -p 5671:5671 -p 4369:4369 \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3-management
```
## Mariadb
```sh
$ docker run -d --name mariadb --network ecommerce-network \
-p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=test1234 \
-e MYSQL_DATABASE=mydb mariadb:10.4
``` 