
FROM adoptopenjdk AS builder

RUN apt-get update && apt-get install -y maven

WORKDIR /app


COPY pom.xml .

RUN mvn dependency:go-offline


COPY src ./src


RUN mvn package -DskipTests


FROM adoptopenjdk


COPY --from=builder /app/target/*.jar /app/app.jar


CMD ["java", "-jar", "/app/app.jar"]

