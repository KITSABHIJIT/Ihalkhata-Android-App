package com.example.ihalkhata.Activityfragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.ihalkhata.R;
import com.example.ihalkhata.activity.TryAgainActivity;
import com.example.ihalkhata.adapter.BlankListAdapter;
import com.example.ihalkhata.adapter.NotificationListAdapter;
import com.example.ihalkhata.utils.AlertDialogManager;
import com.example.ihalkhata.utils.JSONParser;
import com.example.ihalkhata.utils.SharedResources;
import com.example.ihalkhata.utils.Utils;
import com.example.ihalkhata.utils.WebServiceConstants;

public class CreditNotificationFragment extends SherlockFragment {

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog waitDialog;
	ListView credit_list;
	private static String waitMessage;
	NotificationListAdapter creditAdapter;
	BlankListAdapter blankListAdapter;
	View CreditNotificationView;
	boolean ActivityOn = true;
	public static final String NOTIFICATION_USER = "user";
	public static final String NOTIFICATION_DATE = "date"; // parent node
	public static final String NOTIFICATION_ITEM = "item";
	public static final String NOTIFICATION_DESC = "desc";
	public static final String NOTIFICATION_AMOUNT = "balance";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		CreditNotificationView = inflater.inflate(
				R.layout.fragment_credit_notification, container, false);
		waitMessage = "Loading Notificatons";
		String group = SharedResources.getLoginUserGroupId(getActivity());
		String userId = SharedResources.getLoginUserId(getActivity());
		new CreditNotifiactionTask().execute(group, userId);
		return CreditNotificationView;
	}

	/**
	 * Set the callback to null so we don't accidentally leak the Activity
	 * instance.
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		ActivityOn = false;
	}

	private JSONObject processCreditNotifiaction(String groupId, String userId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("param1", groupId));
		nameValuePairs.add(new BasicNameValuePair("param2", userId));
		nameValuePairs.add(new BasicNameValuePair("param3", "2"));
		return JSONParser.getJSONFromUrl(WebServiceConstants.NOTIFICATION,
				nameValuePairs);
	}

	private class CreditNotifiactionTask extends
			AsyncTask<String, Object, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... param) {
			// TODO Auto-generated method stub
			return processCreditNotifiaction(param[0], param[1]);

		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			/*
			 * waitDialog = new ProgressDialog(getActivity());
			 * waitDialog.setMessage(waitMessage);
			 * waitDialog.setCancelable(false); waitDialog.show();
			 */
			getActivity().findViewById(R.id.content).setVisibility(View.GONE);
			getActivity().findViewById(R.id.loadingPanel).setVisibility(
					View.VISIBLE);
			SharedResources.startLoading(getActivity());
		}

		protected void onPostExecute(JSONObject mainJsonData) {
			if (!ActivityOn)
				return;
			Log.i("Async-Example", "onPostExecute Called");
			// String userName =
			// SharedResources.getLoginUserName(getActivity());
			if (mainJsonData != null) {
				// Utils.showToastMessage(getActivity(), userName +
				// "'s Credits.");
				ArrayList<HashMap<String, String>> notiList = new ArrayList<HashMap<String, String>>();
				credit_list = (ListView) CreditNotificationView
						.findViewById(R.id.credit_list);
				try {
					if (mainJsonData.has("success")) {
						JSONArray creditNotificationJson = new JSONArray(
								mainJsonData.getString("msg"));
						for (int i = 0; i < creditNotificationJson.length(); i++) {
							HashMap<String, String> expense = new HashMap<String, String>();
							expense.put(NOTIFICATION_USER,
									creditNotificationJson.getJSONObject(i)
											.getString(NOTIFICATION_USER));
							expense.put(NOTIFICATION_DATE,
									creditNotificationJson.getJSONObject(i)
											.getString(NOTIFICATION_DATE));
							expense.put(NOTIFICATION_ITEM,
									creditNotificationJson.getJSONObject(i)
											.getString(NOTIFICATION_ITEM));
							expense.put(NOTIFICATION_DESC,
									creditNotificationJson.getJSONObject(i)
											.getString(NOTIFICATION_DESC));
							expense.put(NOTIFICATION_AMOUNT,
									creditNotificationJson.getJSONObject(i)
											.getString(NOTIFICATION_AMOUNT));
							notiList.add(expense);
						}
						if (notiList.size() > 0) {
							creditAdapter = new NotificationListAdapter(
									getActivity(), notiList, "2");
							credit_list.setAdapter(creditAdapter);
						} else {
							HashMap<String, String> expense = new HashMap<String, String>();
							expense.put(BlankListAdapter.Blank_Data,
									"No data Available");
							notiList.add(expense);
							blankListAdapter = new BlankListAdapter(
									getActivity(), notiList);
							credit_list.setAdapter(blankListAdapter);
						}
					} else {
						Utils.startNewActivity(getActivity(),
								TryAgainActivity.class);
						getActivity().finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Utils.startNewActivity(getActivity(), TryAgainActivity.class);
				getActivity().finish();

			}
			/*
			 * if (null != waitDialog && waitDialog.isShowing()) {
			 * waitDialog.dismiss(); }
			 */getActivity().findViewById(R.id.loadingPanel).setVisibility(
					View.GONE);
			getActivity().findViewById(R.id.content)
					.setVisibility(View.VISIBLE);
			SharedResources.endLoading(getActivity());
		}
	}

}