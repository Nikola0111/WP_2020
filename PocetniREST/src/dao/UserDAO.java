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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import model.User;

public class UserDAO {
	
	private Map<String, User> users = new HashMap<String, User>();

	public UserDAO() {
		super();
	}
	
	public UserDAO(String contextPath) {
		loadUsers(contextPath);
	}
	
	public Map<String, User> getUsers() {
		return users;
	}
	
	public void setUsers(Map<String, User> users) {
		this.users = users;
	}
	
	public User findById(String id) {
		if (!users.containsKey(id)) {
			return null;
		}
		User user = users.get(id);
		return user;
	}
	
	public User findByUsernameAndPassword(String userName, String password) {
		if (users.values().size() == 0) {
			return null;
		}
		ArrayList<User> usersList = new ArrayList<User>(users.values());
		for (User currentUser : usersList) {
			if (currentUser.getUserName().equals(userName)) {
				if (!currentUser.getPassword().equals(password)) {
					return null;
				} else {
					return currentUser;
				}
			}
		}
		return null;
	}
	
	public User findByUsername(String userName) {
		if (users.values().size() == 0) {
			return null;
		}
		ArrayList<User> usersList = new ArrayList<User>(users.values());
		for (User currentUser : usersList) {
			if (currentUser.getUserName().equals(userName)) {
				return currentUser;
			}
		}
		return null;
	}
	
	public Collection<User> findAll() {
		return users.values();
	}
	
	@SuppressWarnings("unchecked")
	public void loadUsers(String path) {
		String filePath = path + "/JSON/users.json";
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
			MapType type = factory.constructMapType(HashMap.class, String.class, User.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			users = (HashMap<String, User>) objectMapper.readValue(file, type);
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String string = objectMapper.writeValueAsString(users);
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
	
	public void saveUsers(String path) {
		String filePath = path + "/JSON/users.json";
		File f = new File(filePath);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String string = objectMapper.writeValueAsString(users);
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
