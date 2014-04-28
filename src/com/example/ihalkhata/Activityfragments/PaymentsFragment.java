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
import com.example.ihalkhata.adapter.PaymentListAdapter;
import com.example.ihalkhata.utils.AlertDialogManager;
import com.example.ihalkhata.utils.JSONParser;
import com.example.ihalkhata.utils.SharedResources;
import com.example.ihalkhata.utils.Utils;
import com.example.ihalkhata.utils.WebServiceConstants;

public class PaymentsFragment extends SherlockFragment {

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog waitDialog;
	ListView payment_list;
	public static final String PAYMENT_PAYEE = "payee"; // parent node
	public static final String PAYMENT_BORROWER = "borrower";
	public static final String PAYMENT_AMOUNT = "amount";
	PaymentListAdapter paymentAdapter;
	BlankListAdapter blankListAdapter;
	View PaymentView;
	boolean ActivityOn = true;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		PaymentView = inflater.inflate(R.layout.fragment_payment_tab,
				container, false);
		String groupId = SharedResources.getLoginUserGroupId(getActivity());
		new PaymentTask().execute(groupId);
		return PaymentView;
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

	private JSONObject processPaymentData(String groupId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("param1", groupId));
		return JSONParser.getJSONFromUrl(WebServiceConstants.PAYMENT,
				nameValuePairs);
	}

	private class PaymentTask extends AsyncTask<String, Object, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... param) {
			// TODO Auto-generated method stub
			return processPaymentData(param[0]);

		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			/*
			 * simpleWaitDialog = ProgressDialog.show(LoginActivity.this,
			 * "Wait", "Logging...", true);
			 */
			/*
			 * waitDialog = new ProgressDialog(getActivity());
			 * waitDialog.setMessage("Loading ...");
			 * waitDialog.setCancelable(false); waitDialog.show();
			 */
			SharedResources.startLoading(getActivity());
		}

		protected void onPostExecute(JSONObject mainJsonData) {
			if (!ActivityOn)
				return;
			Log.i("Async-Example", "onPostExecute Called");
			// String userName =
			// SharedResources.getLoginUserName(getActivity());
			if (mainJsonData != null) {
				/*
				 * Utils.showToastMessage(getActivity(), userName +
				 * " Check your Payments");
				 */
				ArrayList<HashMap<String, String>> paymentList = new ArrayList<HashMap<String, String>>();
				payment_list = (ListView) PaymentView
						.findViewById(R.id.paymentlist);
				try {
					if (mainJsonData.has("success")) {
						JSONArray paymentJson = new JSONArray(
								mainJsonData.getString("msg"));
						for (int i = 0; i < paymentJson.length(); i++) {
							HashMap<String, String> payment = new HashMap<String, String>();
							payment.put(PAYMENT_PAYEE, paymentJson
									.getJSONObject(i).getString(PAYMENT_PAYEE));
							payment.put(
									PAYMENT_BORROWER,
									paymentJson.getJSONObject(i).getString(
											PAYMENT_BORROWER));
							payment.put(PAYMENT_AMOUNT,
									"Rs."
											+ paymentJson.getJSONObject(i)
													.getString(PAYMENT_AMOUNT));
							paymentList.add(payment);
						}
						if (paymentList.size() > 0) {
							paymentAdapter = new PaymentListAdapter(
									getActivity(), paymentList, "1");
							payment_list.setAdapter(paymentAdapter);
						} else {
							HashMap<String, String> expense = new HashMap<String, String>();
							expense.put(BlankListAdapter.Blank_Data,
									"No data Available");
							paymentList.add(expense);
							blankListAdapter = new BlankListAdapter(
									getActivity(), paymentList);
							payment_list.setAdapter(blankListAdapter);
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
			 */
			SharedResources.endLoading(getActivity());
		}
	}
}
