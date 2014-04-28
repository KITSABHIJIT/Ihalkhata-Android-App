package com.example.ihalkhata.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;

import com.example.ihalkhata.GCMConstants;
import com.example.ihalkhata.R;
import com.example.ihalkhata.utils.SharedResources;
import com.example.ihalkhata.utils.Utils;
import com.google.android.gcm.GCMRegistrar;

public class OnLoadActivity extends Activity {
	private static final String PROJECT_ID = GCMConstants.PROJECT_ID;
	private String regId = "";
	IntentFilter gcmFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_on_load);
		gcmFilter = new IntentFilter();
		gcmFilter.addAction("GCM_RECEIVED_ACTION");
		registerClient();
		if (SharedResources.getBooleanLoginStatus(getApplicationContext())) {
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
			finish();
		} else {
			Intent i = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(i);
			finish();
		}

	}

	public void registerClient() {

		try {
			// Check that the device supports GCM (should be in a try / catch)
			GCMRegistrar.checkDevice(this);

			// Check the manifest to be sure this app has all the required
			// permissions.
			GCMRegistrar.checkManifest(this);

			// Get the existing registration id, if it exists.
			regId = GCMRegistrar.getRegistrationId(this);

			if (Utils.isNullorEmpty(regId)) {
				GCMRegistrar.unregister(getApplicationContext());
				// register this device for this project
				GCMRegistrar.register(getApplicationContext(), PROJECT_ID);
				regId = GCMRegistrar.getRegistrationId(getApplicationContext());
			} else
				SharedResources
						.saveAndroidRegId(getApplicationContext(), regId);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.on_load, menu);
		return true;
	}

}
