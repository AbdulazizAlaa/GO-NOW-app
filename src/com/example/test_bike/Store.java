package com.example.test_bike;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.store_adapter;
import beans.serviceHandler;
import beans.values;
import model.store_data;

public class Store extends Activity implements OnItemClickListener,
		OnClickListener {

	private ListView store_lv;
	private ArrayList<store_data> data_list;
	private store_adapter adapter;

	private String user_id;
	private Bundle b;

	private FrameLayout addNewV;
	private SwipeRefreshLayout SwipeToRefresh;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setLogo(R.drawable.store_icon);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(R.drawable.store_icon);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();
				user_id = b.getString(values.TAG_ID);
			}
		}


		//list view
		store_lv = (ListView) findViewById(R.id.Store_lV);

		data_list = new ArrayList<store_data>();

		adapter = new store_adapter(Store.this, data_list);

		store_lv.setAdapter(adapter);
		
		
		
		addNewV = (FrameLayout) findViewById(R.id.Store_addV);

		addNewV.setOnClickListener(this);

		
		SwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.store_SwipeToRefresh);

		SwipeToRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						data_list.clear();
						if(adapter != null)
							adapter.notifyDataSetChanged();
						new fetch_bikes_task().execute("http://" + values.IP
								+ "/cycleapp/fetchstorebikes.php", "refresh");
					}
				});

		new fetch_bikes_task().execute("http://" + values.IP
				+ "/cycleapp/fetchstorebikes.php", "");

		store_lv.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store, menu);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Store_addV:
			Intent i = new Intent(Store.this, StoreAddNewBike.class);

			i.putExtra(values.TAG_ID, user_id);

			startActivity(i);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
		// TODO Auto-generated method stub

		if (pos < data_list.size()) {
			store_data temp = data_list.get(pos);

			Intent i = new Intent(Store.this, Store_bike_page.class);

			i.putExtra(values.TAG_STORE_BIKE_TYPE, temp.getTypeOfBike());

			i.putExtra(values.TAG_STORE_DESCRIPTION, temp.getDescription());

			i.putExtra(values.TAG_STORE_PRICE, temp.getPrice());

			i.putExtra(values.TAG_STORE_PHONE, temp.getPhone());

			i.putExtra(values.TAG_STORE_EMAIL, temp.getEmail());

			i.putExtra(values.TAG_STORE_BIKE_ID, temp.getBike_id());

			startActivity(i);
		}

	}

	private class fetch_bikes_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			String biketype, description, price, phone, email, bike_id;

			Log.i("HA", "start fetch bikes");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST);

				if (!response.equals("")) {
					Log.i("HA", "res");

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						JSONArray bikesArray = json
								.getJSONArray(values.TAG_DATA);
						Log.i("HA", "array fetched");

						for (int i = 0; i < bikesArray.length(); i++) {
							JSONObject bike = bikesArray.getJSONObject(i);

							biketype = bike
									.getString(values.TAG_STORE_BIKE_TYPE);

							description = bike
									.getString(values.TAG_STORE_DESCRIPTION);

							price = bike.getString(values.TAG_STORE_PRICE);

							phone = bike.getString(values.TAG_STORE_PHONE);

							email = bike.getString(values.TAG_STORE_EMAIL);

							bike_id = bike.getString(values.TAG_STORE_BIKE_ID);

							data_list.add(new store_data(biketype, price,
									description, phone, email, bike_id, null));
						}

						Log.i("HA", "success");
					} else {
						Log.i("HA", "failure");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "store : " + e.getMessage());
			}
			if (params[1].equals("refresh")) {
				SwipeToRefresh.setRefreshing(false);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			/*adapter = new store_adapter(Store.this, data_list);

			store_lv.setAdapter(adapter);*/
			if(adapter != null)
				adapter.notifyDataSetChanged();

		}

	}

}
