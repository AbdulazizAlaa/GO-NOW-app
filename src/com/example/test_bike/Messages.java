package com.example.test_bike;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.messages_adapter;
import beans.serviceHandler;
import beans.values;
import model.messages_data;

public class Messages extends Activity implements
		android.view.View.OnClickListener {

	ListView messagesLV;
	ArrayList<messages_data> messages_list;
	messages_adapter messagesAdapter;

	EditText messageED;
	Button messageB;

	SwipeRefreshLayout swipeToRefresh;

	String chat_id, user_id, friend_id, username, friendname;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {

				b = getIntent().getExtras();

				chat_id = b.getString(values.TAG_MESSAGES_CHAT_ID);
				user_id = b.getString(values.TAG_MESSAGES_FIRST_ID);
				username = b.getString(values.TAG_USERNAME);
				friend_id = b.getString(values.TAG_MESSAGES_SECOND_ID);
				friendname = b.getString(values.TAG_MESSAGES_NAME);
			}
		}
		swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.Messages_SwipeToRefresh);
		
		//list view
		messagesLV = (ListView) findViewById(R.id.Messages_messagesLV);
		messages_list = new ArrayList<messages_data>();
		messagesAdapter = new messages_adapter(messages_list, user_id, Messages.this);
		messagesLV.setAdapter(messagesAdapter);
		
		
		messageED = (EditText) findViewById(R.id.Messages_WriteMessage_ED);

		messageB = (Button) findViewById(R.id.Message_sent_B);
		messageB.setOnClickListener(this);

		

		swipeToRefresh.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

				messages_list.clear();

				if (messagesAdapter != null)
					messagesAdapter.notifyDataSetChanged();
				new fetch_messages_task().execute("http://" + values.IP
						+ "/cycleapp/fetchmessages.php", chat_id, "refresh");
			}
		});

		new fetch_messages_task().execute("http://" + values.IP
				+ "/cycleapp/fetchmessages.php", chat_id, "");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Message_sent_B:
			if (!messageED.getText().toString().equals("")) {
				new send_message_task().execute("http://" + values.IP
						+ "/cycleapp/sendmessage.php", chat_id, user_id,
						friend_id, username, messageED.getText().toString());
				messages_list.add(new messages_data("", chat_id, user_id,
						friend_id, username, messageED.getText().toString(), true, null));
				messagesAdapter.notifyDataSetChanged();
			}
				messageED.setText("");

			break;

		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messages, menu);
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

	private class fetch_messages_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start fetch messages");
			serviceHandler sr = new serviceHandler();

			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_MESSAGES_CHAT_ID,
					params[1]));
			Log.i("HA", "messages:" + params[1]);
			try {

				String response = sr.makeServiceCall(params[0],
						serviceHandler.POST, param);

				if (!response.equals("")) {

					JSONObject json = new JSONObject(response);

					String message = json.getString(values.TAG_MESSAGE);
					if (message.equals(values.TAG_SUCCESS)) {

						String id, chat_id, first_id, second_id, name, text;
						boolean isHost;

						JSONArray messages = json.getJSONArray(values.TAG_DATA);

						Log.i("HA", "messages array");
						for (int i = 0; i < messages.length(); i++) {
							JSONObject messageBody = messages.getJSONObject(i);

							id = messageBody.getString(values.TAG_MESSAGES_ID);
							chat_id = messageBody
									.getString(values.TAG_MESSAGES_CHAT_ID);
							first_id = messageBody
									.getString(values.TAG_MESSAGES_FIRST_ID);
							second_id = messageBody
									.getString(values.TAG_MESSAGES_SECOND_ID);
							name = messageBody
									.getString(values.TAG_MESSAGES_NAME);
							text = messageBody
									.getString(values.TAG_MESSAGES_TEXT);

							isHost = (messageBody
									.getInt(values.TAG_MESSAGES_IS_HOST) == 1);

							messages_list.add(new messages_data(id, chat_id,
									first_id, second_id, name, text, isHost,
									null));

						}

						if (params[2].equals("refresh")) {
							swipeToRefresh.setRefreshing(false);
						}

						Log.i("HA", "success");
					}
				} else {
					Log.i("HA", "failure");
				}

			} catch (Exception e) {
				Log.i("HA", "fetch messages : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (messagesAdapter != null)
				messagesAdapter.notifyDataSetChanged();
		}

	}

	private class send_message_task extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("HA", "start send message");
			serviceHandler sr = new serviceHandler();

			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(values.TAG_MESSAGES_CHAT_ID,
					params[1]));
			param.add(new BasicNameValuePair(values.TAG_MESSAGES_FIRST_ID,
					params[2]));
			param.add(new BasicNameValuePair(values.TAG_MESSAGES_SECOND_ID,
					params[3]));
			param.add(new BasicNameValuePair(values.TAG_MESSAGES_NAME,
					params[4]));
			param.add(new BasicNameValuePair(values.TAG_MESSAGES_TEXT,
					params[5]));
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
				Log.i("HA", "send message : " + e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (messagesAdapter != null)
				messagesAdapter.notifyDataSetChanged();
		}

	}

}
