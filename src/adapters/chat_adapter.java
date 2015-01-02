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

import model.chat_data;
import model.chat_viewHolder;

public class chat_adapter extends BaseAdapter {

	Context mContext;
	ArrayList<chat_data> list;

	public chat_adapter(Context mContext, ArrayList<chat_data> list) {
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
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (view == null) {
			LayoutInflater inflator = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflator.inflate(R.layout.chat_list_item, null);

			chat_viewHolder viewHolder = new chat_viewHolder();

			viewHolder.nameTV = (TextView) view
					.findViewById(R.id.chat_list_item_ProfilenameTv);
			viewHolder.numMessagesTV = (TextView) view
					.findViewById(R.id.chat_list_item_num_messagesTV);

			viewHolder.profilePicIV = (ImageView) view
					.findViewById(R.id.chat_list_item_profileImg_IV);

			view.setTag(viewHolder);
		}

		chat_viewHolder holder = (chat_viewHolder) view.getTag();

		holder.nameTV.setText(list.get(pos).getUsername());
		holder.numMessagesTV.setText(list.get(pos).getNumMessages());

		if (list.get(pos).getProfilePic() != null)
			holder.profilePicIV.setImageBitmap(list.get(pos).getProfilePic());

		return view;
	}

}
