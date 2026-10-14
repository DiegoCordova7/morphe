FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Definir los ARG (Render los inyecta desde las Environment Variables)
ARG GITHUB_ACTOR
ARG GITHUB_TOKEN

ENV GITHUB_ACTOR=$GITHUB_ACTOR
ENV GITHUB_TOKEN=$GITHUB_TOKEN

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon --stacktrace

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/morphe-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]