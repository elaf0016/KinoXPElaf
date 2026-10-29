const params = new URLSearchParams(window.location.search);
const filmId = params.get("id");
const forvalgtForestilling = Number(params.get("forestilling"));

const filmDetaljer = document.getElementById("filmDetaljer");
const forestillingContainer = document.getElementById("forestillinger");
const reservationSektion = document.getElementById("reservation");
const formular = document.getElementById("reservationForm");
const besked = document.getElementById("besked");
const bekraeftelse = document.getElementById("bekraeftelse");

let valgt = null;

hentJson(`/api/film/${encodeURIComponent(filmId)}`)
    .then(film => {
        document.title = `${film.titel} – KinoXP`;
        filmDetaljer.innerHTML = `
            <img src="${escapeHtml(film.plakatUrl)}" alt="Plakat for ${escapeHtml(film.titel)}">
            <div>
                <h1>${escapeHtml(film.titel)}</h1>
                <div class="info-linje">
                    <span class="maerke alder">${formatAlder(film.aldersGraense)}</span>
                    <span>${escapeHtml(film.genre)}</span>
                    <span>·</span>
                    <span>${formatVarighed(film.varighed)}</span>
                </div>
                <p class="beskrivelse">${escapeHtml(film.beskrivelse)}</p>
            </div>
        `;
    })
    .catch(error => {
        filmDetaljer.innerHTML = `<p class="besked-fejl">Filmen blev ikke fundet.</p>`;
        console.error("Fejl:", error);
    });

hentJson(`/api/film/${encodeURIComponent(filmId)}/forestillinger`)
    .then(forestillinger => {
        if (forestillinger.length === 0) {
            forestillingContainer.innerHTML = `<p class="tom">Der er ingen kommende forestillinger for denne film.</p>`;
            return;
        }

        // Grupper forestillingerne efter dag
        const perDag = new Map();
        forestillinger.forEach(f => {
            const dag = formatDag(f.startTid);
            if (!perDag.has(dag)) perDag.set(dag, []);
            perDag.get(dag).push(f);
        });

        perDag.forEach((liste, dag) => {
            const dagDiv = document.createElement("div");
            dagDiv.className = "dag";
            dagDiv.innerHTML = `<h3>${dag}</h3><div class="tider"></div>`;

            liste.forEach(f => {
                const knap = document.createElement("button");
                knap.type = "button";
                knap.className = "tid tid-knap";
                knap.disabled = f.ledigePladser <= 0;
                knap.innerHTML = `
                    <strong>${formatKlokken(f.startTid)}</strong>
                    <small>${escapeHtml(f.sal.navn)} · ${f.ledigePladser > 0 ? f.ledigePladser + " ledige" : "Udsolgt"}</small>
                `;
                knap.addEventListener("click", () => vaelgForestilling(f, knap));
                dagDiv.querySelector(".tider").appendChild(knap);

                if (f.id === forvalgtForestilling && !knap.disabled) {
                    vaelgForestilling(f, knap);
                }
            });

            forestillingContainer.appendChild(dagDiv);
        });
    })
    .catch(error => {
        forestillingContainer.innerHTML = `<p class="besked-fejl">Kunne ikke hente forestillinger.</p>`;
        console.error("Fejl:", error);
    });

function vaelgForestilling(forestilling, knap) {
    valgt = forestilling;
    document.querySelectorAll(".tid-knap.valgt").forEach(k => k.classList.remove("valgt"));
    knap.classList.add("valgt");

    document.getElementById("valgtForestilling").textContent =
        `${formatDag(forestilling.startTid)} kl. ${formatKlokken(forestilling.startTid)} i ${forestilling.sal.navn}`;
    document.getElementById("antal").max = Math.min(10, forestilling.ledigePladser);
    besked.textContent = "";
    bekraeftelse.hidden = true;
    reservationSektion.hidden = false;
}

formular.addEventListener("submit", event => {
    event.preventDefault();
    if (!valgt) return;

    const knap = document.getElementById("reserverKnap");
    knap.disabled = true;

    hentJson("/api/reservationer", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            navn: document.getElementById("navn").value.trim(),
            antalBilletter: Number(document.getElementById("antal").value),
            forestilling: { id: valgt.id }
        })
    })
        .then(reservation => {
            formular.reset();
            reservationSektion.hidden = true;
            visBekraeftelse(reservation);
        })
        .catch(error => {
            besked.textContent = error.message;
            besked.className = "besked besked-fejl";
        })
        .finally(() => knap.disabled = false);
});

function visBekraeftelse(reservation) {
    const f = reservation.forestilling;
    bekraeftelse.innerHTML = `
        <h2>Tak for din reservation! 🎉</h2>
        <div class="billet">
            <div>Din bookingkode</div>
            <div class="kode">${escapeHtml(reservation.bookingKode)}</div>
            <dl>
                <dt>Film</dt><dd>${escapeHtml(f.film.titel)}</dd>
                <dt>Tid</dt><dd>${formatDag(f.startTid)} kl. ${formatKlokken(f.startTid)}</dd>
                <dt>Sal</dt><dd>${escapeHtml(f.sal.navn)}</dd>
                <dt>Navn</dt><dd>${escapeHtml(reservation.navn)}</dd>
                <dt>Billetter</dt><dd>${reservation.antalBilletter}</dd>
            </dl>
        </div>
        <p class="tom">Gem koden – du kan altid finde din booking under <a href="booking.html">Find booking</a>.</p>
    `;
    bekraeftelse.hidden = false;
    bekraeftelse.scrollIntoView({ behavior: "smooth" });
}
