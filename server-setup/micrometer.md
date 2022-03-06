### 1. 개요
    * Application의 모니터링을 위해 필요한 각 종 Metrics 정보를 수집한다.
    * Spring Framework 5 부터 Promethus와 자동 연동되며, 그외 다양한 모니터링 도구를 지원한다.
    * Timer
        > 가장 중요한 개념으로 짧은 지연시간, 이벤트 사용 빈도 등을 측정한다.
        ```java
        @Timed //Annotation 지원
        ```
### 2. Dependency
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```
### 3. Applicationy.yml
```yaml
management:
  endpoints:
    web:
      exposure:
        include: info, metrics, prometheus
```
### 4. Method에 @Timed 적용
```java
@Timed(value = "user.status", longTask = true)
public String status() {
    ...
}
```
### 5. Metric정보 확인
> 호출된 정보는 micrometer에의해 기록되고, 기록된 정보는 prometheus에 전달된다.
```
http://localhost:18082/actuator/metrics
...
users.status
...
```
```
http://localhost:18082/actuator/prometheus
...
users_status ...
...
```