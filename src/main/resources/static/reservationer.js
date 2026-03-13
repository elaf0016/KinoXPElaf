fetch("/api/reservationer")
    .then(response => response.json())
    .then(data => {
        const container = document.getElementById("reservationer");

        data.forEach(reservation => {
            const div = document.createElement("div");

            div.innerHTML = `
                <p><strong>Navn:</strong> ${reservation.navn}</p>
                <p><strong>Film:</strong> ${reservation.forestilling.film.titel}</p>
                <p><strong>Starttid:</strong> ${reservation.forestilling.startTid}</p>
                <p><strong>Antal billetter:</strong> ${reservation.antalBilletter}</p>
            `;

            container.appendChild(div);
        });
    });