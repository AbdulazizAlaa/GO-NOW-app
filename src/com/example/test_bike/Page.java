package com.example.test_bike;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.feeds_adapter;
import beans.serviceHandler;
import beans.values;
import model.feed_data;

public class Page extends Activity implements OnClickListener,
		OnMenuItemClickListener,
		android.content.DialogInterface.OnClickListener {

	TextView pageNameTV, followTV, numberOfLikesTV;
	View moreInfoV, menuV;

	ImageView followB, messagesB, inviteB;

	Bundle b;

	SharedPreferences pref;
	String[] follow_data;

	SwipeRefreshLayout SwipeToRefresh;

	String username, id, page_id, user_id, pageName, product, description,
			location, founded, rides_ids, followers;

	int numFollowers;

	boolean isFollowed = false;
	boolean isEnabled = false;

	private ListView feeds_lv;
	private ArrayList<feed_data> feeds_list;
	private feeds_adapter feedsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();

				id = b.getString(values.TAG_ID);

				username = b.getString(values.TAG_USERNAME);
				followers = b.getString(values.TAG_PAGE_FOLLOWERS);
				page_id = b.getString(values.TAG_PAGE_PAGE_ID);
				user_id = b.getString(values.TAG_PAGE_USER_ID);
				pageName = b.getString(values.TAG_PAGE_NAME);
				product = b.getString(values.TAG_PAGE_PRODUCT);
				description = b.getString(values.TAG_PAGE_DESCRIPTION);
				location = b.getString(values.TAG_PAGE_LOCATION);
				founded = b.getString(values.TAG_PAGE_FOUNDED);
				rides_ids = b.getString(values.TAG_PAGE_RIDES_ID);

				numFollowers = followers.split("/").length;

			}
		}

		getActionBar().setTitle(pageName);

		new fetch_page_task().execute("http://" + values.IP
				+ "/cycleapp/fetchpage.php", page_id);
		
		SwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.page_SwipeToRefresh);

		SwipeToRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						feeds_list.clear();
						if (feedsAdapter != null)
							feedsAdapter.notifyDataSetChanged();
						new fetch_feeds_task().execute("http://" + values.IP
								+ "/cycleapp/page_fetchfeeds.php", page_id,
								pageName, "refresh");
					}
				});

		// list view

		feeds_lv = (ListView) findViewById(R.id.page_feedsLV);

		feeds_list = new ArrayList<feed_data>();

		feedsAdapter = new feeds_adapter(Page.this, username, user_id,
				feeds_list);

		new fetch_feeds_task().execute("http://" + values.IP
				+ "/cycleapp/page_fetchfeeds.php", page_id, pageName, "");

		// page header view
		View pageHeaderV = getLayoutInflater().inflate(
				R.layout.page_list_header_view, null);

		pageNameTV = (TextView) findViewById(R.id.page_page_nameTV);
		numberOfLikesTV = (TextView) pageHeaderV
				.findViewById(R.id.page_numberoflikesTv);

		followB = (ImageView) pageHeaderV.findViewById(R.id.page_followIv);
		messagesB = (ImageView) pageHeaderV.findViewById(R.id.page_messageIv);
		inviteB = (ImageView) pageHeaderV.findViewById(R.id.page_inviteIv);

		menuV = (View) pageHeaderV.findViewById(R.id.page_MenuV);
		moreInfoV = (View) pageHeaderV.findViewById(R.id.page_moreInfoV);

		followTV = (TextView) pageHeaderV.findViewById(R.id.page_FollowTv);

		if (id.equals(user_id)) {
			menuV.setVisibility(View.GONE);
		} else {

			if (followers.contains(id)) {
				isFollowed = true;
				followB.setImageResource(R.drawable.unfollow_icon_selector);
				followTV.setText("unFollow");
			}
		}

		pageNameTV.setText(pageName);
		numberOfLikesTV.setText("" + numFollowers);

		followB.setOnClickListener(this);
		messagesB.setOnClickListener(this);
		inviteB.setOnClickListener(this);
		moreInfoV.setOnClickListener(this);

		// list view
		pageHeaderV.setEnabled(false);
		pageHeaderV.setOnClickListener(null);
		
		feeds_lv.addHeaderView(pageHeaderV);
		feeds_lv.setAdapter(feedsAdapter);

		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.page, menu);
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
			// setResult(RESULT_OK);
			finish();
			// NavUtils.navigateUpFromSameTask(this);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub

		if (item.getItemId() == R.id.page_pop_menuB) {
			AlertDialog.Builder b = new AlertDialog.Builder(Page.this);

			if (id.equals(user_id)) {
				CharSequence[] items = { "Add post", "Share", "Invite friend",
						"Create event", "Create page" };
				b.setItems(items, this);
			} else {
				CharSequence[] items = { "Share", "Create page", "Block/Report" };
				b.setItems(items, this);
			}

			AlertDialog d = b.create();
			d.show();
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		Intent i;

		if (id.equals(user_id)) {
			switch (which) {
			case 0:
				i = new Intent(Page.this, PageAddPost.class);

				i.putExtra(values.TAG_PAGE_PAGE_ID, page_id);

				startActivity(i);
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "share",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "invite friend",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				i = new Intent(Page.this, CreateEvent.class);
				i.putExtra(values.TAG_ID, id);
				i.putExtra(values.TAG_PAGE_PAGE_ID, page_id);
				startActivity(i);
				break;
			case 4:
				i = new Intent(Page.this, CreatePage.class);
				i.putExtra(values.TAG_ID, id);
				startActivity(i);
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
				i = new Intent(Page.this, CreatePage.class);
				i.putExtra(values.TAG_ID, id);
				startActivity(i);
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "Block",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		switch (v.getId()) {
		case R.id.page_followIv:
			if (isEnabled) {
				if (!isFollowed) {
					new follow_task().execute("http://" + values.IP
							+ "/cycleapp/followpage.php", id, page_id);

					followB.setImageResource(R.drawable.unfollow_icon_selector);
					followTV.setText("unFollow");
					isFollowed = true;
					//numberOfLikesTV.setText(Integer.parseInt(numberOfLikesTV.getText().toString()) + 1);
					/*
					 * String temp = pref.getString(values.TAG_PAGE_PREF, "");
					 * if (!temp.equals("")) { temp += "/"; } temp += page_id;
					 * 
					 * Editor edit = pref.edit();
					 * 
					 * edit.putString(values.TAG_PAGE_PREF, temp);
					 * edit.commit();
					 */

				} else {

					new follow_task().execute("http://" + values.IP
							+ "/cycleapp/unfollowpage.php", id, page_id);

					followB.setImageResource(R.drawable.follow_page_icon_selector);
					followTV.setText("Follow");
					isFollowed = false;
					//numberOfLikesTV.setText(Integer.parseInt(numberOfLikesTV.getText().toString()) - 1);

					/*
					 * String temp = pref.getString(values.TAG_PAGE_PREF, "");
					 * String[] data = temp.split("/"); temp = ""; for (String t
					 * : data) { if (!t.equals(page_id)) { if (!temp.equals(""))
					 * { temp += "/"; } temp += t; } }
					 * 
					 * Editor edit = pref.edit();
					 * 
					 * edit.putString(values.TAG_PAGE_PREF, temp);
					 * 
					 * edit.commit();
					 */

				}
			}

			break;
		case R.id.page_messageIv:

			break;
		case R.id.page_inviteIv:

			break;

		case R.id.page_moreInfoV:
			i = new Intent(Page.this, Page_more_info.class);

			i.putExtra(values.TAG_PAGE_FOUNDED, founded);
			i.putExtra(values.TAG_PAGE_LOCATION, location);
			i.putExtra(values.TAG_PAGE_PRODUCT, product);
			i.putExtra(values.TAG_PAGE_NAME, pageName);
			i.putExtra(values.TAG_PAGE_DESCRIPTION, description);

			startActivity(i);
			break;
		default:
			break;
		}
	}

	private class follow_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start follow");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_ID, params[1]));
			param.add(new BasicNameValuePair(values.TAG_PAGE_PAGE_ID, params[2]));

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
				Log.i("HA", "follow : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		return false;
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

			String text, pageName, day, feed_id, like_ids;
			int numLikes, numComments;
			// Bitmap coverImg, pageImg;

			Log.i("HA", "start fetch feeds");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_PAGE_PAGE_ID, params[1]));
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

			if (feedsAdapter != null)
				feedsAdapter.notifyDataSetChanged();

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

			isEnabled = true;
			if (followers.contains(id)) {
				isFollowed = true;
				followB.setImageResource(R.drawable.unfollow_icon_selector);
				followTV.setText("unFollow");
			} else {
				isFollowed = false;
				followB.setImageResource(R.drawable.follow_page_icon_selector);
				followTV.setText("Follow");
			}

			pageNameTV.setText(pageName);
			numberOfLikesTV.setText("" + numFollowers);
		}

	}

}
