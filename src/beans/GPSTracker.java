package beans;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class GPSTracker extends Service {

	private final Context mContext;

	// flag for GPS status
	private boolean isGPSEnabled = false;

	// flag for network status
	private boolean isNetworkEnabled = false;

	private String Provider;

	// flag for GPS status
	private boolean canGetLocation = false;

	private Location location; // location
	private double latitude; // latitude
	private double longitude; // longitude
	private LatLng latLng;

	// The minimum distance to change Updates in meters
	private static final int MIN_DISTANCE_CHANGE_FOR_UPDATES = 15; // 15 meters

	// The minimum time between updates in milliseconds
	private static final int MIN_TIME_BW_UPDATES = 3*1000; // 3 seconds

	// Declaring a Location Manager
	protected LocationManager locationManager;

	public GPSTracker(Context context) {
		this.mContext = context;
		initGPSService();
	}

	public Location initGPSService() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			Log.i("ha", isGPSEnabled?"GPS:true":"GPS:false");
			
			if (!isGPSEnabled) {
				// no GPS provider is enabled
				showSettingsAlert();
			} else{
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES,
						(LocationListener) mContext);
				Log.d("ha", "GPS Enabled");
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();
					}
				}

			}
			
			/*if (!isGPSEnabled && !isNetworkEnabled) {
				// no GPS provider is enabled
				showSettingsAlert();
			} else {
				Criteria c = new Criteria();
				c.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
				c.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
				c.setPowerRequirement(Criteria.ACCURACY_MEDIUM);
				c.setSpeedRequired(true);

				Provider = locationManager.getBestProvider(c, true);

				this.canGetLocation = true;

				// First get location from Network Provider
				if (isNetworkEnabled
						&& Provider.equals(LocationManager.NETWORK_PROVIDER)) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES,
							(LocationListener) mContext);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}

				// if GPS Enabled get lat/long using GPS Services
				else if (isGPSEnabled
						&& Provider.equals(LocationManager.GPS_PROVIDER)) {
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES,
							(LocationListener) mContext);
					Log.d("GPS Enabled", "GPS Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}

				}
			}*/

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("ha", "GPS : "+e.getMessage());
		}

		return location;
	}

	/**
	 * Stop using GPS listener Calling this function will stop using GPS in your
	 * app
	 * */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates((LocationListener) mContext);
		}
	}
	
	public LocationManager getLocationManager(){
		return this.locationManager;
	}
	
	/**
	 * Function to get location
	 * */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	/**
	 * Function to get latlng
	 * */
	public LatLng getLatLng() {
		if (location != null) {
			latLng = new LatLng(location.getLatitude(), location.getLongitude());
		}

		// return latitude
		return latLng;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * 
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	public void showSettingsAlert() {
		AlertDialog.Builder providerDialog = new AlertDialog.Builder(mContext);

		providerDialog
				.setTitle("Provder Dialog")
				.setMessage(
						"GPS is not working! you have to get it working first.")
				.setPositiveButton("make It work", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent i = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(i);
					}
				}).setNegativeButton("cancel", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
		AlertDialog c = providerDialog.create();
		c.show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
