const apiUrl = 'http://localhost:8080/camps';
let camps = [];

const statesAndUTs = [
  "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa",
  "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala",
  "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
  "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
  "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands",
  "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep",
  "Delhi", "Puducherry", "Ladakh", "Lakshadweep"
];

// Fetch all camps
async function fetchCamps() {
  try {
    const response = await fetch(apiUrl);
    camps = await response.json();
    displayCamps(camps);
  } catch (error) {
    console.error('Error fetching camps:', error);
  }
}

// Display camps in table
function displayCamps(campsList) {
  const tableBody = document.querySelector('#campTable tbody');
  tableBody.innerHTML = '';

  campsList.forEach(camp => {
    const formattedDate = new Date(camp.date).toLocaleDateString();

    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${camp.title}</td>
      <td>${camp.location}</td>
      <td>${camp.city}</td>
      <td>${camp.organizerBy}</td>
      <td>${formattedDate}</td>
      <td>
        <button class="btn" onclick='editCamp(${JSON.stringify(camp).replace(/'/g, "\\'")})'>Edit</button>
        <button class="btn delete" onclick='deleteCamp(${camp.id})'>Delete</button>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

// Filter by form inputs
function filterCamps() {
  const title = document.getElementById('searchTitle').value.toLowerCase();
  const location = document.getElementById('searchLocation').value.toLowerCase();
  const state = document.getElementById('searchState').value.toLowerCase();
  const organizerBy = document.getElementById('searchOrganizer').value.toLowerCase();

  const filtered = camps.filter(camp =>
    camp.title.toLowerCase().includes(title) &&
    camp.location.toLowerCase().includes(location) &&
    (camp.city.toLowerCase().includes(state) || state === '') &&
    camp.organizerBy.toLowerCase().includes(organizerBy)
  );

  displayCamps(filtered);
}

// Populate state dropdowns
function populateStates() {
  const stateSelect = document.getElementById('state');
  const searchState = document.getElementById('searchState');

  stateSelect.innerHTML = `<option value="">Select State/UT</option>`;
  searchState.innerHTML = `<option value="">Search by State/UT</option>`;

  statesAndUTs.forEach(state => {
    const option = new Option(state, state);
    stateSelect.appendChild(option);
    searchState.appendChild(option.cloneNode(true));
  });
}

// Create or update a camp
async function createOrUpdateCamp(event) {
  event.preventDefault();

  const newCamp = {
    title: document.getElementById('title').value,
    location: document.getElementById('location').value,
    city: document.getElementById('state').value,
    organizerBy: document.getElementById('organizerBy').value,
    date: new Date(document.getElementById('date').value).getTime(),
  };

  const campId = document.getElementById('campId').value;
  const method = campId ? 'PUT' : 'POST';
  const url = campId ? `${apiUrl}/${campId}` : apiUrl;

  try {
    const response = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newCamp)
    });

    if (response.ok) {
      fetchCamps();
      document.getElementById('createCampForm').reset();
      closeCampModal();
    } else {
      console.error('Failed to save camp:', response.statusText);
    }
  } catch (error) {
    console.error('Error saving camp:', error);
  }
}

// Open modal for new camp
function openCampModal() {
  document.getElementById("createCampForm").reset();
  document.getElementById("campId").value = "";
  document.getElementById("modalTitle").innerText = "Create Camp";
  document.getElementById("campModal").style.display = "flex";
}

// Close modal
function closeCampModal() {
  document.getElementById("campModal").style.display = "none";
}

// Open modal for editing a camp
function editCamp(camp) {
  document.getElementById("campId").value = camp.id;
  document.getElementById("title").value = camp.title;
  document.getElementById("location").value = camp.location;
  document.getElementById("state").value = camp.city;
  document.getElementById("organizerBy").value = camp.organizerBy;
  document.getElementById("date").value = new Date(camp.date).toISOString().split("T")[0];

  document.getElementById("modalTitle").innerText = "Edit Camp";
  document.getElementById("campModal").style.display = "flex";
}

// Delete camp
async function deleteCamp(id) {
  if (!confirm("Delete this camp?")) return;

  try {
    const response = await fetch(`${apiUrl}/${id}`, { method: 'DELETE' });
    if (response.ok) fetchCamps();
    else console.error('Error deleting camp:', response.statusText);
  } catch (error) {
    console.error('Error deleting camp:', error);
  }
}

// Initialize
window.onload = () => {
  fetchCamps();
  populateStates();
};
