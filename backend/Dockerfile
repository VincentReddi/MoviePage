FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

# 🛠 Das hier erlaubt das Ausführen von ./gradlew
RUN chmod +x gradlew

# ✅ Projekt bauen
RUN ./gradlew build

# ✅ JAR starten
CMD ["java", "-jar", "build/libs/app.jar"]

