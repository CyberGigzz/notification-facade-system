# Customer Notification Address Facade System

This project is a microservice designed to centralize and manage customer contact information and notification preferences. It acts as a single source of truth for all recipient addresses and delivery statuses, allowing other systems in an ecosystem to fetch and update customer delivery data efficiently.

## Key Features

- **Admin Portal:** A secure, web-based UI for administrators to manage all system data.
- **Customer Management:** Full CRUD (Create, Read, Update, Delete) functionality for customer records.
- **Address & Preference Management:** Manage multiple contact addresses (Email, SMS) and notification preferences for each customer.
- **Notification Tracking:** An API endpoint to receive and store the status of sent notifications, with history visible in the admin UI.
- **Reporting Dashboard:** A high-level dashboard displaying key statistics about customers and notification delivery.
- **Dynamic Data Tables:** The main customer list supports dynamic search, filtering, and sorting with pagination.
- **RESTful API:** Provides API endpoints for other systems to interact with customer data.

---

## Technology Stack

- **Backend:** Java 21, Spring Boot 3.5.0
- **Data Persistence:** Spring Data JPA, Hibernate
- **Database:** PostgreSQL (managed via Docker)
- **Security:** Spring Security
- **Frontend (Server-Side):** Thymeleaf, HTML5, Bootstrap 5
- **Build Tool:** Apache Maven

---

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You will need the following software installed on your system:

- **Java Development Kit (JDK) 21** or newer.
- **Apache Maven** 3.6 or newer(Recommended).
- **Docker** and **Docker Compose** (for running the PostgreSQL database).
- A **Git** client.

### 1. Clone the Repository

Clone this repository to your local machine:

```bash
git clone git@github.com:CyberGigzz/notification-facade-system.git
cd notification-facade-system
```
    
### 2. Configure Environment Variables
This project uses a .env file to manage database credentials. This file is not checked into version control, so you must create it yourself.

In the root directory of the project, create a file named .env with the following content:

```bash
# PostgreSQL Credentials
POSTGRES_USER=admin
POSTGRES_PASSWORD=password
POSTGRES_DB=notifications

# pgAdmin Credentials (for the optional database GUI)
PGADMIN_DEFAULT_EMAIL=admin@example.com
PGADMIN_DEFAULT_PASSWORD=admin
```

### 3. Start the Database
This application requires a running PostgreSQL database. You have two options:

### Option 1: Using Docker (Recommended)

This is the easiest method. From the root directory, run the following command to start the self-contained database service:

```bash
docker-compose up -d
``` 

The database will be available on localhost:5432.

(Optional) You can use the pgAdmin database management tool by navigating to http://localhost:5050 in your web browser.

### Option 2: Using a Local or External PostgreSQL Instance

If you prefer not to use Docker, ensure you have a PostgreSQL instance running and configured to match the application's default settings.

The application expects the following by default:

    Database Name: notifications

    Username: admin

    Password: password

If your local setup uses different credentials, you must update them in the `src/main/resources/application.properties` file:

```properties
spring.datasource.url=jdbc:postgresql://<your-host>:<your-port>/notifications
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
```

## 4. Run the Application

You can run the Spring Boot application using Maven or directly from your IDE.

Using Maven:

In the root directory of the project, run:

```bash
mvn spring-boot:run
``` 

Using an IDE:

Import the project as a Maven project into your IDE (e.g., IntelliJ IDEA, Eclipse).
Locate the main application class com.notificationsystem.Application.java.
Run the main method.

The application will start on http://localhost:8080.


## 5. Access the Application

Admin Portal: Navigate to http://localhost:8080/admin/dashboard in your web browser.
You will be redirected to the login page.
Default Admin Credentials:

Username: admin

Password: password

Project Structure and Design

### Project Structure
This application follows a standard layered architecture to ensure a clean separation of concerns:

* `controller`: Contains Spring MVC controllers (`@Controller` for the UI, `@RestController` for the API) that handle incoming HTTP requests.
* `service`: Contains the business logic. It orchestrates operations between the controllers and repositories.
* `repository`: Contains Spring Data JPA repository interfaces for database access. This layer also includes a custom repository implementation (`CustomerRepositoryImpl`) for dynamic query building.
* `domain`: The core of the application, containing the JPA entity classes (e.g., `Customer`, `Address`) that map to database tables.
* `dto`: Data Transfer Objects used to transfer data between the layers, especially between the controllers and services.
* `config`: Contains Spring configuration classes, such as `SecurityConfig`.
* `exception`: Contains custom exception classes and global exception handlers.
* `validation`: Contains custom validation logic, such as the `@ValidAddress` constraint.
* `resources/templates`: Contains all the Thymeleaf HTML templates for the UI.
* `resources/static`: Contains all static assets like CSS and JavaScript files.


## API Endpoints

A summary of the key API endpoints available for other systems:

* `GET /api/v1/customers`: Retrieves a paginated list of all customers.
* `GET /api/v1/customers/{id}`: Retrieves a single customer by their ID.
* `POST /api/v1/notifications/track`: Allows an external system to log the status of a sent notification.