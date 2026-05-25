# Fintech Payment Processing Microservices

Scalable fintech backend system built with Java, Spring Boot 4, microservices architecture, Redis caching, idempotent payment processing, and double-entry ledger accounting.

Designed as a production-oriented backend engineering portfolio project focused on:

- Distributed systems
- Financial transaction consistency
- Payment orchestration
- Event-driven architecture preparation
- Cloud-native deployment patterns
- AWS infrastructure readiness
- Observability and scalability engineering

---

# Architecture Overview

This project is split into independent microservices with separate responsibilities and private databases.

## Current Services

### Accounts Service
Responsible for:

- Account management
- Balance calculation
- Ledger accounting
- Deposits
- Transfers
- Idempotency validation
- Redis balance caching

### Payment Service
Responsible for:

- Payment orchestration
- Transaction lifecycle
- Idempotency handling
- External payment API
- Synchronous communication with Accounts Service

---

# Microservice Design

## Accounts Service

Owns:

- `accounts`
- `ledger_entries`

Responsibilities:

- Money ownership
- Double-entry accounting
- Balance calculation
- Funds validation
- Transfer consistency

---

## Payment Service

Owns:

- `transactions`

Responsibilities:

- Payment orchestration
- Transaction state machine
- Retry safety
- Idempotent processing
- External request tracking

---

# Current Architecture Flow

```text
Client
   │
   ▼
Payment Service
   │
   │ HTTP/WebClient
   ▼
Accounts Service
   │
   ├── Accounts Table
   ├── Ledger Entries
   └── Redis Cache