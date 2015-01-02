package com.example.test_bike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import java.util.Timer;
import java.util.TimerTask;

import beans.values;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
				SharedPreferences pref = SplashActivity.this.getSharedPreferences(values.TAG_ACCOUNT_FILE, Context.MODE_PRIVATE);
				Intent i;
				
				String id = pref.getString(values.TAG_ID, "");
				String username = pref.getString(values.TAG_USERNAME, "");
				String password = pref.getString(values.TAG_PASSWORD, "");
				String email = pref.getString(values.TAG_EMAIL, "");
				String bikeType = pref.getString(values.TAG_BIKETYPE, "");
				String distance = pref.getString(values.TAG_DISTANCE, "");
				int mSpeed = pref.getInt(values.TAG_MSPEED, 0);
				String friends = pref.getString(values.TAG_FRIENDS, "");
				String file_path = pref.getString(values.TAG_PROFILE_IMG, "");

				Log.i("HA", username+" splash "+id);
				
				if(!username.equals("") && !password.equals("")){
					i = new Intent(SplashActivity.this, Home.class);
					
					i.putExtra(values.TAG_ID, id);
					i.putExtra(values.TAG_USERNAME, username);
					i.putExtra(values.TAG_EMAIL, email);
					i.putExtra(values.TAG_PASSWORD, password);
					i.putExtra(values.TAG_BIKETYPE, bikeType);
					i.putExtra(values.TAG_DISTANCE, distance);
					i.putExtra(values.TAG_MSPEED, mSpeed);
					i.putExtra(values.TAG_FRIENDS, friends);
					i.putExtra(values.TAG_PROFILE_IMG, file_path);
					i.putExtra(values.TAG_CALLER, "login");
					
				}else{
					i = new Intent(SplashActivity.this, SignInUpActivity.class);
				}

				/*DialogFragment dialogFrag = new ip_dialog(i);		
				dialogFrag.show(getFragmentManager(), "php");
				*/
				startActivity(i);
				SplashActivity.this.finish();
			}
		}, 1500);
 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
}
