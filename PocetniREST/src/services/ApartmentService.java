package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.AmenityDAO;
import dao.ApartmentDAO;
import dto.ApartmentDTO;
import dto.FilterApartmentDTO;
import dto.SearchApartmentDTO;
import enumeration.UserRole;
import model.Amenity;
import model.Apartment;
import model.User;

import java.util.List;

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
		if (context.getAttribute("amenities") == null) {
			System.out.println("Inicijalizaovao amenities");
			context.setAttribute("amenities", new AmenityDAO(context.getRealPath("")));
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
	
	public ApartmentDAO getApartments() {
        ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("apartments");
        return apartments;
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
