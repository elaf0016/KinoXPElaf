package dk.kinoxp.repository;

import dk.kinoxp.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByBookingKode(String bookingKode);

    boolean existsByBookingKode(String bookingKode);

    @Query("select coalesce(sum(r.antalBilletter), 0) from Reservation r where r.forestilling.id = :forestillingId")
    int antalSolgteBilletter(@Param("forestillingId") Long forestillingId);
}
