FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/multiservicios-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} multiservicios.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","multiservicios.jar"]
