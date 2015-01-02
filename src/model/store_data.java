package model;

import android.graphics.Bitmap;

public class store_data {

	public String typeOfBike, price, description, phone, email, bike_id;
	public Bitmap bikeImg;

	

	public store_data(String typeOfBike, String price, String description,
			String phone, String email, String bike_id, Bitmap bikeImg) {
		super();
		this.typeOfBike = typeOfBike;
		this.price = price;
		this.description = description;
		this.phone = phone;
		this.email = email;
		this.bike_id = bike_id;
		this.bikeImg = bikeImg;
	}

	public String getBike_id() {
		return bike_id;
	}

	public void setBike_id(String bike_id) {
		this.bike_id = bike_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTypeOfBike() {
		return typeOfBike;
	}

	public void setTypeOfBike(String typeOfBike) {
		this.typeOfBike = typeOfBike;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Bitmap getBikeImg() {
		return bikeImg;
	}

	public void setBikeImg(Bitmap bikeImg) {
		this.bikeImg = bikeImg;
	}

}
