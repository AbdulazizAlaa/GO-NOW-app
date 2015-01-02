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
import android.view.View.OnClickListener;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import beans.serviceHandler;
import beans.values;

public class SignInUpActivity extends Activity implements OnClickListener {

	Button catch_google, signup;
	View signin;
	String username, passwrod, email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in_up);

		catch_google = (Button) findViewById(R.id.sign_in_up_catch_google_B);
		signup = (Button) findViewById(R.id.sign_in_up_signup_B);
		signin = findViewById(R.id.sign_in_up_signin_B);

		signin.setOnClickListener(this);
		signup.setOnClickListener(this);
		catch_google.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in_up, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent i;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sign_in_up_signin_B:
			i = new Intent(SignInUpActivity.this, Sign_in.class);
			startActivity(i);
			break;
		case R.id.sign_in_up_signup_B:
			i = new Intent(SignInUpActivity.this, sign_up.class);
			startActivity(i);
			break;
		case R.id.sign_in_up_catch_google_B:
			
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	private class signup_task extends AsyncTask<String, Void, Void> {

		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(SignInUpActivity.this);
			progress.setCancelable(false);
			progress.setMessage("Member In Progress...");
			progress.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_USERNAME, params[1]));
			param.add(new BasicNameValuePair(values.TAG_PASSWORD, params[2]));
			param.add(new BasicNameValuePair(values.TAG_EMAIL, params[3]));
			// param.add(new BasicNameValuePair(values.TAG_individual,
			// params[4]));
			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {
					// JSONArray json = new JSONArray(response);

					JSONObject jsonMessage = new JSONObject(response);

					String message = jsonMessage.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						String id = jsonMessage.getString(values.TAG_ID);

						SharedPreferences pref = SignInUpActivity.this
								.getSharedPreferences(values.TAG_ACCOUNT_FILE,
										Context.MODE_PRIVATE);
						Editor e = pref.edit();

						e.putString(values.TAG_USERNAME, params[1]);
						e.putString(values.TAG_PASSWORD, params[2]);
						e.putString(values.TAG_EMAIL, params[3]);
						e.putString(values.TAG_ID, id);

						e.commit();

						Intent i = new Intent(SignInUpActivity.this, Home.class);

						i.putExtra(values.TAG_ID, id);
						i.putExtra(values.TAG_USERNAME, params[1]);
						i.putExtra(values.TAG_PASSWORD, params[2]);
						i.putExtra(values.TAG_EMAIL, params[3]);
						i.putExtra(values.TAG_CALLER, "signup");

						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);

						startActivity(i);
						Log.i("HA", "success");

					}
				}

			} catch (Exception e) {
				Log.i("HA", "" + e.getMessage());
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
