package model;

import java.util.Date;
import java.util.UUID;

import enumeration.ReservationStatus;

public class Reservation {

	private String id;
	private String apartmentId;
	private Date startingDate;
	private int rentalDuration;
	private float fullPrice;
	private String optionalMessage;
	private String guestId;
	private ReservationStatus reservationStatus;
	
	public Reservation() {
		super();
	}

	public Reservation(String apartmentId, Date startingDate, int rentalDuration, float fullPrice,
			String optionalMessage, String guestId, ReservationStatus reservationStatus) {
		super();
		UUID uuid = UUID.randomUUID();
		this.id = uuid.toString();
		this.apartmentId = apartmentId;
		this.startingDate = startingDate;
		this.rentalDuration = rentalDuration;
		this.fullPrice = fullPrice;
		this.optionalMessage = optionalMessage;
		this.guestId = guestId;
		this.reservationStatus = reservationStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public int getRentalDuration() {
		return rentalDuration;
	}

	public void setRentalDuration(int rentalDuration) {
		this.rentalDuration = rentalDuration;
	}

	public float getFullPrice() {
		return fullPrice;
	}

	public void setFullPrice(float fullPrice) {
		this.fullPrice = fullPrice;
	}

	public String getOptionalMessage() {
		return optionalMessage;
	}

	public void setOptionalMessage(String optionalMessage) {
		this.optionalMessage = optionalMessage;
	}

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	public ReservationStatus getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(ReservationStatus reservationStatus) {
		this.reservationStatus = reservationStatus;
	}
	
	
	
}
