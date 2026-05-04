# Hotel Management Microservices

### Overview
This microservice-based project contains 3 microservices User, Hotel, and Rating that communicate with each other to give the results.

### Architecture Diagram
![Microservice drawio](https://github.com/user-attachments/assets/a90051d5-4a7a-44c9-9b84-2e7e07961f16)

A Java Spring Boot 3.x microservices-based hotel management system with service discovery, API gateway, and centralized configuration.

## Architecture

```
                    +------------------+
                    |   API Gateway    |
                    |   Port: 8083     |
                    +--------+---------+
                             |
              +--------------+--------------+
              |              |              |
    +---------v----+  +------v------+ +-----v--------+
    | UserService  |  |HotelService | |RatingServices|
    | Port: 8080   |  | Port: 8081  | | Port: 8082   |
    +------+-------+  +------+------+ +------+-------+
           |                 |               |
    +------v------+   +------v------+ +------v------+
    |   MySQL     |   | PostgreSQL  | |  MongoDB    |
    +-------------+   +-------------+ +-------------+

    All services register with:
    +------------------+
    | ServiceRegistry  |
    | Eureka: 8761     |
    +------------------+
```

## Tech Stack

| Component | Technology |
|-----------|------------|
| Framework | Spring Boot 3.3.1 |
| Language | Java 17 |
| Cloud | Spring Cloud 2023.0.2 |
| Build System | Maven 3.9.7 (with Maven Wrapper) |
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway (WebFlux) |
| Config Management | Spring Cloud Config Server |
| Inter-service Communication | RestTemplate + OpenFeign |
| Resilience | Resilience4j (Circuit Breaker, Rate Limiter) |
| ORM | Spring Data JPA (Hibernate) |
| Databases | MySQL, PostgreSQL, MongoDB |
| Code Generation | Lombok |
| Monitoring | Spring Boot Actuator |

## Prerequisites

- Java 17 or higher
- Maven 3.9+ (or use included Maven Wrapper)
- Running database instances:
  - **MySQL** (Port 3306) — create database `microservices`
  - **PostgreSQL** (Port 5432) — create database `microservice`
  - **MongoDB** (Port 27017) — database `microservices` created automatically

## Database Credentials

| Database | Service | URL | Username | Password |
|----------|---------|-----|----------|----------|
| MySQL | UserService | `jdbc:mysql://localhost:3306/microservices` | root | `<your-mysql-password>` |
| PostgreSQL | HotelService | `jdbc:postgresql://localhost:5432/microservice` | postgres | `<your-postgres-password>` |
| MongoDB | RatingServices | `mongodb://localhost:27017/microservices` | — | — |

## Services

| Service | Port | Description |
|---------|------|-------------|
| ServiceRegistry | 8761 | Netflix Eureka Service Discovery Server |
| ConfigServer | 8084 | Spring Cloud Config Server (native + Git) |
| ApiGateway | 8083 | Spring Cloud Gateway — routes requests to microservices |
| UserService | 8080 | User management with CRUD, circuit breaker, rate limiting |
| HotelService | 8081 | Hotel data management with CRUD |
| RatingServices | 8082 | Rating and feedback management (MongoDB) |

## API Gateway Routes

| Route | Destination |
|-------|-------------|
| `/users/**` | `lb://USERSERVICE` |
| `/hotels/**` | `lb://HOTELSERVICE` |
| `/ratings/**` | `lb://RATINGSERVICES` |

## Quick Start

### 1. Build All Services

```bash
cd ServiceRegistry && mvnw.cmd clean install && cd ..
cd ConfigServer && mvnw.cmd clean install && cd ..
cd ApiGateway && mvnw.cmd clean install && cd ..
cd UserService && mvnw.cmd clean install && cd ..
cd HotelService && mvnw.cmd clean install && cd ..
cd RatingServices && mvnw.cmd clean install && cd ..
```

### 2. Start Services (in order)

```bash
# Terminal 1 - Service Registry (start first)
cd ServiceRegistry && mvnw.cmd spring-boot:run

# Terminal 2 - Config Server
cd ConfigServer && mvnw.cmd spring-boot:run

# Terminal 3 - API Gateway
cd ApiGateway && mvnw.cmd spring-boot:run

# Terminal 4 - User Service
cd UserService && mvnw.cmd spring-boot:run

# Terminal 5 - Hotel Service
cd HotelService && mvnw.cmd spring-boot:run

# Terminal 6 - Rating Service
cd RatingServices && mvnw.cmd spring-boot:run
```

### 3. Verify

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8083
- **UserService**: http://localhost:8080
- **HotelService**: http://localhost:8081
- **RatingServices**: http://localhost:8082

## Sample API Requests

### Create User

```bash
curl -X POST http://localhost:8083/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com", "about": "Developer"}'
```

### Create Hotel

```bash
curl -X POST http://localhost:8083/hotels \
  -H "Content-Type: application/json" \
  -d '{"name": "Grand Hotel", "location": "New York", "about": "Luxury hotel"}'
```

### Create Rating

```bash
curl -X POST http://localhost:8083/ratings \
  -H "Content-Type: application/json" \
  -d '{"userId": "user-id", "hotelId": "hotel-id", "rating": 5, "feedback": "Excellent!"}'
```

### Get User with Ratings & Hotel Details

```bash
curl http://localhost:8083/users/{userId}
```

## Configuration

The ConfigServer pulls configuration from:
- **Native profile**: Local `src/main/resources/config` directory
- **Git repository**: `https://github.com/Pritam0077/Microservice-Project-Config`

## Resilience Patterns

UserService implements:
- **Circuit Breaker**: `ratingHotelBreaker` — protects calls to external services
- **Rate Limiter**: `userRateLimiter` — controls request throughput

## Notes

- Ensure all databases are running before starting services
- Eureka Server must be started first so other services can register
- Update database credentials in each service's `application.yml` if different from defaults
- ConfigServer's Git config repo is optional; falls back to native profile
