FROM openjdk:19-ea-11-slim
VOLUME /tmp
COPY target/catalog-service-0.0.1-SNAPSHOT.jar catalog-service.jar
ENTRYPOINT ["java"]
CMD ["-jar", "catalog-service.jar"]