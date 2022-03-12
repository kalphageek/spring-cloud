FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/order-service-0.0.1-SNAPSHOT.jar order-service.jar
ENTRYPOINT ["java"]
CMD ["-jar", "order-service.jar"]