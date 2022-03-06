## 웹에서 설치 스크립트를 받아 간단설치
대부분의 리눅스 운영체제에서 개발환경에 빠르고 간단하게 사용할 수 있는 방법이다. 그러나 일반적으로는 권장하지 않는다.
```sh
$ curl -fsSL https://get.docker.com -o get-docker.sh
$ sudo sh get-docker.sh
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

## 컨테이너 실행
``` sh
$ docker run [option] image:tag [command] [arg...]
# 예)
$ docker exec -it mariadb /bin/bash
```
* run : image의 pull + create + start
* exec -it : Interactive 모드로 실행
* logs : container 실행 log

|옵션|설명|
|---|---|
|-d|detached mode|
|-p|port forwarding|
|-e|환경변수 설정|
|-v|호스트와 컨테이너의 디렉토리 연결(마운트)|
|--name|컨테이너 이름 설정|
|-it|터미널 입력을 위한 옵션|
|-link|커테이너 연결|

## Dockerfile 생성 & Docker Hub에 올리기
```sh
$ cd workspace/user-service
$ mvn clean compile package -DskipTests=true
$ Vi Dockerfile
FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/user-service-0.0.1-SNAPSHOT.jar user-service .jar
ENTRYPOINT ["java", "-jar", "user-service.jar"]
:wq
$ docker build --tag kalphageek/user-service:1.0 .
$ docker images
$ sudo docker login -u kalphageek
$ docker push kalphageek/user-service:1.0
```

* Open JDK 11 Slim 버전  Docker image 검색
    > http://hub.docker.com
    ```
    search : openjdk -> Official image -> Tags
    filter : 11 -
        > 19-ea-11-slim
    ```
