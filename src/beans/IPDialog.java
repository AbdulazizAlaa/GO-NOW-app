package beans;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.test_bike.R;

@SuppressLint("NewApi")
public class IPDialog extends DialogFragment {

	View dialogV;
	EditText ipTF;
	Intent i;
	
	public IPDialog(Intent i) {
		// TODO Auto-generated constructor stub
		this.i = i;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		dialogV = inflater.inflate(R.layout.dialog_layout, null);
		
		ipTF = (EditText) dialogV.findViewById(R.id.Dialog_ipTF);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		
		dialog.setTitle("IP address");
		
		dialog.setView(dialogV);
		dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//Intent i = new Intent(getActivity(), SignInUpActivity.class);
				
				values.IP = ipTF.getText().toString();
				
				startActivity(i);
				getActivity().finish();
				
			}
		} );
		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		
		return dialog.create();
	}
	
	

}
