const bannerApi = "https://bloodmate-production.up.railway.app/api/banners";

// Replace with your Cloudinary credentials
const cloudName = "video786";
const uploadPreset = "MyBloodMate"; // Must allow unsigned uploads

function uploadBanner() {
  const fileInput = document.getElementById("bannerInput");
  const file = fileInput.files[0];

  if (!file) return alert("Please select a banner image.");
  if (!file.type.startsWith("image/")) return alert("Only image files are allowed.");
  if (file.size > 2 * 1024 * 1024) return alert("Image must be less than 2MB.");

  const formData = new FormData();
  formData.append("file", file);
  formData.append("upload_preset", uploadPreset);

  fetch(`https://api.cloudinary.com/v1_1/${cloudName}/image/upload`, {
    method: "POST",
    body: formData
  })
    .then(res => res.json())
    .then(data => {
      if (data.secure_url) {
        return fetch(bannerApi, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ url: data.secure_url })
        });
      } else {
        throw new Error("Cloudinary upload failed");
      }
    })
    .then(() => {
      alert("Banner uploaded successfully!");
      document.getElementById("bannerInput").value = "";
      fetchBanners();
    })
    .catch(err => {
      console.error(err);
      alert("Failed to upload banner.");
    });
}


function fetchBanners() {
    fetch(bannerApi)
      .then(res => res.json())
      .then(data => {
        const body = document.getElementById("bannerTableBody");
        body.innerHTML = "";
  
        data.forEach((banner, index) => {
          const row = document.createElement("tr");
          row.innerHTML = `
            <td>${index + 1}</td>
            <td><img src="${banner.url}" alt="Banner" class="banner-thumb" /></td>
            <td><a href="${banner.url}" target="_blank">${banner.url}</a></td>
            <td><button class="btn delete" onclick="deleteBanner(${banner.id})">Delete</button></td>
          `;
          body.appendChild(row);
        });
      });
  }
  

function deleteBanner(id) {
  if (!confirm("Are you sure you want to delete this banner?")) return;
  fetch(`${bannerApi}/${id}`, { method: "DELETE" })
    .then(() => {
      alert("Banner deleted.");
      fetchBanners();
    });
}

window.onload = fetchBanners;
