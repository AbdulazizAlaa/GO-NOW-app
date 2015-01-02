package com.mobisoft.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.example.test_bike.R;


public class SpeedometerView extends View {

	Bitmap needle = null;
	Bitmap speedo_meter = null;

	Paint paint_needle, paint_text;

	Context context;

	SpeedometerView speedo_obj;

	String priceValue;

	public static float minAngle = (float) 0;

	public static float maxAngle = (float) 180;

	public static float exact_angle = 0;

	public static float angle_of_deviation = maxAngle;

	public static float maxValue = 100;

	int width, height;

	Matrix matrix_needle;

	public SpeedometerView(Context context) {
		super(context);
		initializeView(context);

	}

	public SpeedometerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView(context);
	}

	public SpeedometerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView(context);
	}

	// Create view
	private void initializeView(Context context) {

		this.context = context;
		speedo_obj = this;

		this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		releaseImageResources();

		speedo_meter = getImage(R.drawable.counter_icon_session);


		needle = getImage(R.drawable.pointer_icon_session);

		paint_needle = new Paint();
		paint_needle.setStyle(Paint.Style.FILL);
		paint_needle.setAntiAlias(true);

		paint_text = new Paint();
		paint_text.setColor(Color.WHITE);
		paint_text.setAntiAlias(true);
		paint_text.setStyle(Paint.Style.FILL_AND_STROKE);
		paint_text.setTextSize(40);

	}

	protected void onDraw(Canvas canvas) {

		canvas.drawBitmap(speedo_meter,
				(canvas.getWidth() / 2) - speedo_meter.getWidth() / 2,
				(canvas.getHeight() / 3) - speedo_meter.getHeight() / 2, null);

		// Main Meter Needle
		matrix_needle = new Matrix();
		matrix_needle.setTranslate((canvas.getWidth() / 2) - needle.getWidth()
				/ 2, needle.getHeight());
		Log.d("ANGLE OF DEVIATION : ", "" + angle_of_deviation);

		matrix_needle.postRotate(angle_of_deviation, canvas.getWidth() / 2+needle.getWidth()/6,
				2 * needle.getHeight() + 30);
		/*matrix_needle.setTranslate((canvas.getWidth() / 2) - needle.getWidth()
				/ 2, (canvas.getHeight() / 2) - needle.getHeight());
		Log.d("ANGLE OF DEVIATION : ", "" + angle_of_deviation);

		matrix_needle.postRotate(angle_of_deviation, canvas.getWidth() / 2,
				2 * needle.getHeight() - 10);*/

		canvas.drawBitmap(needle, matrix_needle, paint_needle);

		// speed value
		priceValue = Float.toString(exact_angle);

		if (exact_angle < 100) {

			priceValue = "0 " + priceValue.charAt(0) + " "
					+ priceValue.charAt(1);

			Log.d("Formatted Price from if : ", priceValue);
		} else {
			priceValue = priceValue.charAt(0) + " " + priceValue.charAt(1)
					+ " " + priceValue.charAt(2);
		}

	}

	public void calculateAngleOfDeviation(int randomly_generated_value) {

		exact_angle = (float) randomly_generated_value;

		float angleDef = (float) (maxAngle + minAngle - 130);

		angle_of_deviation = ((exact_angle * angleDef) / maxValue) + minAngle;

		speedo_obj.invalidate();

	}

	public Bitmap getImage(int res_id) {
		return BitmapFactory.decodeResource(getResources(), res_id);

	}

	public void releaseImageResources() {
		if (speedo_meter != null)
			speedo_meter = null;
		if (needle != null)
			needle = null;
	}

}
