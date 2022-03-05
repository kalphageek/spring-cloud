1. 개요
> Metrics를 수집하고 모니터링 및 알람에 사용. Pull방식의 구조와 다양한 exporter제공.

2. Prometheus 설치
```shell
$ cd Downloads
$ wget https://github.com/prometheus/prometheus/releases/download/v2.34.0-rc.0/prometheus-2.34.0-rc.0.linux-amd64.tar.gz
$ tar xvf prometheus-2.34.0-rc.0.linux-amd64.tar.gz
$ mv mv prometheus-2.34.0-rc.0.linux-amd64 ../workspace/prometheus-2.34.0
```

3. user-service 에 설정 적용
* pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
* application.yml
```yaml
management:
  endpoints:
    web:
      exposure:
        include: info, metrics, prometheus
```

4. Prometheus 설정
```shell
$ cd workspace/prometheus-2.34.0
$ vi prometheus.yml
scrape_configs:
# add on the end of the file
  - job_name: 'user-serivce'
    scrape_interval: 15s
    metrics_path: '/user-service/actuator/prometheus'
    static_configs:
    - targets: ['localhost:8000']
  - job_name: 'order-serivce'
    scrape_interval: 15s
    metrics_path: '/order-service/actuator/prometheus'
    static_configs:
    - targets: ['localhost:8000']
  - job_name: 'apigateway-serivce'
    scrape_interval: 15s
    metrics_path: '/actuator/prometheus'
    static_configs:
    - targets: ['localhost:8000']
  :wq
```

5. Prometheus 실행
```shell
$ ./prometheus --config.file=prometheus.yml
```

6. Web UI
> http://localhost:9090