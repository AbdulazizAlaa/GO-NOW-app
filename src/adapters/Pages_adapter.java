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

import model.PageData;
import model.page_ViewHolder;

public class Pages_adapter  extends BaseAdapter{

	private Context context;
	private ArrayList<PageData> list;
	Typeface squareFont;
	
	public Pages_adapter(Context context, ArrayList<PageData> list) {
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
			
			view = inflator.inflate(R.layout.pages_search_list_item, null);
			
			page_ViewHolder viewHolder = new page_ViewHolder();
			
			viewHolder.pageNameTV = (TextView) view.findViewById(R.id.Page_list_item_profieName_TV);
			viewHolder.pagePicIV = (ImageView) view.findViewById(R.id.Page_list_item_pageImg_IV);
			viewHolder.coverIV = (ImageView) view.findViewById(R.id.Page_list_item_coverimgIv);
			
			view.setTag(viewHolder);
		}
		
		page_ViewHolder holder = (page_ViewHolder) view.getTag();
		
		holder.pageNameTV.setText(list.get(pos).getName());
		
		holder.pageNameTV.setTypeface(squareFont);
		
		//if(list.get(pos).getCover()!=null)
			//holder.coverIV.setImageBitmap(list.get(pos).getCover());
			//holder.pagePicIV.setImageBitmap(list.get(pos).getCover());

		return view;
		
	}
	
	

}