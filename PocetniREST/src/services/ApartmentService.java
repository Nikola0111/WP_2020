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
import dao.CommentDAO;
import dao.ReservationDAO;
import dao.UserDAO;
import dto.ApartmentForFrontDTO;
import enumeration.ApartmentType;
import dto.ApartmentDTO;
import dto.FilterApartmentDTO;
import dto.SearchApartmentDTO;
import enumeration.UserRole;
import model.Amenity;
import model.Apartment;
import model.User;

import java.util.List;

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
		
		if (context.getAttribute("amenities") == null) {
			System.out.println("Inicijalizaovao amenities");
			context.setAttribute("amenities", new AmenityDAO(context.getRealPath("")));
		}
		
		if (context.getAttribute("apartments") == null) {
			context.setAttribute("apartments", new ApartmentDAO(context.getRealPath("")));
		}
		
		if (context.getAttribute("comments") == null) {
			context.setAttribute("comments", new CommentDAO(context.getRealPath("")));
		}
		
		if (context.getAttribute("reservations") == null) {
			context.setAttribute("reservations", new ReservationDAO(context.getRealPath("")));
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
		
		int amenityId = 0;
		
		try {
			amenityId = amenities.getAmenities().size();
		} catch(Exception e) {
			System.out.println("Nalazi se 0 amenitija u fajlu");
		}
		
		Apartment apartment = new Apartment(apartmentDTO.getApartmentType(), apartmentDTO.getNumberOfRooms(), apartmentDTO.getNumberOfGuests(),
				apartmentDTO.getLocation(), apartmentDTO.getDatesForRent(), apartmentDTO.getPhotos(), apartmentDTO.getPricePerNight(),
				apartmentDTO.getCheckInTime(), apartmentDTO.getCheckOutTime());
		
		System.out.println(apartmentDTO);
		
		for (Amenity temp: apartmentDTO.getAmenities()) {
			temp.setId(amenityId + "");
			System.out.println(temp);
			amenities.getAmenities().put(temp.getId(), temp);
			apartment.getAmenityIds().add(temp.getId());
			amenityId++;
		}

		apartment.setHostId(loggedUser == null ? "1" : loggedUser.getId());
		apartment.setId(apartments.getApartments().size() + "");
		
		apartments.getApartments().put(apartment.getId(), apartment);
		saveApartments(apartments);
		
		////////ovo se treba obrisati kad se naprave amenitiji/////////
		saveAmenities(amenities);
		///////////////////////////////////////////////////////////////
	}
	
	@POST
	@Path("searchApartment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Apartment> searchApartment(@Context HttpServletRequest request, SearchApartmentDTO apartmentDTO) {
		ApartmentDAO apartmentDAO = getApartments();
		
		return apartmentDAO.findBySearchApartmentDTOFields(apartmentDTO);
	}
	
	@POST
	@Path("filterApartments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Apartment> filterApartments(@Context HttpServletRequest request, FilterApartmentDTO apartmentDTO) {
		ApartmentDAO apartmentDAO = getApartments();
		//Koristnik filtrira apartmane samo po tipu i sadrzaju, jer im se uvek prikazuju samo aktivni apartmani, a admin moze i po statusu, mozda i host?
		List<Apartment> ret =  apartmentDAO.findByFilterApartmentDTOFields(apartmentDTO);
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		
		if(loggedUser.getUserRole() != UserRole.GUEST) {
			for(Apartment temp : ret) {
				if(temp.isActivityStatus() != apartmentDTO.isActivityStatus()) {
					ret.remove(temp);
				}
			}
		}
		
		return ret;
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
	@Path("HostApartments/{hostId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ApartmentForFrontDTO> getAllApartmentsFromOneHost(@PathParam("hostId") String hostId, @Context HttpServletRequest request) {
		
		ArrayList<ApartmentForFrontDTO> activeApartments = new ArrayList<ApartmentForFrontDTO>();
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		
		User host = users.findById(hostId);
		
		ArrayList<Apartment> activeApartmentsByHost = apartments.findAllByHostId(hostId);
		System.out.println(activeApartmentsByHost);
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
	
	@GET
	@Path("deleteApartment/{apartmentID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteApartment(@PathParam("apartmentID") String apartmentID, @Context HttpServletRequest request) {
		
		ApartmentDAO apartmentDAO = getApartments();
		if(apartmentDAO.getApartments().containsKey(apartmentID)) {
			apartmentDAO.getApartments().get(apartmentID).setDeleted(true);
			saveApartments(apartmentDAO);
			
			return true;
		}
		
		return false;
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
