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
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.TypeFactory;
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
	
	public ArrayList<Apartment> findAllByHostIdAndActivityStatus(String hostId, boolean activityStatus) {
		ArrayList<Apartment> allApartments = new ArrayList<Apartment>(apartments.values());
		ArrayList<Apartment> apartmentsByHost = new ArrayList<Apartment>();
		
		for (Apartment apartment : allApartments) {
			if (apartment.getHostId().equals(hostId) && apartment.isActivityStatus() == activityStatus) {
				apartmentsByHost.add(apartment);
			}
		}
		return apartmentsByHost;
		
	}
	
	public Collection<Apartment> findAll() {
		return apartments.values();
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
