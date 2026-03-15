# 1. сборка
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
# 2. оффлайн-сборка зависимостей
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# 3. Финальная стадия
FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=target/minesweeper-backend-*.jar
WORKDIR /app
COPY --from=builder /app/target/minesweeper-backend-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]