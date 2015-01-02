package com.example.test_bike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

import com.andreabaccega.widget.FormEditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import beans.Essentials;
import beans.serviceHandler;
import beans.values;

public class Sign_in extends Activity implements View.OnClickListener,
		OnFocusChangeListener {

	FormEditText username, password;
	boolean usernameFoucs = false;

	Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);

		username = (FormEditText) findViewById(R.id.sign_in_username_ED);
		password = (FormEditText) findViewById(R.id.sign_in_password_ED);
		login = (Button) findViewById(R.id.sign_in_signin_B);

		username.setOnFocusChangeListener(this);
		password.setOnFocusChangeListener(this);

		login.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.sign_in_signin_B:
			FormEditText[] fields = {username, password};
			if (Essentials.FormValid(fields))
				new login_task().execute("http://" + values.IP
						+ "/cycleapp/login.php", username.getText().toString(),
						password.getText().toString());
			break;
		default:

		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.sign_in_username_ED:
			if (!hasFocus)
				username.testValidity();
			break;
		case R.id.sign_in_password_ED:
			if (!hasFocus)
				password.testValidity();
			break;
		default:
			break;
		}

	}

	

	private class login_task extends AsyncTask<String, Void, Void> {

		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(Sign_in.this);
			progress.setCancelable(false);
			progress.setMessage("Login In Progress...");
			progress.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start login");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_USERNAME, params[1]));
			param.add(new BasicNameValuePair(values.TAG_PASSWORD, params[2]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				Log.i("ha", response);
				
				if (!response.equals("")) {
					Log.i("HA", "res");

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {
						Log.i("HA", "message");

						JSONArray userArray = json
								.getJSONArray(values.TAG_DATA);

						JSONObject user = userArray.getJSONObject(0);

						SharedPreferences pref = Sign_in.this
								.getSharedPreferences(values.TAG_ACCOUNT_FILE,
										Context.MODE_PRIVATE);
						Editor e = pref.edit();
						e.putString(values.TAG_ID,
								user.getString(values.TAG_ID));
						e.putString(values.TAG_USERNAME, params[1]);
						e.putString(values.TAG_PASSWORD, params[2]);
						e.putString(values.TAG_EMAIL,
								user.getString(values.TAG_EMAIL));
						e.putString(values.TAG_BIKETYPE,
								user.getString(values.TAG_BIKETYPE));
						e.putString(values.TAG_DISTANCE,
								user.getString(values.TAG_DISTANCE));
						e.putString(values.TAG_PROFILE_IMG, user.getString(values.TAG_PROFILE_IMG));

						// e.putInt(values.TAG_MSPEED,
						// user.getInt(values.TAG_MSPEED));
						// e.putString(values.TAG_FRIENDS,
						// user.getString(values.TAG_FRIENDS));

						e.commit();

						Intent i = new Intent(Sign_in.this, Home.class);

						i.putExtra(values.TAG_ID, user.getString(values.TAG_ID));
						i.putExtra(values.TAG_USERNAME,
								user.getString(values.TAG_USERNAME));
						i.putExtra(values.TAG_EMAIL,
								user.getString(values.TAG_EMAIL));
						i.putExtra(values.TAG_PASSWORD,
								user.getString(values.TAG_PASSWORD));
						i.putExtra(values.TAG_BIKETYPE,
								user.getString(values.TAG_BIKETYPE));
						i.putExtra(values.TAG_DISTANCE,
								user.getString(values.TAG_DISTANCE));
						i.putExtra(values.TAG_PROFILE_IMG, user.getString(values.TAG_PROFILE_IMG));
						// i.putExtra(values.TAG_MSPEED,
						// user.getInt(values.TAG_MSPEED));
						// i.putExtra(values.TAG_FRIENDS,
						// user.getString(values.TAG_FRIENDS));
						i.putExtra(values.TAG_CALLER, "login");

						
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

						startActivity(i);
						Log.i("HA", "success");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "login : " + e.getMessage());
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

		}

	}

}
