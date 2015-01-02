package com.example.test_bike;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import beans.values;

public class Page_more_info extends Activity {

	Bundle b;

	String name, product, description, location, founded;

	TextView foundedTV, productTV, descriptionTV, locationTV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_more_info);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		
		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();

				name = b.getString(values.TAG_PAGE_NAME);
				product = b.getString(values.TAG_PAGE_PRODUCT);
				description = b.getString(values.TAG_PAGE_DESCRIPTION);
				location = b.getString(values.TAG_PAGE_LOCATION);
				founded = b.getString(values.TAG_PAGE_FOUNDED);
			}
		}
		getActionBar().setTitle(name);
		
		foundedTV = (TextView) findViewById(R.id.page_more_info_FoundedTv);
		productTV = (TextView) findViewById(R.id.page_more_info_ProductTv);
		descriptionTV = (TextView) findViewById(R.id.page_more_info_AboutTv);
		locationTV = (TextView) findViewById(R.id.page_more_info_LocationTv);
		
		foundedTV.setText(founded);
		productTV.setText(product); 
		descriptionTV.setText(description);
		locationTV.setText(location);

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.page_more_info, menu);
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
}
