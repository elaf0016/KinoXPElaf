package dk.kinoxp.repository;

import dk.kinoxp.entity.Forestilling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ForestillingRepository extends JpaRepository<Forestilling, Long> {
    List<Forestilling> findByStartTidAfterOrderByStartTid(LocalDateTime tidspunkt);

    List<Forestilling> findByFilmIdAndStartTidAfterOrderByStartTid(Long filmId, LocalDateTime tidspunkt);
}
