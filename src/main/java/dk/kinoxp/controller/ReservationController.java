package dk.kinoxp.controller;

import dk.kinoxp.entity.Forestilling;
import dk.kinoxp.entity.Reservation;
import dk.kinoxp.repository.ForestillingRepository;
import dk.kinoxp.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ReservationController {

    private static final int MAKS_BILLETTER = 10;

    private final ReservationRepository reservationRepository;
    private final ForestillingRepository forestillingRepository;

    public ReservationController(ReservationRepository reservationRepository,
                                 ForestillingRepository forestillingRepository) {
        this.reservationRepository = reservationRepository;
        this.forestillingRepository = forestillingRepository;
    }

    @GetMapping("/api/reservationer")
    public List<Reservation> hentAlleReservationer() {
        return reservationRepository.findAll();
    }

    @PostMapping("/api/reservationer")
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation opretReservation(@RequestBody Reservation reservation) {
        if (reservation.getNavn() == null || reservation.getNavn().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Navn skal udfyldes");
        }
        if (reservation.getAntalBilletter() < 1 || reservation.getAntalBilletter() > MAKS_BILLETTER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Antal billetter skal være mellem 1 og " + MAKS_BILLETTER);
        }
        if (reservation.getForestilling() == null || reservation.getForestilling().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vælg en forestilling");
        }

        // Hent den rigtige forestilling fra databasen, så svaret indeholder film og sal
        Forestilling forestilling = forestillingRepository.findById(reservation.getForestilling().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forestillingen findes ikke"));

        reservation.setNavn(reservation.getNavn().trim());
        reservation.setForestilling(forestilling);
        return reservationRepository.save(reservation);
    }
}
