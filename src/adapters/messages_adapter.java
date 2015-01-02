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

import model.messages_data;
import model.messages_viewHolder;

public class messages_adapter extends BaseAdapter {

	ArrayList<messages_data> list;
	String user_id;
	Context mContext;

	public messages_adapter(ArrayList<messages_data> list, String user_id,
			Context mContext) {
		super();
		this.list = list;
		this.user_id = user_id;
		this.mContext = mContext;
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
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.messages_list_item, null);

			messages_viewHolder holder = new messages_viewHolder();

			holder.nameTV = (TextView) view
					.findViewById(R.id.messages_list_item_nameTV);
			holder.textTV = (TextView) view
					.findViewById(R.id.messages_list_item_message_textTV);
			holder.profileIV = (ImageView) view
					.findViewById(R.id.messages_list_item_profileimg);
			holder.bubbleBgIV = (ImageView) view
					.findViewById(R.id.messages_list_item_text_bubbleIV);

			view.setTag(holder);
		}

		messages_viewHolder holder = (messages_viewHolder) view.getTag();

		holder.nameTV.setText(list.get(pos).getName());
		holder.textTV.setText(list.get(pos).getText());

		if (list.get(pos).getProfilePic() != null) {
			holder.profileIV.setImageBitmap(list.get(pos).getProfilePic());
		}

		if (!user_id.equals(list.get(pos).getFirst_id())) {
			holder.bubbleBgIV.setImageResource(R.drawable.bubblesecond_image_messages);
		}

		return view;
	}

}
