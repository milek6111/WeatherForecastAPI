
FROM adoptopenjdk AS builder

RUN apt-get update && \
    apt-get install -y wget && \
    wget https://apache.osuosl.org/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz && \
    tar -xf apache-maven-3.8.5-bin.tar.gz && \
    mv apache-maven-3.8.5 /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/local/bin/mvn && \
    rm -f apache-maven-3.8.5-bin.tar.gz

WORKDIR /app


COPY pom.xml .

RUN mvn dependency:go-offline


COPY src ./src


RUN mvn package -DskipTests -Dmaven.compiler.source=22 -Dmaven.compiler.target=22


FROM adoptopenjdk


COPY --from=builder /app/target/*.jar /app/app.jar


CMD ["java", "-jar", "/app/app.jar"]

