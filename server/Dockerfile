FROM openjdk:17-jdk-slim

WORKDIR /server

COPY ./server/build/libs/curatorium-server-0.0.1-SNAPSHOT.jar ./server.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "server.jar"]