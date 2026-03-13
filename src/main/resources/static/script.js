fetch("/api/forestillinger")
    .then(response => response.json())
    .then(data => {
        const container = document.getElementById("forestillinger");

        data.forEach(forestilling => {
            const div = document.createElement("div");
            div.innerHTML = `
                <img src="${forestilling.film.plakatUrl}" alt="${forestilling.film.titel}">
                <h3>
                    <a href="film.html?id=${forestilling.film.id}">
                        ${forestilling.film.titel}
                    </a>
                </h3>
                <p>Starttid: ${forestilling.startTid}</p>
                <p>Sal: ${forestilling.sal.navn}</p>
            `;
            container.appendChild(div);
        });
    })
    .catch(error => console.error("Fejl:", error));