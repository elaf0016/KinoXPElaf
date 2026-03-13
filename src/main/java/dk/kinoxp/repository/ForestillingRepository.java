package dk.kinoxp.repository;

import dk.kinoxp.entity.Forestilling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForestillingRepository extends JpaRepository<Forestilling, Long> {
    List<Forestilling> findByFilmId(Long filmId);
}