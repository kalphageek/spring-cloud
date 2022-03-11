FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/discovery-service-0.0.1-SNAPSHOT.jar discovery-service.jar
ENTRYPOINT ["java"]
CMD ["-jar", "discovery-service.jar"]