package com.example.ihalkhata.Activityfragments;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.astuetz.PagerSlidingTabStrip;
import com.example.ihalkhata.R;

public class PaymentTabStripFragment extends SherlockFragment {

	public static final String TAG = "Payments";

	public static PaymentTabStripFragment newInstance() {
		return new PaymentTabStripFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_payments, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		private final String[] TITLES = { "Payments", "Already Paid" };
		private final int[] ICONS = { R.drawable.expense, R.drawable.offering };

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		public int getPageIconResId(int position) {
			return ICONS[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public SherlockFragment getItem(int position) {
			switch (position) {
			case 0:
				// Fragement for DebitNotification Tab
				return new PaymentsFragment();
			case 1:
				// Fragment for CreditNotification Tab
				return new PaymentsDoneFragment();
			}
			return null;
		}

	}

}
