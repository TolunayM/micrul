# micrul

`micrul` is a compact, production-oriented distributed system for URL creation, persistence, and redirection.  
Although intentionally small in scope, it includes the architectural primitives that matter in real distributed systems: asynchronous messaging, change data capture, schema governance, idempotent consumption, retry handling, and clear service boundaries.

The project is designed to validate distributed-system patterns in a controlled environment rather than to act as a full-scale platform. Its structure also allows additional services to be introduced with minimal friction as the domain grows.

## Architecture Overview

The system is organized into independently deployable services with clear responsibility boundaries:

- **UrlService** — URL creation, persistence, and event publication
- **RedirectService** — event consumption, redirect processing, and deduplication logic

The architecture is centered around the following principles:

- **Event-driven communication** with Kafka
- **Outbox Pattern** for reliable event publication from the persistence layer
- **Change Data Capture (CDC)** using Debezium and Kafka Connect
- **Schema governance** through Confluent Schema Registry
- **Protobuf-based contracts** for strongly typed payloads
- **Idempotent consumer design** to prevent duplicate side effects
- **Dead Letter Topic (DLT)** support for failed message isolation and retry flows
- **Document-oriented persistence** with MongoDB
- **Caching** with Redis for hot-path optimization

## What This Project Demonstrates

This repository is meant to validate the following engineering concerns:

- how services can communicate asynchronously without tight coupling
- how domain events can be persisted and consumed reliably
- how the Outbox Pattern can bridge transactional writes and event publication
- how CDC pipelines can be integrated into an event-driven workflow
- how schema evolution can be managed in a Kafka-based architecture
- how duplicate message delivery can be handled safely
- how a small system can still be structured in a way that scales in complexity and team size

## Tech Stack

- Java 25
- Spring Boot
- MongoDB
- Kafka
- Protobuf
- Kafka Connect
- Debezium
- Confluent Schema Registry
- Redis
- Docker / Docker Compose
- Snowflake-based distributed ID generation

## Infrastructure Components

The local development environment includes:

- **Kafka** — event backbone
- **Schema Registry** — schema storage and compatibility handling
- **Debezium Connect** — CDC runtime for reading database changes
- **MongoDB** — primary persistence layer
- **Kafka UI** — topic, cluster, and connector inspection
- **Snowflake-based IDs** — globally unique identifier generation for persisted records and outbox entries

## Data Flow

### 1. URL Creation
A URL is created in the application layer and persisted to MongoDB.

### 2. Outbox Write
Alongside the domain write, an outbox record is stored in the database to ensure the event is not lost if publication fails.

### 3. Protobuf Payload
The event payload is modeled with Protobuf to keep the message contract strongly typed and consistent across services.

### 4. CDC / Debezium Flow
Debezium, running through Kafka Connect, observes the outbox collection in MongoDB and captures the changes as downstream events.

This is the core integration point of the project: the database becomes the source of truth, while Debezium turns persisted outbox records into consumable event streams.

### 5. Kafka Publication
Captured events are published to Kafka, where downstream consumers can process them independently.

### 6. Downstream Consumption
`RedirectService` consumes the relevant Kafka events, checks whether the event was already processed, and applies redirect-related processing only once.

### 7. Failure Handling
Failed messages are routed to a Dead Letter Topic so they can be inspected or retried without blocking the main stream.

## Outbox, Protobuf, and Debezium

The project uses the **Outbox Pattern** to avoid dual-write inconsistencies.

Instead of writing application state and publishing Kafka messages as two unrelated operations, the service first persists the business state and the outbox record into MongoDB. The outbox entry contains metadata such as topic, aggregate type, event type, aggregate id, timestamp, and the serialized payload.

The payload itself is represented with **Protobuf**, which gives the system:

- a stable message contract
- compact serialization
- language-agnostic interoperability
- compatibility with schema-managed event processing

Once the outbox document is written, **Debezium** reads the database change through **Kafka Connect** and publishes it into the event pipeline. This makes the persistence layer the system of record while still enabling reliable asynchronous communication.

This combination is useful for experimenting with:

- transactional integrity
- event publication reliability
- schema-driven messaging
- CDC-based integration patterns

## Service Discovery and API Gateway

Service discovery and API gateway layers are intentionally not emphasized in this repository.  
For the current scope, they are not required to validate the main architectural goals.

The focus is on testing:

- Kafka-based event flow
- outbox-driven event publication
- CDC integration with Debezium
- schema management
- Protobuf-based contracts
- idempotent event handling
- retry and DLT behavior
- persistence-to-event integration patterns

In other words, the project is designed to explore the mechanics of distributed systems rather than to demonstrate a full platform edge layer.

## Design Characteristics

### Loose Coupling
Services are separated by responsibility and communicate through explicit contracts or events rather than direct internal dependencies.

### Extensibility
The current structure allows additional services to be introduced without requiring a redesign of the existing pipeline.

### Reliability
Idempotency, outbox-based publication, and DLT recovery reduce the operational risk of duplicate or failed message processing.

### Observability-Friendly Flow
Kafka, Kafka UI, Schema Registry, and Debezium make it possible to inspect the system from persistence to downstream consumption.

## Runtime Requirements

To run the platform locally, the following infrastructure services are required:

- MongoDB
- Kafka
- Schema Registry
- Debezium Connect
- Redis

The environment can be started with Docker Compose:
```
bash docker compose up -d
``` 
## Summary

`micrul` is a deliberately scoped distributed system that focuses on the parts that matter most in real-world architectures: event streaming, outbox-based reliability, CDC, schema management, and service boundaries.

It is small enough to understand and evolve quickly, while still being structured enough to support additional services and more advanced workflows over time.

