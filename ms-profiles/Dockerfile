# Etapa 1: build
FROM gradle:8.5-jdk21-alpine AS builder
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon

COPY src ./src

RUN gradle build -x test --no-daemon

# Etapa 2: runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]