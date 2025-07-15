# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only POM files first for dependency caching
COPY pom.xml .
COPY weindependent-app/pom.xml weindependent-app/
RUN mvn dependency:go-offline

# Now copy full source and build
COPY . .
RUN mvn clean package -pl weindependent-app -am -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/weindependent-app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--server.address=0.0.0.0"]