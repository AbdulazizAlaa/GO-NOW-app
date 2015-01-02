package com.example.test_bike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import beans.serviceHandler;
import beans.values;

public class CreatePage extends Activity {

	private EditText nameTF, locationTF, productTF, foundedTF, descriptionTF;
	private String user_id;
	private Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_page);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		
		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();
				user_id = b.getString(values.TAG_ID);
			}
		}

		nameTF = (EditText) findViewById(R.id.createPage_NameTF);
		locationTF = (EditText) findViewById(R.id.createpage_locationTF);
		productTF = (EditText) findViewById(R.id.createPage_ProductTF);
		foundedTF = (EditText) findViewById(R.id.createPage_FoundedTF);
		descriptionTF = (EditText) findViewById(R.id.createPage_DescriptionTF);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_page, menu);
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
		} else if (id == R.id.createpage_createB) {
			new create_page_task().execute("http://" + values.IP
					+ "/cycleapp/createpage.php", nameTF.getText().toString(),
					productTF.getText().toString(), locationTF.getText()
							.toString(), foundedTF.getText().toString(),
					descriptionTF.getText().toString(), user_id);
			CreatePage.this.finish();
		} else if (id == android.R.id.home) {
			finish();
			// NavUtils.navigateUpFromSameTask(this);
		}
		return super.onOptionsItemSelected(item);
	}

	private class create_page_task extends AsyncTask<String, Void, Void> {

		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*
			 * progress = new ProgressDialog(CreatePage.this);
			 * progress.setCancelable(false);
			 * progress.setMessage("Page In Progress..."); progress.show();
			 */
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start create page");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_PAGE_NAME, params[1]));
			param.add(new BasicNameValuePair(values.TAG_PAGE_PRODUCT, params[2]));
			param.add(new BasicNameValuePair(values.TAG_PAGE_LOCATION,
					params[3]));
			param.add(new BasicNameValuePair(values.TAG_PAGE_FOUNDED, params[4]));
			param.add(new BasicNameValuePair(values.TAG_PAGE_DESCRIPTION,
					params[5]));
			param.add(new BasicNameValuePair(values.TAG_ID, params[6]));

			Log.i("HA", "id : " + params[6]);

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						String page_id = json.getString(values.TAG_ID);
						
						Log.i("HA", "page : " + page_id);
						
						new follow_task().execute("http://" + values.IP
								+ "/cycleapp/followpage.php", params[6], page_id);
						
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
			/*
			 * if (progress.isShowing()) { progress.dismiss(); }
			 */
			//CreatePage.this.finish();
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
}
