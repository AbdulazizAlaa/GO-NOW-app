package com.example.test_bike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import beans.serviceHandler;
import beans.values;

public class StoreAddNewBike extends Activity {

	TextView addPhotoTV;
	EditText descriptionED, priceED, phoneED, emailED, typeED;

	String user_id;
	Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_add_new_bike);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();
				user_id = b.getString(values.TAG_ID);
			}
		}
		
		addPhotoTV = (TextView) findViewById(R.id.store_add_new_bike_addphotoTV);

		descriptionED = (EditText) findViewById(R.id.store_add_new_bike_descriptionED);
		priceED = (EditText) findViewById(R.id.store_add_new_bike_priceED);
		phoneED = (EditText) findViewById(R.id.store_add_new_bike_phoneED);
		emailED = (EditText) findViewById(R.id.store_add_new_bike_emailED);
		typeED = (EditText) findViewById(R.id.store_add_new_bike_typeED);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_add_new_bike, menu);
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
		} else if (id == R.id.store_add_new_bike_addB) {
			// add button
			new add_bike_task().execute("http://" + values.IP
					+ "/cycleapp/addstorebike.php", user_id, typeED.getText()
					.toString(), descriptionED.getText().toString(), priceED
					.getText().toString(), phoneED.getText().toString(),
					emailED.getText().toString());
		}
		return super.onOptionsItemSelected(item);
	}

	private class add_bike_task extends AsyncTask<String, Void, Void> {

		ProgressDialog progress;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(StoreAddNewBike.this);
			progress.setCancelable(false);
			progress.setMessage("Please Wait...");
			progress.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start add bike");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_STORE_USER_ID,
					params[1]));
			param.add(new BasicNameValuePair(values.TAG_STORE_BIKE_TYPE,
					params[2]));
			param.add(new BasicNameValuePair(values.TAG_STORE_DESCRIPTION,
					params[3]));
			param.add(new BasicNameValuePair(values.TAG_STORE_PRICE, params[4]));
			param.add(new BasicNameValuePair(values.TAG_STORE_PHONE, params[5]));
			param.add(new BasicNameValuePair(values.TAG_STORE_EMAIL, params[6]));

			Log.i("HA", "add new bike : " + params[1]);

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
				Log.i("HA", "update : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progress.isShowing()) {
				progress.dismiss();
			}
			StoreAddNewBike.this.finish();
		}

	}

}
