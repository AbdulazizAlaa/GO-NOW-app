package com.example.test_bike;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mobisoft.Utils.SpeedPreference;
import com.mobisoft.view.SpeedometerView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import beans.Essentials;
import beans.GPSTracker;
import beans.serviceHandler;
import beans.values;

public class Session extends FragmentActivity implements
		android.view.View.OnClickListener, LocationListener {

	private TextView speedTV, timeTV, distanceTV, StartTimeTV, sScreenSpeedTV,
			sScreenTimeTV, sScreenDistanceTV, fScreenSpeedTV, fScreenTimeTV,
			fScreenDistanceTV, pauseTV;
	private FrameLayout startB, stopB, captureB, pauseB;
	private ImageView img, pauseIV;
	private View startedView, startTimeV, stopTimeV, normalScreenV,
			fullscreenV;

	private Timer sessionTimer;

	private GoogleMap map;
	private LocationManager locationManager;
	private CameraPosition bikeCamPosition;
	private Location bikeLocation, startLocation, previousLocation;

	private double rideDistance, rideMaxSpeed;
	private int rideTime;

	private DecimalFormat myFormater;
	private boolean startButtonEnabled;

	private ArrayList<MarkerOptions> bikemarkers;
	MarkerOptions bikeMarker;
	PolylineOptions polyOptions, routePolyOptions;

	ArrayList<LatLng> route, rideRoute;

	private GPSTracker Gps;

	private Bundle b;
	private String user_id, caller, route_lat, route_lng;

	Typeface squareFont;
	int counter = 5;
	Timer startTimeViewTimer;
	boolean fullscreen = false, paused = false;

	// wakelock
	WakeLock wakeLock;

	// ** SpeedoMeter **/
	private long delay = 10000;
	SpeedometerView mView;
	ImageView arrowImag;
	static Handler handler;
	String result;
	int startAngle;
	String priceStr, speedValueStr;
	Thread threadMainMeter;
	SpeedPreference speedPreference;

	// ** End SpeedoMeter **/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_session);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setLogo(R.drawable.session_icon);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(R.drawable.session_icon);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();

				user_id = b.getString(values.TAG_ID);

				caller = b.getString(values.TAG_CALLER);

				if (caller.equals("ride")) {
					route_lat = b.getString(values.TAG_RIDE_LAT);
					route_lng = b.getString(values.TAG_RIDE_LNG);
					getWindow().addFlags(
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					Log.i("ha", "no wakelock");
				} else {
					route_lat = "";
					route_lng = "";
				}

			}
		}

		// ** SpeedoMeter **/

		/*initUI();

		speedPreference = new SpeedPreference(this);

		speedPreference.setPreviousNeedleValue("0");

		// moveNeedle();
*/
		// ** End SpeedoMeter **/

		squareFont = Typeface.createFromAsset(getAssets(),
				"HallandaleSquare.ttf");

		route = new ArrayList<LatLng>();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		myFormater = new DecimalFormat("####.##");
		startButtonEnabled = true;
		bikemarkers = new ArrayList<MarkerOptions>();

		rideDistance = rideMaxSpeed = rideTime = 0;

		polyOptions = new PolylineOptions().width(2).color(Color.RED)
				.visible(true);
		routePolyOptions = new PolylineOptions().width(2).color(Color.BLUE)
				.visible(true);

		startB = (FrameLayout) findViewById(R.id.session_startB);
		stopB = (FrameLayout) findViewById(R.id.session_stopB);
		pauseB = (FrameLayout) findViewById(R.id.session_pauseB);
		// captureB = (Button) findViewById(R.id.session_imgCaptureB);

		// img = (ImageView) findViewById(R.id.session_imgIV);

		pauseIV = (ImageView) findViewById(R.id.session_pause_imageB);

		pauseTV = (TextView) findViewById(R.id.session_pause_titleB);

		speedTV = (TextView) findViewById(R.id.session_SpeedTV);
		timeTV = (TextView) findViewById(R.id.session_TimeTV);
		distanceTV = (TextView) findViewById(R.id.session_distanceTV);
		StartTimeTV = (TextView) findViewById(R.id.session_start_timeTV);

		sScreenSpeedTV = (TextView) findViewById(R.id.session_stop_SpeedTV);
		sScreenTimeTV = (TextView) findViewById(R.id.session_stop_TimeTV);
		sScreenDistanceTV = (TextView) findViewById(R.id.session_stop_distanceTV);

		fScreenSpeedTV = (TextView) findViewById(R.id.session_fullscreen_SpeedTV);
		fScreenTimeTV = (TextView) findViewById(R.id.session_fullscreen_TimeTV);
		fScreenDistanceTV = (TextView) findViewById(R.id.session_fullscreen_distanceTV);

		speedTV.setTypeface(squareFont);
		timeTV.setTypeface(squareFont);
		distanceTV.setTypeface(squareFont);
		StartTimeTV.setTypeface(squareFont);

		sScreenSpeedTV.setTypeface(squareFont);
		sScreenTimeTV.setTypeface(squareFont);
		sScreenDistanceTV.setTypeface(squareFont);

		fScreenSpeedTV.setTypeface(squareFont);
		fScreenTimeTV.setTypeface(squareFont);
		fScreenDistanceTV.setTypeface(squareFont);

		startedView = findViewById(R.id.session_StartedView);
		startTimeV = findViewById(R.id.session_startV);
		stopTimeV = findViewById(R.id.session_stopV);
		normalScreenV = findViewById(R.id.session_normalScreenV);
		fullscreenV = findViewById(R.id.session_fullscreenV);

		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.session_mapFragment)).getMap();
			map.setMyLocationEnabled(true);
		}

		if (caller.equals("ride")) {
			if (!route_lat.equals("") && !route_lng.equals("")) {
				rideRoute = Essentials.getRouteLatLng(route_lat, route_lng);

				if (map != null) {
					CameraPosition cam = new CameraPosition.Builder()
							.target(rideRoute.get(0)).zoom(13).tilt(25)
							.bearing(0).build();

					map.animateCamera(CameraUpdateFactory
							.newCameraPosition(cam));

					MarkerOptions mOptions = new MarkerOptions().flat(true)
							.title("Start").visible(true)
							.position(rideRoute.get(0));
					map.addMarker(mOptions);
					mOptions = new MarkerOptions().flat(true).title("Finish")
							.visible(true)
							.position(rideRoute.get(rideRoute.size() - 1));
					map.addMarker(mOptions);

					routePolyOptions.addAll(rideRoute);
					map.addPolyline(routePolyOptions);
				}
			}
		}

		stopB.setOnClickListener(this);
		startB.setOnClickListener(this);
		pauseB.setOnClickListener(this);
		// captureB.setOnClickListener(this);

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng l) {
				// TODO Auto-generated method stub
				if (!fullscreen) {
					normalScreenV.setVisibility(View.GONE);
					fullscreenV.setVisibility(View.VISIBLE);
					fullscreen = !fullscreen;
				} else {
					normalScreenV.setVisibility(View.VISIBLE);
					fullscreenV.setVisibility(View.GONE);
					fullscreen = !fullscreen;
				}
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.session_mapFragment)).getMap();
			map.setMyLocationEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		/*
		 * case R.id.session_imgCaptureB: if (map != null) { Bitmap screenShot =
		 * Essentials.takeScreenshot(map, findViewById(R.id.session_content));
		 * img.setImageBitmap(screenShot); }
		 * 
		 * 
		 * View v1 = findViewById(R.id.session_mapFragment);
		 * 
		 * // View rootView = v1.getRootView();
		 * 
		 * v1.setDrawingCacheEnabled(true);
		 * 
		 * Bitmap m =
		 * Bitmap.createBitmap(v1.getDrawingCache());//rootView.getDrawingCache
		 * (); img.setImageBitmap(m);
		 * 
		 * 
		 * break;
		 */
		case R.id.session_pauseB:

			if (!paused) {
				pauseIV.setImageResource(R.drawable.resume_button_sessionstarted);
				pauseTV.setText("RESUME");
				if (Gps != null)
					Gps.stopUsingGPS();

				{
					sessionTimer.cancel();
					sessionTimer.purge();
					sessionTimer = null;
				}
				startButtonEnabled = true;

				paused = !paused;
			} else {
				pauseIV.setImageResource(R.drawable.pause_button_sessionstarted);
				pauseTV.setText("PAUSE");

				startB.performClick();

				startButtonEnabled = false;

				paused = !paused;
			}

			break;
		case R.id.session_stopB:

			stopTimeV.setVisibility(View.VISIBLE);

			// wakelock
			if (!caller.equals("ride")) {
				wakeLock.release();
				Log.i("ha", "wakelock");
			}

			if (Gps != null)
				Gps.stopUsingGPS();

			if (sessionTimer != null) {
				sessionTimer.cancel();
				sessionTimer.purge();
				sessionTimer = null;
			}

			if (map != null) {
				bikeMarker = new MarkerOptions().title("Stoped").position(
						new LatLng(map.getMyLocation().getLatitude(), map
								.getMyLocation().getLongitude()));
				bikemarkers.add(bikeMarker);
				map.addMarker(bikeMarker);
			}

			String final_lat = Essentials.decodeRouteLat(route);
			String final_lng = Essentials.decodeRouteLng(route);

			new add_session_task().execute("http://" + values.IP
					+ "/cycleapp/addsession.php", user_id, rideDistance + "",
					rideMaxSpeed + "", rideTime + "", final_lat, final_lng);

			break;
		case R.id.session_startB:

			if (startButtonEnabled) {

				Gps = new GPSTracker(Session.this);

				if (Gps.getLocation() == null) {
					Gps = null;
				} else {

					// wakelock
					if (!caller.equals("ride")) {
						PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
						wakeLock = powerManager
								.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
										"MyWakelockTag");
						wakeLock.acquire();
						Log.i("ha", "wakelock");
					}

					startB.setVisibility(View.GONE);
					startedView.setVisibility(View.VISIBLE);
					startTimeV.setVisibility(View.VISIBLE);

					startTimeViewTimer = new Timer();
					startTimeViewTimer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							runOnUiThread(new Runnable() {
								public void run() {
									if (counter >= 0) {
										// counter--;
										StartTimeTV.setText(counter-- + "");
									} else {
										startTimeV.setVisibility(View.GONE);
										startTimeViewTimer.cancel();
									}
								}
							});

						}
					}, 1000, 1000);

					sessionTimer = new Timer();
					startButtonEnabled = false;

					locationManager = Gps.getLocationManager();

					bikeLocation = Gps.getLocation();

					if (map != null) {
						// LatLng myLocation = new
						// LatLng(map.getMyLocation().getLatitude(),
						// map.getMyLocation().getLongitude());

						bikeLocation = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);

						bikeMarker = new MarkerOptions().title("spotted")
								.position(
										new LatLng(bikeLocation.getLatitude(),
												bikeLocation.getLongitude()));
						bikemarkers.add(bikeMarker);
						map.addMarker(bikeMarker);

						bikeCamPosition = new CameraPosition.Builder()
								.target(new LatLng(bikeLocation.getLatitude(),
										bikeLocation.getLongitude())).zoom(13)
								.build();
						map.animateCamera(CameraUpdateFactory
								.newCameraPosition(bikeCamPosition));

						startLocation = bikeLocation;
					}

					// Timer
					sessionTimer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							rideTime++;
							runOnUiThread(new Runnable() {
								public void run() {
									timeTV.setText(rideTime / 60 + " : "
											+ rideTime % 60);
									sScreenTimeTV.setText(rideTime / 60 + " : "
											+ rideTime % 60);
									fScreenTimeTV.setText(rideTime / 60 + " : "
											+ rideTime % 60);
									if (bikeLocation != null) {
										if (bikeLocation.hasSpeed()) {

											if (bikeLocation.getSpeed() > rideMaxSpeed) {
												rideMaxSpeed = bikeLocation
														.getSpeed();
											}

											speedTV.setText(myFormater
													.format(bikeLocation
															.getSpeed()));
											sScreenSpeedTV.setText(myFormater
													.format(bikeLocation
															.getSpeed()));
											fScreenSpeedTV.setText(myFormater
													.format(bikeLocation
															.getSpeed()));
										}

										distanceTV.setText(myFormater
												.format(rideDistance / 1000));
										sScreenDistanceTV.setText(myFormater
												.format(rideDistance / 1000));
										fScreenDistanceTV.setText(myFormater
												.format(rideDistance / 1000));
									}

								}
							});
						}
					}, 1000, 1000);
				}
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location l) {
		// TODO Auto-generated method stub

		if (counter == 0) {
			bikeMarker = new MarkerOptions().title("spotted").position(
					new LatLng(bikeLocation.getLatitude(), bikeLocation
							.getLongitude()));
			bikemarkers.add(bikeMarker);
			map.addMarker(bikeMarker);
			counter--;
		}

		previousLocation = bikeLocation;

		bikeLocation = l;

		rideDistance += bikeLocation.distanceTo(previousLocation);

		polyOptions.add(new LatLng(l.getLatitude(), l.getLongitude()));
		if (map != null)
			map.addPolyline(polyOptions);

		route.add(new LatLng(l.getLatitude(), l.getLongitude()));

		bikeCamPosition = new CameraPosition.Builder()
				.target(new LatLng(l.getLatitude(), l.getLongitude()))
				.zoom(map.getCameraPosition().zoom)
				.tilt(map.getCameraPosition().tilt).bearing(0).build();

		map.animateCamera(CameraUpdateFactory
				.newCameraPosition(bikeCamPosition));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.session, menu);
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

	private class add_session_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start add_session");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_SESSION_USER_ID,
					params[1]));
			param.add(new BasicNameValuePair(values.TAG_SESSION_DISTANCE,
					params[2]));
			param.add(new BasicNameValuePair(values.TAG_SESSION_SPEED,
					params[3]));
			param.add(new BasicNameValuePair(values.TAG_SESSION_TIME, params[4]));
			param.add(new BasicNameValuePair(values.TAG_SESSION_ROUTE_LAT,
					params[5]));
			param.add(new BasicNameValuePair(values.TAG_SESSION_ROUTE_LNG,
					params[6]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {
					Log.i("HA", "res");

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {
						Log.i("HA", "success");
					} else {
						Log.i("HA", "failure");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "home : " + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}

	}

	// ** SpeedoMeter **/

	/*private void moveNeedle() {

		handler = new Handler() {

			public void handleMessage(android.os.Message msg) {

				Bundle b = msg.getData();
				int key = b.getInt("angle_in_degrees", 0);

				if (key == 0) {

				} else {
					mView.calculateAngleOfDeviation(key);
				}

			};
		};
		handler.postDelayed(null, delay);

		threadMainMeter = new Thread(new Runnable() {

			@Override
			public void run() {

				startAngle = Integer.parseInt(speedPreference
						.getPreviousNeedleValue());

				generateValue();

				if (Integer.parseInt(speedValueStr) > 100) {
					speedValueStr = "100";
				}
				if (startAngle > Integer.parseInt(speedValueStr)) {
					for (int i = startAngle; i >= Integer
							.parseInt(speedValueStr); i = i - 1) {

						try {
							Thread.sleep(15);
							Message msg = new Message();
							Bundle b = new Bundle();
							b.putInt("angle_in_degrees", i);
							msg.setData(b);
							handler.sendMessage(msg);

						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					}

				} else {
					for (int i = startAngle; i <= Integer
							.parseInt(speedValueStr); i = i + 1) {

						try {
							Thread.sleep(15);
							Message msg = new Message();
							Bundle b = new Bundle();
							b.putInt("angle_in_degrees", i);
							msg.setData(b);
							handler.sendMessage(msg);

						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					}
				}
			}
		});

		threadMainMeter.start();

	}

	private void generateValue() {

		Random r = new Random();
		speedValueStr = String.valueOf(r.nextInt(100 - 10) + 10);
		speedPreference.setPreviousNeedleValue(speedValueStr);

	}

	private void initUI() {

		mView = (SpeedometerView) findViewById(R.id.session_speedometer_view);

	}*/

	// ** End of SpeedoMeter **/

}
