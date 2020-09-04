package com.snj.flightreservation.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snj.flightreservation.entities.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

}
