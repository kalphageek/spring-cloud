## 웹에서 설치 스크립트를 받아 간단설치
대부분의 리눅스 운영체제에서 개발환경에 빠르고 간단하게 사용할 수 있는 방법이다. 그러나 일반적으로는 권장하지 않는다.
```sh
$ curl -fsSL https://get.docker.com -o get-docker.sh
$ sudo sh get-docker.sh
```

## Ubuntu 정식 설치
```sh
# 설치 위치
https://docs.docker.com/engine/install/ubuntu/
# Old버전 삭제
$ sudo apt-get remove docker docker-engine docker.io containerd runc
# Repository 설정
$ sudo apt-get update
$ sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
# Docker의 공식 GPG 키 추가
$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/$ 
# 안정적인 Repository 설정docker-archive-keyring.gpg
$ echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
# 도커엔진 설칠
$ sudo apt-get update
$ sudo apt-get install docker-ce docker-ce-cli containerd.io      
```

## 사용자 권한 부여
```sh
# 현재 사용자를 docker Group에 추가
$ sudo usermod -aG docker $USER
# Docker 재시작 또는 시스템 재시작 필요
$ sudo service docker restart
```

## 도커 엔진 제거
* 커 엔진 및 관련 패키지 제거
```sh
$ sudo apt-get purge docker-ce docker-ce-cli containerd.io
```
* 잔여 호스트 이미지, 볼륨, 컨테이너 제거
```sh
$ sudo rm -rf /var/lib/docker
$ sudo rm -rf /var/lib/containerd
```

## Docker Resource
* network
* image
* container
```sh
$ docker network ls
$ docker image ls
$ docker container ls
```

## Container 실행
``` sh
$ docker [cmd] [option] [image:tag] [command] [arg...]
# 예)
$ docker run -d mariadb -p 3306:3306 -name mariadb mariadb:latest
$ docker exec -it mariadb /bin/bash
$ docker logs mariadb
$ docker system prune
```
* CMD

|CMD|설명|
|---|---|
|run|image의 pull + create + start|
|exec|container 실행|
|logs|container 실행 log|
|system prune|불필요한 resource와 container를 삭제한다.|

* 옵션

|옵션|설명|
|---|---|
|-d|detached mode|
|-p|port forwarding|
|-e|환경변수 설정|
|-v|호스트와 컨테이너의 디렉토리 연결(마운트)|
|--name|컨테이너 이름 설정|
|-it|터미널 입력을 위한 옵션|
|-link|커테이너 연결|

## Docker Network
> 하나의 network에 container들을 참여시킬 수 있는데, 동일한 network에 참여한 container들 간에는 ip 외에도 container-id/name으로도 통신이 가능하다.<br>
Container의 ip나 id는 새로 생성되는 시점에 변경될 수 있는데, 이 때문에 container-name으로 통신하도록 구성한다.
1. 타입
    * Bridge Netowrk
    * Host Network : Host와 container 간 network
    * None Network
1. 고정 ip사용을 고려한 network 생성
```sh
$ docker network create --gateway 172.18.0.1 --subnet 172.18.0.0/16 ecommerce-network
```
2. network 상세정보 보기
```sh
$ docker network ecommerce-network inspect
```
3. container를 network에 참여 시키기
```sh
$ docker network connect ecommerce-network rabbitmq
```

## Dockerfile 생성 & Docker Hub에 올리기
```sh
$ cd workspace/user-service
$ mvn clean compile package -DskipTests=true
$ Vi Dockerfile
FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/user-service-0.0.1-SNAPSHOT.jar user-service .jar
ENTRYPOINT ["java"]
CMD ["-jar", "user-service.jar"]
:wq
$ docker build --tag kalphageek/user-service:1.0 .
$ docker images
$ docker push kalphageek/user-service:1.0
```

## Open jdk 11 slim 버전  docker image 검색
> http://hub.docker.com
```
search : openjdk -> Official image -> Tags
filter : 11 -
    > 19-ea-11-slim
    ```
```