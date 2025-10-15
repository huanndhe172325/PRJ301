| name                  | about                                                               | title                         | labels       | assignees |
|-----------------------|----------------------------------------------------------------------|-------------------------------|--------------|-----------|
| Doctor User Stories   | Functional user stories for the doctor role in the Smart Clinic system | "[STORY] Doctor User Stories" | user-stories |           |

> [!IMPORTANT]  
> Define workflows that streamline scheduling and enhance doctor readiness.  
> Support both availability management and patient context awareness.

## **Role: Doctor**

**As a** doctor  
**I need** access to my appointment calendar and patient information  
**So that** I can deliver efficient care and manage my availability.

---

### User Story 1 – Doctor Login

_As a doctor, I want to log into the portal, so that I can manage my appointments._

```gherkin
Given I am registered as a doctor  
When I enter valid credentials on the login page  
Then I should be redirected to the doctor dashboard  
And I should see my upcoming schedule
````

---

### User Story 2 – Doctor Logout

*As a doctor, I want to log out of the portal, so that I can protect my session and personal data.*

```gherkin
Given I am logged in as a doctor  
When I click the logout button  
Then I should be logged out and redirected to the homepage  
And my session should be terminated
```

---

### User Story 3 – View Appointment Calendar

*As a doctor, I want to view my appointment calendar, so that I can organize my work schedule.*

```gherkin
Given I am logged in  
When I navigate to the calendar  
Then I should see a visual representation of my appointments for the day/week/month
```

---

### User Story 4 – Mark Unavailability

*As a doctor, I want to mark specific times as unavailable, so that patients cannot book during those periods.*

```gherkin
Given I am logged in  
When I mark certain time slots as unavailable  
Then the system should block those slots from being booked by patients
```

---

### User Story 5 – View Patient Details

*As a doctor, I want to view patient details for each appointment, so that I can be better prepared for consultations.*

```gherkin
Given I have scheduled appointments  
When I click on a specific appointment  
Then I should see the patient's name, reason for visit, and basic history
```


