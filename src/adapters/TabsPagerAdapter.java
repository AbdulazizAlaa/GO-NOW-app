package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	ArrayList<Fragment> list;

	String [] CONTENT;
	
	public TabsPagerAdapter(FragmentManager fm, ArrayList<Fragment> list, String [] CONTENT) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.list = list;
		this.CONTENT = CONTENT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return CONTENT[position % CONTENT.length].toUpperCase();
	}

	@Override
	public Fragment getItem(int pos) {
		// TODO Auto-generated method stub
		return list.get(pos);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
