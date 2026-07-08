# HRMS Backend

API REST per la gestione delle risorse umane costruita con **Spring Boot 4.0.2** e **Java 21**. Espone endpoint sicuri con autenticazione JWT e gestisce dipendenti, contratti, progetti, timbrature e richieste di ferie.

---

## Stack tecnologico

| Dipendenza | Versione | Ruolo |
|---|---|---|
| Spring Boot | 4.0.2 | Framework applicativo |
| Java | 21 | Runtime |
| Spring Security | (Boot 4.x) | Autenticazione + RBAC con JWT |
| Spring Data JPA | (Boot 4.x) | ORM + repository layer |
| Spring Validation | (Boot 4.x) | Bean validation (JSR-380) |
| Spring Mail + Thymeleaf | (Boot 4.x) | Invio email HTML |
| H2 Database | runtime | DB in-memory per sviluppo |
| MySQL | runtime | DB per produzione |
| jjwt | 0.13.0 | Generazione e validazione JWT |
| Lombok | 1.18.34 | Riduzione boilerplate |
| SpringDoc OpenAPI | 3.0.3 | Documentazione Swagger UI |

---

## Prerequisiti

- **JDK 21** (consigliato: Eclipse Temurin 21 o GraalVM 21)
- **Maven** 3.9+ (oppure usare il wrapper `mvnw` incluso)
- **MySQL 8+** (opzionale, solo per produzione)
- **Mailtrap** o SMTP configurato (opzionale, per le email)

---

## Setup e avvio

### Modalità sviluppo (H2 in-memory)

```bash
cd hrms-backend

# Avvio con Maven Wrapper (nessuna installazione Maven richiesta)
./mvnw spring-boot:run          # Linux/macOS
mvnw.cmd spring-boot:run        # Windows
```

Il server si avvia su **`http://localhost:8080`**.

La console H2 è accessibile su **`http://localhost:8080/h2-console`**:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(vuota)*

### Modalità produzione (MySQL)

1. Crea il database MySQL:

```sql
CREATE DATABASE `HRMS-DB` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Decommenta la sezione MySQL in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/HRMS-DB
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

3. Imposta la variabile d'ambiente per il JWT secret:

```bash
export JWT_SECRET="your-256-bit-secret-key-at-least-32-chars"
```

4. Build e avvio:

```bash
./mvnw clean package -DskipTests
java -jar target/hrms-0.0.1-SNAPSHOT.jar
```

### Configurazione email (Mailtrap)

Aggiorna `application.yml` con le credenziali SMTP:

```yaml
spring:
  mail:
    host: sandbox.smtp.mailtrap.io
    port: 2525
    username: YOUR_MAILTRAP_USERNAME
    password: YOUR_MAILTRAP_PASSWORD
```

---

## Configurazione JWT

| Proprietà | Default | Descrizione |
|---|---|---|
| `app.jwt.secret` | base64 di fallback | Chiave HMAC per firmare i token. **Cambiare in produzione.** |
| `app.jwt.expiration-ms` | `86400000` | Scadenza token (24 ore) |

Il payload JWT generato contiene:

```json
{
  "sub": "utente@azienda.it",
  "roles": ["ROLE_HR"],
  "iat": 1720000000,
  "exp": 1720086400
}
```

---

## Architettura del backend

```
┌─────────────────────────────────────────────┐
│               Controller Layer              │
│  AuthController  EmployeeController         │
│  ContractController  ProjectController      │
│  ProjectAssignmentController                │
│  TimeEntryController  LeaveRequestController│
│  LeaveBalanceController  LeaveAccrualCtrl   │
└───────────────────┬─────────────────────────┘
                    │  (DTO in / DTO out)
┌───────────────────▼─────────────────────────┐
│               Service Layer                 │
│  IEmployeeService    IContractService       │
│  IProjectService     IProjectAssignService  │
│  ITimeEntryService   ILeaveRequestService   │
│  ILeaveBalanceService  ILeaveAccrualService │
│  IEmailService       IJwtService            │
└───────────────────┬─────────────────────────┘
                    │  (Entity in/out)
┌───────────────────▼─────────────────────────┐
│             Repository Layer                │
│  JPA Repositories per ogni entità          │
└───────────────────┬─────────────────────────┘
                    │
┌───────────────────▼─────────────────────────┐
│          Database (H2 / MySQL)              │
└─────────────────────────────────────────────┘
```

### Flusso sicurezza

```
Request HTTP
    │
    ▼
JwtAuthenticationFilter
    │ estrae token dall'header Authorization: Bearer <token>
    │ valida firma e scadenza (JwtService)
    │ carica UserDetails (email → Employee)
    │ imposta SecurityContext
    ▼
