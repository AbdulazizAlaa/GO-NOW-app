package com.example.test_bike;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.feeds_adapter;
import beans.Essentials;
import beans.serviceHandler;
import beans.values;
import model.PageData;
import model.feed_data;
import model.people_data;

public class Ride extends Activity implements OnClickListener,
		android.content.DialogInterface.OnClickListener {

	private String title, city, country, day, dateTime, description,
			ticketPrice, ticketCount, numGoing, numMaybe, going_ids, maybe_ids,
			decline_ids, numDecline, username, user_id, ride_id, ride_page_id,
			ride_user_id, route_lat, route_lng;

	private TextView titleTV, locationTV, dateTimeTV, descriptionTV, ownerTV,
			numGoingTV, numMaybeTV, numDeclineTV;

	private PageData page_data;
	private people_data profileData;
	String chat_id;

	private View menuV, aboutV, hideV, ownerV;

	private ImageView joinIV, maybeIV, declineIV;

	private boolean isJoin, isMaybe, isDecline, isDesShown;

	private ListView feeds_lv;
	private ArrayList<feed_data> feeds_list;
	private feeds_adapter feedsAdapter;

	GoogleMap map;
	ArrayList<LatLng> route;
	PolylineOptions polyOptions;

	SwipeRefreshLayout SwipeToRefresh;

	private Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ride);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();

				username = b.getString(values.TAG_USERNAME);
				ride_page_id = b.getString(values.TAG_RIDE_PAGE_ID);
				ride_user_id = b.getString(values.TAG_RIDE_USER_ID);
				user_id = b.getString(values.TAG_ID);
				ride_id = b.getString(values.TAG_RIDE_ID);
				title = b.getString(values.TAG_RIDE_NAME);
				city = b.getString(values.TAG_RIDE_CITY);
				country = b.getString(values.TAG_RIDE_COUNTRY);
				day = b.getString(values.TAG_RIDE_DAY);
				dateTime = b.getString(values.TAG_RIDE_DATE_TIME);
				description = b.getString(values.TAG_RIDE_DESCRIPTION);
				numGoing = b.getString(values.TAG_RIDE_NUM_GOING);
				numMaybe = b.getString(values.TAG_RIDE_NUM_MAYBE);
				numDecline = b.getString(values.TAG_RIDE_NUM_DECLINE);
				going_ids = b.getString(values.TAG_RIDE_GOING_IDS);
				maybe_ids = b.getString(values.TAG_RIDE_MAYBE_IDS);
				decline_ids = b.getString(values.TAG_RIDE_DECLINE_IDS);
				ticketCount = b.getString(values.TAG_RIDE_TICKET_COUNT);
				ticketPrice = b.getString(values.TAG_RIDE_TICKET_PRICE);
				route_lat = b.getString(values.TAG_RIDE_LAT);
				route_lng = b.getString(values.TAG_RIDE_LNG);
				Log.i("ha", "" + ride_id);
			}
		}

		if (!ride_page_id.equals("")) {
			new fetch_page_task().execute("http://" + values.IP
					+ "/cycleapp/fetchpage.php", ride_page_id);
		} else {
			new fetch_people_task().execute("http://" + values.IP
					+ "/cycleapp/fetchprofile.php", user_id, ride_user_id);

		}

		new fetch_ride_task().execute("http://" + values.IP
				+ "/cycleapp/fetchride.php", ride_id);

		SwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.Ride_page_SwipeToRefresh);

		SwipeToRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						feeds_list.clear();
						if (feedsAdapter != null)
							feedsAdapter.notifyDataSetChanged();
						new fetch_feeds_task().execute("http://" + values.IP
								+ "/cycleapp/ride_page_fetchfeeds.php",
								ride_id, title, "refresh");
					}
				});

		polyOptions = new PolylineOptions().color(Color.RED).width(3)
				.visible(true);

		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.Ride_page_map)).getMap();
			map.setMyLocationEnabled(true);
			map.getUiSettings().setCompassEnabled(false);
			map.getUiSettings().setMyLocationButtonEnabled(false);
			map.getUiSettings().setZoomControlsEnabled(false);
		}

		if (!route_lat.equals("") && !route_lng.equals("")) {
			route = Essentials.getRouteLatLng(route_lat, route_lng);

			if (map != null) {
				CameraPosition cam = new CameraPosition.Builder()
						.target(route.get(0)).zoom(13).tilt(25).bearing(0)
						.build();

				map.animateCamera(CameraUpdateFactory.newCameraPosition(cam));

				MarkerOptions mOptions = new MarkerOptions().flat(true)
						.title("Start").visible(true).position(route.get(0));
				map.addMarker(mOptions);
				mOptions = new MarkerOptions().flat(true).title("finish")
						.visible(true).position(route.get(route.size() - 1));
				map.addMarker(mOptions);

				polyOptions.addAll(route);
				map.addPolyline(polyOptions);
			}
		}

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng l) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Ride.this, Session.class);

				i.putExtra(values.TAG_ID, user_id);
				i.putExtra(values.TAG_CALLER, "ride");
				i.putExtra(values.TAG_RIDE_LAT, route_lat);
				i.putExtra(values.TAG_RIDE_LNG, route_lng);

				startActivity(i);
			}
		});

		isJoin = isMaybe = isDecline = isDesShown = false;

		// list view
		feeds_lv = (ListView) findViewById(R.id.ride_page_feedsLV);

		feeds_list = new ArrayList<feed_data>();

		feedsAdapter = new feeds_adapter(Ride.this, username, user_id,
				feeds_list);

		new fetch_feeds_task().execute("http://" + values.IP
				+ "/cycleapp/ride_page_fetchfeeds.php", ride_id, title, "");

		getActionBar().setTitle(title);

		// ride header view
		View rideHeaderV = getLayoutInflater().inflate(
				R.layout.ride_list_header_view, null);

		joinIV = (ImageView) rideHeaderV.findViewById(R.id.Ride_page_joinIV);
		maybeIV = (ImageView) rideHeaderV.findViewById(R.id.Ride_page_maybeIv);
		declineIV = (ImageView) rideHeaderV
				.findViewById(R.id.Ride_page_declineIv);

		joinIV.setOnClickListener(this);
		maybeIV.setOnClickListener(this);
		declineIV.setOnClickListener(this);

		titleTV = (TextView) findViewById(R.id.Ride_page_nameTV);
		locationTV = (TextView) rideHeaderV
				.findViewById(R.id.Ride_page_eventLocationTv);
		dateTimeTV = (TextView) rideHeaderV
				.findViewById(R.id.Ride_page_eventDateTv);
		descriptionTV = (TextView) rideHeaderV
				.findViewById(R.id.Ride_page_aboutTV);
		ownerTV = (TextView) rideHeaderV.findViewById(R.id.Ride_page_ownerTV);
		numGoingTV = (TextView) rideHeaderV
				.findViewById(R.id.Ride_page_numGoingTV);
		numMaybeTV = (TextView) rideHeaderV
				.findViewById(R.id.Ride_page_numMaybeTV);
		numDeclineTV = (TextView) rideHeaderV
				.findViewById(R.id.Ride_page_numDeclineTV);

		titleTV.setText(title);
		locationTV.setText(city + ", " + country);
		dateTimeTV.setText(day + ", " + dateTime);
		descriptionTV.setText(description);
		
		numGoingTV.setText(numGoing);
		numMaybeTV.setText(numMaybe);
		numDeclineTV.setText(numDecline);

		ownerTV.setOnClickListener(this);
		
		menuV = rideHeaderV.findViewById(R.id.Ride_page_MenuV);
		aboutV = rideHeaderV.findViewById(R.id.Ride_page_AboutV);
		hideV = rideHeaderV.findViewById(R.id.Ride_page_hideV);
		ownerV = rideHeaderV.findViewById(R.id.Ride_page_ownerV);
		
		aboutV.setOnClickListener(this);
		ownerV.setOnClickListener(this);
		
		if (user_id.equals(ride_user_id)) {
			menuV.setVisibility(View.GONE);
		} else {
			if (going_ids.contains(user_id)) {
				joinIV.setImageResource(R.drawable.joinsel_icon_event);
				isJoin = true;
			} else if (maybe_ids.contains(user_id)) {
				maybeIV.setImageResource(R.drawable.maybesel_icon_event);
				isMaybe = true;
			} else if (decline_ids.contains(user_id)) {
				declineIV.setImageResource(R.drawable.declinesel_icon_event);
				isDecline = true;
			}
		}

		// list view

		rideHeaderV.setEnabled(false);
		rideHeaderV.setOnClickListener(null);
		
		feeds_lv.addHeaderView(rideHeaderV);
		feeds_lv.setAdapter(feedsAdapter);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			// setResult(RESULT_OK);
			finish();
			// NavUtils.navigateUpFromSameTask(this);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.ride_page_pop_menuB) {
			AlertDialog.Builder b = new AlertDialog.Builder(Ride.this);
			if (user_id.equals(ride_user_id)) {
				CharSequence[] items = { "Add post", "Share" };
				b.setItems(items, this);
			} else {
				CharSequence[] items = { "Share", "Report" };
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
		getMenuInflater().inflate(R.menu.ride, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Ride_page_joinIV:
			if (!isJoin) {
				if (!going_ids.contains(user_id)) {
					if (!going_ids.equals("")) {
						going_ids += "/";
					}
					going_ids += user_id;
				}

				new update_ride_status_task().execute("http://" + values.IP
						+ "/cycleapp/insert_ride_status.php", ride_id, "going",
						going_ids, "add", user_id);

				joinIV.setImageResource(R.drawable.joinsel_icon_event);
				isJoin = true;

				int tempNum = Integer.parseInt((String) numGoingTV.getText()) + 1;
				numGoingTV.setText(tempNum + "");

				if (isDecline) {
					declineIV.performClick();
				} else if (isMaybe) {
					maybeIV.performClick();
				}
			} else {
				String[] ids = going_ids.split("/");
				String temp = "";

				for (int i = 0; i < ids.length; i++) {
					if (!ids[i].equals(user_id)) {
						if (!temp.equals("")) {
							temp += "/";
						}
						temp += ids[i];
					}
				}

				new update_ride_status_task().execute("http://" + values.IP
						+ "/cycleapp/insert_ride_status.php", ride_id, "going",
						temp, "reduce", user_id);

				joinIV.setImageResource(R.drawable.join_event_icon_selector);
				isJoin = false;

				int tempNum = Integer.parseInt((String) numGoingTV.getText()) - 1;
				numGoingTV.setText(tempNum + "");
			}

			break;
		case R.id.Ride_page_maybeIv:
			if (!isMaybe) {

				if (!maybe_ids.contains(user_id)) {
					if (!maybe_ids.equals("")) {
						maybe_ids += "/";
					}
					maybe_ids += user_id;
				}

				new update_ride_status_task().execute("http://" + values.IP
						+ "/cycleapp/insert_ride_status.php", ride_id, "maybe",
						maybe_ids, "add", user_id);

				maybeIV.setImageResource(R.drawable.maybesel_icon_event);
				isMaybe = true;

				int tempNum = Integer.parseInt((String) numMaybeTV.getText()) + 1;
				numMaybeTV.setText(tempNum + "");

				if (isDecline) {
					declineIV.performClick();
				} else if (isJoin) {
					joinIV.performClick();
				}
			} else {

				String[] ids = maybe_ids.split("/");
				String temp = "";

				for (int i = 0; i < ids.length; i++) {
					if (!ids[i].equals(user_id)) {
						if (!temp.equals("")) {
							temp += "/";
						}
						temp += ids[i];
					}
				}

				new update_ride_status_task().execute("http://" + values.IP
						+ "/cycleapp/insert_ride_status.php", ride_id, "maybe",
						temp, "reduce", user_id);

				maybeIV.setImageResource(R.drawable.maybe_event_icon_selector);
				isMaybe = false;

				int tempNum = Integer.parseInt((String) numMaybeTV.getText()) - 1;
				numMaybeTV.setText(tempNum + "");

			}

			break;
		case R.id.Ride_page_declineIv:
			if (!isDecline) {
				if (!decline_ids.contains(user_id)) {
					if (!decline_ids.equals("")) {
						decline_ids += "/";
					}
					decline_ids += user_id;
				}

				new update_ride_status_task().execute("http://" + values.IP
						+ "/cycleapp/insert_ride_status.php", ride_id,
						"decline", decline_ids, "add", user_id);

				declineIV.setImageResource(R.drawable.declinesel_icon_event);
				isDecline = true;

				int tempNum = Integer.parseInt((String) numDeclineTV.getText()) + 1;
				numDeclineTV.setText(tempNum + "");

				if (isJoin) {
					joinIV.performClick();
				} else if (isMaybe) {
					maybeIV.performClick();
				}
			} else {

				String[] ids = decline_ids.split("/");
				String temp = "";

				for (int i = 0; i < ids.length; i++) {
					if (!ids[i].equals(user_id)) {
						if (!temp.equals("")) {
							temp += "/";
						}
						temp += ids[i];
					}
				}

				new update_ride_status_task().execute("http://" + values.IP
						+ "/cycleapp/insert_ride_status.php", ride_id,
						"decline", temp, "reduce", user_id);

				declineIV
						.setImageResource(R.drawable.decline_event_icon_selector);
				isDecline = false;

				int tempNum = Integer.parseInt((String) numDeclineTV.getText()) - 1;
				numDeclineTV.setText(tempNum + "");

			}

			break;
		case R.id.Ride_page_AboutV:
			if (isDesShown) {
				hideV.setVisibility(View.GONE);
				isDesShown = !isDesShown;
			} else {
				hideV.setVisibility(View.VISIBLE);
				isDesShown = !isDesShown;
			}
			break;
		case R.id.Ride_page_ownerTV:
			Log.i("ha", "Click");
			if (!ride_page_id.equals("")) {

				Log.i("ha", "page:"+ride_page_id);
				Intent i = new Intent(Ride.this, Page.class);


				i.putExtra(values.TAG_USERNAME, username);
				i.putExtra(values.TAG_ID, user_id);
				i.putExtra(values.TAG_PAGE_FOLLOWERS, page_data.getFollwers());
				i.putExtra(values.TAG_PAGE_PAGE_ID, page_data.getPage_id());
				i.putExtra(values.TAG_PAGE_USER_ID, page_data.getUser_id());
				i.putExtra(values.TAG_PAGE_NAME, page_data.getName());
				i.putExtra(values.TAG_PAGE_PRODUCT, page_data.getProduct());
				i.putExtra(values.TAG_PAGE_DESCRIPTION, page_data.getDescription());
				i.putExtra(values.TAG_PAGE_LOCATION, page_data.getLocation());
				i.putExtra(values.TAG_PAGE_FOUNDED, page_data.getFounded());
				i.putExtra(values.TAG_PAGE_RIDES_ID, page_data.getRides_ids());
				startActivityForResult(i, 1);
			} else {
				Log.i("ha", "profile:"+ride_page_id);
				Intent i = new Intent(Ride.this, Profile.class);

				i.putExtra(values.TAG_ID, user_id);
				i.putExtra(values.TAG_USER_FRIENDS, "");

				i.putExtra(values.TAG_PAGE_USER_ID, profileData
						.getUser_id());
				i.putExtra(values.TAG_USERNAME, username);

				startActivity(i);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		if (user_id.equals(ride_user_id)) {

			switch (which) {
			case 0:
				Intent i = new Intent(Ride.this, RideAddPost.class);

				i.putExtra(values.TAG_RIDE_ID, ride_id);

				startActivity(i);
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "share",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		} else {
			switch (which) {
			case 0:
				Toast.makeText(getApplicationContext(), "share",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "report",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		}

	}

	private class update_ride_status_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start update");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_RIDE_ID, params[1]));
			if (params[2].equals("going")) {
				Log.i("ha", "going");
				param.add(new BasicNameValuePair(values.TAG_RIDE_GOING_IDS,
						params[3]));
			} else if (params[2].equals("maybe")) {
				Log.i("ha", "maybe");
				param.add(new BasicNameValuePair(values.TAG_RIDE_MAYBE_IDS,
						params[3]));
			} else if (params[2].equals("decline")) {
				Log.i("ha", "decline");
				param.add(new BasicNameValuePair(values.TAG_RIDE_DECLINE_IDS,
						params[3]));
			}

			param.add(new BasicNameValuePair(values.TAG_RIDE_UPDATE_TYPE,
					params[4]));
			param.add(new BasicNameValuePair(values.TAG_ID, params[5]));

			Log.i("HA", "ride status : " + params[1]);

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						Log.i("HA", "success");
					} else {
						Log.i("HA",
								"failure: " + json.getString(values.TAG_REASON));
					}
				}

			} catch (Exception e) {
				Log.i("HA", "ride status : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// CreateEvent.this.finish();
		}

	}

	private class fetch_feeds_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			// feeds_list = new ArrayList<feed_data>();

			String text, pageName, day, feed_id, like_ids;
			int numLikes, numComments;
			// Bitmap coverImg, pageImg;

			Log.i("HA", "start fetch feeds");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_RIDE_ID, params[1]));
			param.add(new BasicNameValuePair(values.TAG_RIDE_NAME, params[2]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {
					Log.i("HA", "res");

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						JSONArray ridesArray = json
								.getJSONArray(values.TAG_DATA);
						Log.i("HA", "array fetched");
						for (int i = 0; i < ridesArray.length(); i++) {
							JSONObject ride = ridesArray.getJSONObject(i);

							feed_id = ride.getString(values.TAG_FEED_ID);

							like_ids = ride.getString(values.TAG_FEED_LIKE_IDS);

							pageName = ride
									.getString(values.TAG_FEED_PAGE_NAME);

							text = ride.getString(values.TAG_FEED_FEED);

							// day = ride.getString(values.TAG_FEED_DATE);

							numLikes = ride
									.getInt(values.TAG_FEED_NUMBER_LIKES);

							numComments = ride
									.getInt(values.TAG_FEED_NUMBER_COMMENTS);

							Log.i("HA", "finished feed " + i);

							feeds_list.add(new feed_data(text, pageName, "2",
									feed_id, like_ids, numLikes, numComments,
									null, null));
						}

						Log.i("HA", "success");
					} else {
						Log.i("HA", "failure: " + json.getString("reason"));
					}
				}

			} catch (Exception e) {
				Log.i("HA", "home : " + e.getMessage());
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
			/*
			 * feedsAdapter = new feeds_adapter(Ride.this, username, user_id,
			 * feeds_list);
			 * 
			 * feeds_lv.setAdapter(feedsAdapter);
			 */

			if (feedsAdapter != null)
				feedsAdapter.notifyDataSetChanged();

		}

	}

	private class fetch_ride_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start fetch page");
			serviceHandler sr = new serviceHandler();

			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_RIDE_ID, params[1]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						JSONArray ridesArray = json
								.getJSONArray(values.TAG_DATA);

						JSONObject ride = ridesArray.getJSONObject(0);

						going_ids = ride.getString(values.TAG_RIDE_GOING_IDS);
						maybe_ids = ride.getString(values.TAG_RIDE_MAYBE_IDS);
						decline_ids = ride
								.getString(values.TAG_RIDE_DECLINE_IDS);

						ride_page_id = ride.getString(values.TAG_RIDE_PAGE_ID);
						ride_user_id = ride.getString(values.TAG_RIDE_USER_ID);

						ride_id = ride.getString(values.TAG_RIDE_ID);

						title = ride.getString(values.TAG_RIDE_NAME);

						String datetime = ride
								.getString(values.TAG_RIDE_DATE_TIME);
						String[] datetimearr = datetime.split(",");
						String month = "";
						day = "";
						String time = "";
						for (int j = 0; j < datetimearr.length; j++) {
							if (j == 0)
								month = datetimearr[0];
							else if (j == 1)
								day = datetimearr[1];
							else if (j == 2)
								time = datetimearr[2];
						}

						String location1 = ride
								.getString(values.TAG_RIDE_LOCATION);
						String[] loc = location1.split(",");
						city = "";
						country = "";
						for (int j = 0; j < loc.length; j++) {
							if (j == 0)
								city = loc[0];
							else if (j == 1)
								country = loc[1];
							else if (j == 2)
								country += "," + loc[2];
						}

						description = ride
								.getString(values.TAG_RIDE_DESCRIPTION);

						ticketPrice = ride
								.getString(values.TAG_RIDE_TICKET_PRICE);
						ticketCount = ride
								.getString(values.TAG_RIDE_TICKET_COUNT);

						numGoing = ride.getString(values.TAG_RIDE_NUM_GOING);
						numMaybe = ride.getString(values.TAG_RIDE_NUM_MAYBE);
						numDecline = ride
								.getString(values.TAG_RIDE_NUM_DECLINE);
						route_lat = ride.getString(values.TAG_RIDE_LAT);

						route_lng = ride.getString(values.TAG_RIDE_LNG);

						Log.i("HA", "success");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "fetch page : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			titleTV.setText(title);
			locationTV.setText(city + ", " + country);
			dateTimeTV.setText(day + ", " + dateTime);
			descriptionTV.setText(description);
			numGoingTV.setText(numGoing);
			numMaybeTV.setText(numMaybe);
			numDeclineTV.setText(numDecline);

			if (user_id.equals(ride_user_id)) {
				menuV.setVisibility(View.GONE);
			} else {
				if (going_ids.contains(user_id)) {
					joinIV.setImageResource(R.drawable.joinsel_icon_event);
					isJoin = true;

					maybeIV.setImageResource(R.drawable.maybe_icon_event);
					isMaybe = false;
					declineIV.setImageResource(R.drawable.decline_icon_event);
					isDecline = false;
				} else if (maybe_ids.contains(user_id)) {
					maybeIV.setImageResource(R.drawable.maybesel_icon_event);
					isMaybe = true;

					joinIV.setImageResource(R.drawable.join_icon_event);
					isJoin = false;
					declineIV.setImageResource(R.drawable.decline_icon_event);
					isDecline = false;
				} else if (decline_ids.contains(user_id)) {
					declineIV
							.setImageResource(R.drawable.declinesel_icon_event);
					isDecline = true;

					joinIV.setImageResource(R.drawable.join_icon_event);
					isJoin = false;
					maybeIV.setImageResource(R.drawable.maybe_icon_event);
					isMaybe = false;
				}
			}

		}

	}

	private class fetch_page_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start fetch page");
			serviceHandler sr = new serviceHandler();

			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_PAGE_PAGE_ID, params[1]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String followers, page_id, user_id, pageName, product, description, location, founded, rides_ids;
					int numFollowers;

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						JSONArray pages = json.getJSONArray(values.TAG_DATA);

						JSONObject page = pages.getJSONObject(0);

						followers = page.getString(values.TAG_PAGE_FOLLOWERS);
						page_id = page.getString(values.TAG_PAGE_PAGE_ID);
						user_id = page.getString(values.TAG_PAGE_USER_ID);
						pageName = page.getString(values.TAG_PAGE_NAME);
						product = page.getString(values.TAG_PAGE_PRODUCT);
						description = page
								.getString(values.TAG_PAGE_DESCRIPTION);
						location = page.getString(values.TAG_PAGE_LOCATION);
						founded = page.getString(values.TAG_PAGE_FOUNDED);
						rides_ids = page.getString(values.TAG_PAGE_RIDES_ID);

						numFollowers = followers.split("/").length;

						page_data = new PageData(page_id, user_id, pageName,
								product, description, location, founded,
								rides_ids, followers);

						Log.i("HA", "success");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "fetch page : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			ownerTV.setText("Ride by: " + page_data.getName());
		}

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
			ownerTV.setText("Ride by: " + profileData.getUsername());

		}

	}

}
