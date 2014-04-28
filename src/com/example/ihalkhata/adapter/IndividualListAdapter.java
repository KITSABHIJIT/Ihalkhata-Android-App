package com.example.ihalkhata.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ihalkhata.R;
import com.example.ihalkhata.Activityfragments.IndividualFragment;

public class IndividualListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public IndividualListAdapter(Activity a,
			ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.individual_list_row, null);

		TextView individual_date = (TextView) vi
				.findViewById(R.id.individual_date); // individual_date
		TextView individual_item = (TextView) vi
				.findViewById(R.id.individual_item); // individual_item
		TextView individual_desc = (TextView) vi
				.findViewById(R.id.individual_desc); // individual_desc
		TextView individual_amount = (TextView) vi
				.findViewById(R.id.individual_amount); // individual_amount

		HashMap<String, String> expense = new HashMap<String, String>();
		expense = data.get(position);

		// Setting all values in listview
		individual_date
				.setText(expense.get(IndividualFragment.INDIVIDUAL_DATE));
		individual_item
				.setText(expense.get(IndividualFragment.INDIVIDUAL_ITEM));
		individual_desc
				.setText(expense.get(IndividualFragment.INDIVIDUAL_DESC));
		individual_amount.setText(expense
				.get(IndividualFragment.INDIVIDUAL_AMOUNT));
		return vi;
	}

}
