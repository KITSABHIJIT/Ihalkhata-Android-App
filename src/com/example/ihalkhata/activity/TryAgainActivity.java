package com.example.ihalkhata.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ihalkhata.R;
import com.example.ihalkhata.utils.Utils;

public class TryAgainActivity extends Activity {
	// login button
	Button btnTryAgain;
	TextView tryAgain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retry);
		if (!Utils.isNetworkAvailable(TryAgainActivity.this)) {
			tryAgain = (TextView) findViewById(R.id.retry_txt);
			tryAgain.setText("Unable to load any pages,\n\t\t\t\tcheck your\n\t\tNetwork Connection");
		}

		// Login button
		btnTryAgain = (Button) findViewById(R.id.btnRetry);

		// Login button click event
		btnTryAgain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Utils.startNewActivity(getApplicationContext(),
						OnLoadActivity.class);
				finish();
			}
		});

	}
}
