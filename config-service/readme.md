## 버전특성 조정
1. Parent와 Dependency 맞추기 위해 Spring Cloud 버전2020.0.1로 변경한다.
```xml
	<properties>
		<java.version>11</java.version>
		<spring-cloud.version>2020.0.1</spring-cloud.version>
	</properties>
```
## Git Local Repository 생성
```shell
<Git Bash> 실행
$mkdir /c:/workspace/spring-learning/git-local-repo
$cd /c:/workspace/spring-learning/git-local-repo
$git init
$vi ecommerce.yml
  token:
    expiration_time: 864000000
    secret: user_token

  gateway:
    ip: 192.168.0.2
  >wq
$git add ecommerce.yml
$git commit -m ""upload default application yaml file" 
```
## Spring Config에서 Git Local Repository 접근
> http://localhost:8888/ecommerce/default
* ecommerce -> ecommerce.yml
* default -> default profile
