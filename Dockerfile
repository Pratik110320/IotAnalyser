# --- build stage ---
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace
# copy only what is needed for Maven to leverage cache
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# --- runtime stage ---
FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=/workspace/target/*.jar
COPY --from=build ${JAR_FILE} /app/app.jar
# Use a non-root user (optional)
USER 1000
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
