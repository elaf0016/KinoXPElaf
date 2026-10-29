const filmContainer = document.getElementById("film");

// Viser alle film som kort med de næste tre forestillinger
Promise.all([hentJson("/api/film"), hentJson("/api/forestillinger")])
    .then(([alleFilm, forestillinger]) => {
        if (alleFilm.length === 0) {
            filmContainer.innerHTML = `<p class="tom">Der er ingen film på programmet lige nu.</p>`;
            return;
        }

        alleFilm.forEach(film => {
            const naesteTre = forestillinger
                .filter(f => f.film.id === film.id)
                .slice(0, 3);

            const tider = naesteTre.map(f => `
                <a class="tid" href="film.html?id=${film.id}&forestilling=${f.id}">
                    <strong>${formatKlokken(f.startTid)}</strong>
                    <small>${formatKortDag(f.startTid)}</small>
                </a>
            `).join("");

            const kort = document.createElement("article");
            kort.className = "film-kort";
            kort.innerHTML = `
                <a class="plakat" href="film.html?id=${film.id}">
                    <img src="${escapeHtml(film.plakatUrl)}" alt="Plakat for ${escapeHtml(film.titel)}" loading="lazy">
                </a>
                <div class="film-kort-indhold">
                    <h3><a href="film.html?id=${film.id}">${escapeHtml(film.titel)}</a></h3>
                    <div class="info-linje">
                        <span class="maerke alder">${formatAlder(film.aldersGraense)}</span>
                        <span>${escapeHtml(film.genre)}</span>
                        <span>·</span>
                        <span>${formatVarighed(film.varighed)}</span>
                    </div>
                    <div class="tider">${tider || `<span class="tom">Ingen forestillinger</span>`}</div>
                </div>
            `;
            filmContainer.appendChild(kort);
        });
    })
    .catch(error => {
        filmContainer.innerHTML = `<p class="besked-fejl">Kunne ikke hente programmet. Prøv igen om lidt.</p>`;
        console.error("Fejl:", error);
    });
