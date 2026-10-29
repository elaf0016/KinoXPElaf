const params = new URLSearchParams(window.location.search);
const filmId = params.get("id");

const filmDetaljer = document.getElementById("filmDetaljer");
const forestillingContainer = document.getElementById("filmForestillinger");

hentJson(`/api/film/${encodeURIComponent(filmId)}`)
    .then(film => {
        filmDetaljer.innerHTML = `
            <img src="${escapeHtml(film.plakatUrl)}" alt="${escapeHtml(film.titel)}" width="200">
            <h2>${escapeHtml(film.titel)}</h2>
            <p>Varighed: ${film.varighed} min</p>
            <p>Aldersgrænse: ${film.aldersGraense} år</p>
        `;
    })
    .catch(error => {
        filmDetaljer.innerHTML = "<p>Filmen blev ikke fundet.</p>";
        console.error("Fejl:", error);
    });

hentJson(`/api/film/${encodeURIComponent(filmId)}/forestillinger`)
    .then(data => {
        if (data.length === 0) {
            forestillingContainer.innerHTML = "<p>Ingen forestillinger for denne film.</p>";
            return;
        }

        data.forEach(forestilling => {
            const div = document.createElement("div");
            div.innerHTML = `
                <p><strong>Starttid:</strong> ${formatTid(forestilling.startTid)}</p>
                <p><strong>Sal:</strong> ${escapeHtml(forestilling.sal.navn)}</p>

                <input type="text" id="navn-${forestilling.id}" placeholder="Dit navn">
                <input type="number" id="antal-${forestilling.id}" placeholder="Antal billetter" min="1" max="10">

                <button onclick="opretReservation(${forestilling.id})">
                    Reserver billetter
                </button>

                <p id="besked-${forestilling.id}"></p>
                <hr>
            `;
            forestillingContainer.appendChild(div);
        });
    })
    .catch(error => console.error("Fejl:", error));

function opretReservation(forestillingId) {
    const navn = document.getElementById(`navn-${forestillingId}`).value.trim();
    const antalBilletter = Number(document.getElementById(`antal-${forestillingId}`).value);
    const besked = document.getElementById(`besked-${forestillingId}`);

    if (!navn || !Number.isInteger(antalBilletter) || antalBilletter < 1) {
        visBesked(besked, "Udfyld navn og antal billetter.", false);
        return;
    }

    hentJson("/api/reservationer", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            navn: navn,
            antalBilletter: antalBilletter,
            forestilling: { id: forestillingId }
        })
    })
        .then(() => visBesked(besked, "Reservation gennemført!", true))
        .catch(error => visBesked(besked, error.message, false));
}

function visBesked(element, tekst, ok) {
    element.innerText = tekst;
    element.className = ok ? "besked-ok" : "besked-fejl";
}
