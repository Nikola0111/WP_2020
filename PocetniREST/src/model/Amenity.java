package model;

import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
public class Amenity implements Serializable{
	
	private String id;
	private String caption;
	private String description;
	private String type;
	private boolean deleted;
	
	public Amenity() {
		super();
	}

	public Amenity(String caption, String description, String type, boolean deleted) {
		super();
		UUID uuid = UUID.randomUUID();
		this.id = uuid.toString();
		this.caption = caption;
		this.description = description;
		this.type = type;
		this.deleted = deleted;
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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "\nAmenity [id=" + id + ", caption=" + caption + ", description=" + description + ", type=" + type + "]";
	}
	
	
}
