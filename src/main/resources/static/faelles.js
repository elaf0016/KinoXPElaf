// Fælles hjælpefunktioner til alle sider

// Undgår at tekst fra brugere (fx navne) bliver tolket som HTML
function escapeHtml(tekst) {
    return String(tekst ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#39;");
}

// "2026-11-02T19:30:00" -> "19.30"
function formatKlokken(tid) {
    return new Date(tid).toLocaleTimeString("da-DK", { hour: "2-digit", minute: "2-digit" });
}

// "2026-11-02T19:30:00" -> "mandag 2. november"
function formatDag(tid) {
    return new Date(tid).toLocaleDateString("da-DK", { weekday: "long", day: "numeric", month: "long" });
}

// "2026-11-02T19:30:00" -> "man. 2. nov."
function formatKortDag(tid) {
    return new Date(tid).toLocaleDateString("da-DK", { weekday: "short", day: "numeric", month: "short" });
}

function formatAlder(aldersGraense) {
    return aldersGraense === 0 ? "Tilladt for alle" : `Fra ${aldersGraense} år`;
}

function formatVarighed(minutter) {
    return `${Math.floor(minutter / 60)} t ${minutter % 60} min`;
}

// fetch, der giver en fejl, hvis serveren svarer med fx 400 eller 404
async function hentJson(url, options) {
    const response = await fetch(url, options);
    const data = await response.json().catch(() => null);
    if (!response.ok) {
        throw new Error(data?.message || `Serveren svarede ${response.status}`);
    }
    return data;
}
