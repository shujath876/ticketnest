TicketNest

A microservices-based ticket booking platform built with Java and Spring Boot. TicketNest focuses on how distributed services find each other, talk to each other, and stay consistent when something fails.

## Features

- Browse events and book tickets
- Independent services, each with its own responsibility and database
- Service discovery with Eureka (no hardcoded service URLs)
- Single entry point through an API Gateway
- Distributed transactions across services using the Saga pattern
- Fault tolerance with Resilience4j circuit breakers and fallbacks

## Architecture


            ┌──────────────┐
  Client ──▶│  API Gateway │
            └──────┬───────┘
                   │ routes requests
     ┌─────────────┼─────────────┐
     ▼             ▼             ▼
┌─────────┐   ┌──────────┐   ┌─────────┐
│ Service │   │ Service  │   │ Service │   ... (add your services)
└────┬────┘   └────┬─────┘   └────┬────┘
     │   OpenFeign + Resilience4j  │
     └─────────────┬───────────────┘
                   ▼
          ┌─────────────────┐
          │ Eureka Server   │  (service discovery)
          └─────────────────┘



## Tech Stack

| Area | Technology |
|------|------------|
| Language | Java |
| Framework | Spring Boot |
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Inter-service calls | OpenFeign |
| Fault tolerance | Resilience4j (circuit breaker, fallback factory) |
| Distributed transactions | Saga pattern |
| Build tool | Maven |

## Key Concepts Implemented

**Service Discovery** – Every service registers with Eureka. Services call each other by name, not by IP or port.

**API Gateway** – All client requests go through one gateway, which routes them to the right service.

**Saga Pattern** – A booking touches more than one service. Instead of one big database transaction, each step has a compensating action. If a step fails, earlier steps are rolled back.

**Circuit Breaker with Fallback Factory** – Feign clients are protected with Resilience4j. A `fallbackFactory` is used instead of a plain `fallbackMethod` so the fallback has access to the actual exception that caused the failure. This allows different responses for different errors and better logging.

## Getting Started

### Prerequisites

- Java 17+
- Maven
- A running database (update credentials in each service's `application.properties`)

### Run locally

1. Clone the repo
  
   git clone https://github.com/shujath876/ticketnest.git

2. Start the **Eureka Server** first.
3. Start the individual services.
4. Start the **API Gateway** last.
5. Open the Eureka dashboard at `http://localhost:8761` to check that all services are registered.

## Roadmap

- [ ] Kafka for event-driven communication between services
- [ ] Redis for caching and seat locking
- [ ] Docker Compose setup
- [ ] Centralized configuration
- [ ] Distributed tracing

Author

**Syed Shujath Hussain**
GitHub: [@shujath876](https://github.com/shujath876)
