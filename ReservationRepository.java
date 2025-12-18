package com.restaurant.repository;

import com.restaurant.model.Reservation;
import com.restaurant.model.Reservation.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByReservationDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByCustomerEmail(String email);
}
