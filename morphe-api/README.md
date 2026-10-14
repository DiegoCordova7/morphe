# Morphe API

**The official web connector, compilation backend, and execution server for the Morphe programming language.**

`Morphe API` acts as the secure, asynchronous bridge between the core Morphe compiler/VM and web-based consumers—most notably the **Morphe Docs Interactive Playground**. It exposes a tightly controlled REST interface designed to compile source code, manage stateful execution sessions, and stream real-time input/output safely.

## Architecture & Role in the Ecosystem

In the Morphe ecosystem, this service is responsible for decoupling heavy interpreter execution from client-side interfaces (such as the Morphe Docs web documentation site).

```text
┌─────────────────────────┐          HTTP / REST          ┌──────────────────────────┐
│  Morphe Docs Playground │ ────────────────────────────> │        Morphe API        │
│  (Frontend Consumer)    │ <──────────────────────────── │  (Backend Execution VM)  │
└─────────────────────────┘      JSON DTO Responses       └──────────────────────────┘
```

## Package Structure (`dev.morphe.api`)

```text
dev.morphe.api
│   MorpheApiApplication.java
├───config        # Infrastructure beans (CORS policies, execution thread pools)
├───controller    # REST endpoints exposing the execution engine to clients
├───dto           # Payloads and response models (RunRequest, InputRequest, ExecutionResultDto)
├───exception     # Global advice and runtime limit guards (MorpheExecutionLimitException)
├───filter        # Security, request filtering, and rate limiting
├───io            # Thread-safe buffer handlers for web-based streams (WebIOHandler)
└───service       # Core coordination, state retention, and SessionManager logic
```

## Safety & Concurrency Design

Exposing a programming language interpreter over the web requires strict safeguards against abuse and resource exhaustion:

- **Isolated Execution Pools (`MorpheExecutionConfig`):** User code runs in a dedicated daemon thread pool, preventing infinite loops or hanging execution threads from starving Tomcat or blocking HTTP requests.

- **Interactive Session Management (`SessionManager`):** Supports multi-step runtime flows (e.g., programs that prompt the user for console input dynamically via a `sessionId`).

- **Incremental I/O Flushing (`WebIOHandler`):** Captures and flushes console prints incrementally, enforcing strict line count limits to prevent memory overflow from runaway printing loops.

- **Execution Timeouts:** Automatically cancels tasks exceeding configured time boundaries.

## Configuration (`application.properties`)
The application is configured using standard Spring Boot properties located in `src/main/resources/application.properties`:

```properties
spring.application.name=morphe-api
server.port=8080

# Morphe Execution Safeguards & Limits
morphe.execution.timeout-seconds=5
morphe.execution.max-output-lines=500
morphe.execution.pool-size=4

# CORS Policy (Restrict to your Morphe Docs playground domain in production)
morphe.cors.allowed-origins=*
```

## API Contract for Consumers

Designed with simplicity in mind for frontend consumers like **Morphe Docs:**

- `GET /api/health` — Liveness probe for monitoring service availability.

- `POST /api/morphe/run` — Submits raw source code for compilation and initial execution. Returns output logs, execution state (HALTED or WAITING_FOR_INPUT), and an optional sessionId for interactive programs.

- `POST /api/morphe/run/{sessionId}/input` — Resumes a paused session by feeding user console input back into the virtual machine.

## Getting Started

### Prerequisites
- Java 17+
- Maven

### Running the Backend
```Bash
git clone https://github.com/DiegoCordova7/morphe-api.git
cd morphe-api
./mvnw spring-boot:run
```

The API will be available at http://localhost:8080.

## License
Distributed under the MIT License. See `LICENSE` for more information.