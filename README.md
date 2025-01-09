# Beauty Salon Backend System

This is a backend system for a beauty salon, designed to handle **client authentication** and **reservation management**. The system uses **JWT authentication**, blacklisting for refresh tokens via **Redis**, and manages user data in a **PostgreSQL** database. **JPA (Java Persistence API)** is used for managing database operations, including storing and querying client and reservation information. The system also integrates **Spring Validation** for input data validation.

The backend is built using **Kotlin**, **Spring Boot**, **Spring Security**, **Spring Data JPA**, and **Spring Validation**. The system supports basic **client authentication**, with token-based login and secure role-based access control.

---

## Features:

- **Authentication**:
    - Client registration and login using JWT (access token and refresh token).
    - Role-based access control.
    - **Access Token**: Used for short-lived user authentication.
    - **Refresh Token**: Issued for token renewal, with blacklisting handled by Redis.

- **Reservations**:
    - Clients can book, view, and manage reservations for beauty services.

- **Token Blacklisting**:
    - Refresh tokens are stored and blacklisted in **Redis** for security purposes.

- **JPA (Java Persistence API)**:
    - **JPA** is used for managing persistent data, such as client and reservation information, in the **PostgreSQL** database.

- **Input Validation**:
    - **Spring Validation** is used for validating client inputs, such as registration data (email, phone number, password) and reservation details (service type, date, and time).

- **CI: GH actions**:
    -  For Continuous Integration (CI), automating the process of testing and building the project.

---

## Technologies Used:

- **Kotlin**: Programming language used for the backend logic.
- **Spring Boot**: Framework for building the backend service.
- **Spring Security**: For handling authentication and role-based authorization.
- **JWT (JSON Web Tokens)**: For token-based authentication.
- **PostgreSQL**: Used as the main database to store client data and reservations.
- **Redis**: For storing and blacklisting refresh tokens.
- **JPA (Java Persistence API)**: For interacting with the database and performing CRUD operations on clients and reservations.
- **Spring Validation**: For validating input data such as registration details, email, phone number, and reservation data.
- **JUnit 5**: For unit and integration testing.
- **MockK**: For mocking dependencies in tests.
