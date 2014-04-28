package com.example.ihalkhata.Activityfragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.ihalkhata.R;
import com.example.ihalkhata.activity.TryAgainActivity;
import com.example.ihalkhata.utils.AlertDialogManager;
import com.example.ihalkhata.utils.ImageDownloader;
import com.example.ihalkhata.utils.JSONParser;
import com.example.ihalkhata.utils.SharedResources;
import com.example.ihalkhata.utils.Utils;
import com.example.ihalkhata.utils.WebServiceConstants;

public class HomeFragment extends SherlockFragment {

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog homeWaitDialog;
	// Button Logout
	ImageView img;
	Bitmap bmp;
	View HomeView;
	boolean ActivityOn = true;
	public static final String TAG = "Home";

	public static HomeFragment newInstance() {
		return new HomeFragment();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		HomeView = inflater.inflate(R.layout.fragment_home, container, false);
		String userId = SharedResources.getLoginUserId(getActivity());
		String groupId = SharedResources.getLoginUserGroupId(getActivity());
		new HomeTask().execute(userId, groupId);
		return HomeView;
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

	private JSONObject processHomeData(String username, String groupId) {

		String userid = SharedResources.getLoginUserId(getActivity());
		img = (ImageView) HomeView.findViewById(R.id.userImg);
		if (SharedResources.getBooleanImageStatus(getActivity()))
			bmp = SharedResources.getUserImage(getActivity());
		else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("param1", userid));
			bmp = ImageDownloader.getBitmapFromURL(WebServiceConstants.IMAGE,
					nameValuePairs);
			SharedResources.saveUserImage(getActivity(), true, bmp);
		}

		List<NameValuePair> nameValuePairsNew = new ArrayList<NameValuePair>(2);
		nameValuePairsNew.add(new BasicNameValuePair("param1", username));
		nameValuePairsNew.add(new BasicNameValuePair("param2", groupId));
		return JSONParser.getJSONFromUrl(WebServiceConstants.HOMEDATA,
				nameValuePairsNew);
	}

	private class HomeTask extends AsyncTask<String, Object, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... param) {
			// TODO Auto-generated method stub
			return processHomeData(param[0], param[1]);

		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			/*
			 * simpleWaitDialog = ProgressDialog.show(LoginActivity.this,
			 * "Wait", "Logging...", true);
			 */
			/*
			 * homeWaitDialog = new ProgressDialog(getActivity());
			 * homeWaitDialog.setMessage("Loading ...");
			 * homeWaitDialog.setCancelable(true); homeWaitDialog.show();
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
			TextView userNameLabel = (TextView) HomeView
					.findViewById(R.id.userName);
			TextView gainerLabel = (TextView) HomeView
					.findViewById(R.id.upValue);
			TextView loserLabel = (TextView) HomeView
					.findViewById(R.id.downValue);
			TextView debitLabel = (TextView) HomeView
					.findViewById(R.id.debitValue);
			TextView creditLabel = (TextView) HomeView
					.findViewById(R.id.creditValue);
			String userName = SharedResources.getLoginUserName(getActivity());
			img.setScaleType(ScaleType.CENTER_CROP);
			img.setImageBitmap(bmp);
			if (mainJsonData != null) {
				Utils.showToastMessage(getActivity(), "Welcome: " + userName);
				userNameLabel.setText(Html.fromHtml("<b>Welcome " + userName
						+ "</b>"));

				try {
					if (mainJsonData.has("success")) {
						JSONObject mainJson = new JSONObject(
								mainJsonData.getString("msg"));
						String gainerValue = (mainJson.has("gainer")) ? mainJson
								.getString("gainer") : "Not Available";
						String loserValue = (mainJson.has("loser")) ? mainJson
								.getString("loser") : "Not Available";
						String debitValue = (mainJson.has("debit")) ? mainJson
								.getString("debit") : "Not Available";
						String creditValue = (mainJson.has("credit")) ? mainJson
								.getString("credit") : "Not Available";
						gainerLabel.setText(gainerValue);
						loserLabel.setText(loserValue);
						debitLabel.setText(debitValue);
						creditLabel.setText(creditValue);
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
			 * if (null != homeWaitDialog && homeWaitDialog.isShowing()) {
			 * homeWaitDialog.dismiss(); }
			 */
			getActivity().findViewById(R.id.loadingPanel).setVisibility(
					View.GONE);
			getActivity().findViewById(R.id.content)
					.setVisibility(View.VISIBLE);
			SharedResources.endLoading(getActivity());
		}
	}

}
