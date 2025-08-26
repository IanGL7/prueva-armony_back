# ---------- Build stage ----------
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el POM primero para cachear dependencias
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Ahora el código
COPY src ./src

# Empaquetar (jar) sin tests
RUN mvn -q -DskipTests -ntp package

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
ENV PORT=8080
EXPOSE 8080

# Memoria más segura para free tier
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70 -XX:+ExitOnOutOfMemoryError"

COPY --from=build /app/target/*.jar app.jar
CMD ["sh","-c","java -Dserver.port=${PORT} -jar app.jar"]
