FROM eclipse-temurin:17-jdk-alpine

# Arbeitsverzeichnis im Container
WORKDIR /app

# Projektdateien kopieren
COPY . .

# Projekt bauen
RUN ./gradlew build

# Die erstellte JAR-Datei starten
CMD ["sh", "-c", "java -jar build/libs/*.jar"]
