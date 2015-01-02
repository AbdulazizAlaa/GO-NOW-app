package com.example.test_bike;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import TabFragments.FeedsFragment;
import TabFragments.RidesFragment;
import adapters.TabsPagerAdapter;
import adapters.drawerListAdapter;
import beans.AppController;
import beans.values;
import model.drawerListItem;

public class Home extends FragmentActivity implements OnItemClickListener,
		TabListener, OnClickListener {

	private ListView drawerLV;

	private ArrayList<drawerListItem> drawerList;

	private drawerListAdapter drawerAdapter;

	private SlidingMenu menu;

	ImageView profilePictureIV;

	private String[] navDrawerItems;
	private TypedArray navDrawerIcons;

	private CharSequence title, drawerTitle;

	private Bundle b;
	private String username, password, email, user_id, file_path;

	TextView searchED, profileNameTV;
	ImageView searchB;
	View profileClickableV;
	SearchView ridesSearchView;

	// tabs
	ViewPager pager;
	TabsPagerAdapter pagerAdapter;
	ActionBar actionBar;

	ArrayList<Fragment> list;

	private String[] tabTitles = { "Feeds", "Rides" };
	boolean drawerOpen = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (getIntent() != null) {
			if (getIntent().getExtras() != null) {
				b = getIntent().getExtras();

				username = b.getString(values.TAG_USERNAME);
				password = b.getString(values.TAG_PASSWORD);
				email = b.getString(values.TAG_EMAIL);
				user_id = b.getString(values.TAG_ID);
				file_path = b.getString(values.TAG_PROFILE_IMG);
				Log.i("HA", username + " home " + user_id);
				if (b.getString(values.TAG_CALLER) == "login") {

				}
			}
		}

		actionBar = getActionBar();

		// ** navigation drawer

		navDrawerItems = getResources().getStringArray(
				R.array.home_drawer_items);
		navDrawerIcons = getResources().obtainTypedArray(
				R.array.home_drawer_icons);

		View drawerLVLayout = getLayoutInflater().inflate(
				R.layout.drawer_list_view, null);

		drawerLV = (ListView) drawerLVLayout
				.findViewById(R.id.home_left_drawer);

		drawerList = new ArrayList<drawerListItem>();

		for (int i = 0; i < navDrawerItems.length; i++) {
			drawerList.add(new drawerListItem(navDrawerItems[i], navDrawerIcons
					.getResourceId(i, -1)));
		}

		navDrawerIcons.recycle();

		drawerAdapter = new drawerListAdapter(getApplicationContext(),
				drawerList);

		View header = getLayoutInflater().inflate(R.layout.drawer_list_header,
				null);

		profileNameTV = (TextView) header
				.findViewById(R.id.drawer_list_header_ProfileNameTV);

		profileClickableV = (View) header
				.findViewById(R.id.drawer_list_header_ProfileClickableV);

		profileNameTV.setText(username);

		profileClickableV.setOnClickListener(this);

		drawerLV.addHeaderView(header);
		profilePictureIV = (ImageView) header
				.findViewById(R.id.drawer_list_header_profilePicIV);

		ImageLoader loader = AppController.getInstance().getImageLoader();

		Log.i("ha", file_path);
		
		loader.get(
				file_path,
				new ImageListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						// TODO Auto-generated method stub
						Log.i("ha", "image:" + e.getMessage());
					}

					@Override
					public void onResponse(ImageContainer image, boolean arg1) {
						// TODO Auto-generated method stub
						if (image.getBitmap() != null){
							profilePictureIV.setImageBitmap(image.getBitmap());
						}else
							Log.i("ha", "no image");
					}
				});

		/*
		 * profilePictureIV.setImageBitmap(Essentials
		 * .getRoundedCornerBitmap(new BitmapFactory().decodeResource(
		 * getResources(), R.drawable.pp_image_profile)));
		 */

		drawerLV.setAdapter(drawerAdapter);

		actionBar.setHomeButtonEnabled(true);
		actionBar.setLogo(R.drawable.home_icon);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setIcon(R.drawable.home_icon);

		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setBehindOffset(100);
		menu.setMenu(drawerLVLayout);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setFadeEnabled(true);
		menu.setFadeDegree(.35f);

		drawerLV.setOnItemClickListener(this);

		// ** end of navigation drawer

		// ** action bar tabs

		pager = (ViewPager) findViewById(R.id.home_pager);

		list = new ArrayList<Fragment>();
		list.add(new FeedsFragment());
		list.add(new RidesFragment());

		pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), list,
				new String[] { "Feeds", "Rides" });

		pager.setAdapter(pagerAdapter);
		actionBar.setNavigationMode(actionBar.NAVIGATION_MODE_TABS);

		for (String tab : tabTitles) {
			actionBar.addTab(actionBar.newTab().setText(tab)
					.setTabListener(this));
		}

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				if (actionBar.getNavigationMode() == actionBar.NAVIGATION_MODE_TABS)
					actionBar.setSelectedNavigationItem(pos);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		// ** end of action bar tabs

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;

		case android.R.id.home:
			menu.toggle();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		switch (v.getId()) {
		case R.id.drawer_list_header_ProfileClickableV:
			i = new Intent(Home.this, Profile.class);

			i.putExtra(values.TAG_ID, user_id);
			i.putExtra(values.TAG_PAGE_USER_ID, user_id);
			i.putExtra(values.TAG_USERNAME, username);
			i.putExtra(values.TAG_EMAIL, email);
			i.putExtra(values.TAG_CALLER, "user");
			i.putExtra(values.TAG_PROFILE_IMG, file_path);

			startActivity(i);
			menu.toggle();

			break;
		default:
			break;
		}
	}

	// drawer list items selected
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
		// TODO Auto-generated method stub
		Intent i;
		switch (pos) {
		case 1:
			i = new Intent(Home.this, Session.class);

			i.putExtra(values.TAG_ID, user_id);
			i.putExtra(values.TAG_CALLER, "home");

			startActivity(i);
			menu.toggle();
			break;
		case 2:
			i = new Intent(Home.this, Store.class);
			i.putExtra(values.TAG_ID, user_id);
			startActivity(i);
			menu.toggle();
			break;
		case 3:
			// people
			i = new Intent(Home.this, PeopleSearch.class);
			i.putExtra(values.TAG_ID, user_id);
			i.putExtra(values.TAG_USERNAME, username);
			i.putExtra(values.TAG_EMAIL, email);
			i.putExtra(values.TAG_PROFILE_IMG, file_path);

			startActivity(i);
			menu.toggle();

			break;
		case 4:
			i = new Intent(Home.this, ChatList.class);
			i.putExtra(values.TAG_ID, user_id);
			i.putExtra(values.TAG_USERNAME, username);
			i.putExtra(values.TAG_EMAIL, email);
			startActivity(i);
			menu.toggle();
			break;
		case 5:
			i = new Intent(Home.this, Settings.class);
			i.putExtra(values.TAG_ID, user_id);
			startActivity(i);
			menu.toggle();
			break;
		case 6:
			menu.toggle();
			break;
		case 7:
			AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);

			dialog.setTitle("Logout")
					.setMessage("Do you want to logout?")
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									SharedPreferences pref = Home.this
											.getSharedPreferences(
													values.TAG_ACCOUNT_FILE,
													Context.MODE_PRIVATE);
									Editor e = pref.edit();

									e.clear();
									e.commit();
									Home.this.finish();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});
			AlertDialog c = dialog.create();
			c.show();
			menu.toggle();
			break;

		default:
			break;
		}
	}

	// handling tabs
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.home, menu);
		MenuItem searchItem = menu.findItem(R.id.home_menu_search);
		ridesSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		ridesSearchView.setBackgroundResource(R.drawable.search_bar_drawable);
		ridesSearchView.setSubmitButtonEnabled(false);
		ridesSearchView.setIconifiedByDefault(false);

		int idED = ridesSearchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		EditText searchED = (EditText) ridesSearchView.findViewById(idED);
		searchED.setTextColor(Color.WHITE);
		searchED.setHintTextColor(Color.WHITE);
		searchED.setHint("Search");
		searchED.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));

		int idIV = ridesSearchView.getContext().getResources()
				.getIdentifier("android:id/search_mag_icon", null, null);
		ImageView imageView = (ImageView) ridesSearchView.findViewById(idIV);
		imageView.setImageResource(android.R.color.transparent);

		ridesSearchView
				.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

					@Override
					public boolean onQueryTextSubmit(String query) {
						// TODO Auto-generated method stub
						Intent i = new Intent(Home.this, PageSearch.class);

						i.putExtra(values.TAG_USERNAME, username);
						i.putExtra(values.TAG_ID, user_id);
						i.putExtra(values.TAG_SEARCH_SEARCH_WORD, query);

						startActivity(i);

						return true;
					}

					@Override
					public boolean onQueryTextChange(String newText) {
						// TODO Auto-generated method stub
						return false;
					}
				});

		return super.onCreateOptionsMenu(menu);
	}

}
