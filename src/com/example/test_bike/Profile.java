package com.example.test_bike;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.viewpagerindicator.TitlePageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import TabFragments.ProfileRidesFragment;
import TabFragments.SessionFragment;
import adapters.TabsPagerAdapter;
import beans.AppController;
import beans.serviceHandler;
import beans.values;
import model.people_data;

public class Profile extends FragmentActivity implements OnClickListener,
		android.content.DialogInterface.OnClickListener {

	private TextView nameTV, locationTV, motoTV, distanceTV, maxSpeedTV,
			numFriendsTV;
	private ImageView profilePictureIV, addRideB, addFriendB;

	private Bundle b;
	private String username, email, user_id, caller, id, userFriends, chat_id,
			file_path;

	people_data profileData;

	// tabs
	ViewPager pager;
	TabsPagerAdapter pagerAdapter;
	ActionBar actionBar;

	ArrayList<Fragment> list;

	String[] tabs = { "My Rides", "Session Summary" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		actionBar = getActionBar();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setLogo(R.drawable.profile_icon);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(R.drawable.profile_icon);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();

				username = b.getString(values.TAG_USERNAME);
				// email = b.getString(values.TAG_EMAIL);
				user_id = b.getString(values.TAG_PAGE_USER_ID);
				// caller = b.getString(values.TAG_CALLER);
				id = b.getString(values.TAG_ID);
				userFriends = b.getString(values.TAG_USER_FRIENDS);
				file_path = b.getString(values.TAG_PROFILE_IMG);

			}
		}

		chat_id = "";

		// ** action bar tabs		
		
		pager = (ViewPager) findViewById(R.id.profile_pager);

		list = new ArrayList<Fragment>();
		// list.add(new RidesFragment());
		// list.add(new SessionFragment());

		// pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(),
		// list,tabs);

		// pager.setAdapter(pagerAdapter);

		// TitlePageIndicator titleIndicator = (TitlePageIndicator)
		// findViewById(R.id.profile_titles);
		// titleIndicator.setViewPager(pager);

		// ** end of action bar tabs

		nameTV = (TextView) findViewById(R.id.profile_ProfilenameTv);
		locationTV = (TextView) findViewById(R.id.profile_LocTv);
		motoTV = (TextView) findViewById(R.id.profile_motoBeanTv);
		distanceTV = (TextView) findViewById(R.id.Profile_NumberOfDistanceTv);
		maxSpeedTV = (TextView) findViewById(R.id.profile_NumberOfMaxSpeedTv);
		numFriendsTV = (TextView) findViewById(R.id.profile_NumberOfFriendsTv);

		addRideB = (ImageView) findViewById(R.id.profile_add_ridesB);
		profilePictureIV = (ImageView) findViewById(R.id.profile_ProfilePicIV);
		addFriendB = (ImageView) findViewById(R.id.profile_addFriend_IV);

		addRideB.setOnClickListener(this);
		addFriendB.setOnClickListener(this);

		nameTV.setText(username);

		new fetch_people_task().execute("http://" + values.IP
				+ "/cycleapp/fetchprofile.php", user_id, id);

		if (user_id.equals(id)) {
			addFriendB.setVisibility(View.GONE);
			addRideB.setVisibility(View.VISIBLE);
		} else {
			if (userFriends.contains(user_id)) {
				addFriendB.setVisibility(View.GONE);
			} else {
				addFriendB.setVisibility(View.VISIBLE);
			}

			addRideB.setVisibility(View.GONE);
		}
		
		if(file_path!= null && !file_path.equals("")){
			ImageLoader loader = AppController.getInstance().getImageLoader();


			loader.get(file_path, new ImageListener() {

				@Override
				public void onErrorResponse(VolleyError e) {
					// TODO Auto-generated method stub
					Log.i("ha", "image:" + e.getMessage());
				}

				@Override
				public void onResponse(ImageContainer image, boolean arg1) {
					// TODO Auto-generated method stub
					if (image.getBitmap() != null) {
						profilePictureIV.setImageBitmap(image.getBitmap());
					} else
						Log.i("ha", "no image");
				}
			});
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		switch (v.getId()) {
		case R.id.profile_add_ridesB:
			i = new Intent(Profile.this, CreateEvent.class);
			i.putExtra(values.TAG_ID, user_id);
			i.putExtra(values.TAG_PAGE_PAGE_ID, "user");
			startActivity(i);
			break;
		case R.id.profile_addFriend_IV:
			addFriendB.setVisibility(View.GONE);
			new add_friend_task().execute("http://" + values.IP
					+ "/cycleapp/addfriend.php", id, user_id);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		if (user_id.equals(id)) {
			if (which == 0) {
				// edit profile
			}
		} else {
			if (which == 0) {
				// send a message

				Intent i = new Intent(Profile.this, Messages.class);

				i.putExtra(values.TAG_MESSAGES_FIRST_ID, id);
				i.putExtra(values.TAG_USERNAME, username);
				i.putExtra(values.TAG_MESSAGES_SECOND_ID, user_id);
				i.putExtra(values.TAG_MESSAGES_NAME, profileData.getUsername());
				i.putExtra(values.TAG_MESSAGES_CHAT_ID, chat_id);
				Log.i("ha", chat_id);
				startActivity(i);

			}
		}

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.profile_pop_menuB) {
			AlertDialog.Builder b = new AlertDialog.Builder(Profile.this);

			if (user_id.equals(id)) {
				CharSequence[] items = { "Edit Profile" };
				b.setItems(items, this);
			} else {
				CharSequence[] items = { "Send a Message" };
				b.setItems(items, this);
			}

			AlertDialog d = b.create();
			d.show();
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			// NavUtils.navigateUpFromSameTask(this);
		}
		return super.onOptionsItemSelected(item);
	}

	private class fetch_people_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			String user_id, username, email, biketype, friends;
			int distance, maxSpeed;
			// Bitmap profilePic;

			Log.i("HA", "start fetch profile");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_ID, params[1]));
			param.add(new BasicNameValuePair(values.TAG_PAGE_USER_ID, params[2]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {
					Log.i("HA", "res");

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						JSONArray peopleArray = json
								.getJSONArray(values.TAG_DATA);
						Log.i("HA", "array profile");
						JSONObject person = peopleArray.getJSONObject(0);

						user_id = person.getString(values.TAG_ID);
						username = person.getString(values.TAG_USERNAME);
						email = person.getString(values.TAG_EMAIL);
						biketype = person.getString(values.TAG_BIKETYPE);
						friends = person.getString(values.TAG_FRIENDS);
						distance = person.getInt(values.TAG_DISTANCE);
						maxSpeed = person.getInt(values.TAG_MSPEED);

						chat_id = person.getString(values.TAG_MESSAGES_CHAT_ID);

						Log.i("HA", "finished person " + 0 + friends);

						profileData = new people_data(user_id, username, email,
								biketype, friends, distance, maxSpeed, null);

						Log.i("HA", "success");
					} else {
						Log.i("HA", "failure");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "profile : " + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (profileData != null) {
				// profile header view
				nameTV.setText(profileData.getUsername());
				// locationTV.setText(profileData.get);
				motoTV.setText(profileData.getBiketype());
				distanceTV.setText(profileData.getDistance() / 1000 + "");
				maxSpeedTV.setText(profileData.getMaxSpeed() + "");
				numFriendsTV.setText(profileData.getFriends().split("/").length
						+ "");

				// tabs
				list.add(new ProfileRidesFragment(profileData.getUser_id()));
				list.add(new SessionFragment(profileData.getUser_id()));

				pagerAdapter = new TabsPagerAdapter(
						getSupportFragmentManager(), list, tabs);

				pager.setAdapter(pagerAdapter);

				TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.profile_titles);
				titleIndicator.setViewPager(pager);
			}

		}

	}

	private class add_friend_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start add friend");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_ID, params[1]));
			param.add(new BasicNameValuePair(values.TAG_RIDE_USER_ID, params[2]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						Log.i("HA", "success");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "start add friend : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}

	}

}
