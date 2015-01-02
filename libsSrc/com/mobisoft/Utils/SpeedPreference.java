package com.mobisoft.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpeedPreference {
	
		private SharedPreferences sharedPref;
		private Editor editor;
		
		private static final String SHARED = "Speed Preference";
		private static final String SPEED_VALUE = "speedValue";
		
		 public SpeedPreference(Context context) 
		 {
			sharedPref 	  = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
			
			editor 		  = sharedPref.edit();
		 }
		 
		 public void setPreviousNeedleValue(String speedValueStr)
		 {
			 	editor.putString(SPEED_VALUE, speedValueStr);
				System.out.println("Previous Value...."+speedValueStr);
				
				editor.commit();
		 }
		 
		 public String getPreviousNeedleValue()
		 {
			return sharedPref.getString(SPEED_VALUE, "");
		 }

}
