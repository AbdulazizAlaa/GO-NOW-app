package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test_bike.R;

import java.util.ArrayList;

import model.drawerListItem;
import model.home_drawer_ViewHolder;

public class drawerListAdapter extends BaseAdapter{

	private Context context;
	ArrayList<drawerListItem> list;
	
	
	public drawerListAdapter(Context context, ArrayList<drawerListItem> list) {
		super();
		this.context = context;
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
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		if(v==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			v = inflater.inflate(R.layout.drawer_list_item, null);
			
			home_drawer_ViewHolder viewHolder = new home_drawer_ViewHolder();
			
			viewHolder.setTitle((TextView) v.findViewById(R.id.home_drawer_titleTV));
			viewHolder.setIcon((ImageView) v.findViewById(R.id.home_drawer_iconIV));

			v.setTag(viewHolder);
		}
		
		home_drawer_ViewHolder viewHolder = (home_drawer_ViewHolder) v.getTag();		
		
		viewHolder.getTitle().setText(list.get(pos).getTitle());
		viewHolder.getIcon().setImageResource(list.get(pos).getIcon());
		
		return v;
	}

	
	
	
}
