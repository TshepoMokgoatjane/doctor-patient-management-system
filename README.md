# Doctor Patient Management System

A Java Web Application demonstrating MVC architecture using JSP, Servlets, JDBC, and MySQL to manage doctors and patients, with full CRUD functionality and PRG pattern.

## Tech Stack

- **Language:** Java 21
- **Web Server:** Apache Tomcat 9
- **Frontend:** JSP, JSTL, Bootstrap 5
- **Database:** MySQL 8
- **Logging:** SLF4J + Logback
- **Build Tool:** Maven
- **Containerization:** Docker (multi-stage build)

## Architecture

```
Controller (Servlets) → Service Layer → DAO Layer → MySQL
     ↕                       ↕
  JSP Views            Validation Utility
```

- **MVC Pattern** — Servlets as controllers, JSPs as views, POJOs as models
- **PRG Pattern** — Post-Redirect-Get to prevent duplicate form submissions
- **Soft Delete** — Doctors are flagged as deleted rather than removed from the database
- **Role-Based Access** — Admin vs User roles with servlet filter authorization
- **Password Hashing** — SHA-256 with random salt for secure credential storage

## Prerequisites

- Java 21 (JDK)
- Maven 3.9+
- MySQL 8+
- Docker (for containerized deployment)

## Local Development

### 1. Database Setup

Create the MySQL database and tables:

```sql
CREATE DATABASE web_doctor_patient_management_system;

USE web_doctor_patient_management_system;

CREATE TABLE doctor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP NULL
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    is_active BOOLEAN DEFAULT TRUE
);
```

### 2. Build with Maven

```bash
mvn clean package
```

This produces `target/doctor-patient-management-system.war`.

### 3. Run with Docker

```bash
docker build -t doctor-patient-app .
docker run -p 8080:8080 \
  -e MYSQLUSER=root \
  -e MYSQLPASSWORD=yourpassword \
  -e MYSQL_URL=mysql://host.docker.internal:3306/web_doctor_patient_management_system \
  doctor-patient-app
```

Access the app at: `http://localhost:8080/LoginController`

## Deploying to Railway

### 1. Push to GitHub

```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/doctor-patient-management-system.git
git push -u origin main
```

### 2. Create Railway Project

1. Go to [railway.app](https://railway.app) and sign in with GitHub
2. Click **"New Project"** → **"Deploy from GitHub Repo"**
3. Select your `doctor-patient-management-system` repository
4. Railway will auto-detect the Dockerfile and start building

### 3. Add MySQL Database

1. In your Railway project, click **"+ New"** → **"Database"** → **"MySQL"**
2. Railway provisions a MySQL instance and provides connection variables

### 4. Configure Environment Variables

In your Railway service settings, add these variables (use values from the MySQL add-on):

| Variable | Value |
|----------|-------|
| `MYSQLUSER` | `${{MySQL.MYSQLUSER}}` |
| `MYSQLPASSWORD` | `${{MySQL.MYSQLPASSWORD}}` |
| `MYSQL_URL` | `${{MySQL.MYSQL_URL}}` |

> Railway supports variable references — the `${{MySQL.VARIABLE}}` syntax auto-links to your MySQL add-on.

### 5. Run Database Migrations

Use Railway's MySQL connection details to connect via a MySQL client and run the SQL from step 1 of Local Development above.

### 6. Generate a Domain

In your service settings, go to **"Settings"** → **"Networking"** → **"Generate Domain"** to get a public URL.

## Project Structure

```
src/main/java/za/co/doctorpatient/management/system/
├── admin/auth/filter/       # Authentication & Authorization filters
├── controller/              # Servlet controllers (MVC)
├── dao/                     # Data Access Objects (JDBC)
├── exceptions/              # Custom exceptions
├── model/                   # Domain models (POJOs)
├── roles/                   # Role enum
├── security/                # Password hashing utility
├── service/                 # Business logic layer
├── validation/              # Input validation
└── web/jdbc/test/           # DB connection test servlet

src/main/webapp/
├── META-INF/context.xml     # JNDI DataSource configuration
├── WEB-INF/
│   ├── views/               # JSP pages
│   ├── classes/logback.xml  # Logging configuration
│   └── web.xml              # Deployment descriptor
└── index.html               # Landing page
```

## License

This project is for educational and portfolio purposes.
