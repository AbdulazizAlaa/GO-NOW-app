package model;

public class people_data {
	private String user_id, username, email, biketype, friends;
	private int distance, maxSpeed;
	private String profilePic;
	public people_data(String user_id, String username, String email,
			String biketype, String friends, int distance, int maxSpeed,
			String profilePic) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.email = email;
		this.biketype = biketype;
		this.friends = friends;
		this.distance = distance;
		this.maxSpeed = maxSpeed;
		this.profilePic = profilePic;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBiketype() {
		return biketype;
	}
	public void setBiketype(String biketype) {
		this.biketype = biketype;
	}
	public String getFriends() {
		return friends;
	}
	public void setFriends(String friends) {
		this.friends = friends;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public String getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}


}
