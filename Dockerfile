# syntax=docker/dockerfile:1

# -------- Build stage --------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# download deps first for better caching
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# now add source and build
COPY src ./src
RUN mvn -B -DskipTests clean package

# -------- Runtime stage --------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Render provides PORT; Spring Boot must bind to it
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
ENV PORT=8080
EXPOSE 8080

# copy built jar from the builder image
COPY --from=build /app/target/*.jar app.jar

# run the app
CMD ["sh","-c","java $JAVA_OPTS -jar app.jar"]
