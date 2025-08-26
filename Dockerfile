# ---------- Build ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw -q -DskipTests dependency:go-offline
COPY src src
RUN ./mvnw -q -DskipTests package

# ---------- Run ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
ENV PORT=8080
EXPOSE 8080
# Seguridad/estabilidad para memoria limitada
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70 -XX:+ExitOnOutOfMemoryError"
COPY --from=build /app/target/*.jar app.jar
CMD ["sh","-c","java -Dserver.port=${PORT} -jar app.jar"]
