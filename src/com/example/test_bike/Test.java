package com.example.test_bike;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class Test extends Activity {

	Button testB;
	TextView testTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		testB = (Button) findViewById(R.id.test_B);
		testTV = (TextView) findViewById(R.id.test_TV);

		testB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						
						
						/*try {
							serviceHandler service = new serviceHandler();

							ArrayList<BodyData> stringBody = new ArrayList<BodyData>();
							ArrayList<BodyData> fileBody = new ArrayList<BodyData>();

							stringBody.add(new BodyData("comment",
									new StringBody("ay 7aga",
											ContentType.TEXT_PLAIN), null));
							fileBody.add(new BodyData("bin", null,
									new FileBody(new File(
											"/mnt/sdcard/Download/hommer.jpg"))));
							runOnUiThread(new Runnable() {
								public void run() {
									testTV.setText("hay");

								}
							});

							service.makeServiceCallFiles(
									"http://azizapp.eu5.org/cycleapp/uploadimage.php",
									stringBody, fileBody);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
					}
				}).start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

}
