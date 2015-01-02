package TabFragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.test_bike.R;
import com.example.test_bike.Ride;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.rides_adapter;
import beans.serviceHandler;
import beans.values;
import model.ride_data;

public class ProfileRidesFragment extends Fragment implements OnItemClickListener {

	private ListView rides_lv;
	private ArrayList<ride_data> rides_list;
	private rides_adapter ridesAdapter;
	private SwipeRefreshLayout SwipeToRefresh;

	private Bundle b;
	private String username, password, email, user_id, profileUserId;

	public ProfileRidesFragment(String id) {
		super();
		this.profileUserId = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		if (getActivity().getIntent() != null) {
			if (getActivity().getIntent().getExtras() != null) {
				b = getActivity().getIntent().getExtras();

				username = b.getString(values.TAG_USERNAME);
				password = b.getString(values.TAG_PASSWORD);
				email = b.getString(values.TAG_EMAIL);
				user_id = b.getString(values.TAG_ID);
			}
		}


		View rootView = inflater.inflate(R.layout.activity_rides_tab,
				container, false);

		SwipeToRefresh = (SwipeRefreshLayout) rootView
				.findViewById(R.id.RidesTab_SwipeToRefresh);

		SwipeToRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						rides_list.clear();
						if (ridesAdapter != null)
							ridesAdapter.notifyDataSetChanged();
						new fetch_rides_task().execute("http://" + values.IP
								+ "/cycleapp/fetchuserrides.php", profileUserId,
								"refresh");
					}
				});

		//list view
		rides_lv = (ListView) rootView.findViewById(R.id.ridesTab_RidesLV);

		rides_list = new ArrayList<ride_data>();
		
		ridesAdapter = new rides_adapter(getActivity(), rides_list);

		rides_lv.setAdapter(ridesAdapter);
		
		
		rides_lv.setOnItemClickListener(this);

		new fetch_rides_task().execute("http://" + values.IP
				+ "/cycleapp/fetchuserrides.php", profileUserId, "");

		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
		// TODO Auto-generated method stub

		Intent i = new Intent(getActivity(), Ride.class);

		i.putExtra(values.TAG_USERNAME, username);
		i.putExtra(values.TAG_ID, user_id);
		i.putExtra(values.TAG_RIDE_PAGE_ID, rides_list.get(pos).getPage_id());
		i.putExtra(values.TAG_RIDE_USER_ID, rides_list.get(pos).getUser_id());
		i.putExtra(values.TAG_RIDE_ID, rides_list.get(pos).getRide_id());
		i.putExtra(values.TAG_RIDE_NAME, rides_list.get(pos).getPageTitle());
		i.putExtra(values.TAG_RIDE_CITY, rides_list.get(pos).getCity());
		i.putExtra(values.TAG_RIDE_COUNTRY, rides_list.get(pos).getCountry());
		i.putExtra(values.TAG_RIDE_DAY, rides_list.get(pos).getDay());
		i.putExtra(values.TAG_RIDE_DATE_TIME, rides_list.get(pos).getMonth()
				+ ", " + rides_list.get(pos).getTime());
		i.putExtra(values.TAG_RIDE_DESCRIPTION, rides_list.get(pos)
				.getDescription());
		i.putExtra(values.TAG_RIDE_NUM_GOING, rides_list.get(pos).getNumGoing());
		i.putExtra(values.TAG_RIDE_NUM_MAYBE, rides_list.get(pos).getNumMaybe());
		i.putExtra(values.TAG_RIDE_NUM_DECLINE, rides_list.get(pos)
				.getNumDecline());
		i.putExtra(values.TAG_RIDE_GOING_IDS, rides_list.get(pos)
				.getGoing_ids());
		i.putExtra(values.TAG_RIDE_MAYBE_IDS, rides_list.get(pos)
				.getMaybe_ids());
		i.putExtra(values.TAG_RIDE_DECLINE_IDS, rides_list.get(pos)
				.getDecline_ids());
		i.putExtra(values.TAG_RIDE_TICKET_COUNT, rides_list.get(pos)
				.getTicketCount());
		i.putExtra(values.TAG_RIDE_TICKET_PRICE, rides_list.get(pos)
				.getTicketPrice());
		i.putExtra(values.TAG_RIDE_LAT, rides_list.get(pos).getRoute_lat());
		i.putExtra(values.TAG_RIDE_LNG, rides_list.get(pos).getRoute_lng());

		startActivityForResult(i, 1);
	}

	private class fetch_rides_task extends AsyncTask<String, Void, Void> {


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			//rides_list = new ArrayList<ride_data>();

			String route_lat, route_lng, ride_page_id, ride_user_id, ride_id, title, city, country, day, month, time, description, ticketPrice, ticketCount, numGoing, numMaybe, numDecline, going_ids, maybe_ids, decline_ids;

			Log.i("HA", "start fetch rides");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_ID, params[1]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {
					Log.i("HA", "res");

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						JSONArray ridesArray = json
								.getJSONArray(values.TAG_DATA);
						Log.i("HA", "array fetched");
						for (int i = 0; i < ridesArray.length(); i++) {
							JSONObject ride = ridesArray.getJSONObject(i);

							going_ids = ride
									.getString(values.TAG_RIDE_GOING_IDS);
							maybe_ids = ride
									.getString(values.TAG_RIDE_MAYBE_IDS);
							decline_ids = ride
									.getString(values.TAG_RIDE_DECLINE_IDS);

							ride_page_id = ride
									.getString(values.TAG_RIDE_PAGE_ID);
							ride_user_id = ride
									.getString(values.TAG_RIDE_USER_ID);

							ride_id = ride.getString(values.TAG_RIDE_ID);

							title = ride.getString(values.TAG_RIDE_NAME);

							String datetime = ride
									.getString(values.TAG_RIDE_DATE_TIME);
							String[] datetimearr = datetime.split(",");
							month = "";
							day = "";
							time = "";
							for (int j = 0; j < datetimearr.length; j++) {
								if (j == 0)
									month = datetimearr[0];
								else if (j == 1)
									day = datetimearr[1];
								else if (j == 2)
									time = datetimearr[2];
							}

							String location = ride
									.getString(values.TAG_RIDE_LOCATION);
							String[] loc = location.split(",");
							city = "";
							country = "";
							for (int j = 0; j < loc.length; j++) {
								if (j == 0)
									city = loc[0];
								else if (j == 1)
									country = loc[1];
								else if (j == 2)
									country += "," + loc[2];
							}

							description = ride
									.getString(values.TAG_RIDE_DESCRIPTION);

							ticketPrice = ride
									.getString(values.TAG_RIDE_TICKET_PRICE);
							ticketCount = ride
									.getString(values.TAG_RIDE_TICKET_COUNT);

							numGoing = ride
									.getString(values.TAG_RIDE_NUM_GOING);
							numMaybe = ride
									.getString(values.TAG_RIDE_NUM_MAYBE);
							numDecline = ride
									.getString(values.TAG_RIDE_NUM_DECLINE);

							route_lat = ride.getString(values.TAG_RIDE_LAT);

							route_lng = ride.getString(values.TAG_RIDE_LNG);

							Log.i("HA", "finished ride " + i);

							rides_list.add(new ride_data(ride_id, ride_page_id,
									ride_user_id, title, day, month, datetime,
									city, country, description, ticketPrice,
									ticketCount, numGoing, numMaybe,
									numDecline, going_ids, maybe_ids,
									decline_ids, route_lat, route_lng, null));
						}

						Log.i("HA", "success");
					} else {
						Log.i("HA", "failure");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "home : " + e.getMessage());
			}

			if (params[2].equals("refresh")) {
				SwipeToRefresh.setRefreshing(false);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			/*ridesAdapter = new rides_adapter(getActivity(), rides_list);

			rides_lv.setAdapter(ridesAdapter);*/

			if(ridesAdapter != null)
				ridesAdapter.notifyDataSetChanged();
		}

	}

}
