const container = document.getElementById("forestillinger");

hentJson("/api/forestillinger")
    .then(data => {
        if (data.length === 0) {
            container.innerHTML = "<p>Der er ingen forestillinger lige nu.</p>";
            return;
        }

        data.forEach(forestilling => {
            const film = forestilling.film;
            const div = document.createElement("div");
            div.innerHTML = `
                <img src="${escapeHtml(film.plakatUrl)}" alt="${escapeHtml(film.titel)}">
                <h3>
                    <a href="film.html?id=${film.id}">
                        ${escapeHtml(film.titel)}
                    </a>
                </h3>
                <p>Starttid: ${formatTid(forestilling.startTid)}</p>
                <p>Sal: ${escapeHtml(forestilling.sal.navn)}</p>
            `;
            container.appendChild(div);
        });
    })
    .catch(error => {
        container.innerHTML = "<p>Kunne ikke hente forestillinger.</p>";
        console.error("Fejl:", error);
    });
