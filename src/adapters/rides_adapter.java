package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test_bike.R;

import java.util.ArrayList;

import model.ride_ViewHolder;
import model.ride_data;

public class rides_adapter extends BaseAdapter{

	private Context context;
	private ArrayList<ride_data> list;
	Typeface squareFont;
	
	public rides_adapter(Context context, ArrayList<ride_data> list) {
		super();
		this.context = context;
		this.list = list;
		squareFont = Typeface.createFromAsset(this.context.getAssets(),
				"HallandaleSquare.ttf");
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
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub

		if(view==null){
			LayoutInflater inflator  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			view = inflator.inflate(R.layout.rides_tab_item, null);
			
			ride_ViewHolder viewHolder = new ride_ViewHolder();
			
			viewHolder.title = (TextView) view.findViewById(R.id.ridestab_page_titleTV);
			viewHolder.month = (TextView) view.findViewById(R.id.ridestab_item_monthTV);
			viewHolder.cover = (ImageView) view.findViewById(R.id.ridestab_coverIV);
			viewHolder.day = (TextView) view.findViewById(R.id.ridestab_item_dayTV);
			viewHolder.city = (TextView) view.findViewById(R.id.ridestab_item_cityTV);
			viewHolder.country = (TextView) view.findViewById(R.id.ridestab_item_CountryTV);
			
			view.setTag(viewHolder);
		}
		
		ride_ViewHolder holder = (ride_ViewHolder) view.getTag();
		
		holder.title.setText(list.get(pos).getPageTitle());
		holder.month.setText(list.get(pos).getMonth());
		holder.day.setText(list.get(pos).getDay());
		holder.city.setText(list.get(pos).getCity());
		holder.country.setText(list.get(pos).getCountry());
		
		holder.title.setTypeface(squareFont);
		holder.month.setTypeface(squareFont);
		holder.day.setTypeface(squareFont);
		
		if(list.get(pos).getCover()!=null)
			holder.cover.setImageBitmap(list.get(pos).getCover());
		
		return view;
		
	}
	
	

}


