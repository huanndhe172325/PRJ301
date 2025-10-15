| name                   | about                                                             | title                          | labels       | assignees |
|------------------------|--------------------------------------------------------------------|--------------------------------|--------------|-----------|
| Patient User Stories   | Functional user stories for patient role in the Smart Clinic system | "[STORY] Patient User Stories" | user-stories |           |

> [!IMPORTANT]  
> Prioritize usability and patient empowerment through clear, goal-driven user flows.  
> Consider authentication, appointment management, and data privacy.

## **Role: Patient**

**As a** patient  
**I need** the ability to explore, book, and manage appointments  
**So that** I can receive medical care efficiently and securely.

---

### User Story 1 – View Doctors Without Logging In

_As a patient, I want to view a list of doctors without logging in, so that I can explore my options before registering._

```gherkin
Given I access the doctor listing page  
When I am not logged in  
Then I should see names, specialties, and contact info of available doctors
````

---

### User Story 2 – Patient Sign-Up

*As a patient, I want to sign up using my email and password, so that I can book appointments.*

```gherkin
Given I am on the signup page  
When I submit valid registration details  
Then my account should be created  
And I should be redirected to the login page
```

---

### User Story 3 – Patient Login

*As a patient, I want to log into the portal, so that I can manage my bookings.*

```gherkin
Given I have a registered account  
When I enter valid credentials  
Then I should be redirected to my dashboard  
And gain access to appointment management features
```

---

### User Story 4 – Book Appointment

*As a patient, I want to book an hour-long appointment, so that I can consult with a doctor.*

```gherkin
Given I am logged in  
When I select a doctor, date, and time  
Then the system should verify availability  
And confirm the booking
```

---

### User Story 5 – View Upcoming Appointments

*As a patient, I want to view my upcoming appointments, so that I can prepare in advance.*

```gherkin
Given I am logged in  
When I visit the "My Appointments" section  
Then I should see a list of all upcoming appointments with relevant details
```


