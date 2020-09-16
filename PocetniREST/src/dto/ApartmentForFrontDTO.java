package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import model.Location;

@SuppressWarnings("serial")
public class ApartmentForFrontDTO implements Serializable{

	
	private String id;
	private String apartmentType;
	private Location location;
	private String hostUserName;
	private float pricePerNight;
	private String activityStatus;
	private ArrayList<Date> startDates;
	private ArrayList<Date> endDates;

	public ApartmentForFrontDTO() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApartmentType() {
		return apartmentType;
	}

	public void setApartmentType(String apartmentType) {
		this.apartmentType = apartmentType;
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


	public ArrayList<Date> getStartDates() {
		return startDates;
	}

	public void setStartDates(ArrayList<Date> startDates) {
		this.startDates = startDates;
	}

	public ArrayList<Date> getEndDates() {
		return endDates;
	}

	public void setEndDates(ArrayList<Date> endDates) {
		this.endDates = endDates;
	}

	@Override
	public String toString() {
		return "ApartmentForFrontDTO [id=" + id + ", apartmentType=" + apartmentType + ", location=" + location
				+ ", hostUserName=" + hostUserName + ", pricePerNight=" + pricePerNight + ", activityStatus="
				+ activityStatus + "]";
	}
	
	
}
