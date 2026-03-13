package dk.kinoxp.repository;

import dk.kinoxp.entity.Sal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalRepository extends JpaRepository<Sal, Long> {
}