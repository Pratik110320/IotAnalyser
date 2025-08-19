const API_DEV = "http://localhost:8080/device/detail"; // detailed endpoint

// Load all devices
async function loadDevices() {
  try {
    const res = await fetch(API_DEV);
    const devices = await res.json();
    renderDevices(devices);
  } catch (err) {
    console.error("Failed to load devices", err);
    showToast("Failed to load devices", "error");
  }
}

// Render device table
function renderDevices(devices) {
  const list = document.getElementById("devicesList");
  list.innerHTML = "";
  devices.forEach(d => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${d.deviceId}</td>
      <td>${d.deviceName}</td>
      <td>${d.deviceType ?? "-"}</td>
      <td>${fmtDate(d.registeredAt)}</td>
      <td>${fmtDate(d.lastActiveAt)}</td>
      <td>${d.status}</td>
      <td>
        <button class="btn warning" onclick="showUpdateForm(${d.deviceId}, '${d.deviceName}', '${d.deviceType}', '${d.status}')">Edit</button>
        <button class="btn danger" onclick="deleteDevice(${d.deviceId})">Delete</button>
      </td>`;
    list.appendChild(tr);
  });
}

// Add new device
document.getElementById("deviceForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const name = document.getElementById("deviceName").value;
  const type = document.getElementById("deviceType").value;

  try {
    await fetch("http://localhost:8080/device", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ deviceName: name, deviceType: type })
    });
    showToast("Device added", "success");
    document.getElementById("deviceForm").reset();
    loadDevices();
  } catch (err) {
    console.error("Failed to add device", err);
    showToast("Failed to add device", "error");
  }
});

// Delete device
async function deleteDevice(id) {
  if (!confirm("Delete device " + id + "?")) return;
  try {
    await fetch(`http://localhost:8080/device/${id}`, { method: "DELETE" });
    showToast("Device deleted", "success");
    loadDevices();
  } catch (err) {
    console.error("Failed to delete device", err);
    showToast("Failed to delete device", "error");
  }
}

// Show update form
function showUpdateForm(id, name, type, status) {
  document.getElementById("updateCard").style.display = "block";
  document.getElementById("updateDeviceId").value = id;
  document.getElementById("updateDeviceName").value = name;
  document.getElementById("updateDeviceType").value = type;
  document.getElementById("updateDeviceStatus").value = status;
}

function cancelUpdate() {
  document.getElementById("updateCard").style.display = "none";
}

// Handle update
document.getElementById("updateForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = document.getElementById("updateDeviceId").value;
  const name = document.getElementById("updateDeviceName").value;
  const type = document.getElementById("updateDeviceType").value;
  const status = document.getElementById("updateDeviceStatus").value;

  try {
    await fetch(`http://localhost:8080/device/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ deviceName: name, deviceType: type, status: status })
    });
    showToast("Device updated", "success");
    cancelUpdate();
    loadDevices();
  } catch (err) {
    console.error("Failed to update device", err);
    showToast("Failed to update device", "error");
  }
});

// Filter by status
async function filterByStatus() {
  const status = document.getElementById("statusFilter").value;
  if (!status) {
    loadDevices();
    return;
  }
  try {
    const res = await fetch(`http://localhost:8080/device/status/${status}`);
    const devices = await res.json();
    renderDevices(devices);
  } catch (err) {
    console.error("Failed to filter devices", err);
    showToast("Failed to filter devices", "error");
  }
}

// Format date
function fmtDate(iso) {
  if (!iso) return "-";
  const d = new Date(iso);
  return isNaN(d) ? iso : d.toLocaleString();
}

window.onload = loadDevices;
