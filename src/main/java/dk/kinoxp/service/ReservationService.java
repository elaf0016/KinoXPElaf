package dk.kinoxp.service;

import dk.kinoxp.entity.Forestilling;
import dk.kinoxp.entity.Reservation;
import dk.kinoxp.repository.ForestillingRepository;
import dk.kinoxp.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class ReservationService {

    public static final int MAKS_BILLETTER = 10;

    // Uden 0/O og 1/I, så koden er nem at læse
    private static final String KODE_TEGN = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ReservationRepository reservationRepository;
    private final ForestillingRepository forestillingRepository;
    private final ForestillingService forestillingService;

    public ReservationService(ReservationRepository reservationRepository,
                              ForestillingRepository forestillingRepository,
                              ForestillingService forestillingService) {
        this.reservationRepository = reservationRepository;
        this.forestillingRepository = forestillingRepository;
        this.forestillingService = forestillingService;
    }

    @Transactional
    public Reservation opret(Reservation reservation) {
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

        if (forestilling.getStartTid().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Forestillingen er allerede startet");
        }

        int ledige = forestillingService.ledigePladser(forestilling);
        if (reservation.getAntalBilletter() > ledige) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Der er kun " + ledige + " ledige pladser til denne forestilling");
        }

        reservation.setNavn(reservation.getNavn().trim());
        reservation.setForestilling(forestilling);
        reservation.setBookingKode(nyBookingKode());
        Reservation gemt = reservationRepository.save(reservation);
        forestilling.setLedigePladser(ledige - reservation.getAntalBilletter());
        return gemt;
    }

    public Reservation find(String bookingKode) {
        return reservationRepository.findByBookingKode(bookingKode.trim().toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Der findes ingen booking med koden " + bookingKode));
    }

    private String nyBookingKode() {
        String kode;
        do {
            StringBuilder sb = new StringBuilder("KXP-");
            for (int i = 0; i < 6; i++) {
                sb.append(KODE_TEGN.charAt(RANDOM.nextInt(KODE_TEGN.length())));
            }
            kode = sb.toString();
        } while (reservationRepository.existsByBookingKode(kode));
        return kode;
    }
}
