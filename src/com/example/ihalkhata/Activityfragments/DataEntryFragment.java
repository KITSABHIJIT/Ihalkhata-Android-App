package com.example.ihalkhata.Activityfragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.ihalkhata.R;
import com.example.ihalkhata.activity.TryAgainActivity;
import com.example.ihalkhata.customview.CustomAutoCompleteTextView;
import com.example.ihalkhata.dialogFragment.MultiSelectDialog;
import com.example.ihalkhata.dialogFragment.SingleSelectDialog;
import com.example.ihalkhata.notificationmanager.NotificationReceiverActivity;
import com.example.ihalkhata.utils.AlertDialogManager;
import com.example.ihalkhata.utils.JSONParser;
import com.example.ihalkhata.utils.SharedResources;
import com.example.ihalkhata.utils.Utils;
import com.example.ihalkhata.utils.WebServiceConstants;

public class DataEntryFragment extends SherlockFragment {
	EditText dateEditText;
	EditText shareholderEditText;
	TextView shareholderHiddenText;
	EditText paidByEditText;
	TextView paidByHiddenText;
	List<Integer> mSelectedItems;
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog waitDialog;
	String[] items;
	CharSequence[] userList;
	CharSequence[] userIdList;
	boolean allUserChecked = false;
	boolean personalChecked = false;
	View DataEntryView;
	boolean ActivityOn = true;
	public static final String TAG = "Enter Data";

