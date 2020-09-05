package dto;

import java.util.ArrayList;
import java.util.Date;

import enumeration.ApartmentType;
import model.Amenity;
import model.Location;
import model.Reservation;

public class ApartmentDTO {
	private String id;
	private ApartmentType apartmentType;
	private int numberOfRooms;
	private int numberOfGuests;
	private Location location;
	private ArrayList<Date> datesForRent;
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
		this.datesForRent = new ArrayList<Date>();
		this.commentIds = new ArrayList<String>();
		this.photos = new ArrayList<String>();
		this.amenities = new ArrayList<Amenity>();
		this.reservations = new ArrayList<Reservation>();
		this.grades = new ArrayList<Double>();
	}

	public ApartmentDTO(ApartmentType apartmentType, int numberOfRooms, int numberOfGuests, Location location,
			ArrayList<Date> datesForRent, String host, ArrayList<String> comments, ArrayList<String> photos,
			float pricePerNight, String checkInTime, String checkOutTime, boolean activityStatus,
			ArrayList<Amenity> amenities, ArrayList<Reservation> reservations, boolean deleted, ArrayList<Double> grades) {
		super();
		 
		this.apartmentType = apartmentType;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.location = location;
		this.datesForRent = datesForRent;
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

	public ApartmentType getApartmentType() {
		return apartmentType;
	}

	public void setApartmentType(ApartmentType apartmentType) {
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

	public ArrayList<Date> getDatesForRent() {
		return datesForRent;
	}

	public void setDatesForRent(ArrayList<Date> datesForRent) {
		this.datesForRent = datesForRent;
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
				+ ", numberOfGuests=" + numberOfGuests + ", location=" + location + ", datesForRent=" + datesForRent
				+ ", hostId=" + hostId + ", commentIds=" + commentIds + ", photos=" + photos + ", pricePerNight="
				+ pricePerNight + ", checkInTime=" + checkInTime + ", checkOutTime=" + checkOutTime
				+ ", activityStatus=" + activityStatus + ", amenities=" + amenities + ", reservations=" + reservations
				+ ", deleted=" + deleted + ", grades=" + grades + "]";
	}
	
	
	
	
}
