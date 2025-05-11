# Gwarancje dostarczania w Apache Kafka

Kod źródłowy do webinaru [Gwarancje dostarczania wiadomości w Apache Kafka](https://effectivedev.pl/webinar/gwarancje-dostarczania)

## Jak uruchomić

### Wymagania wstępne

* Java 21 lub wyższa
* Docker i Docker Compose
* Maven (lub użyj dołączonego wrapperu Maven)

### Uruchamianie klastra Kafki

1. Uruchom klaster Kafki (składający się z 3 brokerów) oraz Kafka-UI do pogłądu:

```bash
   docker-compose up -d
   ```
2. Automatycznie zostaną utworzone dwa topiki:
   - `app-execute_stuff-command`
   - `app-stuff_executed-event`
3. Kafka-UI będzie dostępne pod adresem: http://localhost:8080
4. Klaster Kafki będzie dostępny pod adresami:
   - `localhost:19092,localhost:19093,localhost:19094`

### Opis przykładów

W repozytorium znajdziesz implementację 3 serwisów.

```mermaid
flowchart LR
    A["producer-service"] --> B(["app-execute_stuff-command"])
    B --> C["app-service"]
    C --> D(["app-stuff_executed-event"])
    D --> E["consumer-service"]

    A@{ shape: rounded}
    C@{ shape: rounded}
    E@{ shape: rounded}
    style A color:#FFFFFF,fill:#00C853
    style C color:#FFFFFF,fill:#00C853
    style E color:#FFFFFF,fill:#00C853

```

#### producer-service
Produkuje inicjalną `command` na topik: `app-execute_stuff-command`

#### app-service
Konsumuje `command` i produkuje wynik w postaci `eventu` na topik: `app-stuff_executed-event`

#### consumer-service
Konsumuje wynikowy `event`

Każdy z serwisów posaiada zestaw dodatkowych plików konfiguracyjnych, dla różnych poziomów gwarancji dostarczania.
Czyli mamy profile:
- `at-most-once`
- `at-least-once`
- `exactly-once`

Po uruchomieniu wszystkich serwisów z tym samym, wybranym profilem, otrzymasz aplikację działającą w odpowiednim trybie gwarancji.


### Uruchamianie aplikacji

Uruchom aplikację z wybranym profilem, aby zobaczyć różne przypadki użycia.

#### Użycie IntelliJ IDEA
Po zaimportowaniu projektu, znajdziesz gotowe, zapisane Run Configuration do uruchomienia aplikacji.

#### Użycie Mavena
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=<profil>
```

#### Użycie wrappera Mavena
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=<profil>
```