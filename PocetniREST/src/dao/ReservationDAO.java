package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import model.Reservation;
import model.User;

@SuppressWarnings("serial")
public class ReservationDAO implements Serializable{
	
	private Map<String, Reservation> reservations = new HashMap<String, Reservation>();
	
	public ReservationDAO() {
		super();
	}

	public ReservationDAO(String contextPath) {
		loadReservations(contextPath);
	}
	
	public Reservation find(String id) {
		if (!reservations.containsKey(id)) {
			return null;
		}
		Reservation reservation = reservations.get(id);
		return reservation;
		
	}
	
	public Collection<Reservation> findAll() {
		return reservations.values();
	}
	

	public ArrayList<Reservation> findAllByGuestId(String guestId) {
		ArrayList<Reservation> allReservations = new ArrayList<Reservation>(reservations.values());
		ArrayList<Reservation> reservationsToSend = new ArrayList<Reservation>();
		
		for (Reservation reservation : allReservations) {
			if (reservation.getGuestId().equals(guestId) && reservation.isDeleted() == false) {
				reservationsToSend.add(reservation);
			}
		}
		
		return reservationsToSend;
	}
	
	public Reservation findById(String id) {
		return reservations.get(id);
	}
	
	public List<Reservation> findReservationsByUsername(String username, UserDAO userDAO) {
		List<Reservation> ret = new ArrayList<Reservation>();
		
		User user = userDAO.findByUsername(username);
		System.out.println(username);
		
		if(user == null) {
			System.out.println("User je null");
			return ret;
		}
		
		System.out.println(user);
		for(Map.Entry<String, Reservation> entry : getReservations().entrySet()) {
			if(entry.getValue().getGuestId().equals(user.getId())) {
				ret.add(entry.getValue());
			}
		}
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public void loadReservations(String path) {
		String filePath = path + "/JSON/reservations.json";
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
			MapType type = factory.constructMapType(HashMap.class, String.class, Reservation.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			reservations = (HashMap<String, Reservation>) objectMapper.readValue(file, type);
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String string = objectMapper.writeValueAsString(reservations);
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
	
	public void saveReservations(String path) {
		String filePath = path + "/JSON/reservations.json";
		File f = new File(filePath);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String string = objectMapper.writeValueAsString(reservations);
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

	public Map<String, Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Map<String, Reservation> reservations) {
		this.reservations = reservations;
	}
}
