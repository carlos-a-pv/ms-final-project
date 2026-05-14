# ---------- STAGE 1: build ----------
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src src
RUN mvn -q -DskipTests clean package


# ---------- STAGE 2: runtime ----------
FROM gcr.io/distroless/java17-debian12

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
USER nonroot

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]