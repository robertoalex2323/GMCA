const API_BASE = "";
const ROLE_HOME = {
  ROLE_GERENCIA: "/admin/",
  ROLE_VENTAS: "/ventas/",
  ROLE_ALMACEN: "/almacen/",
  ROLE_PRODUCCION: "/produccion/",
  ROLE_CONTABILIDAD: "/contabilidad/",
  ROLE_DESPACHO: "/despacho/"
};
const NAV_ITEMS = [
  ["/admin/", "Admin", ["ROLE_GERENCIA"]],
  ["/ventas/", "Ventas", ["ROLE_VENTAS", "ROLE_GERENCIA"]],
  ["/almacen/", "Almacen", ["ROLE_ALMACEN", "ROLE_GERENCIA"]],
  ["/produccion/", "Produccion", ["ROLE_PRODUCCION", "ROLE_GERENCIA"]],
  ["/contabilidad/", "Contabilidad", ["ROLE_CONTABILIDAD", "ROLE_GERENCIA"]],
  ["/despacho/", "Despacho", ["ROLE_DESPACHO", "ROLE_GERENCIA"]],
  ["/logistica/", "Logistica", ["ROLE_ALMACEN", "ROLE_DESPACHO", "ROLE_GERENCIA"]]
];
function token() { return localStorage.getItem("gmca_token"); }
function currentRoles() { try { return JSON.parse(localStorage.getItem("gmca_roles") || "[]"); } catch (_) { return []; } }
function hasAnyRole(allowed) { const roles = currentRoles(); return allowed.some(role => roles.includes(role)); }
function authHeaders() { return { "Content-Type": "application/json", "Authorization": `Bearer ${token() || ""}` }; }
async function api(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, { ...options, headers: { ...authHeaders(), ...(options.headers || {}) } });
  if (response.status === 401 || response.status === 403) location.href = "/login/";
  if (!response.ok) throw new Error((await response.json().catch(() => ({ message: "Error" }))).message);
  return response.status === 204 ? null : response.json();
}
async function logout() {
  try { await fetch("/api/auth/logout", { method: "POST", headers: authHeaders() }); } catch (_) {}
  localStorage.removeItem("gmca_token");
  localStorage.removeItem("gmca_roles");
  localStorage.removeItem("gmca_username");
  location.href = "/login/";
}
function fmtMoney(value) { return Number(value || 0).toLocaleString("es-PE", { style: "currency", currency: "PEN" }); }
function fmtQty(value) { return Number(value || 0).toLocaleString("es-PE", { maximumFractionDigits: 3 }); }
function optionLabel(item, extra = "") { return `${item.name || item.businessName || item.plate}${extra}`; }
function setNotice(id, message, type = "") { const el = document.getElementById(id); if (el) { el.textContent = message || ""; el.className = `notice ${type}`.trim(); } }
function renderNav() {
  const nav = document.querySelector("[data-nav]");
  if (!nav) return;
  const path = location.pathname;
  nav.innerHTML = NAV_ITEMS.filter(([, , roles]) => hasAnyRole(roles))
    .map(([href, label]) => `<a class="nav-link ${path === href ? "active" : ""}" href="${href}">${label}</a>`)
    .join("") + '<button onclick="logout()">Salir</button>';
}
function guardPage() {
  const allowed = (document.body.dataset.roles || "").split(",").map(r => r.trim()).filter(Boolean);
  if (!allowed.length) return true;
  if (!token() || !hasAnyRole(allowed)) {
    location.href = "/login/";
    return false;
  }
  return true;
}
function showUserArea() {
  const el = document.querySelector("[data-user]");
  if (el) el.textContent = `${localStorage.getItem("gmca_username") || "GMCA"} | ${currentRoles().join(", ")}`;
}
function fillSelect(id, rows, labelFn) {
  const select = document.getElementById(id);
  if (!select) return;
  select.innerHTML = '<option value="">Seleccionar</option>' + rows.map(row => `<option value="${row.id}">${labelFn(row)}</option>`).join("");
}
function badge(value) {
  const type = value === "LOADED" || value === "GATE_RELEASED" ? "ok" : value === "BLOCKED" ? "danger" : "warn";
  return `<span class="badge ${type}">${value}</span>`;
}
document.addEventListener("DOMContentLoaded", () => { if (guardPage()) { showUserArea(); renderNav(); } });