const container = document.getElementById("reservationer");

hentJson("/api/reservationer")
    .then(data => {
        if (data.length === 0) {
            container.innerHTML = "<p>Der er ingen reservationer endnu.</p>";
            return;
        }

        data.forEach(reservation => {
            const div = document.createElement("div");
            div.innerHTML = `
                <p><strong>Navn:</strong> ${escapeHtml(reservation.navn)}</p>
                <p><strong>Film:</strong> ${escapeHtml(reservation.forestilling.film.titel)}</p>
                <p><strong>Starttid:</strong> ${formatTid(reservation.forestilling.startTid)}</p>
                <p><strong>Antal billetter:</strong> ${reservation.antalBilletter}</p>
            `;
            container.appendChild(div);
        });
    })
    .catch(error => {
        container.innerHTML = "<p>Kunne ikke hente reservationer.</p>";
        console.error("Fejl:", error);
    });
