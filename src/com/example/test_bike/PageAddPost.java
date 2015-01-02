package com.example.test_bike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import beans.serviceHandler;
import beans.values;

public class PageAddPost extends Activity {

	TextView addPhotoTV;
	EditText postED;

	String page_id;
	Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_add_post);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();
				page_id = b.getString(values.TAG_PAGE_PAGE_ID);
			}
		}
		
		addPhotoTV = (TextView) findViewById(R.id.page_add_post_addphotoTV);

		postED = (EditText) findViewById(R.id.page_add_post_postED);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.page_add_post, menu);
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
		} else if (id == R.id.page_add_post_postB) {
			// Post button
			new page_add_post_task().execute("http://" + values.IP
					+ "/cycleapp/add_post.php", page_id, postED.getText()
					.toString());

		}
		return super.onOptionsItemSelected(item);
	}

	private class page_add_post_task extends AsyncTask<String, Void, Void> {

		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = new ProgressDialog(PageAddPost.this);
			progress.setCancelable(false);
			progress.setMessage("Please Wait...");
			progress.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start add post");
			serviceHandler sr = new serviceHandler();

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_PAGE_PAGE_ID, params[1]));
			param.add(new BasicNameValuePair(values.TAG_FEED_FEED, params[2]));

			Log.i("HA", "page add new post : " + params[1]);

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
				Log.i("HA", "page post : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progress.isShowing()) {
				progress.dismiss();
			}
			PageAddPost.this.finish();
		}

	}

}
