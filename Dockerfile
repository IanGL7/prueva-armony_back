# ---------- Build ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copia wrapper y POM primero para cachear deps
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Permisos + quitar CRLF de Windows en mvnw
RUN chmod +x mvnw && sed -i 's/\r$//' mvnw

# Descarga dependencias (sin tests) y luego copia el código
RUN ./mvnw -q -DskipTests dependency:go-offline
COPY src src

# Empaqueta jar (sin tests) y sin barra de progreso para ahorrar logs
RUN ./mvnw -q -DskipTests -ntp package

# ---------- Run ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
ENV PORT=8080
EXPOSE 8080

# Memoria más segura en free tier
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70 -XX:+ExitOnOutOfMemoryError"

COPY --from=build /app/target/*.jar app.jar
CMD ["sh","-c","java -Dserver.port=${PORT} -jar app.jar"]
