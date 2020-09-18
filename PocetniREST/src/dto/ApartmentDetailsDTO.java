package dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Amenity;
import model.Location;
import model.Reservation;

public class ApartmentDetailsDTO {

	private String id;
	private String apartmentType;
	private int numberOfRooms;
	private int numberOfGuests;
	private Location location;
	private ArrayList<Date> startDates;
	private ArrayList<Date> endDates;
	private String userName;
	private ArrayList<String> photos;
	private float pricePerNight;
	private String checkInTime;
	private String checkOutTime;
	private String activityStatus;
	private List<Reservation> reservations;
	private List<Amenity> amenities;
	private List<CommentForOneApartmentDTO> comments;
	
	public ApartmentDetailsDTO() {
		this.startDates = new ArrayList<Date>();
		this.endDates = new ArrayList<Date>();
		this.photos = new ArrayList<String>();
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public List<CommentForOneApartmentDTO> getComments() {
		return comments;
	}

	public void setComments(List<CommentForOneApartmentDTO> comments) {
		this.comments = comments;
	}
	
	

	public List<Amenity> getAmenities() {
		return amenities;
	}

	public void setAmenities(List<Amenity> amenities) {
		this.amenities = amenities;
	}

	@Override
	public String toString() {
		return "ApartmentDetailsDTO [id=" + id + ", apartmentType=" + apartmentType + ", numberOfRooms=" + numberOfRooms
				+ ", numberOfGuests=" + numberOfGuests + ", location=" + location + ", startDates=" + startDates
				+ ", endDates=" + endDates + ", userName=" + userName + ", photos=" + photos + ", pricePerNight="
				+ pricePerNight + ", checkInTime=" + checkInTime + ", checkOutTime=" + checkOutTime
				+ ", activityStatus=" + activityStatus + "]";
	}
	
	
}
