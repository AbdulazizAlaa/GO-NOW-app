package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test_bike.R;

import java.util.ArrayList;

import model.profile_session_data;
import model.profile_session_viewHolder;

public class profile_session_adapter extends BaseAdapter{

	Context mContext;
	ArrayList<profile_session_data> list;
	
	
	
	public profile_session_adapter(Context mContext,
			ArrayList<profile_session_data> list) {
		super();
		this.mContext = mContext;
		this.list = list;
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
	public View getView(int pos, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		if(v == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			v = inflater.inflate(R.layout.profile_session_list_item, null);
			
			profile_session_viewHolder viewHolder = new profile_session_viewHolder();
			
			viewHolder.distanceTV = (TextView) v.findViewById(R.id.profile_session_list_item_distanceTV);
			viewHolder.speedTV = (TextView) v.findViewById(R.id.profile_session_list_item_speedTV);
			viewHolder.timeTV = (TextView) v.findViewById(R.id.profile_session_list_item_timeTV);
			viewHolder.dateTV = (TextView) v.findViewById(R.id.profile_session_list_item_dateTV);
			
			v.setTag(viewHolder);
		}
		
		profile_session_viewHolder holder = (profile_session_viewHolder) v.getTag();
		
		holder.distanceTV.setText(list.get(pos).getDistance()+"");
		holder.speedTV.setText(list.get(pos).getSpeed()+"");
		holder.timeTV.setText(list.get(pos).getTime()+"");
		//holder.dateTV.setText(list.get(pos).getDate());
		
		return v;
	}
	
	

}
