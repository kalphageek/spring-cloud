### 1. 개요
> 데이터 시각화, 모니터링 및 분석. 시계열 데이터 시각화

### 2. 설치
```shell
$ cd Downloads
$ wget https://dl.grafana.com/enterprise/release/grafana-enterprise-8.4.2.linux-amd64.tar.gz
$ tar -zxvf grafana-enterprise-8.4.2.linux-amd64.tar.gz
$ mv grafana-8.4.2 ../workspace/
```

### 3. 실행
```shell
$ cd grafana-8.4.2
$ ./bin/grafana-server
```

### 4. Web UI
```
http://localhost:3000 [admin/admin]
```

### 5. Prometheus 와 연동
* 메뉴 : 설정 > Configuration > Datasource
* 연동 대상
    1. Micrometer
    2. Prometheus
    3. Spring Cloud Gateway
* Promethus
    1. URL : http://locahost:9090
    2. Save&Test

### 6. 대시보드 Import 
    1. Grafana에서 제공 Dashboard 가져오기 : http://grafana.com > Dashboards (페이지 가장 아래)
        1. Search : miorometer, Prometheus 2.0 Overview, Spring Cloud Gateway
        2. JVM (Micrometer) 선택
        3. Copy ID to Clipboard 
    2. 메뉴 : 설정 > + > Import
        1. import via gragana.com : Ctrl+V -> Load
        2. Prometheus 선택
        3. Import
    3. 2개의 Dashboard 완성됨

 ### 7. 대시보드 수정
 1. 값이 나오지 않는 Panel에서 edit
 2. Datasource 확인
 3. Metrics Browser 화인
    * sum : 파라미터를 Prometheus 서버의 검색에서 타이핑하면서 확인한다. 파라미터 명이 변경 되었으면, 변경된 이름으로 다시 등록한다. (예: gateway_requests_seconds_count ->  spring_cloud_gateway_requests_seconds_count)
    * job : Prometheus서버 > prometheus.yml > scrape_configs.job_name 변수를 상수로 입력할 수 있다. (예: $gatewayService -> user_service)
    * routeId : apigateway > application.yml > spring.cloud.gateway.routes.id
3. 차트에 서비스 추가
> CPU Usage에 apigateway, order, user 서비스를 한번에 보는 경우
    * [+ Query ] 를 크릴하고, 값음 입력한다.