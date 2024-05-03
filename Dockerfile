FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

EXPOSE 8080

COPY --from=builder /app/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
