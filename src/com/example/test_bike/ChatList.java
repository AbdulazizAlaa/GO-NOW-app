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

import adapters.chat_adapter;
import beans.serviceHandler;
import beans.values;
import model.chat_data;
import model.people_data;

public class ChatList extends Activity {

	ListView chatLV;
	ArrayList<chat_data> chat_list;
	chat_adapter chatAdapter;

	people_data user_data;

	SwipeRefreshLayout SwipeToRefresh;

	String searchWord;

	SearchView chatSearchView;

	private Bundle b;
	private String username, email, user_id, caller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_list);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setLogo(R.drawable.chat_icon);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(R.drawable.chat_icon);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();

				username = b.getString(values.TAG_USERNAME);
				email = b.getString(values.TAG_EMAIL);
				user_id = b.getString(values.TAG_ID);

			}
		}

		searchWord = "";
		chat_list = new ArrayList<chat_data>();

		chatLV = (ListView) findViewById(R.id.chat_list_peopleLV);

		chatLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub

				Intent i = new Intent(ChatList.this, Messages.class);

				i.putExtra(values.TAG_MESSAGES_FIRST_ID, user_id);
				i.putExtra(values.TAG_USERNAME, username);
				i.putExtra(values.TAG_MESSAGES_SECOND_ID, chat_list.get(pos)
						.getUser_id());
				i.putExtra(values.TAG_MESSAGES_NAME, chat_list.get(pos)
						.getUsername());
				i.putExtra(values.TAG_MESSAGES_CHAT_ID, chat_list.get(pos).getChat_id());

				startActivity(i);
			}
		});

		SwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.chat_list_SwipeToRefresh);

		SwipeToRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						chat_list.clear();
						if (chatAdapter != null)
							chatAdapter.notifyDataSetChanged();

						new fetch_chat_task().execute("http://" + values.IP
								+ "/cycleapp/fetchchats.php", user_id,
								"refresh");

					}
				});
		new fetch_chat_task().execute("http://" + values.IP
				+ "/cycleapp/fetchchats.php", user_id, "");

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_list, menu);

		MenuItem searchItem = menu.findItem(R.id.chat_list_menu_search);
		chatSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		chatSearchView.setBackgroundResource(R.drawable.search_bar_drawable);
		chatSearchView.setSubmitButtonEnabled(false);
		chatSearchView.setIconifiedByDefault(false);

		int idED = chatSearchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		EditText searchED = (EditText) chatSearchView.findViewById(idED);
		searchED.setTextColor(Color.WHITE);
		searchED.setHintTextColor(Color.WHITE);
		searchED.setHint("Search");
		searchED.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));

		int idIV = chatSearchView.getContext().getResources()
				.getIdentifier("android:id/search_mag_icon", null, null);
		ImageView imageView = (ImageView) chatSearchView.findViewById(idIV);
		imageView.setImageResource(android.R.color.transparent);

		chatSearchView
				.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

					@Override
					public boolean onQueryTextSubmit(String query) {
						// TODO Auto-generated method stub

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

	private class fetch_chat_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			String user_id, username, email, biketype, friends, numMessages, chat_id;
			int distance, maxSpeed;
			// Bitmap profilePic;

			Log.i("HA", "start fetch people");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_ID, params[1]));

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
							numMessages = person
									.getString(values.TAG_MESSAGES_NUM_MESSAGES);
							chat_id = person
									.getString(values.TAG_MESSAGES_CHAT_ID);

							distance = person.getInt(values.TAG_DISTANCE);
							maxSpeed = person.getInt(values.TAG_MSPEED);

							Log.i("HA", "finished person " + i);

							chat_list.add(new chat_data(user_id, username,
									email, biketype, friends, numMessages, chat_id,
									distance, maxSpeed, null));
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

						Log.i("HA", "finished person 0");

						user_data = new people_data(user_id, username, email,
								biketype, friends, distance, maxSpeed, null);

						Log.i("HA", "success");
					} else {
						Log.i("HA", "failure");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "people : " + e.getMessage());
			}
			if (params[2].equals("refresh")) {
				SwipeToRefresh.setRefreshing(false);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			chatAdapter = new chat_adapter(ChatList.this, chat_list);

			chatLV.setAdapter(chatAdapter);

		}
	}

}
