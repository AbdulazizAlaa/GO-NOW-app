package TabFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.test_bike.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.profile_session_adapter;
import beans.serviceHandler;
import beans.values;
import model.profile_session_data;

public class SessionFragment extends Fragment {

	private ListView sessionsLV;
	private profile_session_adapter sessionsAdapter;
	private ArrayList<profile_session_data> sessionsList;

	private SwipeRefreshLayout SwipeToRefresh;

	private String profileUserId;
	
	
	
	public SessionFragment(String profileUserId) {
		super();
		this.profileUserId = profileUserId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.activity_session_fragment,
				container, false);

		SwipeToRefresh = (SwipeRefreshLayout) rootView
				.findViewById(R.id.sessionTab_SwipeToRefresh);

		SwipeToRefresh.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				sessionsList.clear();
				if (sessionsAdapter != null)
					sessionsAdapter.notifyDataSetChanged();
				new fetch_session_task().execute("http://" + values.IP
						+ "/cycleapp/fetchusersessions.php", profileUserId,
						"refresh");
			}
		});

		//list view
		sessionsLV = (ListView) rootView
				.findViewById(R.id.sessionTab_sessionsLV);

		sessionsList = new ArrayList<profile_session_data>();
		
		sessionsAdapter = new profile_session_adapter(getActivity(),
				sessionsList);

		sessionsLV.setAdapter(sessionsAdapter);
		
		
		new fetch_session_task().execute("http://" + values.IP
				+ "/cycleapp/fetchusersessions.php", profileUserId, "");
		
		return rootView;
	}

	private class fetch_session_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			//sessionsList = new ArrayList<profile_session_data>();

			String session_id, user_id, routeLat, routeLng, date;
			int distance, speed, time;

			Log.i("HA", "start fetch session");
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

						JSONArray sessionssArray = json
								.getJSONArray(values.TAG_DATA);
						Log.i("HA", "array fetched");
						for (int i = 0; i < sessionssArray.length(); i++) {
							JSONObject session = sessionssArray
									.getJSONObject(i);

							session_id = session.getString(values.TAG_SESSION_SESSION_ID);
							user_id = session.getString(values.TAG_SESSION_USER_ID);
							routeLat = session.getString(values.TAG_SESSION_ROUTE_LAT);
							routeLng = session.getString(values.TAG_SESSION_ROUTE_LNG);
							//date = session.getString(values.TAG_SESSION_DATE);
							distance = session.getInt(values.TAG_SESSION_DISTANCE);
							speed = session.getInt(values.TAG_SESSION_SPEED);
							time = session.getInt(values.TAG_SESSION_TIME);

							date = "";
							
							Log.i("HA", "finished session " + i);

							sessionsList.add(new profile_session_data(
									session_id, user_id, routeLat, routeLng,
									date, distance, speed, time));
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

			/*sessionsAdapter = new profile_session_adapter(getActivity(),
					sessionsList);

			sessionsLV.setAdapter(sessionsAdapter);*/
			if(sessionsAdapter != null)
				sessionsAdapter.notifyDataSetChanged();

		}

	}

}
