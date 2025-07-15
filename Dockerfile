# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
# syntax=docker/dockerfile:1.4
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy POM files first
COPY pom.xml .
COPY weindependent-app/pom.xml weindependent-app/

# Cache Maven dependencies using BuildKit mount
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline

# Copy full source and build
COPY . .
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -pl weindependent-app -am -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/weindependent-app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--server.address=0.0.0.0"]