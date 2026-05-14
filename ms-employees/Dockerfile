# ---------- STAGE 1: build ----------
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw clean package -DskipTests


# ---------- STAGE 2: runtime ----------
FROM gcr.io/distroless/java21-debian12

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
USER nonroot

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]