package com.example.ihalkhata.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ihalkhata.R;
import com.example.ihalkhata.utils.AlertDialogManager;
import com.example.ihalkhata.utils.JSONParser;
import com.example.ihalkhata.utils.SharedResources;
import com.example.ihalkhata.utils.Utils;
import com.example.ihalkhata.utils.WebServiceConstants;

public class LoginActivity extends Activity {// Email, password edittext
	EditText txtUsername, txtPassword;

	// login button
	Button btnLogin;
	private ProgressDialog loginWaitDialog;
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Session Manager Class
	// SessionManager session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Session Manager
		// session = new SessionManager(getApplicationContext());

		// Email, Password input text
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtUsername.setHint(Html.fromHtml("<i>User name</i>"));
		txtPassword.setHint(Html.fromHtml("<i>Password</i>"));

		/*
		 * Toast.makeText(getApplicationContext(), "User Login Status: " +
		 * session.isLoggedIn(), Toast.LENGTH_LONG) .show();
		 */

		// Login button
		btnLogin = (Button) findViewById(R.id.btnLogin);

		// Login button click event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!Utils.isNetworkAvailable(LoginActivity.this)) {
					Utils.startNewActivity(getApplicationContext(),
							TryAgainActivity.class);
					finish();
				} else {
					String cryptoUserName = txtUsername.getText().toString();
					String cryptoPassword = txtPassword.getText().toString();

					if (cryptoUserName.trim().length() == 0) {
						txtUsername.requestFocus();
						txtUsername.setError("Please enter UserName!");
					} else if (cryptoPassword.trim().length() == 0) {
						txtPassword.requestFocus();
						txtPassword.setError("Please enter Password!");
					}
					// Getting first & second values and passing to show result
					else {
						new LoginTask().execute(cryptoUserName, cryptoPassword);

					}
				}
			}
		});

	}

	private String processLogin(String username, String password) {
		// Add your data
		String result = "System Error...";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("param1", username));
		nameValuePairs.add(new BasicNameValuePair("param2", password));
		JSONObject loginData = JSONParser.getJSONFromUrl(
				WebServiceConstants.DOLOGIN, nameValuePairs);
		try {
			if (loginData != null) {
				if (loginData.getBoolean("success")) {
					JSONObject mainJsonData = new JSONObject(
							loginData.getString("msg"));
					SharedResources.saveBooleanLoginStatus(
							getApplicationContext(), true,
							mainJsonData.getString("userId"),
							mainJsonData.getString("userName"),
							mainJsonData.getString("groupId"),
							mainJsonData.getString("email"));

					List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(
							2);
					nameValuePairs1.add(new BasicNameValuePair("param1",
							SharedResources
									.getAndroidRegId(getApplicationContext())));
					nameValuePairs1.add(new BasicNameValuePair("param2",
							SharedResources
									.getLoginUserId(getApplicationContext())));
					JSONObject regResult = JSONParser.getJSONFromUrl(
							WebServiceConstants.ANDROIDREGISTRATION,
							nameValuePairs1);
					if (regResult != null) {
						if (regResult.getBoolean("success")) {
							result = "success";
						} else {
							result = "Android GCM Registration failed";
						}
					}
				} else {
					result = loginData.getString("msg");
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}

	private class LoginTask extends AsyncTask<String, Object, String> {

		@Override
		protected String doInBackground(String... param) {
			// TODO Auto-generated method stub
			return processLogin(param[0], param[1]);

		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			/*
			 * simpleWaitDialog = ProgressDialog.show(LoginActivity.this,
			 * "Wait", "Logging...", true);
			 */
			loginWaitDialog = new ProgressDialog(LoginActivity.this);
			loginWaitDialog.setMessage("Processing\n Request ...");
			loginWaitDialog.setCancelable(true);
			loginWaitDialog.show();

		}

		protected void onPostExecute(String result) {
			Log.i("Async-Example", "onPostExecute Called");
			if (result.equals("success")) {
				// Staring MainActivity
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(i);
				finish();

			} else {
				alert.showAlertDialog(LoginActivity.this, "Login failed..",
						result, false);

			}

			if (null != loginWaitDialog && loginWaitDialog.isShowing()) {
				loginWaitDialog.dismiss();
			}

		}
	}

}
