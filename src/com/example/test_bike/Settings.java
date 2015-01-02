package com.example.test_bike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import beans.serviceHandler;
import beans.values;

public class Settings extends Activity implements OnClickListener {

	Button notificationOnB, notificationOffB, saveB, cancelB;
	EditText changeEmailTF, changePasswordTF;
	ImageView soundB, notificationB;
	
	boolean soundOn, notificationOn;
	String id;
	Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

		
		if(getIntent()!=null){
			if(getIntent().getExtras()!=null){
				b = getIntent().getExtras();
				id = b.getString(values.TAG_ID);
			}
		}
		

		
		soundB = (ImageView) findViewById(R.id.settings_sound_no_yesIV);
		notificationB = (ImageView) findViewById(R.id.settings_notification_no_yesIV);
		
		cancelB = (Button) findViewById(R.id.settings_cancelB);
		saveB = (Button) findViewById(R.id.settings_saveB);
		
		changeEmailTF = (EditText) findViewById(R.id.settings_change_emailTF);
		changePasswordTF = (EditText) findViewById(R.id.settings_change_passwordTF);
		
		soundB.setOnClickListener(this);
		notificationB.setOnClickListener(this);
		cancelB.setOnClickListener(this);
		saveB.setOnClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
		}else if(id == android.R.id.home){
			finish();
			//NavUtils.navigateUpFromSameTask(this);
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.settings_cancelB:
			finish();
			break;
		case R.id.settings_saveB:
			SharedPreferences pref = Settings.this.getSharedPreferences(values.TAG_ACCOUNT_FILE, Context.MODE_PRIVATE);
			Editor e = pref.edit();
			
			e.putBoolean(values.TAG_SOUND, soundOn);
			e.putBoolean(values.TAG_NOTIFICATION, notificationOn);
			
			e.commit();
			
			if(!changeEmailTF.getText().toString().equals("") || !changePasswordTF.getText().toString().equals(""))
			{
				new update_info_task().execute("http://"+values.IP+"/cycleapp/update_info.php", id, changeEmailTF.getText().toString(), changePasswordTF.getText().toString());
			}else{
				Settings.this.finish();
			}
			break;
		case R.id.settings_sound_no_yesIV:
			if(soundOn){
				soundOn = false;
				soundB.setPressed(false);
			}else{
				soundOn = true;
				soundB.setPressed(true);
			}
			break;
		
		case R.id.settings_notification_no_yesIV:
			if(notificationOn){
				notificationOn = false;
				notificationB.setPressed(false);
			}else{
				notificationOn = true;
				notificationB.setPressed(true);
			}
			break;
		

		default:
			break;
		}
	}
	
	private class update_info_task extends AsyncTask<String, Void, Void> {

		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*progress = new ProgressDialog(Settings.this);
			progress.setCancelable(false);
			progress.setMessage("Update In Progress...");
			progress.show();*/
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start update");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_ID, params[1]));
			
			if(!changeEmailTF.getText().toString().equals(""))
				param.add(new BasicNameValuePair(values.TAG_EMAIL, params[2]));
			
			if(!changePasswordTF.getText().toString().equals(""))
				param.add(new BasicNameValuePair(values.TAG_PASSWORD, params[3]));
			
			Log.i("HA", "settings : " + params[1]);
			
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
			/*if (progress.isShowing()) {
				progress.dismiss();
			}*/
			Settings.this.finish();
		}

	}
	
}
