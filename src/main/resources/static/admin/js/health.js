const healthUrl = "http://localhost:8080/health-info";

window.onload = () => fetchHealthInfo();

document.getElementById("healthSearchInput").addEventListener("input", function () {
  const term = this.value.toLowerCase();
  const rows = document.querySelectorAll("#healthTableBody tr");
  rows.forEach(row => {
    const userInfo = row.cells[1]?.innerText.toLowerCase() || "";
    const match = userInfo.includes(term);
    row.style.display = match ? "" : "none";
  });
});

function fetchHealthInfo() {
  fetch(healthUrl)
    .then(res => res.json())
    .then(data => {
      const body = document.getElementById("healthTableBody");
      body.innerHTML = "";

      data.forEach(info => {
        const user = info.user || {};
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${info.id}</td>
          <td>${user.id || "-"}<br><small>${user.name || ""} ${user.emailid || ""}</small></td>
          <td>${info.weight}</td>
          <td>${displayBool(info.hasDiabetes)}</td>
<td>${displayBool(info.hasHypertension)}</td>
<td>${displayBool(info.hasAsthma)}</td>
<td>${displayBool(info.hasThyroid)}</td>
<td>${displayBool(info.hasHeartDisease)}</td>
<td>${displayBool(info.anyRecentSurgery)}</td>
<td>${displayBool(info.willDonate)}</td>
          <td>
            <button class="btn" onclick='openHealthModal(${JSON.stringify(info)})'>Edit</button>
            <button class="btn delete" onclick='deleteHealth(${info.id})'>Delete</button>
          </td>
        `;
        body.appendChild(tr);
      });
    });
}

function openHealthModal(data) {
  document.getElementById("editHealthId").value = data.id;
  document.getElementById("editHealthWeight").value = data.weight;
  document.getElementById("editHealthDiabetes").value = data.hasDiabetes ? "true" : "false";
  document.getElementById("editHealthHypertension").value = data.hasHypertension ? "true" : "false";
  document.getElementById("editHealthAsthma").value = data.hasAsthma ? "true" : "false";
  document.getElementById("editHealthThyroid").value = data.hasThyroid ? "true" : "false";
  document.getElementById("editHealthHeart").value = data.hasHeartDisease ? "true" : "false";
  document.getElementById("editHealthSurgery").value = data.anyRecentSurgery ? "true" : "false";
  document.getElementById("editHealthWilling").value = data.willDonate ? "true" : "false";
  autoSetWillingStatus();
  document.getElementById("healthModal").style.display = "block";
}

function closeHealthModal() {
  document.getElementById("healthModal").style.display = "none";
}

document.getElementById("healthForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const id = document.getElementById("editHealthId").value;

  const updated = {
    id: id,
    weight: parseInt(document.getElementById("editHealthWeight").value),
    hasDiabetes: document.getElementById("editHealthDiabetes").value === "true",
    hasHypertension: document.getElementById("editHealthHypertension").value === "true",
    hasAsthma: document.getElementById("editHealthAsthma").value === "true",
    hasThyroid: document.getElementById("editHealthThyroid").value === "true",
    hasHeartDisease: document.getElementById("editHealthHeart").value === "true",
    anyRecentSurgery: document.getElementById("editHealthSurgery").value === "true",
    willDonate: document.getElementById("editHealthWilling").value === "true"
  };

  fetch(`http://localhost:8080/health-info/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(updated)
  })
    .then(response => {
      if (!response.ok) {
        return response.text().then(text => {
          throw new Error(`Server responded with status ${response.status}: ${text}`);
        });
      }
      return response.json();
    })
    .then(() => {
      closeHealthModal();
      fetchHealthInfo();
    })
    .catch(error => {
      console.error("Health update failed:", error);
      alert("Failed to update health info. Check console for details.");
    });

});


function deleteHealth(id) {
  if (confirm("Delete this record?")) {
    fetch(`${healthUrl}/${id}`, { method: "DELETE" })
      .then(() => fetchHealthInfo());
  }
}

function displayBool(val) {
  return val ? "Yes" : "No";
}

function autoSetWillingStatus() {
  const diseaseFields = [
    "editHealthDiabetes",
    "editHealthHypertension",
    "editHealthAsthma",
    "editHealthThyroid",
    "editHealthHeart",
    "editHealthSurgery"
  ];

  const anyDiseaseYes = diseaseFields.some(id => document.getElementById(id).value === "true");

  const willingSelect = document.getElementById("editHealthWilling");
  if (anyDiseaseYes) {
    willingSelect.value = "false";
    willingSelect.disabled = true;
  } else {
    willingSelect.disabled = false;
  }
}

["editHealthDiabetes", "editHealthHypertension", "editHealthAsthma", "editHealthThyroid", "editHealthHeart", "editHealthSurgery"]
  .forEach(id => {
    document.getElementById(id).addEventListener("change", autoSetWillingStatus);
  });

