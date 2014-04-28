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

public class BlankListAdapter extends BaseAdapter {
	public static final String Blank_Data="blank_data";
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	
	public BlankListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
			vi = inflater.inflate(R.layout.blank_list_row, null);
		TextView blank_label = (TextView) vi.findViewById(R.id.blank_label); // expense_user
		HashMap<String, String> expense = new HashMap<String, String>();
		expense = data.get(position);

		// Setting all values in listview
		blank_label.setText(expense.get(Blank_Data));
		return vi;
	}

}
