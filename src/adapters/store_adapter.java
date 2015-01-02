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

import model.store_ViewHolder;
import model.store_data;

public class store_adapter extends BaseAdapter {

	private Context context;
	private ArrayList<store_data> list;

	Typeface squareFont;

	public store_adapter(Context context, ArrayList<store_data> list) {
		super();
		this.context = context;
		this.list = list;
		squareFont = Typeface.createFromAsset(context.getAssets(),
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

		if (view == null) {
			LayoutInflater inflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflator.inflate(R.layout.store_list_item, null);

			store_ViewHolder viewHolder = new store_ViewHolder();

			viewHolder.type = (TextView) view
					.findViewById(R.id.Store_item_type);
			viewHolder.price = (TextView) view
					.findViewById(R.id.Store_item_price);
			viewHolder.bike = (ImageView) view
					.findViewById(R.id.Store_item_bike_image);

			view.setTag(viewHolder);
		}

		store_ViewHolder holder = (store_ViewHolder) view.getTag();

		holder.type.setText(list.get(pos).getTypeOfBike());
		holder.price.setText(list.get(pos).getPrice() + "$");
		holder.price.setTypeface(squareFont);
		if (list.get(pos).getBikeImg() != null)
			holder.bike.setImageBitmap(list.get(pos).getBikeImg());

		return view;

	}

}
