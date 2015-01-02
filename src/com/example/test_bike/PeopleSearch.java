package com.example.test_bike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.people_adapter;
import beans.serviceHandler;
import beans.values;
import model.people_data;

public class PeopleSearch extends Activity {

	ListView peopleLV;
	ArrayList<people_data> people_list;
	people_adapter peopleAdapter;

	people_data user_data;

	SwipeRefreshLayout SwipeToRefresh;

	String searchWord;

	SearchView ridesSearchView;

	private Bundle b;
	private String username, email, user_id, caller, file_path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_search);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setLogo(R.drawable.people_icon);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(R.drawable.people_icon);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();

				username = b.getString(values.TAG_USERNAME);
				email = b.getString(values.TAG_EMAIL);
				user_id = b.getString(values.TAG_ID);
				file_path = b.getString(values.TAG_PROFILE_IMG);

			}
		}

		searchWord = "";
		
		//list view
		peopleLV = (ListView) findViewById(R.id.people_search_peopleLV);
		
		people_list = new ArrayList<people_data>();		
		

		peopleLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub

				Intent i = new Intent(PeopleSearch.this, Profile.class);

				i.putExtra(values.TAG_ID, user_id);
				i.putExtra(values.TAG_USER_FRIENDS, user_data.getFriends());

				i.putExtra(values.TAG_PAGE_USER_ID, people_list.get(pos)
						.getUser_id());
				i.putExtra(values.TAG_USERNAME, username);
				i.putExtra(values.TAG_EMAIL, email);
				i.putExtra(values.TAG_CALLER, "friend");
				i.putExtra(values.TAG_PROFILE_IMG, file_path);
				i.putExtra(values.TAG_PROFILE_IMG, people_list.get(pos).getProfilePic());

				startActivity(i);

			}
		});

		SwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.people_search_SwipeToRefresh);

		SwipeToRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						people_list.clear();
						if (peopleAdapter != null)
							peopleAdapter.notifyDataSetChanged();

						new fetch_people_task().execute("http://" + values.IP
								+ "/cycleapp/fetchpeople.php", searchWord,
								user_id, "refresh");

					}
				});
		new fetch_people_task().execute("http://" + values.IP
				+ "/cycleapp/fetchpeople.php", searchWord,
				user_id, "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.people_search, menu);

		MenuItem searchItem = menu.findItem(R.id.people_search_menu_search);
		ridesSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		ridesSearchView.setBackgroundResource(R.drawable.search_bar_drawable);
		ridesSearchView.setSubmitButtonEnabled(false);
		ridesSearchView.setIconifiedByDefault(false);

		int idED = ridesSearchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		EditText searchED = (EditText) ridesSearchView.findViewById(idED);
		searchED.setTextColor(Color.WHITE);
		searchED.setHintTextColor(Color.WHITE);
		searchED.setHint("Search");
		searchED.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));

		int idIV = ridesSearchView.getContext().getResources()
				.getIdentifier("android:id/search_mag_icon", null, null);
		ImageView imageView = (ImageView) ridesSearchView.findViewById(idIV);
		imageView.setImageResource(android.R.color.transparent);

		ridesSearchView
				.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

					@Override
					public boolean onQueryTextSubmit(String query) {
						// TODO Auto-generated method stub

						people_list.clear();
						if (peopleAdapter != null)
							peopleAdapter.notifyDataSetChanged();

						searchWord = query;
						new fetch_people_task().execute("http://" + values.IP
								+ "/cycleapp/fetchpeople.php", searchWord,
								user_id, "refresh");

						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

						imm.toggleSoftInput(
								InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
						return true;
					}

					@Override
					public boolean onQueryTextChange(String newText) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == android.R.id.home) {
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

			String user_id, username, email, biketype, friends, file_path;
			int distance, maxSpeed;
			// Bitmap profilePic;

			Log.i("HA", "start fetch people");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_USERNAME, params[1]));
			param.add(new BasicNameValuePair(values.TAG_ID, params[2]));

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
						Log.i("HA", "array people");
						for (int i = 0; i < peopleArray.length(); i++) {
							JSONObject person = peopleArray.getJSONObject(i);

							user_id = person.getString(values.TAG_ID);
							username = person.getString(values.TAG_USERNAME);
							email = person.getString(values.TAG_EMAIL);
							biketype = person.getString(values.TAG_BIKETYPE);
							friends = person.getString(values.TAG_FRIENDS);
							distance = person.getInt(values.TAG_DISTANCE);
							maxSpeed = person.getInt(values.TAG_MSPEED);
							file_path = person.getString(values.TAG_PROFILE_IMG);
							
							Log.i("HA", "finished person " + i);

							people_list.add(new people_data(user_id, username,
									email, biketype, friends, distance,
									maxSpeed, file_path));
						}

						JSONArray userArray = json
								.getJSONArray(values.TAG_USER_DATA);
						Log.i("HA", "array user");
						JSONObject person = userArray.getJSONObject(0);

						user_id = person.getString(values.TAG_ID);
						username = person.getString(values.TAG_USERNAME);
						email = person.getString(values.TAG_EMAIL);
						biketype = person.getString(values.TAG_BIKETYPE);
						friends = person.getString(values.TAG_FRIENDS);
						distance = person.getInt(values.TAG_DISTANCE);
						maxSpeed = person.getInt(values.TAG_MSPEED);
						file_path = person.getString(values.TAG_PROFILE_IMG);
						
						Log.i("HA", "finished person 0");

						user_data = new people_data(user_id, username, email,
								biketype, friends, distance, maxSpeed, file_path);

						Log.i("HA", "success");
					} else {
						Log.i("HA", "failure");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "people : " + e.getMessage());
			}
			if (params[3].equals("refresh")) {
				SwipeToRefresh.setRefreshing(false);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			peopleAdapter = new people_adapter(PeopleSearch.this, people_list,
					user_data, user_id);

			peopleLV.setAdapter(peopleAdapter);

		}
	}

}
