package com.example.ihalkhata.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ihalkhata.R;
import com.example.ihalkhata.Activityfragments.CreditNotificationFragment;

public class NotificationListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	private String type;

	public NotificationListAdapter(Activity a,
			ArrayList<HashMap<String, String>> d, String t) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		type = t;
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
			vi = inflater.inflate(R.layout.notification_list_row, null);
		ImageView amountImage = (ImageView) vi.findViewById(R.id.money_image);
		if ("1".equals(type)) {
			amountImage.setImageResource(R.drawable.offering);
		} else {
			amountImage.setImageResource(R.drawable.expense);
		}
		TextView notification_user = (TextView) vi
				.findViewById(R.id.notification_user); // notification_user
		TextView notification_date = (TextView) vi
				.findViewById(R.id.notification_date); // notification_date
		TextView notification_item = (TextView) vi
				.findViewById(R.id.notification_item); // notification_item
		TextView notification_desc = (TextView) vi
				.findViewById(R.id.notification_desc); // notification_desc
		TextView notification_amount = (TextView) vi
				.findViewById(R.id.notification_amount); // notification_amount

		HashMap<String, String> expense = new HashMap<String, String>();
		expense = data.get(position);

		// Setting all values in listview
		notification_user.setText(expense
				.get(CreditNotificationFragment.NOTIFICATION_USER));
		notification_date.setText(expense
				.get(CreditNotificationFragment.NOTIFICATION_DATE));
		notification_item.setText(expense
				.get(CreditNotificationFragment.NOTIFICATION_ITEM));
		notification_desc.setText(expense
				.get(CreditNotificationFragment.NOTIFICATION_DESC));
		notification_amount.setText(expense
				.get(CreditNotificationFragment.NOTIFICATION_AMOUNT));
		return vi;
	}

}
