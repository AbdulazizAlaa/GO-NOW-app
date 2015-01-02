package com.example.test_bike;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import beans.Essentials;
import beans.values;

public class EventRoute extends Activity {

	GoogleMap map;
	
	ArrayList<LatLng> route;
	
	ArrayList<MarkerOptions> mOptionsList;
	PolylineOptions polyOptions;
	int counter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_route);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
	
		route = new ArrayList<LatLng>();
		mOptionsList = new ArrayList<MarkerOptions>();
		counter = 0;
		
		polyOptions = new PolylineOptions().color(Color.RED).width(3).visible(true);
		
		if(map == null){
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.event_route_map)).getMap();
			map.setMyLocationEnabled(true);
		}
		
		map.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng l) {
				// TODO Auto-generated method stub
				
				MarkerOptions mOptions = new MarkerOptions().flat(true).title("Marker "+counter++).position(l);
				
				mOptionsList.add(mOptions);
				
				polyOptions.add(l);
				if(map != null){
					map.addMarker(mOptions);
					map.addPolyline(polyOptions);
				}
				route.add(l);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_route, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int mid = item.getItemId();
		if (mid == R.id.action_settings) {
			return true;
		}else if(mid == R.id.event_route_doneB){
			Intent i = new Intent();
			i.putExtra(values.TAG_RIDE_LAT, Essentials.decodeRouteLat(route));
			i.putExtra(values.TAG_RIDE_LNG, Essentials.decodeRouteLng(route));
			setResult(RESULT_OK,i);
			finish();
		}else if (mid == android.R.id.home) {
			setResult(RESULT_CANCELED);
			finish();
			// NavUtils.navigateUpFromSameTask(this);
		}
		return super.onOptionsItemSelected(item);
	}
}
