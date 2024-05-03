
FROM adoptopenjdk:22-jdk-hotspot AS builder


WORKDIR /app


COPY pom.xml .

RUN mvn dependency:go-offline


COPY src ./src


RUN mvn package -DskipTests


FROM adoptopenjdk:22-jdk-hotspot-slim


COPY --from=builder /app/target/*.jar /app/app.jar


CMD ["java", "-jar", "/app/app.jar"]

