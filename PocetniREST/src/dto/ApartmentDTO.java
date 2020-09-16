package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import enumeration.ApartmentType;
import model.Amenity;
import model.Location;
import model.OccupationDate;
import model.Reservation;

@SuppressWarnings("serial")
public class ApartmentDTO implements Serializable{
	private String id;
	private String apartmentType;
	private int numberOfRooms;
	private int numberOfGuests;
	private Location location;
	private ArrayList<Date> startDates;
	private ArrayList<Date> endDates;
	private String hostId;
	private ArrayList<String> commentIds;
	private ArrayList<String> photos;
	private float pricePerNight;
	private String checkInTime;
	private String checkOutTime;
	private boolean activityStatus;
	private ArrayList<Amenity> amenities;
	private ArrayList<Reservation> reservations;
	private boolean deleted;
	private ArrayList<Double> grades;
	
	public ApartmentDTO() {
		super();
		this.startDates = new ArrayList<Date>();
		this.endDates = new ArrayList<Date>();
		this.commentIds = new ArrayList<String>();
		this.photos = new ArrayList<String>();
		this.amenities = new ArrayList<Amenity>();
		this.reservations = new ArrayList<Reservation>();
		this.grades = new ArrayList<Double>();
	}

	public ApartmentDTO(String apartmentType, int numberOfRooms, int numberOfGuests, Location location,
			ArrayList<Date> startDates, ArrayList<Date> endDates, String host, ArrayList<String> comments, ArrayList<String> photos,
			float pricePerNight, String checkInTime, String checkOutTime, boolean activityStatus,
			ArrayList<Amenity> amenities, ArrayList<Reservation> reservations, boolean deleted, ArrayList<Double> grades) {
		super();
		 
		this.apartmentType = apartmentType;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.location = location;
		this.startDates = startDates;
		this.endDates = endDates;
		this.hostId = host;
		this.commentIds = comments;
		this.photos = photos;
		this.pricePerNight = pricePerNight;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.activityStatus = activityStatus;
		this.amenities = amenities;
		this.reservations = reservations;
		this.deleted = deleted;
		this.grades = grades;
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

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String host) {
		this.hostId = host;
	}

	public ArrayList<String> getCommentIds() {
		return commentIds;
	}

	public void setCommentIds(ArrayList<String> comments) {
		this.commentIds = comments;
	}

	public ArrayList<String> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<String> photos) {
		this.photos = photos;
	}

	public float getPricePerNight() {
		return pricePerNight;
	}

	public void setPricePerNight(float pricePerNight) {
		this.pricePerNight = pricePerNight;
	}

	public String getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public boolean isActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(boolean activityStatus) {
		this.activityStatus = activityStatus;
	}

	public ArrayList<Amenity> getAmenities() {
		return amenities;
	}

	public void setAmenities(ArrayList<Amenity> amenities) {
		this.amenities = amenities;
	}

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public ArrayList<Double> getGrades() {
		return grades;
	}

	public void setGrades(ArrayList<Double> grades) {
		this.grades = grades;
	}

	@Override
	public String toString() {
		return "ApartmentDTO [id=" + id + ", apartmentType=" + apartmentType + ", numberOfRooms=" + numberOfRooms
				+ ", numberOfGuests=" + numberOfGuests + ", location=" + location + ", datesForRent="
				+ ", hostId=" + hostId + ", commentIds=" + commentIds + ", photos=" + photos + ", pricePerNight="
				+ pricePerNight + ", checkInTime=" + checkInTime + ", checkOutTime=" + checkOutTime
				+ ", activityStatus=" + activityStatus + ", amenities=" + amenities + ", reservations=" + reservations
				+ ", deleted=" + deleted + ", grades=" + grades + "]";
	}
	
	
	
	
}
