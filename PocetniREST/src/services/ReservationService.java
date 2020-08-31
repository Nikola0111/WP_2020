package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.ApartmentDAO;
import dao.ReservationDAO;
import dao.UserDAO;
import dto.ReservationDTO;
import enumeration.ApartmentType;
import enumeration.ReservationStatus;
import model.Apartment;
import model.Reservation;
import model.User;

@Path("Reservation")
public class ReservationService {

	@Context
	ServletContext context;
	
	public ReservationService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (context.getAttribute("reservations") == null) {
			context.setAttribute("reservations", new ReservationDAO(context.getRealPath("")));
		}
		
		if (context.getAttribute("users") == null) {
			context.setAttribute("users", new UserDAO(context.getRealPath("")));
		}
		
		if (context.getAttribute("apartments") == null) {
			context.setAttribute("apartments", new ApartmentDAO(context.getRealPath("")));
		}
	}
	
	@GET
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ReservationDTO> getAllReservations(@Context HttpServletRequest request) {
		
		ArrayList<ReservationDTO> reservationsToSend = new ArrayList<ReservationDTO>();
		
		ReservationDAO reservations = getReservations();
		UserDAO users = getUsers();
		ApartmentDAO apartments = getApartments();
		
		ArrayList<Reservation> reservationsList = new ArrayList<Reservation>(reservations.findAll());
		
		for (Reservation reservation : reservationsList) {
			Apartment apartment = apartments.find(reservation.getApartmentId());
			ReservationDTO dto = convertReservationToDTO(reservation, apartment, users.findById(apartment.getHostId()), users.findById(reservation.getGuestId()));
			reservationsToSend.add(dto);
		}
		return reservationsToSend;
	}
	
	public ReservationDAO getReservations() {
		ReservationDAO reservations = (ReservationDAO) context.getAttribute("reservations");
		return reservations;
	}
	
	public UserDAO getUsers() {
		UserDAO users = (UserDAO) context.getAttribute("users");
		return users;
	}
	
	public ApartmentDAO getApartments() {
		ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("apartments");
		return apartments;
	}
	
	public void saveReservations(ReservationDAO reservations) {
		reservations.saveReservations(context.getRealPath(""));
	}
	
	public ReservationDTO convertReservationToDTO(Reservation reservation, Apartment apartment, User host, User guest) {
		
		ReservationDTO dto = new ReservationDTO();
		
		dto.setId(reservation.getId());
		dto.setApartmentType(convertApartmentType(apartment.getApartmentType()));
		dto.setNumberOfRooms(apartment.getNumberOfRooms());
		dto.setLocation(apartment.getLocation());
		dto.setHostUserName(host.getUserName());
		dto.setGuestUserName(guest.getUserName());
		dto.setStartingDate(reservation.getStartingDate());
		dto.setRentalDuration(reservation.getRentalDuration());
		dto.setFullPrice(reservation.getFullPrice());
		dto.setOptionalMessage(reservation.getOptionalMessage());
		dto.setReservationStatus(convertReservationStatus(reservation.getReservationStatus()));
		
		return dto;
	}
	
	public String convertApartmentType(ApartmentType type) {
		
		String convertedType = "";
		
		if (type.equals(ApartmentType.ONE_ROOM)) {
			convertedType = "One room";
		} else if (type.equals(ApartmentType.TWO_ROOMS)) {
			convertedType = "Two rooms";
		} else if (type.equals(ApartmentType.THREE_ROOMS)) {
			convertedType = "Three rooms";
		} else {
			convertedType = "More than three rooms";
		}
		
		return convertedType;
	}
	
	public String convertReservationStatus(ReservationStatus status) {
		
		String convertedStatus = "";
		
		if (status.equals(ReservationStatus.CREATED)) {
			convertedStatus = "Created";
		} else if (status.equals(ReservationStatus.CANCELED)) {
			convertedStatus = "Canceled";
		} else if (status.equals(ReservationStatus.DECLINED)) {
			convertedStatus = "Declined";
		} else if (status.equals(ReservationStatus.ACCEPTED)) {
			convertedStatus = "Accepted";
		} else {
			convertedStatus = "Finished";
		}
		
		return convertedStatus;
	}
	
}
