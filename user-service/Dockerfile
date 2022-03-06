FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/user-service-0.0.1-SNAPSHOT.jar user-service.jar
ENTRYPOINT ["java"]
CMD ["-jar", "user-service.jar"]