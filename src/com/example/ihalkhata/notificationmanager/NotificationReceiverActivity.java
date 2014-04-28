package com.example.ihalkhata.notificationmanager;

import com.example.ihalkhata.R;
import com.example.ihalkhata.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationReceiverActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			TextView dateTxt = (TextView) findViewById(R.id.noti_date);
			dateTxt.setText(extras.getString("date"));
			TextView itemTxt = (TextView) findViewById(R.id.noti_item);
			itemTxt.setText(extras.getString("item"));
			TextView descTxt = (TextView) findViewById(R.id.noti_description);
			descTxt.setText(extras.getString("desc"));
			TextView perHeadTxt = (TextView) findViewById(R.id.noti_per_head);
			perHeadTxt.setText("Rs." + extras.getString("perHead"));
			TextView shareholderTxt = (TextView) findViewById(R.id.noti_shareholder);
			shareholderTxt.setText(extras.getString("shareHolders"));
			TextView shareholderCountTxt = (TextView) findViewById(R.id.noti_count_shareholder);
			shareholderCountTxt.setText(extras.getString("shareholderCount"));
			TextView amountTxt = (TextView) findViewById(R.id.noti_amount);
			amountTxt.setText("Rs."
					+ Utils.getRoundedValue(Double.parseDouble(extras
							.getString("amount"))));
			TextView paidTxt = (TextView) findViewById(R.id.noti_paid);
			paidTxt.setText(extras.getString("paidBy"));
		}
	}

	public void onBackPressed() {
		this.finish();

	}
}