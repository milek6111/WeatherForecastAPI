
FROM adoptopenjdk AS builder

RUN apt-get update && apt-get install -y maven

WORKDIR /app


COPY pom.xml .

RUN mvn dependency:go-offline


COPY src ./src


RUN mvn package -DskipTests -Dmaven.compiler.source=22 -Dmaven.compiler.target=22


FROM adoptopenjdk


COPY --from=builder /app/target/*.jar /app/app.jar


CMD ["java", "-jar", "/app/app.jar"]

