package com.snj.flightreservation.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snj.flightreservation.dto.ReservationRequest;
import com.snj.flightreservation.entities.Flight;
import com.snj.flightreservation.entities.Passenger;
import com.snj.flightreservation.entities.Reservation;
import com.snj.flightreservation.repos.FlightRepository;
import com.snj.flightreservation.repos.PassengerRepository;
import com.snj.flightreservation.repos.ReservationRepository;
import com.snj.flightreservation.util.EmailUtil;
import com.snj.flightreservation.util.PDFGenerator;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Value("${com.snj.flightreservation.itinerary.dirpath}")
	private String ITINERARY_DIR;

	@Autowired
	FlightRepository flightRepository;

	@Autowired
	PassengerRepository passengerRepository;

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	PDFGenerator pdfGenerator;

	@Autowired
	EmailUtil emailUtil;

	private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

	@Override
	@Transactional     
	// either whole thing will be happen or nothing will happen i.e either whole thing get execute well and store in database and get mail either nothing will happen.
	public Reservation bookFlight(ReservationRequest request) {

		LOGGER.info("Inside bookFlight()");
		// Make Payment

		Long flightId = request.getFlightId();
		LOGGER.info("Fetching  flight for flight id:" + flightId);
		Flight flight = flightRepository.findOne(flightId);

		Passenger passenger = new Passenger();
		passenger.setFirstName(request.getPassengerFirstName());
		passenger.setLastName(request.getPassengerLastName());
		passenger.setPhone(request.getPassengerPhone());
		passenger.setEmail(request.getPassengerEmail());
		LOGGER.info("Saving the passenger:" + passenger);
		Passenger savedPassenger = passengerRepository.save(passenger);

		Reservation reservation = new Reservation();
		reservation.setFlight(flight);
		reservation.setPassenger(savedPassenger);
		reservation.setCheckedIn(false);

		LOGGER.info("Saving the reservation:" + reservation);
		Reservation savedReservation = reservationRepository.save(reservation);

		String filePath = ITINERARY_DIR + savedReservation.getId()
				+ ".pdf";
		LOGGER.info("Generating  the itinerary");
		pdfGenerator.generateItinerary(savedReservation, filePath);
		LOGGER.info("Emailing the Itinerary");
		emailUtil.sendItinerary(passenger.getEmail(), filePath);

		return savedReservation;
	}

}
