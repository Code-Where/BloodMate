const baseUrl = "http://localhost:8080/blood-requests";

function fetchRequests() {
  fetch(baseUrl)
    .then(res => res.json())
    .then(data => {
      const searchTerm = document.getElementById("requestSearchInput").value.toLowerCase();
      const body = document.getElementById("requestTableBody");
      body.innerHTML = "";

      data.forEach(req => {
        const combinedText = `
          ${req.patientName} ${req.hospitalName} ${req.bloodGroup}
          ${req.city} ${req.requester?.name || ""} ${req.donor?.name || ""}
        `.toLowerCase();

        if (!combinedText.includes(searchTerm)) return;

        const row = document.createElement("tr");

        let actions = `<button class="btn" onclick='editRequest(${JSON.stringify(req)})'>Edit</button>`;

        if (!req.isDonationCompleted) {
          if (!req.donor) {
            actions += `
              <button class="btn" onclick="assignDonorPrompt(${req.id})">Assign</button>
              <button class="btn" disabled>Unassign</button>
              <button class="btn" disabled>Mark Done</button>
            `;
          } else {
            actions += `
              <button class="btn" disabled>Assign</button>
              <button class="btn" onclick="unassignDonor(${req.id})">Unassign</button>
              <button class="btn" onclick="markComplete(${req.id})">Mark Done</button>
            `;
          }
        }

        actions += `<button class="btn delete" onclick="deleteRequest(${req.id})">Delete</button>`;

        row.innerHTML = `
          <td>${req.id}</td>
          <td>${req.patientName}</td>
          <td>${req.hospitalName}</td>
          <td>${req.bloodGroup}</td>
          <td>${req.city}</td>
          <td>${req.urgencyLevel}</td>
          <td>${req.requester?.name || "-"}</td>
          <td>${req.donor?.name || "<i>Unassigned</i>"}</td>
          <td>${req.isDonationCompleted ? "✔ Completed" : "⏳ Pending"}</td>
          <td>${actions}</td>
        `;
        body.appendChild(row);
      });
    });
}

function openRequestModal() {
  document.getElementById("requestForm").reset();
  document.getElementById("requestModal").style.display = "block";
}

function closeRequestModal() {
  document.getElementById("requestModal").style.display = "none";
}

function editRequest(req) {
  document.getElementById("requestId").value = req.id;
  document.getElementById("patientName").value = req.patientName;
  document.getElementById("hospitalName").value = req.hospitalName;
  document.getElementById("location").value = req.location;
  document.getElementById("city").value = req.city;
  document.getElementById("bloodGroup").value = req.bloodGroup;
  document.getElementById("urgencyLevel").value = req.urgencyLevel;
  document.getElementById("note").value = req.note || "";
  document.getElementById("deadline").value = new Date(req.deadline).toISOString().split("T")[0];

  document.getElementById("requestModal").style.display = "block";
}



document.getElementById("requestForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const id = document.getElementById("requestId").value;
  const isEdit = !!id;

  const request = {
    patientName: document.getElementById("patientName").value,
    hospitalName: document.getElementById("hospitalName").value,
    location: document.getElementById("location").value,
    city: document.getElementById("city").value,
    bloodGroup: document.getElementById("bloodGroup").value,
    urgencyLevel: document.getElementById("urgencyLevel").value,
    note: document.getElementById("note").value,
    deadline: new Date(document.getElementById("deadline").value).getTime()
  };

  const url = isEdit
    ? `http://localhost:8080/blood-requests/${id}`
    : `http://localhost:8080/blood-requests/${document.getElementById("requesterId").value}`;
  const method = isEdit ? "PUT" : "POST";

  fetch(url, {
    method,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(request)
  }).then(() => {
    closeRequestModal();
    fetchRequests();
  });
});


function assignDonorPrompt(requestId) {
  const donorId = prompt("Enter donor ID to assign:");
  if (!donorId) return;

  fetch(`${baseUrl}/${requestId}/assign-donor/${donorId}`, {
    method: "PUT"
  }).then(() => fetchRequests());
}

function unassignDonor(requestId) {
  fetch(`${baseUrl}/${requestId}/unassign-donor`, {
    method: "PUT"
  }).then(() => fetchRequests());
}

function markComplete(id) {
  fetch(`${baseUrl}/donationComplete/${id}`, {
    method: "PUT"
  }).then(() => fetchRequests());
}

function deleteRequest(id) {
  if (confirm("Are you sure you want to delete this request?")) {
    fetch(`${baseUrl}/${id}`, {
      method: "DELETE"
    }).then(() => fetchRequests());
  }
}

window.onload = fetchRequests;
document.getElementById("requestSearchInput").addEventListener("input", fetchRequests);
