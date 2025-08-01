# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-24 AS builder

WORKDIR /app
COPY pom.xml /app/
COPY src /app/src
RUN mvn clean package

# Stage 2: Create runtime image
FROM eclipse-temurin:24-jre

WORKDIR /app
COPY --from=builder /app/target/api-gateway-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8765
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

#BUILD
#docker build -t jherzog89/api-gateway-service:v1 .

#RUN in same network
#docker network create my-app-network
#docker run --name api-gateway-container --network my-app-network -p 8765:8765 jherzog89/api-gateway-service:v1