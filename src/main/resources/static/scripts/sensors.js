const API_SENSOR = "http://localhost:8080/sensor";

// Load sensors
async function loadSensors() {
  try {
    const res = await fetch(API_SENSOR);
    const sensors = await res.json();
    renderSensors(sensors);
  } catch (err) {
    console.error("Failed to load sensors", err);
    showToast("Failed to load sensors", "error");
  }
}

// Render table
function renderSensors(sensors) {
  const list = document.getElementById("sensorsList");
  list.innerHTML = "";
  sensors.forEach(s => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${s.id}</td>
      <td>${s.sensorType}</td>
      <td>${s.value}</td>
      <td>${s.unit}</td>
      <td>${fmtDate(s.timestamp)}</td>
      <td>${s.anomaly ? "Yes" : "No"}</td>
      <td>${s.deviceId ?? "-"}</td>
      <td>
        <button class="btn warning" onclick="showUpdateForm(${s.id}, '${s.sensorType}', '${s.value}', '${s.unit}', ${s.anomaly})">Edit</button>
      </td>`;
    list.appendChild(tr);
  });
}

// Add new sensor
document.getElementById("sensorForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const type = document.getElementById("sensorType").value;
  const value = document.getElementById("sensorValue").value;
  const unit = document.getElementById("sensorUnit").value;
  const anomaly = document.getElementById("sensorAnomaly").value === "true";
  const deviceId = document.getElementById("sensorDeviceId").value;

  try {
    await fetch(API_SENSOR, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ sensorType: type, value, unit, anomaly, deviceId })
    });
    showToast("Sensor added", "success");
    document.getElementById("sensorForm").reset();
    loadSensors();
  } catch (err) {
    console.error("Failed to add sensor", err);
    showToast("Failed to add sensor", "error");
  }
});

// Show update form
function showUpdateForm(id, type, value, unit, anomaly) {
  document.getElementById("updateCard").style.display = "block";
  document.getElementById("updateSensorId").value = id;
  document.getElementById("updateSensorType").value = type;
  document.getElementById("updateSensorValue").value = value;
  document.getElementById("updateSensorUnit").value = unit;
  document.getElementById("updateSensorAnomaly").value = anomaly;
}

function cancelUpdate() {
  document.getElementById("updateCard").style.display = "none";
}

// Handle update
document.getElementById("updateForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = document.getElementById("updateSensorId").value;
  const type = document.getElementById("updateSensorType").value;
  const value = document.getElementById("updateSensorValue").value;
  const unit = document.getElementById("updateSensorUnit").value;
  const anomaly = document.getElementById("updateSensorAnomaly").value === "true";

  try {
    await fetch(`${API_SENSOR}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ sensorType: type, value, unit, anomaly })
    });
    showToast("Sensor updated", "success");
    cancelUpdate();
    loadSensors();
  } catch (err) {
    console.error("Failed to update sensor", err);
    showToast("Failed to update sensor", "error");
  }
});

// Filter by type
async function filterByType() {
  const type = document.getElementById("typeFilter").value;
  if (!type) {
    loadSensors();
    return;
  }
  try {
    const res = await fetch(`${API_SENSOR}/type/${type}`);
    const sensors = await res.json();
    renderSensors(sensors);
  } catch (err) {
    console.error("Failed to filter sensors", err);
    showToast("Failed to filter sensors", "error");
  }
}

// Format date
function fmtDate(iso) {
  if (!iso) return "-";
  const d = new Date(iso);
  return isNaN(d) ? iso : d.toLocaleString();
}

window.onload = loadSensors;
