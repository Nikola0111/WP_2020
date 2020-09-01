package dto;

import model.Location;

public class ApartmentForFrontDTO {

	
	private String id;
	private String ApartmentType;
	private Location location;
	private String hostUserName;
	private float pricePerNight;
	private String activityStatus;
	
	public ApartmentForFrontDTO() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApartmentType() {
		return ApartmentType;
	}

	public void setApartmentType(String apartmentType) {
		ApartmentType = apartmentType;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getHostUserName() {
		return hostUserName;
	}

	public void setHostUserName(String hostUserName) {
		this.hostUserName = hostUserName;
	}

	public float getPricePerNight() {
		return pricePerNight;
	}

	public void setPricePerNight(float pricePerNight) {
		this.pricePerNight = pricePerNight;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	
}
