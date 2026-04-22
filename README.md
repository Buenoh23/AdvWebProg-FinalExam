# International Student Advising Portal

## Theme Justification
This project is an **International Student Advising Portal**, designed to manage logistical and academic support requests for exchange students. 
* **Table B (Group):** `SupportCategory` (e.g., Course Registration, Housing, Immigration).
* **Table A (Main):** `AdvisingRequest`.

*Note: This theme explicitly avoids the restricted store/cart/inventory concepts while providing a meaningful, real-world administrative workflow.*

## Technical Domain Rules Enforced (Backend)
1. **Status Transitions:** Requests strictly follow the `SUBMITTED` ➔ `IN_REVIEW` ➔ `RESOLVED` pipeline. Backwards transitions are blocked by the `AdvisingService` and display an error.
2. **Custom Domain Rule:** The `urgencyScore` must be strictly validated as 1, 2, or 3 on the backend.

## Run Instructions

### 1. Database Setup
Start your MariaDB service and configure the dedicated application database and user:
```bash
sudo service mariadb start
sudo mariadb -u root
```

Inside the MariaDB prompt, execute:
```bash
CREATE DATABASE advising_portal_db;
CREATE USER IF NOT EXISTS 'advisinguser'@'localhost' IDENTIFIED BY 'advisingpass';
GRANT ALL PRIVILEGES ON advising_portal_db.* TO 'advisinguser'@'localhost';
FLUSH PRIVILEGES;
exit;
```
### 2. Start the Application
Run the following command in the root directory:
```bash
./mvnw spring-boot:run
```

### 3. Access the Application
Navigate to http://localhost:8080. The root URL will automatically redirect to the /main request dashboard.

## Data Seeding Method

**Method Used:** `CommandLineRunner`

The application uses a `DataSeeder.java` configuration class. Upon startup, if the database is empty, it automatically injects:

* 3 `SupportCategory` records
* 8 `AdvisingRequest`records spanning different statuses and urgencies