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
import com.example.ihalkhata.Activityfragments.PaymentsDoneFragment;
import com.example.ihalkhata.Activityfragments.PaymentsFragment;

public class PaymentListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	private String type;

	public PaymentListAdapter(Activity a, ArrayList<HashMap<String, String>> d,
			String t) {
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
			vi = inflater.inflate(R.layout.payment_list_row, null);
		TextView relation = (TextView) vi.findViewById(R.id.payment_from_label);
		ImageView amountImage = (ImageView) vi
				.findViewById(R.id.payment_amount_image);

		TextView dateValue = (TextView) vi.findViewById(R.id.payment_date);
		TextView payment_payee = (TextView) vi.findViewById(R.id.payment_payee); // payment_payee
		TextView payment_borrower = (TextView) vi
				.findViewById(R.id.payment_borrower); // payment_borrower
		TextView payment_amount = (TextView) vi
				.findViewById(R.id.payment_amount); // payment_amount

		HashMap<String, String> payment = new HashMap<String, String>();
		payment = data.get(position);
		if ("1".equals(type)) {
			relation.setText("From");
			amountImage.setImageResource(R.drawable.offering);
			dateValue.setVisibility(View.GONE);
			payment_borrower.setText(payment
					.get(PaymentsFragment.PAYMENT_BORROWER));
		} else {
			amountImage.setImageResource(R.drawable.expense);
			relation.setText("Paid");
			dateValue.setText(payment.get(PaymentsDoneFragment.PAYMENT_DATE));
			payment_borrower.setText(payment
					.get(PaymentsDoneFragment.PAYMENT_BORROWER));
		}
		// Setting all values in listview
		payment_payee.setText(payment.get(PaymentsFragment.PAYMENT_PAYEE));

		payment_amount.setText(payment.get(PaymentsFragment.PAYMENT_AMOUNT));
		return vi;
	}

}
