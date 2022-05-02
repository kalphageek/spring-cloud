FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/apigateway-service-0.0.2-SNAPSHOT.jar apigateway-service.jar
ENTRYPOINT ["java"]
CMD ["-jar", "apigateway-service.jar"]