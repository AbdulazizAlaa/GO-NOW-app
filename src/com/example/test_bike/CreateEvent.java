package com.example.test_bike;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import beans.serviceHandler;
import beans.values;

public class CreateEvent extends Activity implements OnClickListener{

	private EditText nameTF, locationTF, datetimeTF, descriptionTF,
			ticketPriceTF, ticketCountTF;

	private Button mapB;
	
	String id, page_id, routeLat, routeLng;

	Bundle b;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();
				id = b.getString(values.TAG_ID);
				page_id = b.getString(values.TAG_PAGE_PAGE_ID);
			}
		}
		
		routeLat = "";
		routeLng = "";

		nameTF = (EditText) findViewById(R.id.Createevent_NameTF);
		locationTF = (EditText) findViewById(R.id.Createevent_LocationTF);
		datetimeTF = (EditText) findViewById(R.id.Createevent_daytimeTF);
		descriptionTF = (EditText) findViewById(R.id.Createevent_descriptionTF);
		ticketPriceTF = (EditText) findViewById(R.id.createevent_ticketpriceTF);
		ticketCountTF = (EditText) findViewById(R.id.createevent_ticketcountTF);

		mapB = (Button) findViewById(R.id.Createevent_mapB);
		
		mapB.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Createevent_mapB:
			Intent i = new Intent(CreateEvent.this, EventRoute.class);
			
			startActivityForResult(i, 1);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if(requestCode == 1 && resultCode == RESULT_OK){
			//map route result
			
			routeLat = data.getStringExtra(values.TAG_RIDE_LAT);
			routeLng = data.getStringExtra(values.TAG_RIDE_LNG);
			
			Log.i("ha", routeLat+":"+routeLng);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int mId = item.getItemId();
		if (mId == R.id.action_settings) {
			return true;
		} else if (mId == R.id.createevent_createB) {
			new create_event_task().execute("http://" + values.IP
					+ "/cycleapp/createevent.php", nameTF.getText().toString(),
					locationTF.getText().toString(), datetimeTF.getText()
							.toString(), ticketCountTF.getText().toString(),
					ticketPriceTF.getText().toString(), descriptionTF.getText()
							.toString(), id, page_id, routeLat, routeLng);
			CreateEvent.this.finish();
		} else if (mId == android.R.id.home) {
			finish();
			// NavUtils.navigateUpFromSameTask(this);
		}
		return super.onOptionsItemSelected(item);
	}

	private class create_event_task extends AsyncTask<String, Void, Void> {

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

			param.add(new BasicNameValuePair(values.TAG_RIDE_NAME, params[1]));
			param.add(new BasicNameValuePair(values.TAG_RIDE_LOCATION,
					params[2]));
			param.add(new BasicNameValuePair(values.TAG_RIDE_DATE_TIME,
					params[3]));
			param.add(new BasicNameValuePair(values.TAG_RIDE_TICKET_COUNT,
					params[4]));
			param.add(new BasicNameValuePair(values.TAG_RIDE_TICKET_PRICE,
					params[5]));
			param.add(new BasicNameValuePair(values.TAG_RIDE_DESCRIPTION,
					params[6]));
			param.add(new BasicNameValuePair(values.TAG_PAGE_USER_ID, params[7]));
			if(!params[8].equals("user")){				
				param.add(new BasicNameValuePair(values.TAG_PAGE_PAGE_ID, params[8]));	
				Log.i("ha","page:"+params[8]);
			}
			param.add(new BasicNameValuePair(values.TAG_RIDE_LAT,
					params[9]));
			param.add(new BasicNameValuePair(values.TAG_RIDE_LNG,
					params[10]));
			
			
			Log.i("HA", "settings : " + params[8]);

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

			//CreateEvent.this.finish();
		}

	}

	
}
