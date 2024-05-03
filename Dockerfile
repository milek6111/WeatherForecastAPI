# Wybierz obraz bazowy
FROM maven:3.8.4-openjdk-22 AS builder


WORKDIR /app


COPY pom.xml .

RUN mvn dependency:go-offline


COPY src ./src


RUN mvn package -DskipTests


FROM openjdk:22


COPY --from=builder /app/target/*.jar /app/app.jar


CMD ["java", "-jar", "/app/app.jar"]

