package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.TypeFactory;

import dto.FilterApartmentDTO;
import dto.SearchApartmentDTO;

import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.core.JsonGenerator;

import model.Apartment;

public class ApartmentDAO {

	private Map<String, Apartment> apartments = new HashMap<String, Apartment>();

	public ApartmentDAO() {
		super();
	}
	
	public ApartmentDAO(String contextPath) {
		loadApartments(contextPath);
	}
	
	public Map<String, Apartment> getApartments() {
		return apartments;
	}
	
	public void setApartments(Map<String, Apartment> apartments) {
		this.apartments = apartments;
	}
	
	public Apartment find(String id) {
		if (!apartments.containsKey(id)) {
			return null;
		}
		Apartment apartment = apartments.get(id);
		return apartment;
		
	}
	
	public Collection<Apartment> findAll() {
		return apartments.values();
	}
	
	public List<Apartment> findBySearchApartmentDTOFields(SearchApartmentDTO apartment) {
		List<Apartment> ret = new ArrayList<Apartment>();
		
		for(Map.Entry<String, Apartment> entry : apartments.entrySet()) {
			boolean condition = false;
			boolean initialized = false;
			
			if(apartment.getStartDate() != null) {
				condition = entry.getValue().getCheckInTime().before(apartment.getStartDate());
				initialized = true;
			}
			
			if(apartment.getEndDate() != null) {
				if(initialized) {
					condition = condition && entry.getValue().getCheckOutTime().after(apartment.getEndDate());
				} else {
					condition = entry.getValue().getCheckOutTime().after(apartment.getEndDate());
					initialized = true;
				}
			}
			
			if(apartment.getNumberOfPeople() != -1) {
				if(initialized) {
					condition = condition && entry.getValue().getNumberOfGuests() >= apartment.getNumberOfPeople();
				} else {
					condition = entry.getValue().getNumberOfGuests() >= apartment.getNumberOfPeople();
					initialized = true;
				}
			}
			
			if(apartment.getNumberOfRooms() != -1) {
				if(initialized) {
					condition = condition && entry.getValue().getNumberOfRooms() >= apartment.getNumberOfRooms();
				} else {
					condition = entry.getValue().getNumberOfRooms() >= apartment.getNumberOfRooms();
					initialized = true;
				}	
			}
			
			if(apartment.getPrice() != -1) {
				if(initialized) {
					condition = condition && entry.getValue().getPricePerNight() <= apartment.getPrice();
				} else {
					condition = entry.getValue().getPricePerNight() <= apartment.getPrice();
					initialized = true;
				}
			}
			
			if(!apartment.getCity().equals("")) {
				if(initialized) {
					condition = condition && entry.getValue().getLocation().getAddress().getCity().equals(apartment.getCity());
				} else {
					condition = entry.getValue().getLocation().getAddress().getCity().equals(apartment.getCity());
					initialized = true;
				}
			}
			
			if(!apartment.getCountry().equals("")) {
				if(initialized) {
					condition = condition && entry.getValue().getLocation().getAddress().getCountry().equals(apartment.getCountry());
				} else {
					condition = entry.getValue().getLocation().getAddress().getCountry().equals(apartment.getCountry());
					initialized = true;
				}
			}
			
			if(condition) {
				ret.add(entry.getValue());
			}
		}
		
		return ret;
	}
	
	public List<Apartment> findByFilterApartmentDTOFields(FilterApartmentDTO apartmentDTO) {
		List<Apartment> ret = new ArrayList<Apartment>();
		
		for(Map.Entry<String, Apartment> entry : apartments.entrySet()) {
			boolean condition = false;
			boolean initialized = false;
			
			
			if(apartmentDTO.getApartmentType() != null) {
				initialized = true;
				condition = entry.getValue().getApartmentType() == apartmentDTO.getApartmentType();
			}
			
			try {
				if(apartmentDTO.getAmenityIds().size() != 0) {
					if(initialized) {
						condition = condition && checkAmenities(entry.getValue(), apartmentDTO);
					} else {
						condition = checkAmenities(entry.getValue(), apartmentDTO);
					}
				}
			} catch(Exception e) {
				
			}
			
			if(condition) {
				ret.add(entry.getValue());
			}
		}
		
		return ret;
	}
	
	private boolean checkAmenities(Apartment entry, FilterApartmentDTO apartmentDTO) {
		boolean contains = false;
		for(String id: apartmentDTO.getAmenityIds()) {
			if(!entry.getAmenityIds().contains(id)) {
				contains = false;
				break;
			} else {
				contains = true;
			}
		}
		
		return contains;
	}
	
	@SuppressWarnings("unchecked")
	public void loadApartments(String path) {
		String filePath = path + "/JSON/apartments.json";
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(filePath);
			in = new BufferedReader(new FileReader(file));
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibilityChecker(
					VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, String.class, Apartment.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			apartments = (HashMap<String, Apartment>) objectMapper.readValue(file, type);
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String string = objectMapper.writeValueAsString(apartments);
				fileWriter.write(string);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void saveApartments(String path) {
		String filePath = path + "/JSON/apartments.json";
		File f = new File(filePath);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String string = objectMapper.writeValueAsString(apartments);
			fileWriter.write(string);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}
