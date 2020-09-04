package com.snj.flightreservation.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snj.flightreservation.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
