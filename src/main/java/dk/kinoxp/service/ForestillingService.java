package dk.kinoxp.service;

import dk.kinoxp.entity.Forestilling;
import dk.kinoxp.repository.ForestillingRepository;
import dk.kinoxp.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForestillingService {

    private final ForestillingRepository forestillingRepository;
    private final ReservationRepository reservationRepository;

    public ForestillingService(ForestillingRepository forestillingRepository,
                               ReservationRepository reservationRepository) {
        this.forestillingRepository = forestillingRepository;
        this.reservationRepository = reservationRepository;
    }

    // Kun forestillinger, der ikke er startet endnu
    public List<Forestilling> kommendeForestillinger() {
        return medLedigePladser(forestillingRepository.findByStartTidAfterOrderByStartTid(LocalDateTime.now()));
    }

    public List<Forestilling> kommendeForestillingerForFilm(Long filmId) {
        return medLedigePladser(
                forestillingRepository.findByFilmIdAndStartTidAfterOrderByStartTid(filmId, LocalDateTime.now()));
    }

    public int ledigePladser(Forestilling forestilling) {
        int solgte = reservationRepository.antalSolgteBilletter(forestilling.getId());
        return forestilling.getSal().getKapacitet() - solgte;
    }

    private List<Forestilling> medLedigePladser(List<Forestilling> forestillinger) {
        forestillinger.forEach(f -> f.setLedigePladser(ledigePladser(f)));
        return forestillinger;
    }
}
