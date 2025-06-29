# Projekt: Moviesite

Dies ist unser Projekt für das Modul **Web-Technologien (Sommersemester 2025)** an der HTW Berlin.

## Projektidee

Eine moderne Web-App zur Verwaltung von Filmen, die man sehen will, bereits gesehen hat oder abgebrochen hat – inklusive Bewertung, Sortierung, TMDb-Anbindung und zufälligen Empfehlungen.

## Geplante Use Cases

1. Film suchen  
2. Filme nach Genre filtern  
3. Film zur Liste hinzufügen  
4. Status setzen (geplant, gesehen, abgebrochen)  
5. Zufallsvorschlag  
6. Filminformationen von TMDb anzeigen  
7. Film selber bewerten  

## Projekt lokal starten

```bash
./gradlew bootRun
```

Danach im Browser aufrufen:  
http://localhost:8080/movies  
→ Gibt eine Liste von zwei Dummy-Filmen als JSON zurück.

## M1 (20. April)

- GitHub-Repo erstellt  
- Thema definiert  
- Entity-Klasse `Movie` erstellt  
- Dummy-GET-Route `/movies` über Spring Boot implementiert