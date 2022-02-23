## 버전특성 조정
1. Parent와 Dependency 맞추기 위해 Spring Cloud 버전2020.0.1로 변경한다.
* pom.xml
```xml
	<properties>
		<java.version>11</java.version>
		<spring-cloud.version>2020.0.1</spring-cloud.version>
	</properties>
```
## Git Local Repository 적용
1. Git Local Repository 생성
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
2-1. Spring Config에서 Git Local Repository 적용
* application.yml
```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: file://${user.home}/workspace/spring-learning/git-local-repo
# Windows format         
#          uri: file:///${user.home}/workspace/spring-learning/native-local-repo
```
> http://localhost:8888/ecommerce/default
* ecommerce -> ecommerce.yml
* default -> default profile
2-2. Spring Config에서 Git Private Repository 적용
* application.yml
```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: git@github.com:kalphageek/spring-cloud-config.git
          username: [username]
          password: [password]
```
## 대칭키를 이용한 암호화
1. Dependency 추가
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```
2. encrypt key 설정
* bootstrap.yml
```yaml
encrypt:
  key: 1234567890
```
2. Encrypt API 호출
```
[POST] http://localhost:8088/encrypt
[body > row > text]
[request body]
password
[return value]
01dc766ab8cdb62c2dc1b30853609965a522962f1fe10d6ad2ad8195fca2cffe
```
3. Decrypt API 호출
```
[POST] http://localhost:8088/encrypt
[body > row > text]
[request body]
01dc766ab8cdb62c2dc1b30853609965a522962f1fe10d6ad2ad8195fca2cffe
[return value]
password
```
4. 활용
* native-file-repo > user-service.yml
```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:testdb
    username: sa
    password: '{cipher}38e40f741d0d517c386a29bcd575bac6394a2a4eb4983b9292c770b147a30e92'
```
5. 확인
```html
http://localhost:8088/user-service/default
```

## 비대칭키를 이용한 암호화
1. 비대칭키 생성
```shell
$cd ~/workspace/spring-learning/spring-cloud
$mkdir keystore
$cd keystore 

#private key 파일 생성
$keytool -genkeypair -alias apiEncryptionKey -keyalg RSA -dname "CN=Kalphageek, OU=API Development, O=me.kalpha, L=Seoul, C=KR" -keypass "test1234" -keystore apiEncryptionKey.jks -storepass "test1234"

#private key 확인
$ls -al
$keytool -list -keystore apiEncryptionKey.jks [-v]
password> test123

#인증서 파일 생성
$keytool -export -alias apiEncryptionKey -keystore apiEncryptionKey.jks -rfc -file trustServer.cer
$ls -al

#public key 파일 생성
$keytool -import -alias trustServer -file trustServer.cer -keystore publicKey.jks
password :> test1234
certificate?> yes

#파일 확인
#config-service에서는 private key만 사용
$ls -al
apiEncryptionKey.jks  #private key
publicKey.jks         #public key
trustServer.cer       #인증서
```
2. Dependency 추가
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```
3. private key 설
* bootstrap.yml
```yaml
encrypt:
  location: file://${user.name}/workspace/spring-learning/spring-cloud/key-store/apiEncryptKey.jks
  alias: apiEncryptKey
  password: test1234
```
4. Encrypt API 호출 -> ## 대칭키를 이용한 암호화와 동일
5. Decrypt API 호출 -> ## 대칭키를 이용한 암호화와 동일
6. 활용 -> ## 대칭키를 이용한 암호화와 동일
7. 확인 -> ## 대칭키를 이용한 암호화와 동일