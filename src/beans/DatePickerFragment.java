package beans;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class DatePickerFragment extends DialogFragment {

	Context mContext;
	int year, month, day;

	public DatePickerFragment(Context mContext, int year, int month, int day) {
		super();
		this.mContext = mContext;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return new DatePickerDialog(getActivity(),
				(OnDateSetListener) mContext, year, month, day);
	}
}
