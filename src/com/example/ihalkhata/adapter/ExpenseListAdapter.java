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
import com.example.ihalkhata.Activityfragments.ExpenseFragment;

public class ExpenseListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	
	public ExpenseListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
			vi = inflater.inflate(R.layout.expense_list_row, null);

		TextView expense_user = (TextView) vi.findViewById(R.id.expense_user); // expense_user
		TextView expense_amount = (TextView) vi
				.findViewById(R.id.expense_amount); // expense_amount
		TextView offering_amount = (TextView) vi
				.findViewById(R.id.offering_amount); // expense_amount
		HashMap<String, String> expense = new HashMap<String, String>();
		expense = data.get(position);

		// Setting all values in listview
		expense_user.setText(expense.get(ExpenseFragment.EXPENSE_USER));
		expense_amount.setText(expense.get(ExpenseFragment.EXPENSE_AMOUNT));
		offering_amount.setText(expense.get(ExpenseFragment.OFFERING_AMOUNT));
		return vi;
	}

}
