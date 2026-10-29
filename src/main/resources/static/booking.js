const soegForm = document.getElementById("soegForm");
const besked = document.getElementById("besked");
const resultat = document.getElementById("resultat");

soegForm.addEventListener("submit", event => {
    event.preventDefault();
    const kode = document.getElementById("kode").value.trim();
    besked.textContent = "";
    resultat.hidden = true;

    hentJson(`/api/reservationer/${encodeURIComponent(kode)}`)
        .then(reservation => {
            const f = reservation.forestilling;
            resultat.innerHTML = `
                <div class="billet">
                    <div>Bookingkode</div>
                    <div class="kode">${escapeHtml(reservation.bookingKode)}</div>
                    <dl>
                        <dt>Film</dt><dd>${escapeHtml(f.film.titel)}</dd>
                        <dt>Tid</dt><dd>${formatDag(f.startTid)} kl. ${formatKlokken(f.startTid)}</dd>
                        <dt>Sal</dt><dd>${escapeHtml(f.sal.navn)}</dd>
                        <dt>Navn</dt><dd>${escapeHtml(reservation.navn)}</dd>
                        <dt>Billetter</dt><dd>${reservation.antalBilletter}</dd>
                    </dl>
                </div>
            `;
            resultat.hidden = false;
        })
        .catch(error => {
            besked.textContent = error.message;
            besked.className = "besked besked-fejl";
        });
});
