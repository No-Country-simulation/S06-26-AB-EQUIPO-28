# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-25-alpine AS build
WORKDIR /app

# Cachear dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120

# Compilar proyecto
COPY src ./src
RUN mvn clean package -DskipTests \
    -Dmaven.wagon.http.pool=false

# Etapa 2: Runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

ENV JAVA_OPTS="-Xms256m -Xmx450m -XX:MaxMetaspaceSize=256m -XX:+UseSerialGC"

# Copiar JAR desde etapa de build
COPY --from=build /app/target/app-bit-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]