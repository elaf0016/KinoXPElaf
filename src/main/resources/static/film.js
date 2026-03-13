const params = new URLSearchParams(window.location.search);
const filmId = params.get("id");

fetch(`/api/film/${filmId}`)
    .then(response => response.json())
    .then(film => {
        const filmDetaljer = document.getElementById("filmDetaljer");

        filmDetaljer.innerHTML = `
            <img src="${film.plakatUrl}" alt="${film.titel}" width="200">
            <h2>${film.titel}</h2>
            <p>Varighed: ${film.varighed} min</p>
            <p>Aldersgrænse: ${film.aldersGraense}</p>
        `;
    });

fetch(`/api/film/${filmId}/forestillinger`)
    .then(response => response.json())
    .then(data => {
        const container = document.getElementById("filmForestillinger");

        data.forEach(forestilling => {
            const div = document.createElement("div");
            div.innerHTML = `
                <p><strong>Starttid:</strong> ${forestilling.startTid}</p>
                <p><strong>Sal:</strong> ${forestilling.sal.navn}</p>

                <input type="text" id="navn-${forestilling.id}" placeholder="Dit navn">
                <input type="number" id="antal-${forestilling.id}" placeholder="Antal billetter">

                <button onclick="opretReservation(${forestilling.id})">
                    Reserver billetter
                </button>

                <p id="besked-${forestilling.id}"></p>
                <hr>
            `;
            container.appendChild(div);
        });
    });

function opretReservation(forestillingId) {
    const navn = document.getElementById(`navn-${forestillingId}`).value;
    const antalBilletter = document.getElementById(`antal-${forestillingId}`).value;
    const besked = document.getElementById(`besked-${forestillingId}`);

    fetch("/api/reservationer", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            navn: navn,
            antalBilletter: antalBilletter,
            forestilling: {
                id: forestillingId
            }
        })
    })
        .then(response => response.json())
        .then(data => {
            besked.innerText = "Reservation gennemført!";
        })
        .catch(error => {
            besked.innerText = "Der skete en fejl.";
            console.error("Fejl:", error);
        });
}