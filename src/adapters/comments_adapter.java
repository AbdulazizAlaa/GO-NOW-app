package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test_bike.R;

import java.util.ArrayList;

import model.comment_ViewHolder;
import model.comment_data;

public class comments_adapter extends BaseAdapter{

	Context mContext;
	ArrayList<comment_data> list;
	
	public comments_adapter(Context mContext, ArrayList<comment_data> list) {
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
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(v == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			v = inflater.inflate(R.layout.comment_list_item, null);
			
			comment_ViewHolder viewHolder = new comment_ViewHolder();
			
			viewHolder.name = (TextView) v.findViewById(R.id.comment_list_item_ProfilenameTv);
			viewHolder.comment = (TextView) v.findViewById(R.id.comment_list_item_CommentTv);
			
			v.setTag(viewHolder);
		}
		
		comment_ViewHolder holder = (comment_ViewHolder) v.getTag();
		
		holder.name.setText(list.get(pos).getName());
		holder.comment.setText(list.get(pos).getComment());
		
		return v;
	}

}
