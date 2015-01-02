package com.example.test_bike;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import beans.values;

public class Store_bike_page extends Activity {

	private TextView descriptionTV, priceTV, phoneTV, emailTV;

	private String bikeType, description, bikeId, price, phone, email;
	private Bundle b;

	Typeface squareFont;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_bike_page);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();

				bikeId = b.getString(values.TAG_STORE_BIKE_ID);

				description = b.getString(values.TAG_STORE_DESCRIPTION);

				bikeType = b.getString(values.TAG_STORE_BIKE_TYPE);

				price = b.getString(values.TAG_STORE_PRICE);

				phone = b.getString(values.TAG_STORE_PHONE);

				email = b.getString(values.TAG_STORE_EMAIL);

			}
		}
		
		getActionBar().setTitle(bikeType);

		squareFont = Typeface.createFromAsset(getAssets(),
				"HallandaleSquare.ttf");

		descriptionTV = (TextView) findViewById(R.id.store_bike_page_descriptionTV);
		priceTV = (TextView) findViewById(R.id.store_bike_page_priceTV);
		phoneTV = (TextView) findViewById(R.id.store_bike_page_phoneTV);
		emailTV = (TextView) findViewById(R.id.store_bike_page_emailTV);

		descriptionTV.setText(description);
		priceTV.setText(price + " $");
		phoneTV.setText(phone);
		emailTV.setText(email);

		priceTV.setTypeface(squareFont);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_bike_page, menu);
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

}
