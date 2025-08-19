// =================== CONFIG ===================
const API_BASE = "http://localhost:8080";
const SIM_BASE = "http://localhost:8081/admin/simulator";

let stompClient;
let realtimeCount = 0;

// Escape helper
function escapeHtml(s) {
  return String(s || "").replace(/[&<>"']/g, m => ({
    "&":"&amp;", "<":"&lt;", ">":"&gt;", '"':"&quot;", "'":"&#039;"
  }[m]));
}

// Format date
function fmtDate(iso) {
  if (!iso) return "-";
  const d = new Date(iso);
  return isNaN(d) ? iso : d.toLocaleString();
}

// =================== WEBSOCKET ===================
function startWebSocket() {
  if (stompClient && stompClient.connected) return;

  const sock = new SockJS(`${API_BASE}/ws-sensor-data`);
  stompClient = Stomp.over(sock);
  stompClient.debug = () => {};

  stompClient.connect({}, () => {
    showToast("WebSocket connected", "success");
    stompClient.subscribe("/topic/sensorData", msg => {
      if (msg.body) {
        const data = JSON.parse(msg.body);
        addLiveFeedEntry(data);
        realtimeCount++;
        document.getElementById("realtimeCount").textContent = realtimeCount;
      }
    });
  }, () => {
    showToast("WebSocket disconnected", "error");
    setTimeout(startWebSocket, 5000);
  });
}

function addLiveFeedEntry(data) {
  const feed = document.getElementById("liveFeed");
  if (feed.children.length === 1 && feed.children[0].textContent.includes("Waiting")) {
    feed.innerHTML = "";
  }
  const div = document.createElement("div");
  div.className = "feed-entry" + (data.anomaly ? " anomaly" : "");
  div.innerHTML = `<strong>Device:</strong> ${escapeHtml(data.device?.deviceName || "-")} |
                   <strong>Type:</strong> ${escapeHtml(data.sensorType)} <br>
                   <span class="small">${data.value} ${data.unit || ""} · ${fmtDate(data.timestamp)}
                   ${data.anomaly ? "⚠️ Anomaly" : ""}</span>`;
  feed.prepend(div);
  while (feed.children.length > 30) feed.removeChild(feed.lastChild);
}

// =================== API HELPERS ===================
async function safeFetch(url, opts = {}) {
  const res = await fetch(url, opts);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// =================== DEVICES ===================
async function loadDevices() {
  try {
    const devices = await safeFetch(`${API_BASE}/device`);
    const table = document.getElementById("devicesTable");
    table.innerHTML = "";
    devices.forEach(d => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${d.deviceId}</td>
        <td>${d.deviceName}</td>
        <td>${d.deviceType}</td>
        <td>${fmtDate(d.registeredAt)}</td>
        <td>${fmtDate(d.lastActiveAt)}</td>
        <td>${d.status}</td>
        <td><button class="btn danger" onclick="deleteDevice(${d.deviceId})">Delete</button></td>`;
      table.appendChild(tr);
    });
  } catch (e) {
    showToast("Failed to load devices", "error");
  }
}

async function deleteDevice(id) {
  if (!confirm("Delete device " + id + "?")) return;
  await fetch(`${API_BASE}/device/${id}`, {method:"DELETE"});
  showToast("Device deleted", "success");
  loadDevices();
}

// =================== SENSORS ===================
async function loadSensors() {
  try {
    const sensors = await safeFetch(`${API_BASE}/sensor`);
    const table = document.getElementById("sensorsTable");
    table.innerHTML = "";
    sensors.slice(-20).reverse().forEach(s => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${s.id}</td>
        <td>${s.device ? s.device.deviceName : "-"}</td>
        <td>${s.sensorType}</td>
        <td>${s.value} ${s.unit || ""}</td>
        <td>${fmtDate(s.timestamp)}</td>
        <td>${s.anomaly ? "⚠️ Yes" : "No"}</td>
        <td><button class="btn danger" onclick="deleteSensor(${s.id})">Delete</button></td>`;
      table.appendChild(tr);
    });
  } catch (e) {
    showToast("Failed to load sensors", "error");
  }
}

async function deleteSensor(id) {
  if (!confirm("Delete sensor " + id + "?")) return;
  await fetch(`${API_BASE}/sensor/${id}`, {method:"DELETE"});
  showToast("Sensor deleted", "success");
  loadSensors();
}

// =================== SIMULATOR ===================
async function doSim(action) {
  try {
    await fetch(`${SIM_BASE}/${action}`, {method:"POST"});
    showToast(`Simulator ${action} OK`, "success");
  } catch {
    showToast("Simulator failed", "error");
  }
}

// =================== INIT ===================
window.onload = () => {
  loadDevices();
  loadSensors();
  startWebSocket();
};
