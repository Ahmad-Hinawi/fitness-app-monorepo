\# Fitness Microservices App



A full-stack fitness tracking system built with a Microservices architecture.



\## 🏗 Architecture

\- \*\*Frontend:\*\* React + Material UI (MUI)

\- \*\*Backend:\*\* Java Spring Boot Microservices

\- \*\*Infrastructure:\*\* Spring Cloud Gateway, Eureka Discovery, Config Server

### 🛡️ Security & Messaging
* **Keycloak:** Centralized OAuth2/OpenID Connect provider for Authentication.
* **RabbitMQ:** Message broker for event-driven communication between services.

\## 🚀 How to Run

1\. \*\*Start Infrastructure:\*\*

&#x20;  - Run `configserver` first.

&#x20;  - Run `eureka` (Discovery Server).

2\. \*\*Start Services:\*\*

&#x20;  - Run `gateway-service`.

&#x20;  - Run `usersrvice`, `activityservice`, and `aiservice`.

3\. \*\*Start Frontend:\*\*



&#x20;  - Navigate to `frontend/my-mui-app`.

&#x20;  - Run `npm install` and then `npm run dev`.

### 🏗 System Architecture

```mermaid
graph TD
    %% Entities
    User((User / Client))
    
    subgraph Frontend
        ReactApp[React SPA - Material UI]
    end

    subgraph "API Management & Security"
        Gateway[Spring Cloud Gateway]
        Keycloak[Keycloak - IAM / OAuth2]
    end

    subgraph "Service Discovery & Config"
        Eureka[Eureka Discovery Server]
        Config[Config Server]
    end

    subgraph "Microservices Cluster"
        UserService[User Service]
        ActivityService[Activity Service]
        AIService[AI Service - Python Integration]
    end

    subgraph "Message Broker & Storage"
        RabbitMQ{RabbitMQ Broker}
        Database[(PostgreSQL / MongoDB)]
    end

    %% Connections
    User --> ReactApp
    ReactApp --> Gateway
    
    Gateway -.->|Authorize| Keycloak
    Gateway -.->|Discover| Eureka
    Gateway --> UserService
    Gateway --> ActivityService
    Gateway --> AIService

    %% Event Driven Communication
    UserService -.->|Event| RabbitMQ
    ActivityService -.->|Event| RabbitMQ
    AIService -.->|Event| RabbitMQ
    
    %% Database Links
    UserService --- Database
    ActivityService --- Database
    AIService --- Database

