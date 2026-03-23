# Campaign Manager

Aplikacja do zarządzania kampaniami (Spring Boot + React/Vite).

## Funkcje
- CRUD kampanii marketingowych
- Obsługa słów kluczowych i miasta
- Wgląd w saldo konta 


## Wymagania
- Java 21
- Maven (lub `./mvnw`)
- Node.js + npm

## Uruchomienie
1. Backend
    -  katalog `backend`
    - Uruchom:
      ```powershell
      .\mvnw.cmd spring-boot:run
      ```
      lub
      ```powershell
      mvn spring-boot:run
      ```
    - H2 Console: `http://localhost:8080/h2-console`
    - API base: `http://localhost:8080/api/v1`

2. Frontend
    - katalog `frontend\campaign-manager-ui`
    - Zainstaluj zależności i uruchom:
      ```powershell
      npm install
      npm run dev
      ```
    - Domyślny dev server Vite: `http://localhost:5173`

## Konfiguracja środowiska
- Frontend używa `VITE_API_URL` (domyślnie `http://localhost:8080/api/v1`). Przykładowy plik `.env` w `frontend\campaign-manager-ui`:
  ```env
  VITE_API_URL=http://localhost:8080/api/v1