	public static DataEntryFragment newInstance() {
		return new DataEntryFragment();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DataEntryView = inflater.inflate(R.layout.fragment_entry, container,
				false);
		String userId = SharedResources.getLoginUserId(getActivity());
		String groupId = SharedResources.getLoginUserGroupId(getActivity());
		new LoadItemTask().execute(userId, groupId);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		dateEditText = (EditText) DataEntryView.findViewById(R.id.entry_date);
		dateEditText.setText(sdf.format(new Date()));
		// personalChkBx click event handler
		CheckBox personalChkBx = (CheckBox) DataEntryView
				.findViewById(R.id.entry_personal);
		personalChkBx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				onPersonalClicked(buttonView);

			}
		});
		// allShareHolderChkBx click event handler
		CheckBox allShareHolderChkBx = (CheckBox) DataEntryView
				.findViewById(R.id.entry_allshareholders);

		allShareHolderChkBx
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						onCheckboxClicked(buttonView);

					}
				});

		// Date picker click event handler

		EditText dateField = (EditText) DataEntryView
				.findViewById(R.id.entry_date);
		dateField.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				selectDate(v);
			}
		});
		ImageButton ib = (ImageButton) DataEntryView
				.findViewById(R.id.date_button);
		ib.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				selectDate(v);
			}
		});
		// shareHolderSelect click event handler
		EditText shareHolderSelect = (EditText) DataEntryView
				.findViewById(R.id.entry_shareholder);
		shareHolderSelect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				selectShareholder(v);
			}
		});

		ImageButton shareHolderib = (ImageButton) DataEntryView
				.findViewById(R.id.entry_shareholder_button);
		shareHolderib.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				selectShareholder(v);
			}
		});
		// paidBy click event handler
		EditText paidBy = (EditText) DataEntryView
				.findViewById(R.id.entry_paidby);
		paidBy.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				selectPaidBy(v);
			}
		});

		ImageButton paidByib = (ImageButton) DataEntryView
				.findViewById(R.id.entry_paidby_button);
		paidByib.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPaidBy(v);
			}
		});
		// Submit button click event handler
		Button button = (Button) DataEntryView.findViewById(R.id.entry_button);

		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showConfirmation(v);
			}
		});
		return DataEntryView;

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

	/* Check box listener starts here */
	public void onCheckboxClicked(View view) {
		CheckBox chk = (CheckBox) DataEntryView
				.findViewById(R.id.entry_allshareholders);
		CheckBox chkDisable = (CheckBox) DataEntryView
				.findViewById(R.id.entry_personal);
		EditText shareholder = (EditText) DataEntryView
				.findViewById(R.id.entry_shareholder);
		ImageButton shareholder_button = (ImageButton) DataEntryView
				.findViewById(R.id.entry_shareholder_button);
		TextView shareholderLabel = (TextView) DataEntryView
				.findViewById(R.id.entry_label_shareholder);
		allUserChecked = chk.isChecked();
		if (view.getId() == R.id.entry_allshareholders) {
			if (allUserChecked) {
				shareholder.setText("");
				// shareholder.setEnabled(false);
				// shareholder_button.setEnabled(false);
				// chkDisable.setEnabled(false);

				shareholderLabel.setVisibility(View.GONE);
				shareholder.setVisibility(View.GONE);
				shareholder_button.setVisibility(View.GONE);
				chkDisable.setVisibility(View.GONE);
			} else {
				// shareholder.setEnabled(true);
				// shareholder_button.setEnabled(true);
				// chkDisable.setEnabled(true);
				shareholderLabel.setVisibility(View.VISIBLE);
				shareholder.setVisibility(View.VISIBLE);
				shareholder_button.setVisibility(View.VISIBLE);
				chkDisable.setVisibility(View.VISIBLE);
			}
		}
	}

	/* Check box listener starts here */
	public void onPersonalClicked(View view) {
		CheckBox chk = (CheckBox) DataEntryView
				.findViewById(R.id.entry_personal);
		CheckBox chkDisable = (CheckBox) DataEntryView
				.findViewById(R.id.entry_allshareholders);
		EditText shareholder = (EditText) DataEntryView
				.findViewById(R.id.entry_shareholder);
		ImageButton shareholder_button = (ImageButton) DataEntryView
				.findViewById(R.id.entry_shareholder_button);
		EditText paidBy = (EditText) DataEntryView
				.findViewById(R.id.entry_paidby);
		ImageButton paidBy_button = (ImageButton) DataEntryView
				.findViewById(R.id.entry_paidby_button);
		TextView shareholderLabel = (TextView) DataEntryView
				.findViewById(R.id.entry_label_shareholder);
		TextView paidByLabel = (TextView) DataEntryView
				.findViewById(R.id.entry_label_paidby);
		personalChecked = chk.isChecked();
		if (view.getId() == R.id.entry_personal) {
			if (personalChecked) {
				shareholder.setText("");
				// shareholder.setEnabled(false);
				// shareholder_button.setEnabled(false);
				shareholderLabel.setVisibility(View.GONE);
				shareholder.setVisibility(View.GONE);
				shareholder_button.setVisibility(View.GONE);
				paidBy.setText("");
				// paidBy.setEnabled(false);
				// paidBy_button.setEnabled(false);
				// chkDisable.setEnabled(false);
				paidByLabel.setVisibility(View.GONE);
				paidBy.setVisibility(View.GONE);
				paidBy_button.setVisibility(View.GONE);
				chkDisable.setVisibility(View.GONE);
			} else {
				// shareholder.setEnabled(true);
				// shareholder_button.setEnabled(true);
				// paidBy.setEnabled(true);
				// paidBy_button.setEnabled(true);
				// chkDisable.setEnabled(true);
				shareholderLabel.setVisibility(View.VISIBLE);
				shareholder.setVisibility(View.VISIBLE);
				shareholder_button.setVisibility(View.VISIBLE);
				paidByLabel.setVisibility(View.VISIBLE);
				paidBy.setVisibility(View.VISIBLE);
				paidBy_button.setVisibility(View.VISIBLE);
				chkDisable.setVisibility(View.VISIBLE);
			}
		}
	}

	/* Check box listener ends here */
	/* Populating date into date picker starts */
	public void selectDate(View view) {
		DialogFragment newFragment = new SelectDateFragment();
		newFragment.show(getSherlockActivity().getSupportFragmentManager(),
				"DatePicker");
	}

	public void populateSetDate(int year, int month, int day) {
		dateEditText = (EditText) DataEntryView.findViewById(R.id.entry_date);
		dateEditText.setText(String.format("%02d", day) + "/"
				+ String.format("%02d", month) + "/" + year);
		AutoCompleteTextView acTextView = (AutoCompleteTextView) DataEntryView
				.findViewById(R.id.item);
		acTextView.requestFocus();

	}

	public class SelectDateFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm + 1, dd);
		}
	}

	/* Populating date into date picker end here */

	/* Multiple selection User list starts here */
	public void selectShareholder(View view) {
		shareholderEditText = (EditText) DataEntryView
				.findViewById(R.id.entry_shareholder);
		shareholderHiddenText = (TextView) DataEntryView
				.findViewById(R.id.hidden_value);
		DialogFragment newFragment = new MultiSelectDialog(shareholderEditText,
				shareholderHiddenText, userList, userIdList,
				R.string.shareholders);
		newFragment.show(getSherlockActivity().getSupportFragmentManager(),
				"ShareHolders");
	}

	/* Single selection User list starts here */
	public void selectPaidBy(View view) {
		paidByEditText = (EditText) DataEntryView
				.findViewById(R.id.entry_paidby);
		paidByHiddenText = (TextView) DataEntryView
				.findViewById(R.id.hidden_paid_value);
		DialogFragment newFragment = new SingleSelectDialog(paidByEditText,
				paidByHiddenText, userList, userIdList, R.string.paidby);
		newFragment.show(getSherlockActivity().getSupportFragmentManager(),
				"PaidBy");
	}

	/* show Confirmation starts here */
	public void showConfirmation(View view) {
		EditText date = (EditText) DataEntryView.findViewById(R.id.entry_date);
		EditText item = (EditText) DataEntryView.findViewById(R.id.item);
		EditText desc = (EditText) DataEntryView.findViewById(R.id.entry_desc);
		EditText amount = (EditText) DataEntryView
				.findViewById(R.id.entry_amount);
		paidByEditText = (EditText) DataEntryView
				.findViewById(R.id.entry_paidby);
		shareholderEditText = (EditText) DataEntryView
				.findViewById(R.id.entry_shareholder);
		paidByHiddenText = (TextView) DataEntryView
				.findViewById(R.id.hidden_paid_value);
		shareholderHiddenText = (TextView) DataEntryView
				.findViewById(R.id.hidden_value);
		int shareholderCount = 0;
		String shareHoldersName = "";
		String shareHoldersId = "";
		String PaidByName = paidByEditText.getText().toString();
		String PaidById = paidByHiddenText.getText().toString();
		if (allUserChecked) {
			shareholderCount = userIdList.length;
			for (int i = 0; i < userIdList.length; i++) {
				shareHoldersName = (Utils.isNullorEmpty(shareHoldersName)) ? (String) userList[i]
						: shareHoldersName + "," + (String) userList[i];
				shareHoldersId = (Utils.isNullorEmpty(shareHoldersId)) ? (String) userIdList[i]
						: shareHoldersId + "," + (String) userIdList[i];
			}
		} else if (personalChecked) {
			shareholderCount = 1;
			shareHoldersName = SharedResources.getLoginUserName(getActivity());
			shareHoldersId = SharedResources.getLoginUserId(getActivity());
			PaidByName = shareHoldersName;
			PaidById = shareHoldersId;
		} else {
			shareHoldersName = shareholderEditText.getText().toString();
			shareHoldersId = shareholderHiddenText.getText().toString();
			shareholderCount = shareHoldersName.split("[,]").length;
		}
		if (validateDetails(date, item, desc, amount, shareholderEditText,
				paidByEditText)) {
			DialogFragment newFragment = new ConfimationDialog(date, item,
					desc, shareholderCount, shareHoldersName, shareHoldersId,
					amount, PaidByName, PaidById, R.string.confirm);
			newFragment.show(getSherlockActivity().getSupportFragmentManager(),
					"Confirmation");
		}
	}

	public boolean validateDetails(EditText date, EditText item, EditText desc,
			EditText amount, EditText shareholder, EditText paidBy) {
		boolean result = true;
		date.setError(null);
		item.setError(null);
		desc.setError(null);
		amount.setError(null);
		shareholder.setError(null);
		paidBy.setError(null);
		if (date.getText().toString().trim().length() == 0) {
			date.requestFocus();
			date.setError("Please select a date!");
			result = false;
		} else if (Utils.greaterThanToday(date.getText().toString(),
				"dd/MM/yyyy")) {
			date.requestFocus();
			date.setError("Future Date cannot be selected!");
			result = false;
		} else if (item.getText().toString().trim().length() == 0) {
			item.requestFocus();
			item.setError("Please select a Item!");
			result = false;
		} else if (desc.getText().toString().trim().length() == 0) {
			desc.requestFocus();
			desc.setError("Please enter Description!");
			result = false;
		} else if (amount.getText().toString().trim().length() == 0
				|| Double.parseDouble(amount.getText().toString()) <= 0) {
			amount.requestFocus();
			amount.setError("Please enter a valid amount!");
			result = false;
		} else if (!allUserChecked && !personalChecked
				&& shareholder.getText().toString().trim().length() == 0) {
			shareholder.requestFocus();
			shareholder.setError("Please select a Shareholder!");
			result = false;
		} else if (!personalChecked
				&& paidBy.getText().toString().trim().length() == 0) {
			paidBy.requestFocus();
			paidBy.setError("Please select a Payee!");
			result = false;
		}
		return result;
	}

	private JSONObject processItemData(String userId, String groupId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("param1", userId));
		nameValuePairs.add(new BasicNameValuePair("param2", groupId));
		return JSONParser.getJSONFromUrl(WebServiceConstants.USERS_ITEMS,
				nameValuePairs);
	}

	private class LoadItemTask extends AsyncTask<String, Object, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... param) {
			// TODO Auto-generated method stub
			return processItemData(param[0], param[1]);

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
			getActivity().findViewById(R.id.content).setVisibility(View.GONE);
			getActivity().findViewById(R.id.loadingPanel).setVisibility(
					View.VISIBLE);
			SharedResources.startLoading(getActivity());
		}

		protected void onPostExecute(JSONObject mainJsonData) {
			if (!ActivityOn)
				return;
			Log.i("Async-Example", "onPostExecute Called");
			if (mainJsonData != null) {
				/* Utils.showToastMessage(getActivity(), "Ready to enter data"); */
				if (mainJsonData.has("success")) {
					try {
						JSONObject dataObj = new JSONObject(
								mainJsonData.getString("msg"));
						JSONArray userArray = new JSONArray(
								dataObj.getString("users"));
						JSONArray userIdArray = new JSONArray(
								dataObj.getString("usersId"));
						JSONArray itemArray = new JSONArray(
								dataObj.getString("items"));
						if (userArray.toString().equals("[]")
								|| userArray.isNull(0)) {
							EditText shareholder = (EditText) DataEntryView
									.findViewById(R.id.entry_shareholder);
							ImageButton shareholder_button = (ImageButton) DataEntryView
									.findViewById(R.id.entry_shareholder_button);
							shareholder.setText("");
							shareholder_button.setEnabled(false);
						} else {
							userList = Utils
									.JSONArrayToCharSequenceArray(userArray);
							userIdList = Utils
									.JSONArrayToCharSequenceArray(userIdArray);
						}
						if (itemArray.toString().equals("[]")
								|| itemArray.isNull(0)) {
							AutoCompleteTextView acTextView = (AutoCompleteTextView) DataEntryView
									.findViewById(R.id.item);
							acTextView.setEnabled(false);
						} else
							items = Utils.JSONArrayToStringArray(itemArray);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// Array of integers points to images stored in
					// /res/drawable-ldpi/
					int flag = R.drawable.success;

					// Each row in the list stores country name, currency and
					// flag
					List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

					for (int i = 0; i < items.length; i++) {
						HashMap<String, String> hm = new HashMap<String, String>();
						hm.put("display", items[i]);
						hm.put("flag", Integer.toString(flag));
						aList.add(hm);
					}
					// Keys used in Hashmap
					String[] from = { "flag", "display" };

					// Ids of views in listview_layout
					int[] to = { R.id.flag, R.id.txt };

					// Instantiating an adapter to store each items
					// R.layout.listview_layout defines the layout of each item
					SimpleAdapter adapter = new SimpleAdapter(getActivity(),
							aList, R.layout.autocomplete_layout, from, to);

					// Getting a reference to CustomAutoCompleteTextView of
					// activity_main.xml layout file
					CustomAutoCompleteTextView autoComplete = (CustomAutoCompleteTextView) DataEntryView
							.findViewById(R.id.item);

					OnItemClickListener itemClickListener = new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long id) {

							/**
							 * Each item in the adapter is a HashMap object. So
							 * this statement creates the currently clicked
							 * hashmap object
							 * */
							HashMap<String, String> hm = (HashMap<String, String>) arg0
									.getAdapter().getItem(position);

							/**
							 * Getting a reference to the TextView of the layout
							 * file activity_main to set Currency
							 */
							AutoCompleteTextView acTextView = (AutoCompleteTextView) DataEntryView
									.findViewById(R.id.item);
							acTextView.setThreshold(2);

							/**
							 * Getting currency from the HashMap and setting it
							 * to the textview
							 */
							acTextView.setText(hm.get("display"));
							EditText desc = (EditText) DataEntryView
									.findViewById(R.id.entry_desc);
							desc.requestFocus();
						}
					};

					/** Setting the itemclick event listener */
					autoComplete.setOnItemClickListener(itemClickListener);

					/** Setting the adapter to the listView */
					autoComplete.setAdapter(adapter);
					AutoCompleteTextView acTextView = (AutoCompleteTextView) DataEntryView
							.findViewById(R.id.item);
					acTextView.setThreshold(2);
				} else {
					Utils.startNewActivity(getActivity(),
							TryAgainActivity.class);
					getActivity().finish();

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

	public class ConfimationDialog extends DialogFragment {

		EditText date;
		EditText itemType;
		EditText desc;
		int shareholdersCount;
		String shareholders;
		String shareholdersId;
		EditText amount;
		String paidBy;
		String paidById;
		int dialogTitle;

		public ConfimationDialog(EditText d, EditText i, EditText de, int sc,
				String s, String sid, EditText a, String p, String pid,
				int title) {
			this.date = d;
			this.itemType = i;
			this.desc = de;
			this.shareholdersCount = sc;
			this.shareholders = s;
			this.shareholdersId = sid;
			this.amount = a;
			this.paidBy = p;
			this.paidById = pid;
			this.dialogTitle = title;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			// Get the layout inflater
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final View myView = inflater.inflate(R.layout.confirmation, null);
			builder.setTitle(dialogTitle);
			TextView conf_date = (TextView) myView.findViewById(R.id.conf_date);
			TextView conf_item = (TextView) myView.findViewById(R.id.conf_item);
			TextView conf_description = (TextView) myView
					.findViewById(R.id.conf_description);
			TextView per_head = (TextView) myView
					.findViewById(R.id.conf_per_head);
			TextView conf_shareholder = (TextView) myView
					.findViewById(R.id.conf_shareholder);
			TextView conf_amount = (TextView) myView
					.findViewById(R.id.conf_amount);
			TextView conf_paid = (TextView) myView.findViewById(R.id.conf_paid);
			double costPerHead = 0;
			costPerHead = Double.parseDouble(amount.getText().toString())
					/ shareholdersCount;
			conf_date.setText(date.getText().toString());
			conf_item.setText(itemType.getText().toString());
			conf_description.setText(desc.getText().toString());
			per_head.setText("Rs." + Utils.getRoundedValue(costPerHead));
			conf_amount.setText("Rs."
					+ Utils.getRoundedValue(Double.parseDouble(amount.getText()
							.toString())));
			conf_paid.setText(paidBy);
			conf_shareholder.setText(Integer.toString(shareholdersCount));

			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			builder.setView(myView)
					// Add action buttons
					.setPositiveButton(R.string.proceed,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									new DataEntryTask().execute(
											date.getText().toString(),
											itemType.getText().toString(),
											desc.getText().toString(),
											amount.getText().toString(),
											shareholdersId,
											Integer.toString(shareholdersCount),
											paidById);
									ConfimationDialog.this.getDialog().cancel();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									ConfimationDialog.this.getDialog().cancel();
								}
							});
			return builder.create();
		}
	}

	private JSONObject processDataEntry(String date, String item, String desc,
			String amount, String shareholders, String shareholderCount,
			String paidBy) {
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("param1", date));
		nameValuePairs.add(new BasicNameValuePair("param2", item));
		nameValuePairs.add(new BasicNameValuePair("param3", desc));
		nameValuePairs.add(new BasicNameValuePair("param4", amount));
		nameValuePairs.add(new BasicNameValuePair("param5", shareholders));
		nameValuePairs.add(new BasicNameValuePair("param6", shareholderCount));
		nameValuePairs.add(new BasicNameValuePair("param7", paidBy));
		return JSONParser.getJSONFromUrl(WebServiceConstants.ENTER_DATA,
				nameValuePairs);
	}

	public class DataEntryTask extends AsyncTask<String, Object, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... param) {
			// TODO Auto-generated method stub
			return processDataEntry(param[0], param[1], param[2], param[3],
					param[4], param[5], param[6]);

		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			/*
			 * simpleWaitDialog = ProgressDialog.show(LoginActivity.this,
			 * "Wait", "Logging...", true);
			 */
			waitDialog = new ProgressDialog(getActivity());
			waitDialog.setMessage("Entering\n Data ...");
			waitDialog.setCancelable(true);
			waitDialog.show();
			SharedResources.startLoading(getActivity());
		}

		protected void onPostExecute(JSONObject mainJson) {
			if (!ActivityOn)
				return;
			Log.i("Async-Example", "onPostExecute Called");
			try {
				if (mainJson != null) {
					if (mainJson.getBoolean("success")) {
						alert.showAlertDialog(getActivity(), "Success",
								mainJson.getString("msg"), true);
						// createNotification();
					} else {
						alert.showAlertDialog(getActivity(), "Entry failed..",
								mainJson.getString("msg"), false);

					}
				} else {
					Utils.startNewActivity(getActivity(),
							TryAgainActivity.class);
					getActivity().finish();

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (null != waitDialog && waitDialog.isShowing()) {
				waitDialog.dismiss();
			}
			SharedResources.endLoading(getActivity());
		}
	}

	public void createNotification() {
		int noti_id = new Random().nextInt(10000);
		Intent resultIntent = new Intent(getActivity(),
				NotificationReceiverActivity.class);
		EditText date = (EditText) DataEntryView.findViewById(R.id.entry_date);
		EditText item = (EditText) DataEntryView.findViewById(R.id.item);
		EditText desc = (EditText) DataEntryView.findViewById(R.id.entry_desc);
		EditText amount = (EditText) DataEntryView
				.findViewById(R.id.entry_amount);
		paidByEditText = (EditText) DataEntryView
				.findViewById(R.id.entry_paidby);
		shareholderEditText = (EditText) DataEntryView
				.findViewById(R.id.entry_shareholder);
		int shareholderCount = 0;
		double costPerHead = 0;
		String shareHolders = "";
		String PaidByName = paidByEditText.getText().toString();
		if (allUserChecked) {
			shareholderCount = userList.length;
			for (CharSequence s : userList)
				shareHolders = (Utils.isNullorEmpty(shareHolders)) ? (String) s
						: shareHolders + "\n" + (String) s;
		} else if (personalChecked) {
			shareholderCount = 1;
			shareHolders = SharedResources.getLoginUserName(getActivity());
			PaidByName = shareHolders;
		} else {
			shareHolders = shareholderEditText.getText().toString();
			shareholderCount = shareHolders.split("[,]").length;
		}
		costPerHead = Double.parseDouble(amount.getText().toString())
				/ shareholderCount;
		resultIntent.putExtra("date", date.getText().toString());
		resultIntent.putExtra("item", item.getText().toString());
		resultIntent.putExtra("desc", desc.getText().toString());
		resultIntent.putExtra("amount", amount.getText().toString());
		resultIntent.putExtra("perHead", Utils.getRoundedValue(costPerHead));
		resultIntent.putExtra("shareHolders", shareHolders);
		resultIntent.putExtra("shareholderCount",
				Integer.toString(shareholderCount));
		resultIntent.putExtra("paidBy", PaidByName);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(
				getActivity(), 0, resultIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				getActivity())
				.setSmallIcon(R.drawable.icon_expense)
				.setContentTitle(item.getText().toString())
				.setContentText(
						"Expense-: Rs." + Utils.getRoundedValue(costPerHead)
								+ " (" + date.getText().toString() + ")")
				.setTicker("Ihalkhata Notification!")
				.setWhen(System.currentTimeMillis())
				.setDefaults(
						Notification.DEFAULT_SOUND
								| Notification.DEFAULT_VIBRATE)
				.setAutoCancel(true).setContentIntent(resultPendingIntent);

		NotificationManager manager = (NotificationManager) getActivity()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(noti_id, mBuilder.build());

	}

}
