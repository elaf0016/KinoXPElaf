package dk.kinoxp.controller;

import dk.kinoxp.entity.Reservation;
import dk.kinoxp.repository.ReservationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {

    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/api/reservationer")
    public List<Reservation> hentAlleReservationer() {
        return reservationRepository.findAll();
    }

    @PostMapping("/api/reservationer")
    public Reservation opretReservation(@RequestBody Reservation reservation) {
        return reservationRepository.save(reservation);
    }

}