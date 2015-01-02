package adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.example.test_bike.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import beans.AppController;
import beans.serviceHandler;
import beans.values;
import model.people_data;
import model.people_viewHolder;

public class people_adapter extends BaseAdapter {

	Context mContext;
	ArrayList<people_data> list;
	people_data userData;
	String id;

	public people_adapter(Context mContext, ArrayList<people_data> list,
			people_data userData, String id) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.userData = userData;
		this.id = id;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return list.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub

		if (view == null) {
			LayoutInflater inflator = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflator.inflate(R.layout.people_search_list_item, null);

			people_viewHolder viewHolder = new people_viewHolder();

			viewHolder.nameTV = (TextView) view
					.findViewById(R.id.people_search_list_item_ProfilenameTv);
			viewHolder.profilePicIV = (ImageView) view
					.findViewById(R.id.people_search_list_item_profileImg_IV);
			viewHolder.addFriendB = (ImageView) view
					.findViewById(R.id.people_search_list_item_addFriend_IV);

			//view.setEnabled(false);
			//view.setOnClickListener(null);
			view.setTag(viewHolder);
		}

		people_viewHolder holder = (people_viewHolder) view.getTag();

		holder.nameTV.setText(list.get(pos).getUsername());
		holder.addFriendB.setVisibility(View.VISIBLE);
		
		holder.profilePicIV.setImageResource(R.drawable.profilepicture_image_feeds);
		
		if (list.get(pos).getProfilePic() != null){
			ImageLoader loader = AppController.getInstance().getImageLoader();

			Log.i("ha", "url");
			loader.get(list.get(pos).getProfilePic(), new imageLoadListener(holder.profilePicIV));
		}

		if (list.get(pos).getUser_id().equals(userData.getUser_id())
				|| userData.getFriends().contains(list.get(pos).getUser_id())) {
			holder.addFriendB.setVisibility(View.GONE);
		} else {
			holder.addFriendB.setOnClickListener(new addFriendClickHandler(
					mContext, pos));
		}

		return view;
	}

	private class imageLoadListener implements ImageListener{

		ImageView profileIV;
		
		
		public imageLoadListener(ImageView profileIV) {
			super();
			this.profileIV = profileIV;
		}

		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onResponse(ImageContainer image, boolean arg1) {
			// TODO Auto-generated method stub
			if (image.getBitmap() != null) {
				profileIV.setImageBitmap(image.getBitmap());
			} else
				Log.i("ha", "no image");
		}
		
	}
	
	private class addFriendClickHandler implements OnClickListener {

		Context mContext;
		int Position;

		public addFriendClickHandler(Context mContext, int pos) {
			super();
			this.mContext = mContext;
			Position = pos;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			v.setVisibility(View.GONE);
			new add_friend_task().execute("http://" + values.IP
					+ "/cycleapp/addfriend.php", id, list.get(Position)
					.getUser_id());
		}

	}

	private class add_friend_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start add friend");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_ID, params[1]));
			param.add(new BasicNameValuePair(values.TAG_RIDE_USER_ID, params[2]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						Log.i("HA", "success");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "start add friend : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}

	}

}
