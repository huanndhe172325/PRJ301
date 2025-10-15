| name                  | about                                                              | title                          | labels       | assignees |
|-----------------------|---------------------------------------------------------------------|--------------------------------|--------------|-----------|
| Admin User Stories    | Functional user stories for the admin role in the Smart Clinic system | "[STORY] Admin User Stories"   | user-stories |           |

> [!IMPORTANT]  
> Ensure each user story defines system goals clearly and reflects realistic admin workflows.  
> Validate features support data integrity, role-based access, and platform security.

## **Role: Admin**

**As an** administrator  
**I need** core capabilities to manage users and monitor operations  
**So that** I can ensure system reliability, user control, and compliance.

---

### User Story 1 – Admin Login

_As an admin, I want to log into the portal with my credentials, so that I can securely manage the system._

```gherkin
Given I am on the login page  
When I enter valid admin credentials  
Then I should be redirected to the admin dashboard  
And my session should be authenticated securely
````

---

### User Story 2 – Admin Logout

*As an admin, I want to log out of the portal, so that I can end my session securely.*

```gherkin
Given I am logged in as an admin  
When I click the logout button  
Then I should be logged out and redirected to the login screen  
And my session should be terminated
```

---

### User Story 3 – Add Doctor

*As an admin, I want to add doctors to the system, so that they can manage appointments.*

```gherkin
Given I am on the "Add Doctor" form  
When I submit valid doctor information  
Then the doctor profile should be saved to MySQL  
And be visible in the doctor list
```

---

### User Story 4 – Delete Doctor Profile

*As an admin, I want to delete a doctor's profile, so that I can manage active practitioners.*

```gherkin
Given a doctor has no future appointments  
When I click "Delete" on their profile  
Then the record should be removed from the system  
And no appointments should reference that doctor
```

---

### User Story 5 – View Monthly Appointments

*As an admin, I want to run a MySQL stored procedure to get monthly appointment counts, so that I can track platform usage.*

```gherkin
Given I have access to the MySQL CLI  
When I run the appointment_stats procedure  
Then I should receive a list of appointment counts grouped by month
```


