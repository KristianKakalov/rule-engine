# rule-engine
A Spring Boot application that processes Kafka messages, applies dynamic conditions and transformations, persists data, and publishes a
processed result to an output topic.

## Quick Start
### 1. Clone this Repository
### 2. Run with Docker Compose
```bash
docker compose build
docker compose up
```
#### Access Services
```
Application: http://localhost:8081

Kafka UI: http://localhost:8090

pgAdmin: http://localhost:5050 (admin@admin.com / admin)
```
#### Configuration
Rule files are located in the ```rules/``` directory:

- ```rules/conditions.json``` - Define message filtering conditions

- ```rules/transformations.json``` - Define message transformations

Edit these files to customize business rules.

#### Check Logs
```bash
docker compose logs -f rule-engine
```
#### Stop Services
```bash
docker compose down
```