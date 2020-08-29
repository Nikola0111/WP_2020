package model;

import java.util.UUID;

public class Amenity {
	
	private String id;
	private String caption;
	private String description;
	private String type;
	
	public Amenity() {
		super();
	}

	public Amenity(String caption, String description, String type) {
		super();
		UUID uuid = UUID.randomUUID();
		this.id = uuid.toString();
		this.caption = caption;
		this.description = description;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
