package adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test_bike.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import beans.serviceHandler;
import beans.values;
import model.comment_data;
import model.feed_ViewHolder;
import model.feed_data;

public class feeds_adapter extends BaseAdapter {

	private Context context;
	private String user_id, username;

	Dialog CommentsDialog, likesDialog;

	EditText commentTF;
	Button commentB;
	ListView commentsLV;

	comments_adapter adapter;

	// SharedPreferences pref;

	ArrayList<comment_data> comments_list;
	private ArrayList<feed_data> list;

	// private String[] like_data;

	public feeds_adapter(Context context, String username, String user_id,
			ArrayList<feed_data> list) {
		super();
		this.username = username;
		this.context = context;
		this.user_id = user_id;
		this.list = list;

		/*
		 * pref = context.getSharedPreferences( values.TAG_LIKE_FILE,
		 * Context.MODE_PRIVATE);
		 * 
		 * String temp = pref.getString(values.TAG_FEED_PREF, ""); like_data =
		 * temp.split("/");
		 */
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (view == null) {

			view = inflator.inflate(R.layout.feeds_tab_item, null);

			feed_ViewHolder viewHolder = new feed_ViewHolder();

			viewHolder.pageNameTV = (TextView) view
					.findViewById(R.id.feeds_list_item_PagenameTv);
			viewHolder.postedTV = (TextView) view
					.findViewById(R.id.feeds_list_item_PostedphotoTv);
			viewHolder.coverimageIV = (ImageView) view
					.findViewById(R.id.feeds_list_item_imgIv);
			viewHolder.pageImgIV = (ImageView) view
					.findViewById(R.id.feeds_list_item_pageImgIV);
			viewHolder.textTV = (TextView) view
					.findViewById(R.id.feeds_list_item_textTV);
			viewHolder.numLikesTV = (TextView) view
					.findViewById(R.id.feeds_list_item_LikeTv);
			viewHolder.numCommentsTV = (TextView) view
					.findViewById(R.id.feeds_list_item_CommentTv);
			viewHolder.dayTV = (TextView) view
					.findViewById(R.id.feeds_list_item_dayTv);
			viewHolder.likeImgIV = (ImageView) view
					.findViewById(R.id.feeds_list_item_LikeImgIV);
			viewHolder.commentImgIV = (ImageView) view
					.findViewById(R.id.feeds_list_item_CommentImgIV);
			//viewHolder.bubbleIV = (ImageView) view.findViewById(R.id.feeds_list_item_bubbleIV);
			viewHolder.bubbleContainerF = (FrameLayout) view
					.findViewById(R.id.feeds_list_item_bubble_ContainerF);
			view.setEnabled(false);
			view.setOnClickListener(null);
			view.setTag(viewHolder);
		}

		feed_ViewHolder holder = (feed_ViewHolder) view.getTag();

		holder.pageNameTV.setText(list.get(pos).getPageName());
		holder.textTV.setText(list.get(pos).getText());
		holder.numCommentsTV.setText(list.get(pos).getNumComments() + "");
		holder.numLikesTV.setText(list.get(pos).getNumLikes() + "");
		// holder.dayTV.setText(list.get(pos).getDay());

		/*
		 * boolean found = false; for(String t : like_data){
		 * if(t.equals(list.get(pos).getFeed_id())){ found = true; break; } }
		 */

		if (list.get(pos).getLike_ids().contains(user_id)) {
			holder.likeImgIV.setImageResource(R.drawable.likesel_icon_feeds);
			holder.likeImgIV.setOnClickListener(new LikeClickHandler(pos, true,
					holder.likeImgIV, holder.numLikesTV));
		} else {
			holder.likeImgIV
					.setImageResource(R.drawable.like_icon_feeds_selector);
			holder.likeImgIV.setOnClickListener(new LikeClickHandler(pos,
					false, holder.likeImgIV, holder.numLikesTV));
		}

		holder.commentImgIV.setOnClickListener(new commentClickHandler(pos));

		if (list.get(pos).getCoverImg() != null) {
			holder.coverimageIV.setImageBitmap(list.get(pos).getCoverImg());
			holder.postedTV.setText("Posted Photo");
		} else {
			holder.coverimageIV.setVisibility(View.GONE);
			holder.postedTV.setText("Posted Text");
		}
		if (list.get(pos).getPageImg() != null)
			holder.pageImgIV.setImageBitmap(list.get(pos).getPageImg());

		return view;

	}

	private class fetch_comments_task extends AsyncTask<String, Void, Void> {

		ArrayList<comment_data> comments_data;
		comments_adapter adapter;
		ListView commentsLV;
		String feed_id;

		public fetch_comments_task(ArrayList<comment_data> comments_data,
				comments_adapter adapter, ListView commentsLV) {
			super();
			this.comments_data = comments_data;
			this.adapter = adapter;
			this.commentsLV = commentsLV;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			// comments_data = new ArrayList<comment_data>();

			String name, comment;

			Log.i("HA", "start fetch comments");

			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_FEED_FEED_ID, params[1]));

