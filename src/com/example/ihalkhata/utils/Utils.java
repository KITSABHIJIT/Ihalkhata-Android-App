package com.example.ihalkhata.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utils {

	public static ProgressDialog simpleWaitDialog;

	public static String getJSONString(String url) {
		String jsonString = null;
		HttpURLConnection linkConnection = null;
		try {
			URL linkurl = new URL(url);
			linkConnection = (HttpURLConnection) linkurl.openConnection();
			linkConnection.setConnectTimeout(50000); // timeout is in
														// milliseconds
			int responseCode = linkConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream linkinStream = linkConnection.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int j = 0;
				while ((j = linkinStream.read()) != -1) {
					baos.write(j);
				}
				byte[] data = baos.toByteArray();
				jsonString = new String(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (linkConnection != null) {
				linkConnection.disconnect();
			}
		}
		return jsonString;
	}

	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void showWaitmessage(Context _context, String msg) {
		simpleWaitDialog = new ProgressDialog(_context);
		simpleWaitDialog.setMessage(msg);
		simpleWaitDialog.setCancelable(false);
		simpleWaitDialog.show();
	}

	public static void clearWaitmessage() {
		if (null != simpleWaitDialog && simpleWaitDialog.isShowing()) {
			simpleWaitDialog.dismiss();
		}
	}

	public static boolean isNullorEmpty(String testString) {
		if (testString == null || testString.trim().equals("") || testString.trim().equals("null"))
			return true;
		else
			return false;
	}

	public static void addDelay(int milliseconds) {
		try {

			Thread.sleep(milliseconds);
		} catch (Exception e) {
			System.out.println("Exception caught");
		}
	}

	public static void showToastMessage(Context _contest, String msg) {
		Toast.makeText(_contest, msg, Toast.LENGTH_LONG).show();
	}

	public static void startNewActivity(Context _context, Class<?> cls) {
		Intent i = new Intent(_context, cls);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Staring New Activity
		_context.startActivity(i);

	}

	public static String[] JSONArrayToStringArray(JSONArray keyArray) {
		String[] keyAttributes = new String[keyArray.length()];
		for (int i = 0; i < keyArray.length(); i++) {
			try {
				keyAttributes[i] = keyArray.getString(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return keyAttributes;
	}

	public static CharSequence[] JSONArrayToCharSequenceArray(JSONArray keyArray) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < keyArray.length(); i++) {
			try {
				list.add(keyArray.getString(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		CharSequence[] cs = list.toArray(new CharSequence[list.size()]);
		return cs;
	}

	public static boolean greaterThanToday(String inputDate, String format) {
		boolean result = false;
		Date givenDate;
		try {
			givenDate = new SimpleDateFormat(format).parse(inputDate);
			Date today = new Date();
			if (givenDate.after(today))
				result = true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static String getRoundedValue(double value) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(value);
	}
}
