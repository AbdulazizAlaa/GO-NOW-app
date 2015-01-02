package model;

import android.graphics.Bitmap;

public class chat_data {
	private String user_id, username, email, biketype, friends, numMessages,
			chat_id;
	private int distance, maxSpeed;
	private Bitmap profilePic;

	public chat_data(String user_id, String username, String email,
			String biketype, String friends, String numMessages,
			String chat_id, int distance, int maxSpeed, Bitmap profilePic) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.email = email;
		this.biketype = biketype;
		this.friends = friends;
		this.numMessages = numMessages;
		this.chat_id = chat_id;
		this.distance = distance;
		this.maxSpeed = maxSpeed;
		this.profilePic = profilePic;
	}

	public String getChat_id() {
		return chat_id;
	}

	public void setChat_id(String chat_id) {
		this.chat_id = chat_id;
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

	public String getNumMessages() {
		return numMessages;
	}

	public void setNumMessages(String numMessages) {
		this.numMessages = numMessages;
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

	public Bitmap getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(Bitmap profilePic) {
		this.profilePic = profilePic;
	}

}
