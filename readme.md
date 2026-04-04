# Finance Data Processing and Access Control Backend

A robust, secure Spring Boot REST API for managing financial records and generating dashboard analytics.

This project was built to demonstrate clean backend architecture, strict role-based access control (RBAC), efficient data aggregation, and resilient error handling.

---

## 🏗️ Architecture & Engineering Approach

### 1. Backend Design & Separation of Concerns

The application strictly follows a multi-layered architecture to ensure code is maintainable and testable:

- **Controllers:** Handle HTTP requests and route them. Absolutely no business logic lives here.
- **Services:** Contain the core business rules and transaction logic.
- **Repositories:** Interface directly with PostgreSQL via Spring Data JPA.
- **DTOs (Data Transfer Objects):** Database Entities (`User`, `Transaction`) are strictly isolated from the API layer. DTOs are used for all incoming and outgoing payloads to prevent over-posting and accidental data exposure.

### 2. Access Control Logic

Security is implemented using **Basic Authentication** paired with Spring Security's **method-level security (`@PreAuthorize`)**. This provides fine-grained, controller-level enforcement of business rules:

| Role | Permissions |
|------|-------------|
| `VIEWER` | Read-only access to records. Blocked from creating, editing, deleting, or viewing analytics. |
| `ANALYST` | Read-only access to records + access to aggregated dashboard analytics. |
| `ADMIN` | Full management access across all endpoints. |

### 3. Database & Data Modeling

- **Data Integrity:** Rigid types are enforced at the database level using Java Enums (`TransactionType.INCOME/EXPENSE`, `Role.VIEWER/ANALYST/ADMIN`).
- **Efficient Aggregations:** Dashboard metrics (Net Balance, Category Totals) are calculated directly in PostgreSQL using custom `@Query` JPQL methods. This prevents pulling thousands of records into Java memory, ensuring the API remains highly performant as the dataset grows.

### 4. Validation & Reliability

The application is designed to gracefully handle invalid states:

- **Input Validation:** Incoming DTOs are sanitized using Jakarta Validation (`@NotNull`, `@DecimalMin`, etc.).
- **Global Exception Handling:** A `@RestControllerAdvice` class intercepts all errors (validation failures, not found exceptions, security rejections) and maps them to clean, consistent JSON responses rather than throwing raw Java stack traces to the client.

---

## 🚀 Features Implemented

- [x] **User and Role Management:** Create users and assign access tiers.
- [x] **Financial Records CRUD:** Full Create, Read, Update, Delete capabilities.
- [x] **Record Filtering:** Fetch records dynamically filtered by `type`, `category`, or `date`.
- [x] **Dashboard Summary APIs:** Real-time financial aggregation.
- [x] **Role-Based Access Control:** Strict endpoint protection using Spring Security `@PreAuthorize`.
- [x] **Validation & Error Handling:** Predictable, structured error responses.
- [x] **Data Persistence:** Relational database mapping via Spring Data JPA and PostgreSQL.

---

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.x** (Web, Data JPA, Security, Validation)
- **PostgreSQL** (Relational Database)
- **Lombok** (Boilerplate reduction)
- **Maven** (Dependency management)

---

## 📁 Project Structure

```
src/main/java/org/example/zorvyn
├── config/                        # Configuration classes
│   └── SecurityConfig.java        # Configures Basic Auth, CORS/CSRF, and route access rules
│
├── controller/                    # REST API endpoints (routing and HTTP handling only)
│   ├── AuthController.java        # Handles public user registration (/api/auth/register)
│   ├── DashboardController.java   # Exposes aggregated financial metrics (/api/dashboard)
│   ├── TransactionController.java # Handles CRUD requests for financial records (/api/transactions)
│   └── UserController.java        # Admin-only endpoints to manage users (/api/users)
│
├── dto/                           # Data Transfer Objects (isolates entities from the API layer)
│   ├── request/                   # Incoming payloads with @Valid constraints
│   │   ├── RegisterRequestDTO.java    # Validates new user registration inputs
│   │   ├── TransactionRequestDTO.java # Validates incoming financial record data
│   │   └── UserUpdateRequestDTO.java  # Validates role and status changes for users
│   └── response/                  # Outgoing payloads (strips sensitive data like passwords)
│       ├── TransactionResponseDTO.java # Formats transaction data sent back to the client
│       └── UserResponseDTO.java        # Formats user data safely for API consumption
│
├── entity/                        # Database models and Enums mapped via JPA/Hibernate
│   ├── Role.java                  # Enum for access tiers (VIEWER, ANALYST, ADMIN)
│   ├── Transaction.java           # Maps to the 'transactions' table in Postgres
│   ├── TransactionType.java       # Enum for record types (INCOME, EXPENSE)
│   ├── User.java                  # Maps to the 'app_users' table in Postgres
│   └── UserStatus.java            # Enum for account states (ACTIVE, INACTIVE)
│
├── exception/                     # Global error handling and custom exceptions
│   ├── GlobalExceptionHandler.java    # Intercepts exceptions and formats them as clean JSON responses
│   └── ResourceNotFoundException.java # Custom exception triggered when a DB query finds nothing
│
├── repository/                    # Spring Data JPA interfaces for database operations
│   ├── TransactionRepository.java # Handles SQL generation for transaction records
│   └── UserRepository.java        # Handles SQL generation for user accounts
│
├── security/                      # Custom security implementations
│   └── CustomUserDetailsService.java  # Connects Spring Security to our database for auth
│
└── service/                       # Core business logic and transaction management
    ├── AuthService.java           # Handles password hashing and saving new user registrations
    ├── DashboardService.java      # Calculates and aggregates total income, expenses, and balances
    ├── TransactionService.java    # Houses rules for creating, filtering, and updating transactions
    └── UserService.java           # Houses logic for promoting, demoting, or locking user accounts
```

