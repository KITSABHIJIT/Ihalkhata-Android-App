package com.example.ihalkhata.notificationmanager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ihalkhata.R;

public class UpdatesReceiverActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ihalkhata_updates);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			TextView updateTxt = (TextView) findViewById(R.id.update_label);
			updateTxt.setText(extras.getString("msg"));
			
		}
	}

	public void onBackPressed() {
		this.finish();

	}
}