package com.example.test_bike;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;

import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import beans.DatePickerFragment;
import beans.Essentials;
import beans.serviceHandler;
import beans.values;
import model.FileBodyData;
import model.StringBodyData;

public class sign_up extends Activity implements OnClickListener,
		OnFocusChangeListener, OnDateSetListener {

	FormEditText username, password, email;
	TextView birthdateTV;
	ImageView profileImgIV;
	RadioButton maleRB, femaleRB;

	boolean isGenderCorrect = false;

	String genderText, birthdate, id, file_path;

	// image
	int serverResponseCode = 0;
	String imagePath;

	String upLoadServerUri = null;

	Calendar c;
	int year, month, day;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

		imagePath = "";

		c = Calendar.getInstance();

		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		birthdate = day + "-" + getMonth(month) + "-" + year;
		
		username = (FormEditText) findViewById(R.id.sign_up_username_ED);
		email = (FormEditText) findViewById(R.id.sign_up_email_ED);
		password = (FormEditText) findViewById(R.id.sign_up_passowrd_ED);

		birthdateTV = (TextView) findViewById(R.id.sign_up_birthdate_TV);
		
		profileImgIV = (ImageView) findViewById(R.id.sign_up_ProfilePicIV);

		maleRB = (RadioButton) findViewById(R.id.sign_up_gender_maleRB);
		femaleRB = (RadioButton) findViewById(R.id.sign_up_gender_femaleRB);

		profileImgIV.setOnClickListener(this);
		birthdateTV.setOnClickListener(this);
		maleRB.setOnClickListener(this);
		femaleRB.setOnClickListener(this);

		maleRB.performClick();
		
		username.setOnFocusChangeListener(this);
		password.setOnFocusChangeListener(this);
		email.setOnFocusChangeListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub

		if (item.getItemId() == R.id.sign_up_signup_B) {

			FormEditText[] fields = { username, password, email };

			if (Essentials.FormValid(fields)) {
				if (imagePath.equals("")) {
					AlertDialog.Builder imageUploadDialog = new AlertDialog.Builder(sign_up.this);

					imageUploadDialog.setTitle("Profile Image")
							.setMessage("Did you forget to add a profile Image ?!")
							.setPositiveButton("No", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									new normal_signup_task().execute("http://" + values.IP
											+ "/cycleapp/normalsignup.php", username.getText()
											.toString(), password.getText().toString(), email
											.getText().toString(), genderText, birthdate);
								}
							}).setNegativeButton("Yeah", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									profileImgIV.performClick();
								}
							});
					
					AlertDialog d = imageUploadDialog.create();
					d.show();

				} else {
					new signup_task().execute("http://" + values.IP
							+ "/cycleapp/signup.php", username.getText()
							.toString(), password.getText().toString(), email
							.getText().toString(), genderText, birthdate, imagePath);
				}

			}

		} else if (item.getItemId() == android.R.id.home) {
			finish();
			// NavUtils.navigateUpFromSameTask(this);
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sign_up_ProfilePicIV:

			Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(i, "Select Profile Picture:"), 1);

			break;
		case R.id.sign_up_gender_maleRB:
			if (((RadioButton) v).isChecked()) {
				genderText = "male";
			}
			break;
		case R.id.sign_up_gender_femaleRB:
			if (((RadioButton) v).isChecked()) {
				genderText = "female";
			}
			break;
		case R.id.sign_up_birthdate_TV:
			DialogFragment datePicker = new DatePickerFragment(sign_up.this, year, month, day);
			datePicker.show(sign_up.this.getFragmentManager(), "datePicker");
			break;
		default:
			break;
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		this.year = year;
		month = monthOfYear;
		day = dayOfMonth;
		
		birthdate = day + "-" + getMonth(month) + "-" + this.year;
		birthdateTV.setText(birthdate);
	}

	public String getMonth(int num) {
		switch (num) {
		case 0:
			return "Jan";
		case 1:
			return "Feb";
		case 2:
			return "Mar";
		case 3:
			return "Apr";
		case 4:
			return "May";
		case 5:
			return "Jun";
		case 6:
			return "Jul";
		case 7:
			return "Aug";
		case 8:
			return "Sep";
		case 9:
			return "Oct";
		case 10:
			return "Nov";
		case 11:
			return "Dec";
		default:
			return "";
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK) {
			Uri imageUri = data.getData();
			imagePath = Essentials.getRealPathFromURI(imageUri, sign_up.this);
			Toast.makeText(getApplicationContext(), "" + imagePath,
					Toast.LENGTH_SHORT).show();

		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (!hasFocus) {
			switch (v.getId()) {
			case R.id.sign_up_username_ED:
				username.testValidity();
				break;
			case R.id.sign_up_passowrd_ED:
				password.testValidity();
				break;
			case R.id.sign_up_email_ED:
				email.testValidity();
				break;

			default:
				break;
			}
		}

	}

	private class signup_task extends AsyncTask<String, Void, Void> {

		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(sign_up.this);
			progress.setCancelable(false);
			progress.setMessage("Member In Progress...");
			progress.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start");
			serviceHandler sr = new serviceHandler();

			ArrayList<StringBodyData> stringBody = new ArrayList<StringBodyData>();
			ArrayList<FileBodyData> fileBody = new ArrayList<FileBodyData>();

			stringBody.add(new StringBodyData(values.TAG_USERNAME,
					new StringBody(params[1], ContentType.TEXT_PLAIN)));

			stringBody.add(new StringBodyData(values.TAG_PASSWORD,
					new StringBody(params[2], ContentType.TEXT_PLAIN)));

			stringBody.add(new StringBodyData(values.TAG_EMAIL, new StringBody(
					params[3], ContentType.TEXT_PLAIN)));

			stringBody.add(new StringBodyData(values.TAG_GENDER,
					new StringBody(params[4], ContentType.TEXT_PLAIN)));

			stringBody.add(new StringBodyData(values.TAG_BIRTHDATE,
					new StringBody(params[5], ContentType.TEXT_PLAIN)));
			
			fileBody.add(new FileBodyData(values.TAG_PROFILE_IMG, new FileBody(
					new File(params[6]))));

			try {
				String response;

				response = sr.makeServiceCallFiles(params[0], stringBody,
						fileBody);

				Log.i("HA", "res1");
				if (!response.equals("")) {
					// JSONArray json = new JSONArray(response);

					JSONObject jsonMessage = new JSONObject(response);

					String message = jsonMessage.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {
						Log.i("HA", "res");
						id = jsonMessage.getString(values.TAG_ID);
						file_path = jsonMessage
								.getString(values.TAG_PROFILE_IMG);

						SharedPreferences pref = sign_up.this
								.getSharedPreferences(values.TAG_ACCOUNT_FILE,
										Context.MODE_PRIVATE);
						Editor e = pref.edit();

						e.putString(values.TAG_USERNAME, params[1]);
						e.putString(values.TAG_PASSWORD, params[2]);
						e.putString(values.TAG_EMAIL, params[3]);
						e.putString(values.TAG_ID, id);
						e.putString(values.TAG_PROFILE_IMG, file_path);

						e.commit();

						Intent i = new Intent(sign_up.this, Home.class);

						i.putExtra(values.TAG_ID, id);
						i.putExtra(values.TAG_USERNAME, params[1]);
						i.putExtra(values.TAG_PASSWORD, params[2]);
						i.putExtra(values.TAG_EMAIL, params[3]);
						i.putExtra(values.TAG_PROFILE_IMG, file_path);
						i.putExtra(values.TAG_CALLER, "signup");

						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);

						startActivity(i);
						Log.i("HA", "success");

					}
				}

			} catch (Exception e) {
				Log.i("HA", "sign up: " + e.getMessage());
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

	private class normal_signup_task extends AsyncTask<String, Void, Void> {

		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(sign_up.this);
			progress.setCancelable(false);
			progress.setMessage("Member In Progress...");
			progress.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start");
			serviceHandler sr = new serviceHandler();

			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair(values.TAG_USERNAME, params[1]));
			param.add(new BasicNameValuePair(values.TAG_PASSWORD, params[2]));
			param.add(new BasicNameValuePair(values.TAG_EMAIL, params[3]));
			param.add(new BasicNameValuePair(values.TAG_GENDER, params[4]));
			param.add(new BasicNameValuePair(values.TAG_BIRTHDATE, params[5]));


			try {
				String response;

				response = sr.makeServiceCall(params[0], serviceHandler.POST,
						param);

				Log.i("HA", "res1");
				if (!response.equals("")) {
					// JSONArray json = new JSONArray(response);

					JSONObject jsonMessage = new JSONObject(response);

					String message = jsonMessage.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {
						Log.i("HA", "res");
						id = jsonMessage.getString(values.TAG_ID);

						SharedPreferences pref = sign_up.this
								.getSharedPreferences(values.TAG_ACCOUNT_FILE,
										Context.MODE_PRIVATE);
						Editor e = pref.edit();

						e.putString(values.TAG_USERNAME, params[1]);
						e.putString(values.TAG_PASSWORD, params[2]);
						e.putString(values.TAG_EMAIL, params[3]);
						e.putString(values.TAG_ID, id);
						e.putString(values.TAG_PROFILE_IMG, "");

						e.commit();

						Intent i = new Intent(sign_up.this, Home.class);

						i.putExtra(values.TAG_ID, id);
						i.putExtra(values.TAG_USERNAME, params[1]);
						i.putExtra(values.TAG_PASSWORD, params[2]);
						i.putExtra(values.TAG_EMAIL, params[3]);
						i.putExtra(values.TAG_PROFILE_IMG, "");
						i.putExtra(values.TAG_CALLER, "signup");

						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);

						startActivity(i);
						Log.i("HA", "success");

					}
				}

			} catch (Exception e) {
				Log.i("HA", "sign up: " + e.getMessage());
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