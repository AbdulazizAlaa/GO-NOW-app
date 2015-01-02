package TabFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import adapters.feeds_adapter;
import beans.serviceHandler;
import beans.values;
import model.feed_data;

public class FeedsFragment extends Fragment {

	private ListView feeds_lv;
	private ArrayList<feed_data> feeds_list;
	private feeds_adapter feedsAdapter;
	private SwipeRefreshLayout SwipeToRefresh;
	private SharedPreferences pref;

	private Bundle b;
	private String username, password, email, user_id;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.activity_feeds_tab,
				container, false);

		if (getActivity().getIntent() != null) {
			if (getActivity().getIntent().getExtras() != null) {
				b = getActivity().getIntent().getExtras();

				username = b.getString(values.TAG_USERNAME);
				password = b.getString(values.TAG_PASSWORD);
				email = b.getString(values.TAG_EMAIL);
				user_id = b.getString(values.TAG_ID);
				Log.i("HA", username + " feeds " + user_id);
			}
		}
		
		//list view
		feeds_lv = (ListView) rootView.findViewById(R.id.feedsTab_FeedsLV);

		feeds_list = new ArrayList<feed_data>();

		feedsAdapter = new feeds_adapter(getActivity(), username, user_id, feeds_list);

		feeds_lv.setAdapter(feedsAdapter);

		
		

		pref = getActivity().getSharedPreferences(values.TAG_ACCOUNT_FILE,
				Context.MODE_PRIVATE);

		SwipeToRefresh = (SwipeRefreshLayout) rootView
				.findViewById(R.id.FeedsTab_SwipeToRefresh);

		SwipeToRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						feeds_list.clear();
						if(feedsAdapter != null)
							feedsAdapter.notifyDataSetChanged();
						new fetch_feeds_task().execute("http://" + values.IP
								+ "/cycleapp/fetchfeeds.php",
								pref.getString(values.TAG_USERNAME, ""),
								"refresh");
					}
				});

		new fetch_feeds_task().execute("http://" + values.IP
				+ "/cycleapp/fetchfeeds.php",
				pref.getString(values.TAG_USERNAME, ""), "");

		return rootView;
	}

	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
	}



	private class fetch_feeds_task extends AsyncTask<String, Void, Void> {

		//ProgressBar bar ;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
/*			bar = new ProgressBar(getActivity());
			feeds_lv.addHeaderView(bar);*/
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			//feeds_list = new ArrayList<feed_data>();

			String text, pageName, day, feed_id, like_ids;
			int numLikes, numComments;
			// Bitmap coverImg, pageImg;

			Log.i("HA", "start fetch feeds");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_USERNAME, params[1]));

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

							feed_id = ride.getString(values.TAG_FEED_ID);

							like_ids = ride.getString(values.TAG_FEED_LIKE_IDS);
							
							pageName = ride
									.getString(values.TAG_FEED_PAGE_NAME);

							text = ride.getString(values.TAG_FEED_FEED);

							// day = ride.getString(values.TAG_FEED_DATE);

							numLikes = ride
									.getInt(values.TAG_FEED_NUMBER_LIKES);

							numComments = ride
									.getInt(values.TAG_FEED_NUMBER_COMMENTS);

							Log.i("HA", "finished feed " + i);

							feeds_list
									.add(new feed_data(text, pageName, "2",
											feed_id, like_ids, numLikes, numComments,
											null, null));
							
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
			/*feedsAdapter = new feeds_adapter(getActivity(), username, user_id, feeds_list);

			feeds_lv.setAdapter(feedsAdapter);
			feeds_lv.removeHeaderView(bar);*/
			if(feedsAdapter != null)
				feedsAdapter.notifyDataSetChanged();
		}

	}
}
