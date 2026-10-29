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

// "2026-03-10T19:30:00" -> "10. mar. 2026 19.30"
function formatTid(tid) {
    return new Date(tid).toLocaleString("da-DK", { dateStyle: "medium", timeStyle: "short" });
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
