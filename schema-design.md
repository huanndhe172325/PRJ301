| name                  | about                                                                       | title                             | labels       | assignees |
|-----------------------|------------------------------------------------------------------------------|-----------------------------------|--------------|-----------|
| Database Schema Design | Defines relational and document-based schema for Smart Clinic architecture | "[DB] Design schema architecture" | schema-design |           |

> [!IMPORTANT]  
> Ensure normalization in SQL design and flexibility in NoSQL design.  
> Provide justifications and comments to guide future implementation decisions.

---

## **MySQL Database Design**

Relational data includes structured records that benefit from strong typing and inter-table relationships.  
This design ensures consistency for patients, doctors, and appointment workflows.

---

### **Table: patients**

| Column Name    | Data Type     | Constraints                  |
|----------------|---------------|------------------------------|
| id             | INT           | PK, AUTO_INCREMENT           |
| name           | VARCHAR(100)  | NOT NULL                     |
| email          | VARCHAR(100)  | NOT NULL, UNIQUE             |
| phone_number   | VARCHAR(15)   | NOT NULL                     |
| date_of_birth  | DATE          |                              |
| gender         | VARCHAR(10)   | CHECK (gender IN ('M','F'))  |
| registered_at  | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP    |

---

### **Table: doctors**

| Column Name     | Data Type     | Constraints                     |
|------------------|---------------|---------------------------------|
| id               | INT           | PK, AUTO_INCREMENT              |
| name             | VARCHAR(100)  | NOT NULL                        |
| specialization   | VARCHAR(100)  | NOT NULL                        |
| email            | VARCHAR(100)  | NOT NULL, UNIQUE                |
| phone_number     | VARCHAR(15)   |                                 |
| is_active        | BOOLEAN       | DEFAULT TRUE                    |

---

### **Table: appointments**

| Column Name      | Data Type     | Constraints                            |
|-------------------|---------------|----------------------------------------|
| id                | INT           | PK, AUTO_INCREMENT                     |
| patient_id        | INT           | FK → patients(id), NOT NULL            |
| doctor_id         | INT           | FK → doctors(id), NOT NULL             |
| appointment_time  | DATETIME      | NOT NULL                               |
| status            | INT           | DEFAULT 0 (0=Scheduled, 1=Completed, 2=Cancelled) |
| created_at        | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP              |

> If a patient is deleted, consider **CASCADE DELETE** for historical cleanup,  
> or archive via logical deletion (e.g., `is_active = FALSE`).

---

### **Table: admin**

| Column Name  | Data Type     | Constraints               |
|---------------|---------------|---------------------------|
| id            | INT           | PK, AUTO_INCREMENT        |
| username      | VARCHAR(50)   | NOT NULL, UNIQUE          |
| password_hash | VARCHAR(255)  | NOT NULL                  |
| email         | VARCHAR(100)  | NOT NULL, UNIQUE          |
| role          | VARCHAR(50)   | DEFAULT 'superadmin'      |

---

## **MongoDB Collection Design**

Unstructured or semi-structured data such as prescriptions benefit from schema flexibility.  
This example uses embedded documents and supports future extensibility.

---

### **Collection: prescriptions**

```json
{
  "_id": "ObjectId('64fabc91234f')",
  "appointmentId": 103,
  "patientId": 23,
  "doctorId": 7,
  "patientName": "Elena Ruiz",
  "medications": [
    {
      "name": "Amoxicillin",
      "dosage": "500mg",
      "frequency": "3 times a day",
      "duration": "7 days"
    },
    {
      "name": "Ibuprofen",
      "dosage": "200mg",
      "frequency": "As needed"
    }
  ],
  "doctorNotes": "Advise rest and hydration. Follow up in 1 week.",
  "issuedAt": "2025-07-03T10:15:00Z",
  "pharmacy": {
    "name": "Greenleaf Pharmacy",
    "location": "432 Oak Blvd, Charleston, SC"
  },
  "refillCount": 1,
  "tags": ["antibiotic", "pain-relief"],
  "metadata": {
    "writtenBy": "Dr. Amanda Singh",
    "signedDigitally": true
  }
}
````

> MongoDB allows nesting and optional fields—ideal for varied prescription formats.
> `appointmentId`, `doctorId`, and `patientId` act as foreign key references conceptually.




