# Diploma Project: Equipment Rental Platform

This is the backend part of a diploma project implementing a platform for renting various equipment. The system is designed using a microservice architecture.

## Architecture

Below is the system architecture diagram:

![System Architecture](https://github.com/IvanKustovsky/diploma-backend/blob/main/System_architecture.jpg)

### System Components:

The system consists of the following microservices:

1.  **API Gateway (gateway-service):**
    * Technology: `Spring Cloud Gateway`
    * Purpose: A single entry point for all client requests. Routes requests to the appropriate microservices. Applies filters for authentication (JWT validation).

2.  **Service Discovery (discovery-service):**
    * Technology: `Spring Cloud Netflix Eureka Server`
    * Purpose: Allows services to register and discover each other on the network, simplifying communication between them.

3.  **Configuration Server (config-service):**
    * Technology: `Spring Cloud Config Server`
    * Purpose: Centralized configuration management for all microservices. Reads configuration data from a GitHub repository.

4.  **Authentication Service (auth-service):**
    * Technology: Wrapper around Keycloak API.
    * Purpose: Responsible for registering users in Keycloak, generating `access_token` and refreshing them using `refresh_token`. Integrates with Keycloak as an Identity and Access Management (IAM) system.

5.  **User Service (user-service):**
    * Technology: `Spring Boot`, `Eureka Client`
    * Purpose: Storing and managing user information.

6.  **Equipment Service (equipment-service):**
    * Technology: `Spring Boot`, `Eureka Client`
    * Purpose: Allows users to create equipment listings, update them, and view listings from other users.

7.  **Rent Service (rent-service):**
    * Technology: `Spring Boot`, `Eureka Client`
    * Purpose: Handles the logic related to equipment rental (details of this service are still under development).

### Service Interaction:

* **External Requests:** Pass through the `API Gateway`.
* **Authentication:** The `API Gateway` validates JWT tokens. The `auth-service` interacts with `Keycloak` to manage tokens and credentials.
* **Internal Communication:** Microservices communicate with each other via the `Eureka Discovery Service`.
* **Configuration:** All services retrieve their configurations from the `Config Service`, which in turn reads them from GitHub.
* **Data Storage:**
    * Currently, for local development and to conserve resources on a personal computer, all microservices (Users, Equipment, and Rent) share a single PostgreSQL database instance running in one Docker container. Keycloak also uses its own database for storing user and role information (which might be a separate instance or also part of this shared development setup).
    * **Important Note for Production:** In a real-world, production-grade application, it is highly recommended that each microservice has its own independent database. This approach, known as "database-per-service," enhances scalability, resilience (an issue in one service's database doesn't directly impact others), and allows each service to choose the database technology best suited for its specific needs.
* **Containerization:** All microservices and auxiliary services (Eureka, Config Server, PostgreSQL) are deployed using Docker. Docker images can be pulled from a public Docker Registry. The current single PostgreSQL container setup is part of this containerized environment for development.

## Technologies

* **Programming Language:** Java
* **Framework:** Spring Boot
* **Microservice Architecture:**
    * Spring Cloud Gateway (API Gateway)
    * Spring Cloud Netflix Eureka (Service Discovery)
    * Spring Cloud Config Server (Configuration Management)
* **Authentication & Authorization:** Keycloak, JWT (JSON Web Tokens)
* **Databases:** PostgreSQL
* **Containerization:** Docker

## Author

* **Ivan Kustovsky** - [IvanKustovsky](https://github.com/IvanKustovsky)