			feed_id = params[1];

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {
					Log.i("HA", "res");

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						JSONArray commentsArray = json
								.getJSONArray(values.TAG_DATA);
						Log.i("HA", "array fetched : " + commentsArray.length());
						for (int i = 0; i < commentsArray.length(); i++) {
							JSONObject commentObj = commentsArray
									.getJSONObject(i);

							name = commentObj
									.getString(values.TAG_COMMENT_PAGE_NAME);

							comment = commentObj
									.getString(values.TAG_COMMENT_PAGE_COMMENT);

							Log.i("ha", name);
							Log.i("HA", "finished comment " + i);

							comments_data.add(new comment_data(name, comment));
						}

						Log.i("HA", "success");
					} else {
						Log.i("HA", "failure");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "home : " + e.getMessage());
			}
			/*
			 * if (params[2].equals("refresh")) {
			 * SwipeToRefresh.setRefreshing(false); }
			 */
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			adapter = new comments_adapter(context, comments_data);

			commentsLV.setAdapter(adapter);
			commentB.setOnClickListener(new commentButtonClickHandler(feed_id,
					commentTF, adapter));
		}

	}

	private class fetch_likes_task extends AsyncTask<String, Void, Void> {

		ArrayList<String> Likes_data;
		ArrayAdapter<String> Likes_adapter;
		ListView likesLV;
		String feed_id;

		public fetch_likes_task(ArrayList<String> likes_data,
				ArrayAdapter<String> likes_adapter, ListView likesLV) {
			super();
			this.Likes_data = likes_data;
			this.Likes_adapter = likes_adapter;
			this.likesLV = likesLV;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			String name;

			Log.i("HA", "start fetch likes");

			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_FEED_FEED_ID, params[1]));

			feed_id = params[1];

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {
					Log.i("HA", "res");

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						JSONArray commentsArray = json
								.getJSONArray(values.TAG_DATA);

						for (int i = 0; i < commentsArray.length(); i++) {
							JSONObject commentObj = commentsArray
									.getJSONObject(i);

							name = commentObj
									.getString(values.TAG_COMMENT_PAGE_NAME);

							Log.i("HA", "finished like " + i);

							Likes_data.add(name);
						}

						Log.i("HA", "success");
					} else {
						Log.i("HA", "failure");
					}
				}

			} catch (Exception e) {
				Log.i("HA", "home : " + e.getMessage());
			}
			/*
			 * if (params[2].equals("refresh")) {
			 * SwipeToRefresh.setRefreshing(false); }
			 */
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			Likes_adapter = new ArrayAdapter<String>(context,
					R.layout.like_list_item, R.id.like_list_item_ProfilenameTv,
					Likes_data);

			likesLV.setAdapter(Likes_adapter);
		}

	}

	private class add_comment_feed_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start add comment");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_FEED_FEED_ID, params[1]));
			param.add(new BasicNameValuePair(values.TAG_PAGE_USER_ID, params[2]));
			param.add(new BasicNameValuePair(values.TAG_COMMENT_PAGE_COMMENT,
					params[3]));

			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						Log.i("HA", "success");
					} else {
						Log.i("HA", "faliure");
					}
				} else {
					Log.i("HA", "faliure");
				}

			} catch (Exception e) {
				Log.i("HA", "add comment : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}

	private class add_like_feed_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start add like");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_FEED_FEED_ID, params[1]));
			param.add(new BasicNameValuePair(values.TAG_PAGE_USER_ID, params[2]));
			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						Log.i("HA", "success");
					} else {
						Log.i("HA", "faliure");
					}
				} else {
					Log.i("HA", "faliure");
				}

			} catch (Exception e) {
				Log.i("HA", "add like : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}

	private class remove_like_feed_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start remove ike");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_FEED_FEED_ID, params[1]));
			param.add(new BasicNameValuePair(values.TAG_PAGE_USER_ID, params[2]));
			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						Log.i("HA", "success");
					} else {
						Log.i("HA", "faliure");
					}
				} else {
					Log.i("HA", "faliure");
				}

			} catch (Exception e) {
				Log.i("HA", "remove like : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}

	private class commentButtonClickHandler implements OnClickListener {

		private String feed_id;
		private EditText commentTF;
		private comments_adapter comment_adapter;

		public commentButtonClickHandler(String feed_id, EditText commentTF,
				comments_adapter comment_adapter) {
			super();
			this.feed_id = feed_id;
			this.commentTF = commentTF;
			this.comment_adapter = comment_adapter;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.feed_comment_list_commentB) {
				Log.i("ha", "comment");
				new add_comment_feed_task().execute("http://" + values.IP
						+ "/cycleapp/addcomment.php", feed_id, user_id,
						commentTF.getText().toString());
				comments_list.add(new comment_data(username, commentTF
						.getText().toString()));
				comment_adapter.notifyDataSetChanged();
				commentTF.setText("");
			}

		}

	}

	private class commentClickHandler implements OnClickListener {

		private int postion;

		public commentClickHandler(int postion) {
			super();
			this.postion = postion;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LayoutInflater inflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (v.getId() == R.id.feeds_list_item_CommentImgIV) {
				Log.i("ha", "comment");

				View commentsView = inflator.inflate(
						R.layout.feed_comments_list, null);

				commentsLV = (ListView) commentsView
						.findViewById(R.id.feed_comment_list_listLV);

				commentTF = (EditText) commentsView
						.findViewById(R.id.feed_comment_list_commentED);
				commentB = (Button) commentsView
						.findViewById(R.id.feed_comment_list_commentB);

				View likesV = commentsView
						.findViewById(R.id.feed_comment_list_likeIV);

				TextView likesTV = (TextView) commentsView
						.findViewById(R.id.feed_comment_list_numlikesTV);

				likesTV.setText(list.get(postion).getNumLikes() + " Likes");

				likesV.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						LayoutInflater inflator = (LayoutInflater) context
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

						View likesView = inflator.inflate(
								R.layout.feed_likes_list, null);

						ArrayAdapter<String> likesAdapter = null;

						View likeDissmissV = likesView
								.findViewById(R.id.feed_comment_list_likeV);

						ListView likesLV = (ListView) likesView
								.findViewById(R.id.feed_like_list_likesLV);
						ArrayList<String> likes_data = new ArrayList<String>();

						new fetch_likes_task(likes_data, likesAdapter, likesLV)
								.execute("http://" + values.IP
										+ "/cycleapp/fetchlikes.php",
										list.get(postion).getFeed_id());

						likesDialog = new Dialog(context);
						likesDialog.setContentView(likesView);
						likesDialog.getWindow().setBackgroundDrawable(
								new ColorDrawable(
										android.graphics.Color.TRANSPARENT));
						likesDialog.show();

						CommentsDialog.dismiss();

						likeDissmissV.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								likesDialog.dismiss();
							}
						});

					}
				});

				adapter = null;
				comments_list = new ArrayList<comment_data>();

				new fetch_comments_task(comments_list, adapter, commentsLV)
						.execute("http://" + values.IP
								+ "/cycleapp/fetchcomments.php",
								list.get(postion).getFeed_id());

				CommentsDialog = new Dialog(context);
				CommentsDialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				CommentsDialog.setContentView(commentsView);
				CommentsDialog.show();

			}

		}
	}

	private class LikeClickHandler implements OnClickListener {

		private int postion;
		private boolean pressed;
		ImageView likeIV;
		TextView numlikeTV;

		public LikeClickHandler(int postion, boolean pressed, ImageView likeIV,
				TextView numlikeTV) {
			super();
			this.postion = postion;
			this.pressed = pressed;
			this.likeIV = likeIV;
			this.numlikeTV = numlikeTV;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.feeds_list_item_LikeImgIV) {
				if (pressed) {
					Log.i("ha", "dislike");
					new remove_like_feed_task().execute("http://" + values.IP
							+ "/cycleapp/removelike.php", list.get(postion)
							.getFeed_id(), user_id);

					/*
					 * String temp = pref.getString(values.TAG_FEED_PREF, "");
					 * String[] data = temp.split("/"); temp = ""; for(String t
					 * : data){ if(!t.equals(list.get(postion).getFeed_id())){
					 * if(!temp.equals("")){ temp += "/"; } temp += t; } }
					 * 
					 * Editor edit = pref.edit();
					 * 
					 * edit.putString(values.TAG_FEED_PREF, temp);
					 * 
					 * edit.commit();
					 */

					pressed = false;
					likeIV.setImageResource(R.drawable.like_icon_feeds_selector);
					numlikeTV.setText((Integer.parseInt(numlikeTV.getText()
							.toString()) - 1) + "");

				} else {
					Log.i("ha", "like");

					new add_like_feed_task().execute("http://" + values.IP
							+ "/cycleapp/addlike.php", list.get(postion)
							.getFeed_id(), user_id);

					/*
					 * String temp = pref.getString(values.TAG_FEED_PREF, "");
					 * if(!temp.equals("")){ temp += "/"; } temp +=
					 * list.get(postion).getFeed_id();
					 * 
					 * Editor edit = pref.edit();
					 * 
					 * edit.putString(values.TAG_FEED_PREF, temp);
					 * edit.commit();
					 */

					pressed = true;
					likeIV.setImageResource(R.drawable.likesel_icon_feeds);

					numlikeTV.setText((Integer.parseInt(numlikeTV.getText()
							.toString()) + 1) + "");
				}
			}

		}

	}

}