| name                       | about                                                                                          | title                                  | labels   | assignees |
|----------------------------|------------------------------------------------------------------------------------------------|----------------------------------------|----------|-----------|
| Document System Architecture | Create an architecture summary and data flow documentation for the Smart Clinic Management System | "[DOCS] Add architecture design document" | architecture |           |

<br>

> [!IMPORTANT]  
> **Regarding Architecture Summary and Flow Description:**  
> Keep structure consistent with future enterprise documentation standards.  
> Ensure clear layer separation, consistent terminology, and testable flow steps.  
> Include both technical and user-facing perspectives.  

<br>

## **Architecture Overview**

**System**: Smart Clinic Management System  
**Tech Stack**: Spring Boot, Thymeleaf, REST APIs, MySQL, MongoDB  

**As a** full-stack application developer  
**I need** a documented architecture overview  
**So that** I can understand the structure, request lifecycle, and data flow for development and troubleshooting.

---

## **Architecture Summary**

This Spring Boot-based application follows a clean three-tier architecture pattern:

- **Presentation Tier**:  
  - Thymeleaf templates render dynamic HTML for Admin and Doctor dashboards.  
  - REST APIs serve data to frontend or mobile clients for modules like Appointments and Patient Records.

- **Application Tier**:  
  - Controllers (both MVC and REST) route requests to a centralized **Service Layer**.  
  - The Service Layer enforces business rules and communicates with data repositories.

- **Data Tier**:  
  - **MySQL** handles structured data (Patients, Doctors, Appointments, Admins).  
  - **MongoDB** stores flexible, document-based prescription data.

Spring Boot enables modular development and integrates well with CI/CD tools. The dual-database setup ensures optimal storage for both structured and semi-structured data.

---

## **Data Flow and Control Walkthrough**

```gherkin
1. User initiates a request  
   - Through Thymeleaf (AdminDashboard/DoctorDashboard) or  
   - Via RESTful clients (Appointment or Patient APIs)

2. The request is routed by Spring Boot  
   - MVC Controllers handle server-rendered views (.html via Thymeleaf)  
   - REST Controllers handle HTTP API requests and respond in JSON

3. The controller invokes the appropriate Service Layer method  
   - Business rules are applied (e.g., check availability, validate form input)

4. The Service Layer calls the Repository Layer  
   - Spring Data JPA Repositories (for MySQL)  
   - Spring Data MongoDB Repositories (for Prescriptions)

5. Repositories query or persist data  
   - MySQL stores relational data (patients, appointments)  
   - MongoDB handles flexible schema documents (prescriptions)

6. Data is bound to Java models  
   - JPA Entities for SQL data (`@Entity`)  
   - MongoDB Documents (`@Document`) for NoSQL collections

7. Response is generated  
   - Thymeleaf templates receive models and render HTML  
   - REST endpoints return serialized JSON data to clients
````

---

## **Acceptance Criteria**

```gherkin
Given the application is deployed and databases are configured  
When a user accesses the Admin or Doctor dashboard via browser  
Then the application should return an HTML page rendered with Thymeleaf templates

Given a client sends a REST request for appointment data  
When the appropriate REST controller handles the request  
Then the service should query MySQL and return the appointment in JSON format

Given a doctor submits a new prescription  
When the service layer processes the input  
Then it should persist the data to MongoDB and return a success message
```