---

## ⚙️ Local Setup Instructions

### 1. Database Setup

Ensure PostgreSQL is installed and running. Create an empty database:

```sql
CREATE DATABASE finance_db;
```

### 2. Configure Application Properties

Update the `application.properties` (or `.yml`) file located in `src/main/resources` with your local PostgreSQL credentials:

```properties
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 3. Run the Application

Run the project via your IDE or terminal:

```bash
./mvnw spring-boot:run
```

> **Note:** Hibernate will automatically generate the schema and tables upon startup (`ddl-auto=update`).

---

## 📖 API Documentation

> All endpoints (except `POST /api/auth/register`) require **Basic Authentication** — pass your registered username and password in the `Authorization` header.

---

### 1. User Authentication

#### Register a New User *(Public)*

```
POST /api/auth/register
```

**Request Body:**

```json
{
    "username": "admin_user",
    "password": "securepassword",
    "role": "ADMIN"
}
```

**Response `200 OK`:**

```
"User registered successfully!"
```

---

### 2. Financial Records

#### Create a Transaction *(Admin Only)*

```
POST /api/transactions
```

**Request Body:**

```json
{
    "amount": 5000.00,
    "type": "INCOME",
    "category": "Salary",
    "date": "2026-04-03",
    "description": "April Salary"
}
```

**Response `201 Created`:**

```json
{
    "id": 1,
    "amount": 5000.00,
    "type": "INCOME",
    "category": "Salary",
    "date": "2026-04-03",
    "description": "April Salary"
}
```

---

#### Get All Transactions *(Viewer, Analyst, Admin)*

```
GET /api/transactions
```

**Query Parameters (Optional):**

| Parameter | Example |
|-----------|---------|
| `type` | `?type=INCOME` |
| `category` | `?category=Salary` |
| `date` | `?date=2026-04-03` |

**Response `200 OK`:**

```json
[
    {
        "id": 1,
        "amount": 5000.00,
        "type": "INCOME",
        "category": "Salary",
        "date": "2026-04-03",
        "description": "April Salary"
    }
]
```

---

#### Update a Transaction *(Admin Only)*

```
PUT /api/transactions/{id}
```

**Request Body:**

```json
{
    "amount": 5500.00,
    "type": "INCOME",
    "category": "Salary",
    "date": "2026-04-03",
    "description": "April Salary + Bonus"
}
```

**Response `200 OK`:** Returns the updated transaction object.

---

#### Delete a Transaction *(Admin Only)*

```
DELETE /api/transactions/{id}
```

**Response `204 No Content`**

---

### 3. Dashboard Analytics

#### Get Dashboard Summary *(Analyst, Admin Only)*

```
GET /api/dashboard/summary
```

**Response `200 OK`:**

```json
{
    "totalIncome": 5000.00,
    "totalExpense": 1500.00,
    "netBalance": 3500.00,
    "categoryWiseTotals": {
        "Salary": 5000.00,
        "Software Subscriptions": 1500.00
    }
}
```

---

### 4. User Management

> **Note:** All endpoints in this section are strictly **Admin Only**.

#### Get All Users

```
GET /api/users
```

**Response `200 OK`:**

```json
[
    {
        "id": 1,
        "username": "admin_user",
        "role": "ADMIN",
        "status": "ACTIVE"
    },
    {
        "id": 2,
        "username": "intern_viewer",
        "role": "VIEWER",
        "status": "ACTIVE"
    }
]
```

---

#### Update a User's Role or Status

Use this endpoint to promote/demote users or lock them out of the system by setting their status to `INACTIVE`.

```
PUT /api/users/{id}
```

**Request Body:** *(Fields are optional; only include what you want to change)*

```json
{
    "role": "ANALYST",
    "status": "INACTIVE"
}
```

**Response `200 OK`:**

```json
{
    "id": 2,
    "username": "intern_viewer",
    "role": "ANALYST",
    "status": "INACTIVE"
}
```

---

#### Delete a User

```
DELETE /api/users/{id}
```

**Response `204 No Content`**

---

## 🛡️ Access Control & Validation Examples

This API is designed to fail predictably and safely. Below are real scenarios showing how the system responds to unauthorized actions and bad data.

---

### Scenario 1: VIEWER Attempts to Modify a Record *(RBAC Enforcement)*

**Action:** A user with role `VIEWER` sends a `PUT /api/transactions/1` request.

**Result:** The system recognizes the valid user but denies the action based on role permissions. No data is modified.

**Response `403 Forbidden`:** Standard Spring Security denial response.

---

### Scenario 2: Missing or Invalid Input Data *(Validation Enforcement)*

**Action:** An `ADMIN` tries to create a transaction with a negative amount and a missing category.

**Request Body:**

```json
{
    "amount": -50.00,
    "type": "EXPENSE",
    "date": "2026-04-03"
}
```

**Result:** The `@Valid` annotation intercepts the DTO. The `GlobalExceptionHandler` formats the exact issues into a structured response.

**Response `400 Bad Request`:**

```json
{
    "amount": "Amount must be greater than zero",
    "category": "Category is required"
}
```

---

### Scenario 3: Requesting a Non-Existent Record *(Not Found Handling)*

**Action:** A user requests `GET /api/transactions/9999` (an ID not in the database).

**Result:** The `ResourceNotFoundException` is caught globally and returned as a clean JSON error.

**Response `404 Not Found`:**

```json
{
  "error": "Transaction not found with ID: 9999"
}
```