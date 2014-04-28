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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.ihalkhata.R;
import com.example.ihalkhata.activity.TryAgainActivity;
import com.example.ihalkhata.adapter.BlankListAdapter;
import com.example.ihalkhata.adapter.ExpenseListAdapter;
import com.example.ihalkhata.utils.AlertDialogManager;
import com.example.ihalkhata.utils.JSONParser;
import com.example.ihalkhata.utils.SharedResources;
import com.example.ihalkhata.utils.Utils;
import com.example.ihalkhata.utils.WebServiceConstants;

public class ExpenseFragment extends SherlockFragment {

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog waitDialog;
	ListView expense_list;
	public static final String EXPENSE_USER = "userId"; // parent node
	public static final String EXPENSE_AMOUNT = "expense";
	public static final String OFFERING_AMOUNT = "offering";
	private Spinner period;
	private static String waitMessage;
	ExpenseListAdapter expenseAdapter;
	BlankListAdapter blankListAdapter;
	View ExpenseView;
	boolean ActivityOn = true;

	public static final String TAG = "Expenses";

	public static ExpenseFragment newInstance() {
		return new ExpenseFragment();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ExpenseView = inflater.inflate(R.layout.fragment_expense, container,
				false);

		addListenerOnSpinnerItemSelection();
		return ExpenseView;
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

	public void addListenerOnSpinnerItemSelection() {
		period = (Spinner) ExpenseView.findViewById(R.id.period_spinner);
		period.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	private JSONObject processExpenseData(String groupId, String period) {
		if ("Current Month".equals(period))
			period = "0";
		else if ("Last month".equals(period))
			period = "1";
		else if ("Last Quarter".equals(period))
			period = "2";
		else if ("Last Year".equals(period))
			period = "3";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("param1", groupId));
		nameValuePairs.add(new BasicNameValuePair("param2", period));
		return JSONParser.getJSONFromUrl(WebServiceConstants.EXPENSE,
				nameValuePairs);
	}

	private class ExpenseTask extends AsyncTask<String, Object, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... param) {
			// TODO Auto-generated method stub
			return processExpenseData(param[0], param[1]);

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
				/*
				 * Utils.showToastMessage(getActivity(), userName +
				 * " Check your Expenses");
				 */
				ArrayList<HashMap<String, String>> expenseList = new ArrayList<HashMap<String, String>>();
				expense_list = (ListView) ExpenseView
						.findViewById(R.id.expense_list);
				try {
					if (mainJsonData.has("success")) {
						JSONArray expenseJson = new JSONArray(
								mainJsonData.getString("msg"));
						if (expenseJson.getJSONObject(0).has(EXPENSE_AMOUNT)
								&& expenseJson.getJSONObject(0).has(
										OFFERING_AMOUNT)) {
							for (int i = 0; i < expenseJson.length(); i++) {
								HashMap<String, String> expense = new HashMap<String, String>();
								expense.put(
										EXPENSE_USER,
										expenseJson.getJSONObject(i).getString(
												EXPENSE_USER));
								expense.put(
										EXPENSE_AMOUNT,
										expenseJson.getJSONObject(i).getString(
												EXPENSE_AMOUNT));
								expense.put(
										OFFERING_AMOUNT,
										expenseJson.getJSONObject(i).getString(
												OFFERING_AMOUNT));
								expenseList.add(expense);
							}
							expenseAdapter = new ExpenseListAdapter(
									getActivity(), expenseList);
							expense_list.setAdapter(expenseAdapter);
						} else {
							HashMap<String, String> expense = new HashMap<String, String>();
							expense.put(BlankListAdapter.Blank_Data,
									"No data Available");
							expenseList.add(expense);
							blankListAdapter = new BlankListAdapter(
									getActivity(), expenseList);
							expense_list.setAdapter(blankListAdapter);
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
			getActivity().findViewById(R.id.loadingPanel).setVisibility(
					View.GONE);
			getActivity().findViewById(R.id.content)
					.setVisibility(View.VISIBLE);
			SharedResources.endLoading(getActivity());
		}
	}

	private class CustomOnItemSelectedListener implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			waitMessage = "Loading Expenses \nof "
					+ parent.getItemAtPosition(pos).toString();
			String periodValue = parent.getItemAtPosition(pos).toString();
			String groupId = SharedResources.getLoginUserGroupId(getActivity());
			new ExpenseTask().execute(groupId, periodValue);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}

	}

}