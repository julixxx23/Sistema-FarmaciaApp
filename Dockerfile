
# ---- Stage 1: compilar ----
FROM gradle:8-jdk17-alpine AS build
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY src ./src
RUN gradle build -x test

# ---- Stage 2: correr ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]