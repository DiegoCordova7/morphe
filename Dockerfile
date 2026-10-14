FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copiar todo el código fuente y submódulos (asegúrate de tener un .dockerignore limpio si hace falta)
COPY . .

RUN chmod +x gradlew
# Compilar exclusivamente el jar ejecutable de la API resolviendo las dependencias internas
RUN ./gradlew :morphe-api:bootJar --no-daemon --stacktrace

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# El JAR de un submódulo se genera dentro de su propia ruta (morphe-api/build/libs/)
COPY --from=build /app/morphe-api/build/libs/morphe-api-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]