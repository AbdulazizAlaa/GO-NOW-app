package model;

import android.graphics.Bitmap;

public class messages_data {
	private String id, chat_id, first_id, second_id, name, text;
	private boolean isHost;
	private Bitmap profilePic;

	public messages_data(String id, String chat_id, String first_id,
			String second_id, String name, String text, boolean isHost,
			Bitmap profilePic) {
		super();
		this.id = id;
		this.chat_id = chat_id;
		this.first_id = first_id;
		this.second_id = second_id;
		this.name = name;
		this.text = text;
		this.isHost = isHost;
		this.profilePic = profilePic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChat_id() {
		return chat_id;
	}

	public void setChat_id(String chat_id) {
		this.chat_id = chat_id;
	}

	public String getFirst_id() {
		return first_id;
	}

	public void setFirst_id(String first_id) {
		this.first_id = first_id;
	}

	public String getSecond_id() {
		return second_id;
	}

	public void setSecond_id(String second_id) {
		this.second_id = second_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	public Bitmap getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(Bitmap profilePic) {
		this.profilePic = profilePic;
	}

}
