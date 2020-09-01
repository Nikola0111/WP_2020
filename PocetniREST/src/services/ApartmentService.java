package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import dao.AmenityDAO;
import dao.ApartmentDAO;
import dao.UserDAO;
import dto.ApartmentForFrontDTO;
import enumeration.ApartmentType;
import dto.ApartmentDTO;
import model.Amenity;
import model.Apartment;
import model.User;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Path("apartments")
public class ApartmentService {

	@Context
	ServletContext context;
	
	public ApartmentService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (context.getAttribute("apartments") == null) {
			context.setAttribute("apartments", new ApartmentDAO(context.getRealPath("")));
		}
		
		if (context.getAttribute("users") == null) {
			context.setAttribute("users", new UserDAO(context.getRealPath("")));
		}
	}
	
	@POST
	@Path("registerApartment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void registerApartment(@Context HttpServletRequest request, ApartmentDTO apartmentDTO) {
		ApartmentDAO apartments = getApartments();
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		System.out.println(loggedUser);

		AmenityDAO amenities = getAmenities();
		int amenityId = amenities.getAmenities().size();
		
		Apartment apartment = new Apartment(apartmentDTO.getApartmentType(), apartmentDTO.getNumberOfRooms(), apartmentDTO.getNumberOfGuests(),
				apartmentDTO.getLocation(), apartmentDTO.getDatesForRent(), apartmentDTO.getPhotos(), apartmentDTO.getPricePerNight(),
				apartmentDTO.getCheckInTime(), apartmentDTO.getCheckOutTime());
		
		for (Amenity temp: apartmentDTO.getAmenities()) {
			temp.setId(amenityId + 1 + "");
			amenities.getAmenities().put(temp.getId(), temp);
			apartment.getAmenityIds().add(temp.getId());
			amenityId++;
		}

		apartment.setHostId(loggedUser == null ? "1" : loggedUser.getId());
		apartment.setId(apartments.getApartments().size() + "");
		
		apartments.getApartments().put(apartment.getId(), apartment);
		saveApartments(apartments);
	}
	
	@GET
	@Path("ActiveApartments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ApartmentForFrontDTO> getAllActiveApartments(@Context HttpServletRequest request) {
		
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		
		ArrayList<ApartmentForFrontDTO> apartmentsToSend = new ArrayList<ApartmentForFrontDTO>();
		ArrayList<Apartment> apartmentsList = new ArrayList<Apartment>(apartments.findAll());
		
		for (Apartment apartment : apartmentsList) {
			if (apartment.isActivityStatus()) {
				User host = users.findById(apartment.getHostId());
				apartmentsToSend.add(convertApartmentToDTO(apartment, host));
			}
		}
		
		return apartmentsToSend;
	}
	
	@GET
	@Path("Apartments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ApartmentForFrontDTO> getAllApartments(@Context HttpServletRequest request) {
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		
		ArrayList<ApartmentForFrontDTO> apartmentsToSend = new ArrayList<ApartmentForFrontDTO>();
		ArrayList<Apartment> apartmentsList = new ArrayList<Apartment>(apartments.findAll());
		
		for (Apartment apartment : apartmentsList) {
			User host = users.findById(apartment.getHostId());
			apartmentsToSend.add(convertApartmentToDTO(apartment, host));
		}
		
		return apartmentsToSend;
	}
	
	@GET
	@Path("HostActiveApartments/{hostId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ApartmentForFrontDTO> getAllActiveApartmentsFromOneHost(@PathParam("hostId") String hostId, @Context HttpServletRequest request) {
		
		ArrayList<ApartmentForFrontDTO> activeApartments = new ArrayList<ApartmentForFrontDTO>();
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		
		User host = users.findById(hostId);
		
		ArrayList<Apartment> activeApartmentsByHost = apartments.findAllByHostIdAndActivityStatus(hostId, true);
		
		for (Apartment apartment : activeApartmentsByHost) {
			ApartmentForFrontDTO dto = convertApartmentToDTO(apartment, host);
			activeApartments.add(dto);
		}
		return activeApartments;	
	}
	
	@GET
	@Path("HostInactiveApartments/{hostId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ApartmentForFrontDTO> getAllInactiveApartmentsFromOneHost(@PathParam("hostId") String hostId, @Context HttpServletRequest request) {
		
		ArrayList<ApartmentForFrontDTO> inactiveApartments = new ArrayList<ApartmentForFrontDTO>();
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		
		User host = users.findById(hostId);
		
		ArrayList<Apartment> inactiveApartmentsByHost = apartments.findAllByHostIdAndActivityStatus(hostId, false);
		
		for (Apartment apartment : inactiveApartmentsByHost) {
			ApartmentForFrontDTO dto = convertApartmentToDTO(apartment, host);
			inactiveApartments.add(dto);
		}
		return inactiveApartments;	
	}
	
	
	public ApartmentForFrontDTO convertApartmentToDTO(Apartment apartment, User host) {
		ApartmentForFrontDTO dto = new ApartmentForFrontDTO();
		
		dto.setId(apartment.getId());
		dto.setApartmentType(convertApartmentType(apartment.getApartmentType()));
		dto.setLocation(apartment.getLocation());
		dto.setHostUserName(host.getUserName());
		dto.setPricePerNight(apartment.getPricePerNight());
		if (apartment.isActivityStatus()) {
			dto.setActivityStatus("Active");
		} else {
			dto.setActivityStatus("Not active");
		}
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
	
	public ApartmentDAO getApartments() {
        ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("apartments");
        return apartments;
    }
	
	public UserDAO getUsers() {
		UserDAO users = (UserDAO) context.getAttribute("users");
		return users;
	}
	
	public void saveApartments(ApartmentDAO apartments) {
		apartments.saveApartments(context.getRealPath(""));
    }
	
	public AmenityDAO getAmenities() {
		AmenityDAO amenities = (AmenityDAO) context.getAttribute("amenities");
        return amenities;
    }
	
	public void saveAmenities(AmenityDAO amenities) {
		amenities.saveAmenities(context.getRealPath(""));
    }

}