Spring Security (autorizzazione per ruolo)
    │
    ▼
Controller → @PreAuthorize / SecurityConfig rules
```

---

## Entità e relazioni

```
Employee ─────┬──── Role (Many-to-Many)
              │
              ├──── Contract (One-to-Many)
              │       └── [active] flag: un solo contratto attivo
              │
              ├──── ProjectAssignment (One-to-Many)
              │       └── Project (Many-to-One)
              │
              ├──── TimeEntry (One-to-Many)
              │       └── Project (Many-to-One)
              │
              ├──── LeaveRequest (One-to-Many)
              │
              ├──── LeaveBalance (One-to-One)
              │
              └──── LeaveAccrualLog (One-to-Many)
```

### Descrizione delle entità

| Entità | Descrizione |
|---|---|
| `Employee` | Anagrafica dipendente con email (username), password cifrata, info personali e ruoli |
| `Role` | Ruolo Spring Security: `ROLE_ADMIN`, `ROLE_HR`, `ROLE_EMPLOYEE` |
| `Contract` | Contratto lavorativo con tipo, RAL, dipartimento, posizione, giorni ferie mensili. Solo uno può essere `active = true` |
| `Project` | Progetto con nome, cliente, budget, date, stato (`PLANNED`, `IN_PROGRESS`, `ON_HOLD`, `COMPLETED`, `CANCELLED`) |
| `ProjectAssignment` | Associazione Employee ↔ Project con ruolo (`PROJECT_MANAGER`, `TECH_LEAD`, ecc.) e date |
| `TimeEntry` | Registrazione ore lavorate da un dipendente su un progetto in una data |
| `LeaveRequest` | Richiesta di ferie/permesso con tipo, date, stato (`PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`) |
| `LeaveBalance` | Saldo ferie accumulato, utilizzato, in attesa e disponibile (in giorni e ore) |
| `LeaveAccrualLog` | Log mensile degli accrediti ferie basati sul contratto attivo |

---

## Endpoint API

### Auth

| Metodo | Path | Ruolo | Descrizione |
|---|---|---|---|
| POST | `/api/v1/auth/login` | pubblico | Login → restituisce JWT |

### Employee

| Metodo | Path | Ruolo | Descrizione |
|---|---|---|---|
| GET | `/api/v1/employee` | ADMIN, HR | Lista paginata dipendenti |
| GET | `/api/v1/employee/{id}` | ADMIN, HR, EMPLOYEE* | Dettaglio dipendente |
| POST | `/api/v1/employee` | ADMIN, HR | Crea dipendente (invia email attivazione) |
| PATCH | `/api/v1/employee/update/{id}` | ADMIN, HR | Aggiorna dati dipendente |
| DELETE | `/api/v1/employee/delete/{id}` | ADMIN | Elimina dipendente |
| GET | `/view/v1/employee/activate/{token}` | pubblico | Attiva account via link email (server-rendered) |

*EMPLOYEE può accedere solo al proprio profilo (`@employeeSecurity.isEmployeeIdMatching()`)

### Contract

| Metodo | Path | Ruolo | Descrizione |
|---|---|---|---|
| POST | `/api/v1/contract` | ADMIN, HR | Crea contratto per dipendente |
| GET | `/api/v1/contract/employee/{employeeId}` | ADMIN, HR, EMPLOYEE* | Storico contratti |
| GET | `/api/v1/contract/employee/{employeeId}/active` | ADMIN, HR, EMPLOYEE* | Contratto attivo |
| PUT | `/api/v1/contract/update/{id}` | ADMIN, HR | Aggiorna contratto |
| PUT | `/api/v1/contract/activate/{id}` | ADMIN, HR | Attiva contratto (disattiva gli altri) |

### Project

| Metodo | Path | Ruolo | Descrizione |
|---|---|---|---|
| GET | `/api/v1/project` | tutti | Lista progetti (filtrabile per `status`) |
| GET | `/api/v1/project/{id}` | tutti | Dettaglio progetto |
| POST | `/api/v1/project` | ADMIN, HR | Crea progetto |
| PATCH | `/api/v1/project/update/{id}` | ADMIN, HR | Aggiorna progetto |
| DELETE | `/api/v1/project/delete/{id}` | ADMIN | Elimina progetto |

### Project Assignment

| Metodo | Path | Ruolo | Descrizione |
|---|---|---|---|
| POST | `/api/v1/project-assignment/assign` | ADMIN, HR | Assegna dipendente al progetto |
| PUT | `/api/v1/project-assignment/close/{id}` | ADMIN, HR | Chiude assegnazione |
| GET | `/api/v1/project-assignment/project/{projectId}` | tutti | Dipendenti assegnati al progetto |
| GET | `/api/v1/project-assignment/employee/{employeeId}` | tutti | Progetti di un dipendente |

### Time Entry

| Metodo | Path | Ruolo | Descrizione |
|---|---|---|---|
| POST | `/api/v1/time-entry` | tutti | Registra ore lavorate |
| GET | `/api/v1/time-entry/my_entries?month=YYYY-MM` | tutti | Ore personali per mese |
| GET | `/api/v1/time-entry/project/{projectId}/report` | ADMIN, HR | Report totale ore per progetto |
| DELETE | `/api/v1/time-entry/{id}` | ADMIN, EMPLOYEE* | Elimina registrazione |

### Leave Request

| Metodo | Path | Ruolo | Descrizione |
|---|---|---|---|
| POST | `/api/v1/leave-request/submit` | tutti | Invia richiesta di ferie |
| GET | `/api/v1/leave-request/my-requests` | tutti | Proprie richieste |
| GET | `/api/v1/leave-request/pending` | ADMIN, HR | Richieste in attesa |
| GET | `/api/v1/leave-request/employee/{employeeId}` | ADMIN, HR | Storico richieste dipendente |
| PUT | `/api/v1/leave-request/review/{id}` | ADMIN, HR | Approva o rifiuta |
| PUT | `/api/v1/leave-request/cancel/{id}` | EMPLOYEE | Annulla propria richiesta |

### Leave Balance

| Metodo | Path | Ruolo | Descrizione |
|---|---|---|---|
| GET | `/api/v1/leave-balance/employee/{employeeId}` | ADMIN, HR, EMPLOYEE* | Saldo ferie completo |
| GET | `/api/v1/leave-balance/employee/{employeeId}/summary` | ADMIN, HR | Sommario + previsione mese successivo |

### Leave Accrual

| Metodo | Path | Ruolo | Descrizione |
|---|---|---|---|
| POST | `/api/v1/leave-accrual/process-now` | ADMIN | Esegue accredito ferie manualmente |
| GET | `/api/v1/leave-accrual/employee/{employeeId}/log` | ADMIN | Log accrediti mensili |

---

## Task pianificati (Scheduler)

| Task | Cron | Descrizione |
|---|---|---|
| Accredito ferie mensile | `0 0 6 1 * *` (1° del mese alle 06:00) | Calcola e accredita ferie per ogni dipendente con contratto attivo |
| Alert scadenza contratti | `0 0 8 * * *` (ogni giorno alle 08:00) | Invia email HR per contratti in scadenza entro 30 giorni |

---

## Email automatiche

Il backend invia email HTML renderizzate con Thymeleaf in questi casi:

| Template | Trigger |
|---|---|
| `welcome-email.html` | Creazione nuovo dipendente |
| `activation-email.html` | Invio link di attivazione account |
| `contract-expiry-alert.html` | Contratto in scadenza (notifica HR) |
| `leave-request-submitted.html` | Ricezione richiesta ferie |

---

## Documentazione API (Swagger)

Con il backend in esecuzione, la documentazione interattiva è disponibile su:

```
http://localhost:8080/swagger-ui.html
```

Il file OpenAPI JSON è disponibile su:

```
http://localhost:8080/v3/api-docs
```

---

## Test

```bash
# Esegui tutti i test
./mvnw test

# Test con report di copertura
./mvnw verify
```

I test usano un profilo H2 dedicato (`application-test.yml`) per isolare il database di test da quello di sviluppo.

---

## Enumerazioni

| Enum | Valori |
|---|---|
| `RoleName` | `ROLE_ADMIN`, `ROLE_HR`, `ROLE_EMPLOYEE` |
| `ContractType` | `FULL_TIME`, `PART_TIME`, `FIXED_TIME`, `INTERNSHIP`, `FREELANCE` |
| `ProjectStatus` | `PLANNED`, `IN_PROGRESS`, `ON_HOLD`, `COMPLETED`, `CANCELLED` |
| `AssignmentRole` | `PROJECT_MANAGER`, `TECH_LEAD`, `DEVELOPER`, `QA_ENGINEER`, `DESIGNER`, `CONSULTANT` |
| `LeaveType` | `SICK_LEAVE`, `CASUAL_LEAVE`, `EARNED_LEAVE`, `MATERNITY_LEAVE`, `PATERNITY_LEAVE`, `BEREAVEMENT_LEAVE`, `UNPAID_LEAVE` |
| `LeaveRequestsStatus` | `PENDING`, `APPROVED`, `REJECTED`, `CANCELLED` |
