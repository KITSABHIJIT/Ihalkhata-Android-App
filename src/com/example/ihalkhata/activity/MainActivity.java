package com.example.ihalkhata.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.example.ihalkhata.R;
import com.example.ihalkhata.Activityfragments.DataEntryFragment;
import com.example.ihalkhata.Activityfragments.ExpenseFragment;
import com.example.ihalkhata.Activityfragments.HomeFragment;
import com.example.ihalkhata.Activityfragments.IndividualFragment;
import com.example.ihalkhata.Activityfragments.NotificationTabStripFragment;
import com.example.ihalkhata.Activityfragments.PaymentTabStripFragment;
import com.example.ihalkhata.menu.MenuAdapter;
import com.example.ihalkhata.menu.MenuItem;
import com.example.ihalkhata.menu.MenuUtils;
import com.example.ihalkhata.utils.SharedResources;
import com.example.ihalkhata.utils.Utils;

public class MainActivity extends SherlockFragmentActivity {

	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private int selectedMenuItem = 100;
	com.actionbarsherlock.app.ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!Utils.isNetworkAvailable(MainActivity.this)) {
			Utils.startNewActivity(getApplicationContext(),
					TryAgainActivity.class);
			finish();
		} else {
			mTitle = mDrawerTitle = getTitle();
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.left_drawer);

			// set a custom shadow that overlays the main content when the
			// drawer
			// opens
			mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
					GravityCompat.START);

			// set up the drawer's list view with items and click listener
			// mDrawerList.setAdapter(new
			// ArrayAdapter<String>(this,R.layout.drawer_list_item,
			// mPlanetTitles));
			ArrayList<MenuItem> menus = MenuUtils.getInstance().parseXml(this,
					R.menu.menu);
			mDrawerList.setAdapter(new MenuAdapter(this, menus));

			mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

			// enable ActionBar app icon to behave as action to toggle nav
			// drawer
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);

			// ActionBarDrawerToggle ties together the the proper interactions
			// between the sliding drawer and the action bar app icon
			mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
			mDrawerLayout, /* DrawerLayout object */
			R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
			R.string.drawer_open, /* "open drawer" description for accessibility */
			R.string.drawer_close /* "close drawer" description for accessibility */
			) {
				public void onDrawerClosed(View view) {
					if (!SharedResources
							.getLoadingStatus(getApplicationContext())) {
						getSupportActionBar().setTitle(mTitle);
						supportInvalidateOptionsMenu(); // creates call to
						// onPrepareOptionsMenu()
					}
				}

				public void onDrawerOpened(View drawerView) {
					if (!SharedResources
							.getLoadingStatus(getApplicationContext())) {
						getSupportActionBar().setTitle(mDrawerTitle);
						supportInvalidateOptionsMenu(); // creates call to
						// onPrepareOptionsMenu()
					}
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);

			if (savedInstanceState == null) {
				selectItem(0);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, (com.actionbarsherlock.view.Menu) menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home: {
			if (!SharedResources.getLoadingStatus(getApplicationContext())) {
				if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
					mDrawerLayout.closeDrawer(mDrawerList);
				} else {
					mDrawerLayout.openDrawer(mDrawerList);
				}
			}
			break;
		}

		case R.id.action_refresh:
			if (!SharedResources.getLoadingStatus(getApplicationContext()))
				refreshItem(selectedMenuItem);
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	// The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (!SharedResources.getLoadingStatus(getApplicationContext()))
				selectItem(position);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void selectItem(int position) {

		if (selectedMenuItem != position) {
			selectedMenuItem = position;
			switch (position) {
			case 0:
				mTitle = HomeFragment.TAG;
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.content, HomeFragment.newInstance(),
								HomeFragment.TAG).commit();
				break;
			case 1:
				mTitle = NotificationTabStripFragment.TAG;
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.content,
								NotificationTabStripFragment.newInstance(),
								NotificationTabStripFragment.TAG).commit();
				break;
			case 2:
				mTitle = DataEntryFragment.TAG;
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.content, DataEntryFragment.newInstance(),
								DataEntryFragment.TAG).commit();
				break;
			case 3:
				mTitle = PaymentTabStripFragment.TAG;
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.content,
								PaymentTabStripFragment.newInstance(),
								PaymentTabStripFragment.TAG).commit();
				break;
			case 4:
				mTitle = ExpenseFragment.TAG;
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.content, ExpenseFragment.newInstance(),
								ExpenseFragment.TAG).commit();
				break;
			case 5:
				mTitle = IndividualFragment.TAG;
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.content, IndividualFragment.newInstance(),
								IndividualFragment.TAG).commit();
				break;
			case 6:
				SharedResources.clearAllUserData(getApplicationContext());
				Utils.showToastMessage(getApplicationContext(),
						"Succesfully logged out");
				finish();
				break;
			default:
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.content, HomeFragment.newInstance(),
								HomeFragment.TAG).commit();
				break;
			}
		}
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private void refreshItem(int position) {

		switch (position) {
		case 0:
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.content, HomeFragment.newInstance(),
							HomeFragment.TAG).commit();
			break;
		case 1:
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.content,
							NotificationTabStripFragment.newInstance(),
							NotificationTabStripFragment.TAG).commit();
			break;
		case 2:
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.content, DataEntryFragment.newInstance(),
							DataEntryFragment.TAG).commit();
			break;
		case 3:
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.content, PaymentTabStripFragment.newInstance(),
							PaymentTabStripFragment.TAG).commit();
			break;
		case 4:
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.content, ExpenseFragment.newInstance(),
							ExpenseFragment.TAG).commit();
			break;
		case 5:
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.content, IndividualFragment.newInstance(),
							IndividualFragment.TAG).commit();
			break;
		case 6:
			SharedResources.clearAllUserData(getApplicationContext());
			Utils.showToastMessage(getApplicationContext(),
					"Succesfully logged out");
			finish();
			break;
		default:
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.content, HomeFragment.newInstance(),
							HomeFragment.TAG).commit();
			break;
		}

	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("Really Exit?")
				.setMessage("Are you sure you want to exit?")
				.setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes, new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						getSupportFragmentManager().popBackStackImmediate();
						finish();

					}
				}).create().show();
	}

}