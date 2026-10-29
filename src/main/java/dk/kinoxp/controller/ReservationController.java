package dk.kinoxp.controller;

import dk.kinoxp.entity.Reservation;
import dk.kinoxp.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Der er bevidst ikke et endpoint, der viser alle reservationer, da de indeholder kunders navne
    @GetMapping("/api/reservationer/{bookingKode}")
    public Reservation hentReservation(@PathVariable String bookingKode) {
        return reservationService.find(bookingKode);
    }

    @PostMapping("/api/reservationer")
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation opretReservation(@RequestBody Reservation reservation) {
        return reservationService.opret(reservation);
    }
}
