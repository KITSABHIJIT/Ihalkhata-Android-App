package com.example.ihalkhata.utils;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.ihalkhata.activity.OnLoadActivity;

public class SharedResources {
	public static void saveBooleanLoginStatus(Context _context, boolean value,
			String user, String userName, String groupId, String email) {
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("ISLOGGEDIN", value);
		editor.putString("USERID", user);
		editor.putString("USERNAME", userName);
		editor.putString("GROUPID", groupId);
		editor.putString("EMAIL", email);
		editor.commit();
	}

	public static void saveUserImage(Context _context, boolean value, Bitmap bmp) {
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("ISIMAGEAVAILABLE", value);
		editor.putString("USERIAMGE", encodedImage);
		editor.commit();
	}

	public static boolean getBooleanImageStatus(Context _context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		return preferences.getBoolean("ISIMAGEAVAILABLE", false);
	}

	public static Bitmap getUserImage(Context _context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		Bitmap bitmap = null;
		String previouslyEncodedImage = preferences
				.getString("USERIAMGE", null);
		if (!previouslyEncodedImage.equalsIgnoreCase("")) {
			byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return bitmap;
	}

	public static boolean getBooleanLoginStatus(Context _context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		return preferences.getBoolean("ISLOGGEDIN", false);
	}

	public static String getLoginUserId(Context _context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		return preferences.getString("USERID", null);
	}

	public static String getLoginUserName(Context _context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		return preferences.getString("USERNAME", null);
	}

	public static String getLoginUserGroupId(Context _context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		return preferences.getString("GROUPID", null);
	}

	public static String getLoginUserEmail(Context _context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		return preferences.getString("EMAIL", null);
	}

	public static void saveAndroidRegId(Context _context, String regId) {
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("ANDROID_ID", regId);
		editor.commit();
	}

	public static void startLoading(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("LOADING_STATUS", true);
		editor.commit();
	}

	public static void endLoading(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("LOADING_STATUS", false);
		editor.commit();
	}

	public static boolean getLoadingStatus(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		return preferences.getBoolean("LOADING_STATUS", false);
	}

	public static String getAndroidRegId(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		return preferences.getString("ANDROID_ID", null);
	}

	public static void clearAllUserData(Context _context) {
		// Clearing all data from Shared Preferences
		SharedPreferences preferences = _context.getSharedPreferences(
				"PROJECTNAME", android.content.Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, OnLoadActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);

	}

}
