package com.example.test_bike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import adapters.Pages_adapter;
import adapters.rides_adapter;
import beans.serviceHandler;
import beans.values;
import model.PageData;
import model.ride_data;

public class PageSearch extends Activity {

	ListView pagesLV;
	ListView ridesLV;

	ArrayList<String> pagesNamesList;
	ArrayList<PageData> pagesList;

	private ArrayList<ride_data> rides_list;

	private rides_adapter ridesAdapter;
	private Pages_adapter pagesAdapter;

	String id, searchWord, username;
	Bundle b;

	SearchView ridesSearchView;

	private SwipeRefreshLayout ridesSwipeToRefresh;
	private SwipeRefreshLayout pagesSwipeToRefresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_search);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setLogo(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		pagesLV = (ListView) findViewById(R.id.page_search_pagesLV);
		ridesLV = (ListView) findViewById(R.id.page_search_ridesLV);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();

				username = b.getString(values.TAG_USERNAME);
				id = b.getString(values.TAG_ID);
				searchWord = b.getString(values.TAG_SEARCH_SEARCH_WORD);
			}
		}

		ridesSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.page_search_ridesSwipeToRefresh);
		pagesSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.page_search_pagesSwipeToRefresh);

		ridesSwipeToRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub

						rides_list.clear();
						if (ridesAdapter != null)
							ridesAdapter.notifyDataSetChanged();

						new search_pages_rides_task().execute(
								"http://" + values.IP
										+ "/cycleapp/pages_ridesSearch.php",
								"ridesRefresh", searchWord);
					}
				});
		pagesSwipeToRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub

						pagesNamesList.clear();
						pagesList.clear();
						if (pagesAdapter != null)
							pagesAdapter.notifyDataSetChanged();

						new search_pages_rides_task().execute(
								"http://" + values.IP
										+ "/cycleapp/pages_ridesSearch.php",
								"pagesRefresh", searchWord);
					}
				});

		new search_pages_rides_task().execute("http://" + values.IP
				+ "/cycleapp/pages_ridesSearch.php", "", searchWord);

		pagesLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long itemId) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PageSearch.this, Page.class);

				PageData temp = pagesList.get(pos);

				i.putExtra(values.TAG_USERNAME, username);
				i.putExtra(values.TAG_ID, id);
				i.putExtra(values.TAG_PAGE_FOLLOWERS, temp.getFollwers());
				i.putExtra(values.TAG_PAGE_PAGE_ID, temp.getPage_id());
				i.putExtra(values.TAG_PAGE_USER_ID, temp.getUser_id());
				i.putExtra(values.TAG_PAGE_NAME, temp.getName());
				i.putExtra(values.TAG_PAGE_PRODUCT, temp.getProduct());
				i.putExtra(values.TAG_PAGE_DESCRIPTION, temp.getDescription());
				i.putExtra(values.TAG_PAGE_LOCATION, temp.getLocation());
				i.putExtra(values.TAG_PAGE_FOUNDED, temp.getFounded());
				i.putExtra(values.TAG_PAGE_RIDES_ID, temp.getRides_ids());
				startActivityForResult(i, 1);
			}
		});
		ridesLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long itemId) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PageSearch.this, Ride.class);

				i.putExtra(values.TAG_USERNAME, username);
				i.putExtra(values.TAG_ID, id);
				i.putExtra(values.TAG_RIDE_NAME, rides_list.get(pos)
						.getPageTitle());

				i.putExtra(values.TAG_RIDE_PAGE_ID, rides_list.get(pos)
						.getPage_id());
				i.putExtra(values.TAG_RIDE_USER_ID, rides_list.get(pos)
						.getUser_id());
				i.putExtra(values.TAG_RIDE_ID, rides_list.get(pos).getRide_id());
				i.putExtra(values.TAG_RIDE_CITY, rides_list.get(pos).getCity());
				i.putExtra(values.TAG_RIDE_COUNTRY, rides_list.get(pos)
						.getCountry());
				i.putExtra(values.TAG_RIDE_DAY, rides_list.get(pos).getDay());
				i.putExtra(values.TAG_RIDE_DATE_TIME, rides_list.get(pos)
						.getMonth() + ", " + rides_list.get(pos).getTime());
				i.putExtra(values.TAG_RIDE_DESCRIPTION, rides_list.get(pos)
						.getDescription());
				i.putExtra(values.TAG_RIDE_NUM_GOING, rides_list.get(pos)
						.getNumGoing());
				i.putExtra(values.TAG_RIDE_NUM_MAYBE, rides_list.get(pos)
						.getNumMaybe());
				i.putExtra(values.TAG_RIDE_NUM_DECLINE, rides_list.get(pos)
						.getNumDecline());
				i.putExtra(values.TAG_RIDE_GOING_IDS, rides_list.get(pos)
						.getGoing_ids());
				i.putExtra(values.TAG_RIDE_MAYBE_IDS, rides_list.get(pos)
						.getMaybe_ids());
				i.putExtra(values.TAG_RIDE_DECLINE_IDS, rides_list.get(pos)
						.getDecline_ids());
				i.putExtra(values.TAG_RIDE_TICKET_COUNT, rides_list.get(pos)
						.getTicketCount());
				i.putExtra(values.TAG_RIDE_TICKET_PRICE, rides_list.get(pos)
						.getTicketPrice());
				i.putExtra(values.TAG_RIDE_LAT, rides_list.get(pos)
						.getRoute_lat());
				i.putExtra(values.TAG_RIDE_LNG, rides_list.get(pos)
						.getRoute_lng());

				startActivityForResult(i, 1);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (requestCode == 1 && resultCode == RESULT_OK) {
			pagesNamesList.clear();
			pagesList.clear();
			if (pagesAdapter != null)
				pagesAdapter.notifyDataSetChanged();

			rides_list.clear();
			if (ridesAdapter != null)
				ridesAdapter.notifyDataSetChanged();

			new search_pages_rides_task().execute("http://" + values.IP
					+ "/cycleapp/pages_ridesSearch.php", "pagesRefresh",
					searchWord);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private class search_pages_rides_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			pagesNamesList = new ArrayList<String>();

			pagesList = new ArrayList<PageData>();

			rides_list = new ArrayList<ride_data>();

			Log.i("HA", "start search pages rides");
			serviceHandler sr = new serviceHandler();

			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_SEARCH_SEARCH_WORD,
					params[2]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						String followers, page_id, user_id, name, product, page_description, location, founded, rides_ids;
						String route_lat, route_lng, ride_page_id, ride_user_id, ride_id, title, city, country, day, month, time, ride_description, ticketPrice, ticketCount, numGoing, numMaybe, numDecline, going_ids, maybe_ids, decline_ids;

						JSONArray pages = json
								.getJSONArray(values.TAG_SEARCH_PAGES);

						for (int i = 0; i < pages.length(); i++) {
							JSONObject page = pages.getJSONObject(i);

							followers = page
									.getString(values.TAG_PAGE_FOLLOWERS);
							page_id = page.getString(values.TAG_PAGE_PAGE_ID);
							user_id = page.getString(values.TAG_PAGE_USER_ID);
							name = page.getString(values.TAG_PAGE_NAME);
							product = page.getString(values.TAG_PAGE_PRODUCT);
							page_description = page
									.getString(values.TAG_PAGE_DESCRIPTION);
							location = page.getString(values.TAG_PAGE_LOCATION);
							founded = page.getString(values.TAG_PAGE_FOUNDED);
							rides_ids = page
									.getString(values.TAG_PAGE_RIDES_ID);

							// pagesNamesList.add("Page: " + name);
							pagesNamesList.add(name);

							pagesList.add(new PageData(page_id, user_id, name,
									product, page_description, location,
									founded, rides_ids, followers));

						}

						JSONArray ridesArray = json
								.getJSONArray(values.TAG_SEARCH_RIDES);
						Log.i("HA", "array fetched");
						for (int i = 0; i < ridesArray.length(); i++) {
							JSONObject ride = ridesArray.getJSONObject(i);

							going_ids = ride
									.getString(values.TAG_RIDE_GOING_IDS);
							maybe_ids = ride
									.getString(values.TAG_RIDE_MAYBE_IDS);
							decline_ids = ride
									.getString(values.TAG_RIDE_DECLINE_IDS);

							ride_page_id = ride
									.getString(values.TAG_RIDE_PAGE_ID);
							ride_user_id = ride
									.getString(values.TAG_RIDE_USER_ID);

							ride_id = ride.getString(values.TAG_RIDE_ID);

							title = ride.getString(values.TAG_RIDE_NAME);

							String datetime = ride
									.getString(values.TAG_RIDE_DATE_TIME);
							String[] datetimearr = datetime.split(",");
							month = "";
							day = "";
							time = "";
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

							ride_description = ride
									.getString(values.TAG_RIDE_DESCRIPTION);

							ticketPrice = ride
									.getString(values.TAG_RIDE_TICKET_PRICE);
							ticketCount = ride
									.getString(values.TAG_RIDE_TICKET_COUNT);

							numGoing = ride
									.getString(values.TAG_RIDE_NUM_GOING);
							numMaybe = ride
									.getString(values.TAG_RIDE_NUM_MAYBE);
							numDecline = ride
									.getString(values.TAG_RIDE_NUM_DECLINE);
							route_lat = ride.getString(values.TAG_RIDE_LAT);

							route_lng = ride.getString(values.TAG_RIDE_LNG);
							Log.i("HA", "finished ride " + i);

							// pagesNamesList.add("Ride: " + title);
							rides_list.add(new ride_data(ride_id, ride_page_id,
									ride_user_id, title, day, month, datetime,
									city, country, ride_description,
									ticketPrice, ticketCount, numGoing,
									numMaybe, numDecline, going_ids, maybe_ids,
									decline_ids, route_lat, route_lng, null));
						}

						if (params[1].equals("pagesRefresh")) {
							pagesSwipeToRefresh.setRefreshing(false);
						} else if (params[1].equals("ridesRefresh")) {
							ridesSwipeToRefresh.setRefreshing(false);
						}

						Log.i("HA", "success");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "fetch pages : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pagesAdapter = new Pages_adapter(PageSearch.this, pagesList);

			ridesAdapter = new rides_adapter(PageSearch.this, rides_list);

			ridesLV.setAdapter(ridesAdapter);
			pagesLV.setAdapter(pagesAdapter);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.page_search, menu);

		MenuItem searchItem = menu.findItem(R.id.search_page_menu_search);
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
						pagesNamesList.clear();
						pagesList.clear();
						if (pagesAdapter != null)
							pagesAdapter.notifyDataSetChanged();

						rides_list.clear();
						if (ridesAdapter != null)
							ridesAdapter.notifyDataSetChanged();

						searchWord = query;
						new search_pages_rides_task().execute(
								"http://" + values.IP
										+ "/cycleapp/pages_ridesSearch.php",
								"refresh", searchWord);

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
}
