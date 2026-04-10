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

