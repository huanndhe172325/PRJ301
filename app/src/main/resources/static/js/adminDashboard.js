// adminDashboard.js — logic for Admin to manage doctors

import { getDoctors, saveDoctor, filterDoctors } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";
import { openModal, closeModal } from "./components/modals.js";

// === Event Listener: Add Doctor Button ===
document.addEventListener("DOMContentLoaded", () => {
  const addBtn = document.getElementById("addDoctorBtn");
  if (addBtn) {
    addBtn.addEventListener("click", () => openModal("addDoctor"));
  }

  // Load all doctors initially
  loadDoctorCards();

  // Add filter listeners
  document.getElementById("searchBar")?.addEventListener("input", filterDoctorsOnChange);
  document.getElementById("timeFilter")?.addEventListener("change", filterDoctorsOnChange);
  document.getElementById("specialtyFilter")?.addEventListener("change", filterDoctorsOnChange);
});

// === Load and Display All Doctor Cards ===
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (err) {
    console.error("Error loading doctor cards:", err);
  }
}

// === Filter Handler ===
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar")?.value.trim() || "";
  const time = document.getElementById("timeFilter")?.value || "";
  const specialty = document.getElementById("specialtyFilter")?.value || "";

  try {
    const result = await filterDoctors(name, time, specialty);

    if (result.length > 0) {
      renderDoctorCards(result);
    } else {
      document.getElementById("content").innerHTML = `<p class="noDoctorMsg">No doctors found with the given filters.</p>`;
    }
  } catch (err) {
    console.error("Error filtering doctors:", err);
    alert("Failed to filter doctors. Please try again.");
  }
}

// === Render Doctor Cards ===
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  doctors.forEach((doc) => {
    const card = createDoctorCard(doc);
    contentDiv.appendChild(card);
  });
}

// === Admin Add Doctor Handler ===
window.adminAddDoctor = async function () {
  const name = document.getElementById("docName")?.value.trim();
  const email = document.getElementById("docEmail")?.value.trim();
  const phone = document.getElementById("docPhone")?.value.trim();
  const password = document.getElementById("docPassword")?.value.trim();
  const specialty = document.getElementById("docSpecialty")?.value.trim();
  const availableTimesInput = document.getElementById("docAvailableTimes")?.value.trim();

  if (!name || !email || !phone || !password || !specialty || !availableTimesInput) {
    alert("Please fill in all fields.");
    return;
  }

  const availableTimes = availableTimesInput.split(",").map(t => t.trim());
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Session expired. Please log in again.");
    window.location.href = "/";
    return;
  }

  const doctor = {
    name,
    email,
    phone,
    password,
    specialty,
    availableTimes
  };

  try {
    const result = await saveDoctor(doctor, token);

    if (result.success) {
      alert("Doctor added successfully!");
      closeModal("addDoctor");
      loadDoctorCards();
    } else {
      alert(`Failed to add doctor: ${result.message}`);
    }
  } catch (err) {
    console.error("Error adding doctor:", err);
    alert("An error occurred. Please try again.");
  }
};




/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form


  Attach a click listener to the "Add Doctor" button
  When clicked, it opens a modal form using openModal('addDoctor')


  When the DOM is fully loaded:
    - Call loadDoctorCards() to fetch and display all doctors


  Function: loadDoctorCards
  Purpose: Fetch all doctors and display them as cards

    Call getDoctors() from the service layer
    Clear the current content area
    For each doctor returned:
    - Create a doctor card using createDoctorCard()
    - Append it to the content div

    Handle any fetch errors by logging them


  Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  On any input change, call filterDoctorsOnChange()


  Function: filterDoctorsOnChange
  Purpose: Filter doctors based on name, available time, and specialty

    Read values from the search bar and filters
    Normalize empty values to null
    Call filterDoctors(name, time, specialty) from the service

    If doctors are found:
    - Render them using createDoctorCard()
    If no doctors match the filter:
    - Show a message: "No doctors found with the given filters."

    Catch and display any errors with an alert


  Function: renderDoctorCards
  Purpose: A helper function to render a list of doctors passed to it

    Clear the content area
    Loop through the doctors and append each card to the content area


  Function: adminAddDoctor
  Purpose: Collect form data and add a new doctor to the system

    Collect input values from the modal form
    - Includes name, email, phone, password, specialty, and available times

    Retrieve the authentication token from localStorage
    - If no token is found, show an alert and stop execution

    Build a doctor object with the form values

    Call saveDoctor(doctor, token) from the service

    If save is successful:
    - Show a success message
    - Close the modal and reload the page

    If saving fails, show an error message
*/
