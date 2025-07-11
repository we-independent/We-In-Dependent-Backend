FROM eclipse-temurin:17-jdk as build
WORKDIR /app

# Copy the entire project including parent POM
COPY . .

RUN apt-get update && apt-get install -y maven && \
    mvn clean package -pl weindependent-app -am -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the jar of the submodule
COPY --from=build /app/weindependent-app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
