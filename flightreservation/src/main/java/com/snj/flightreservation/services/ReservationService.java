package com.snj.flightreservation.services;

import com.snj.flightreservation.dto.ReservationRequest;
import com.snj.flightreservation.entities.Reservation;

public interface ReservationService {
	
	public Reservation bookFlight(ReservationRequest request);

}
