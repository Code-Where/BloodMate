const apiUrl = "http://localhost:8080/users";

window.onload = () => fetchUsers();

document.getElementById("addUserBtn").addEventListener("click", () => openUserModal());

document.getElementById("searchInput").addEventListener("input", function () {
  const value = this.value.toLowerCase();
  document.querySelectorAll("#userTableBody tr").forEach(row => {
    row.style.display = row.innerText.toLowerCase().includes(value) ? "" : "none";
  });
});

document.getElementById("userForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const dobValue = document.getElementById("userDob").value;
  if (!dobValue) return alert("DOB is required.");

  const dob = new Date(dobValue);
  const today = new Date();
  const age = today.getFullYear() - dob.getFullYear();
  const monthDiff = today.getMonth() - dob.getMonth();
  const dayDiff = today.getDate() - dob.getDate();
  const is18Plus = monthDiff > 0 || (monthDiff === 0 && dayDiff >= 0) ? age >= 18 : age > 18;

  if (!is18Plus) return alert("User must be at least 18 years old.");

  const id = document.getElementById("userId").value;

  const user = {
    id: id || undefined,
    name: document.getElementById("userName").value,
    emailid: document.getElementById("userEmail").value || null,
    phone: document.getElementById("userPhone").value,
    password: document.getElementById("userPassword").value,
    bloodgroup: document.getElementById("userBloodGroup").value,
    gender: document.getElementById("userGender").value,
    dob: dob.getTime(),
    lastdonation: document.getElementById("userLastDonation").value
      ? new Date(document.getElementById("userLastDonation").value).getTime()
      : null
  };

  const method = id ? "PUT" : "POST";
  const url = id ? `${apiUrl}/${id}` : apiUrl;

  fetch(url, {
    method,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(user)
  })
    .then(res => res.json())
    .then(createdUser => {
      if (!id) {
        // Post health info to /health-info/{userId}
        const health = {
          weight: null,
          diabetes: false,
          hypertension: false,
          asthma: false,
          thyroid: false,
          heart: false,
          surgery: false,
          willing: true
        };

        return fetch(`http://localhost:8080/health-info/${createdUser.id}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(health)
        });
      }
    })
    .finally(() => {
      closeUserModal();
      fetchUsers();
      if (typeof fetchHealthInfo === "function") fetchHealthInfo();
    });
});


function fetchUsers() {
  fetch(apiUrl)
    .then(res => res.json())
    .then(users => {
      const tbody = document.getElementById("userTableBody");
      tbody.innerHTML = "";
      users.forEach(user => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${user.id}</td>
          <td>${user.name}</td>
          <td>${user.emailid || "-"}</td>
          <td>${user.phone}</td>
          <td>${user.password}</td>
          <td>${user.bloodgroup}</td>
          <td>${user.gender}</td>
          <td>${formatDate(user.dob)}</td>
          <td>${user.lastdonation ? formatDate(user.lastdonation) : "-"}</td>
          <td>
            <button class="btn" onclick='openUserModal(${JSON.stringify(user)})'>Edit</button>
            <button class="btn delete" onclick='deleteUser(${user.id})'>Delete</button>
          </td>
        `;
        tbody.appendChild(tr);
      });
    });
}

function deleteUser(id) {
  if (confirm("Are you sure you want to delete this user?")) {
    fetch(`${apiUrl}/${id}`, { method: "DELETE" }).then(() => fetchUsers());
  }
}

function openUserModal(user = {}) {
  document.getElementById("modalTitle").innerText = user.id ? "Edit User" : "Add User";
  document.getElementById("userId").value = user.id || "";
  document.getElementById("userName").value = user.name || "";
  document.getElementById("userEmail").value = user.emailid || "";
  document.getElementById("userPhone").value = user.phone || "";
  document.getElementById("userPassword").value = user.password || "";
  document.getElementById("userBloodGroup").value = user.bloodgroup || "";
  document.getElementById("userGender").value = user.gender || "";
  document.getElementById("userDob").value = user.dob ? new Date(user.dob).toISOString().split("T")[0] : "";
  document.getElementById("userLastDonation").value = user.lastdonation ? new Date(user.lastdonation).toISOString().split("T")[0] : "";
  document.getElementById("userModal").style.display = "block";
}

function closeUserModal() {
  document.getElementById("userModal").style.display = "none";
}

function formatDate(timestamp) {
  const date = new Date(timestamp);
  return date.toLocaleDateString("en-GB");
}
