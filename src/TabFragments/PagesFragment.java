package TabFragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.test_bike.Page;
import com.example.test_bike.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import beans.serviceHandler;
import beans.values;
import model.PageData;

public class PagesFragment extends Fragment {

	ListView pagesLV;

	ArrayList<String> pagesNamesList;
	ArrayList<PageData> pagesList;

	ArrayAdapter<String> adapter;

	String id, searchWord;
	Bundle b;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.activity_pages_fragment,
				container, false);

		

		if (getActivity().getIntent() != null) {
			if (getActivity().getIntent().getExtras() != null) {
				b = getActivity().getIntent().getExtras();
				id = b.getString(values.TAG_ID);
				searchWord = b.getString(values.TAG_SEARCH_SEARCH_WORD);
			}
		}
		
		//list view
		
		pagesLV = (ListView) rootView.findViewById(R.id.pages_fragment_pagesLV);
		
		pagesNamesList = new ArrayList<String>();
		pagesList = new ArrayList<PageData>();
		
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, pagesNamesList);
		pagesLV.setAdapter(adapter);
		

		new fetch_pages_task().execute("http://" + values.IP
				+ "/cycleapp/fetchingpages.php");

		pagesLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos,
					long itemid) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), Page.class);

				PageData temp = pagesList.get(pos);

				i.putExtra(values.TAG_ID, id);
				i.putExtra(values.TAG_PAGE_PAGE_ID, temp.getPage_id());
				i.putExtra(values.TAG_PAGE_USER_ID, temp.getUser_id());
				i.putExtra(values.TAG_PAGE_NAME, temp.getName());
				i.putExtra(values.TAG_PAGE_PRODUCT, temp.getProduct());
				i.putExtra(values.TAG_PAGE_DESCRIPTION, temp.getDescription());
				i.putExtra(values.TAG_PAGE_LOCATION, temp.getLocation());
				i.putExtra(values.TAG_PAGE_FOUNDED, temp.getFounded());
				i.putExtra(values.TAG_PAGE_RIDES_ID, temp.getRides_ids());

				startActivity(i);

			}
		});

		return rootView;
	}

	private class fetch_pages_task extends AsyncTask<String, Void, Void> {


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start fetch pages");
			serviceHandler sr = new serviceHandler();

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						String followers, page_id, user_id, name, product, description, location, founded, rides_ids;

						JSONArray pages = json.getJSONArray(values.TAG_DATA);

						for (int i = 0; i < pages.length(); i++) {
							JSONObject page = pages.getJSONObject(i);
							
							followers = page.getString(values.TAG_PAGE_FOLLOWERS);
							page_id = page.getString(values.TAG_PAGE_PAGE_ID);
							user_id = page.getString(values.TAG_PAGE_USER_ID);
							name = page.getString(values.TAG_PAGE_NAME);
							product = page.getString(values.TAG_PAGE_PRODUCT);
							description = page
									.getString(values.TAG_PAGE_DESCRIPTION);
							location = page.getString(values.TAG_PAGE_LOCATION);
							founded = page.getString(values.TAG_PAGE_FOUNDED);
							rides_ids = page
									.getString(values.TAG_PAGE_RIDES_ID);

							pagesNamesList.add(name);
							pagesList.add(new PageData(page_id, user_id, name,
									product, description, location, founded,
									rides_ids, followers));

						}

						Log.i("HA", "success");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "fetch pages : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);


			/*adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, pagesNamesList);
			pagesLV.setAdapter(adapter);*/
			if(adapter != null)
				adapter.notifyDataSetChanged();
		}

	}

}