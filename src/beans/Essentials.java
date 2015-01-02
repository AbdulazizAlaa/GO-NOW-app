package beans;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.andreabaccega.widget.FormEditText;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Essentials {

	public static Bitmap m;

	public static Bitmap getBitmapFromURL(String src) {
		try {
			java.net.URL url = new java.net.URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getRealPathFromURI(Uri contentUri, Context mContext) {

		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = mContext.getContentResolver().query(contentUri, proj, // Which
																				// columns
																				// to
																				// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	public static String decodeRouteLat(ArrayList<LatLng> route) {

		String lat = "";

		for (int i = 0; i < route.size(); i++) {
			if (i != 0)
				lat += "/";
			lat += route.get(i).latitude;
		}

		return lat;
	}

	public static String decodeRouteLng(ArrayList<LatLng> route) {

		String lng = "";

		for (int i = 0; i < route.size(); i++) {
			if (i != 0)
				lng += "/";
			lng += route.get(i).longitude;
		}

		return lng;
	}

	public static ArrayList<LatLng> getRouteLatLng(String Lat, String Lng) {

		ArrayList<LatLng> route = new ArrayList<LatLng>();
		String[] lats = Lat.split("/");
		String[] lngs = Lng.split("/");

		int size = (lats.length < lngs.length) ? lats.length : lngs.length;
		for (int i = 0; i < size; i++) {
			route.add(new LatLng(Double.parseDouble(lats[i]), Double
					.parseDouble(lngs[i])));
		}

		return route;
	}

	public static boolean FormValid(FormEditText[] fields) {

		boolean allValid = true;
		for (FormEditText field : fields) {
			allValid = field.testValidity() && allValid;
		}
		return allValid;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 120;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap takeScreenshot(GoogleMap map, View v) {

		SnapshotReadyCallback callback = new SnapshotReadyCallback() {

			@Override
			public void onSnapshotReady(Bitmap snapShot) {
				// TODO Auto-generated method stub
				m = snapShot;
				FileOutputStream out;
				try {
					out = new FileOutputStream(new File(
							"/mnt/sdcard/Gonow/ride"
									+ System.currentTimeMillis() + ".png"));
					m.compress(Bitmap.CompressFormat.PNG, 90, out);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};

		map.snapshot(callback);

		return m;
		/*
		 * View rootView = v.getRootView();
		 * rootView.setDrawingCacheEnabled(true); return
		 * rootView.getDrawingCache();
		 */
	}
}
