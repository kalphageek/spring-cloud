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