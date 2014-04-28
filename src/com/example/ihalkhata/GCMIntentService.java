package com.example.ihalkhata;

import java.util.Random;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ihalkhata.notificationmanager.NotificationReceiverActivity;
import com.example.ihalkhata.notificationmanager.UpdatesReceiverActivity;
import com.example.ihalkhata.utils.SharedResources;
import com.example.ihalkhata.utils.Utils;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String PROJECT_ID = GCMConstants.PROJECT_ID;

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(PROJECT_ID);
		Log.d(TAG, "GCMIntentService init");
	}

	@Override
	protected void onError(Context ctx, String sError) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Error: " + sError);

	}

	@Override
	protected void onMessage(Context ctx, Intent intent) {

		Log.d(TAG, "Message Received");

		String message = intent.getStringExtra("message");
		createGCMNotification(message);
		sendGCMIntent(ctx, message);

	}

	private void sendGCMIntent(Context ctx, String message) {

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("GCM_RECEIVED_ACTION");

		broadcastIntent.putExtra("gcm", message);

		ctx.sendBroadcast(broadcastIntent);

	}

	@Override
	protected void onRegistered(Context ctx, String regId) {
		// TODO Auto-generated method stub
		// send regId to your server
		Log.d(TAG, "Device successfully registered : " + regId);
		if (!Utils.isNullorEmpty(regId)) {
			SharedResources.saveAndroidRegId(getApplicationContext(), regId);
		} else
			Utils.showToastMessage(getApplicationContext(),
					"Android GCM ID is Blank");
	}

	@Override
	protected void onUnregistered(Context ctx, String regId) {
		// TODO Auto-generated method stub
		// send notification to your server to remove that regId
		Log.d(TAG, "Device successfully unregistered : " + regId);
	}

	public void createGCMNotification(String data) {
		try {
			JSONObject notiData = new JSONObject(data);
			int noti_id = new Random().nextInt(10000);
			String contentTitle = "", contentText = "", ticker = "";
			Intent resultIntent = null;
			if (notiData.has("msg")) {
				resultIntent = new Intent(this, UpdatesReceiverActivity.class);
				resultIntent.putExtra("msg", notiData.getString("msg"));
				contentTitle = "Ihalkhata Updates";
				contentText = notiData.getString("msg");
				ticker = "Ihalkhata Updates";
			} else {
				String loggedInUser = SharedResources
						.getLoginUserId(getApplicationContext());
				if (notiData.getString("notiUsers").contains(loggedInUser)) {
					resultIntent = new Intent(this,
							NotificationReceiverActivity.class);

					resultIntent.putExtra("date", notiData.getString("date"));
					resultIntent.putExtra("item", notiData.getString("item"));
					resultIntent.putExtra("desc", notiData.getString("desc"));
					resultIntent.putExtra("amount",
							notiData.getString("amount"));
					resultIntent.putExtra("perHead",
							notiData.getString("perHead"));
					resultIntent.putExtra(
							"shareHolders",
							notiData.getString("shareHolders").replaceAll(",",
									"\n"));
					resultIntent.putExtra("shareholderCount",
							notiData.getString("shareholderCount"));
					resultIntent.putExtra("paidBy",
							notiData.getString("paidBy"));

					resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					contentTitle = notiData.getString("item");
					contentText = "Expense-: Rs."
							+ notiData.getString("perHead") + " ("
							+ notiData.getString("date") + ")";
					ticker = "Ihalkhata Notification!";
				}
			}
			PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
					noti_id, resultIntent, PendingIntent.FLAG_ONE_SHOT);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					getApplicationContext())
					.setSmallIcon(R.drawable.app_logo)
					.setContentTitle(contentTitle)
					.setContentText(contentText)
					.setTicker(ticker)
					.setWhen(System.currentTimeMillis())
					.setDefaults(
							Notification.DEFAULT_SOUND
									| Notification.DEFAULT_VIBRATE)
					.setAutoCancel(true).setContentIntent(resultPendingIntent);

			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(noti_id, mBuilder.build());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
