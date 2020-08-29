package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import enumeration.ApartmentType;

public class Apartment {

	private String id;
	private ApartmentType apartmentType;
	private int numberOfRooms;
	private int numberOfGuests;
	private Location location;
	private ArrayList<Date> datesForRent;
	private User host;
	private ArrayList<Comment> comments;
	private ArrayList<String> photos;
	private float pricePerNight;
	private Date checkInTime;
	private Date checkOutTime;
	private boolean activityStatus;
	private ArrayList<Amenity> amenities;
	private ArrayList<Reservation> reservations;
	private boolean deleted;
	
	public Apartment() {
		super();
		this.datesForRent = new ArrayList<Date>();
		this.comments = new ArrayList<Comment>();
		this.photos = new ArrayList<String>();
		this.amenities = new ArrayList<Amenity>();
		this.reservations = new ArrayList<Reservation>();
	}

	public Apartment(ApartmentType apartmentType, int numberOfRooms, int numberOfGuests, Location location,
			ArrayList<Date> datesForRent, User host, ArrayList<Comment> comments, ArrayList<String> photos,
			float pricePerNight, Date checkInTime, Date checkOutTime, boolean activityStatus,
			ArrayList<Amenity> amenities, ArrayList<Reservation> reservations, boolean deleted) {
		super();
		 
		this.apartmentType = apartmentType;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.location = location;
		this.datesForRent = datesForRent;
		this.host = host;
		this.comments = comments;
		this.photos = photos;
		this.pricePerNight = pricePerNight;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.activityStatus = activityStatus;
		this.amenities = amenities;
		this.reservations = reservations;
		this.deleted = deleted;
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

	public User getHost() {
		return host;
	}

	public void setHost(User host) {
		this.host = host;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
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

	public Date getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
	}

	public Date getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(Date checkOutTime) {
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
	
	
	
	
	
}