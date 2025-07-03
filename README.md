# 🎬 PopcornPilot 

**PopcornPilot** ist eine Webanwendung zur Verwaltung und Bewertung von Filmen. Nutzer können Filme durchsuchen, zur Watchlist hinzufügen, den Status setzen und eigene Bewertungen abgeben.  
Das Projekt wurde mit **Vue.js** im Frontend, **Spring Boot** im Backend und einer **PostgreSQL-Datenbank** umgesetzt.  

---

## 🚀 Live-Demo (Render Deployment)

👉 [Hier geht’s zur öffentlich erreichbaren Web-App](https://popcornpilot.onrender.com/)  

---


## 📦 Funktionen (Use Cases)

- 🔍 Filme suchen  
- 🎬 Filme nach Genre filtern  
- 📥 Film zur Watchlist hinzufügen  
- 🔄 Status setzen (geplant, gesehen, abgebrochen)  
- 🌙 Darkmode  
- 🧠 Filminformationen über externe TMDb-API anzeigen  
- ⭐ Film selber bewerten  

---

## 🧰 Projekt starten (lokal)

### Voraussetzungen

- Node.js (v18+)
- Java 17+
- PostgreSQL
- Gradle

### 1. Datenbank einrichten

- Lege lokal eine PostgreSQL-Datenbank an (z. B. `popcornpilot`)  
- Erstelle einen Benutzer mit Passwort  


### 2. Backend starten
```
cd backend
./gradlew bootRun
```
Läuft unter http://localhost:8080

### 3. Frontend starten
```
cd frontend
npm install
npm run dev
```
läuft unter http://localhost:5173
