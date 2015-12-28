package com.fly.firefly.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.ui.activity.BookingFlight.SearchFlightFragment;
import com.fly.firefly.ui.fragment.MobileCheckIn.MobileCheckInFragment1;
import com.fly.firefly.ui.object.Country;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.DropMenuAdapter;
import com.fly.firefly.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import dmax.dialog.SpotsDialog;


public class BaseFragment extends Fragment {

	public com.fly.firefly.base.AQuery aq;
	private SharedPreferences pref;
	private int indexForState = -1;
	private String selected;
	private static SharedPrefManager prefManager;
	private static Country obj = new Country();
	private static SpotsDialog mProgressDialog;


	public void croutonAlert(Activity act,String msg){
		Crouton.makeText(act, msg, Style.ALERT)
				.setConfiguration(new Configuration.Builder()
				.setDuration(Configuration.DURATION_LONG).build())
				.show();
	}

	public String getMonthInInteger(String monthAlphabet){
		Log.e("MONTH",monthAlphabet);
		int intMonthNo = 0;
		String stringMonthNo = null;
		/*Month*/
		final String[] month = getResources().getStringArray(R.array.month);
		for(int i = 0;i<month.length; i++)
		{
			if(monthAlphabet.equals(month[i])){
				intMonthNo = i+1;
			}
		}

		if(intMonthNo < 10){
			stringMonthNo = "0"+Integer.toString(intMonthNo);
		}else{
			stringMonthNo = Integer.toString(intMonthNo);
		}
		return stringMonthNo;
	}

	/*public void initiateLoading(Activity act){

		ProgressDialog mProgressDialog;
		mProgressDialog = new ProgressDialog(act);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.show();
	}*/

	public static void initiateLoading(Activity act){


		mProgressDialog = new SpotsDialog(act,R.style.CustomDialog);
		//mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("Loading...");

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		//lp.copyFrom(mProgressDialog.getWindow().getAttributes());
		lp.width = 50;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mProgressDialog.getWindow().setAttributes(lp);

		mProgressDialog.show();


	}

	public static void dismissLoading(){
		if(mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}
	}

	public void popupAlert(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// FIRE ZE MISSILES!
					}
				});

		// Create the AlertDialog object and return it
		builder.create();
		builder.show();
	}

	/*Global PoPup*/
	public void popupSelection(final ArrayList array,Activity act,final TextView txt,final Boolean tagStatus){

			prefManager = new SharedPrefManager(act);

			Log.e("Popup Alert", "True");
			final ArrayList<DropDownItem> a = array;
			DropMenuAdapter dropState = new DropMenuAdapter(act);
			dropState.setItems(a);

				AlertDialog.Builder alertStateCode = new AlertDialog.Builder(act);

				alertStateCode.setSingleChoiceItems(dropState, indexForState, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						String selected = a.get(which).getText();
						String selectedCode = a.get(which).getCode();

						txt.setText(selected);
						if(tagStatus){
							txt.setTag(selectedCode);
						}

                        if(a.get(which).getTag() == "FLIGHT"){
                            SearchFlightFragment.filterArrivalAirport(selectedCode);
							MobileCheckInFragment1.filterArrivalAirport(selectedCode);
                        }

						indexForState = which;

						dialog.dismiss();
					}
				});

				//Utils.hideKeyboard(getActivity(), v);
				AlertDialog mDialog = alertStateCode.create();
				mDialog.show();

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				lp.copyFrom(mDialog.getWindow().getAttributes());
				lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
				lp.height = 600;
				mDialog.getWindow().setAttributes(lp);
	}

	/*Global PoPup*/
	public void popupSelectionExtra(final ArrayList array,Activity act,final TextView txt,final Boolean tagStatus,final LinearLayout txt2,final String indicate){

		prefManager = new SharedPrefManager(act);

		final ArrayList<DropDownItem> a = array;
		DropMenuAdapter dropState = new DropMenuAdapter(act);
		dropState.setItems(a);

		AlertDialog.Builder alertStateCode = new AlertDialog.Builder(act);

		alertStateCode.setSingleChoiceItems(dropState, indexForState, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				String selected = a.get(which).getText();
				String selectedCode = a.get(which).getCode();
				txt.setText(selected);
				if(!selected.equals(indicate)){
					txt2.setVisibility(View.VISIBLE);
				}else{
					txt2.setVisibility(View.GONE);
				}
				if(tagStatus){
					txt.setTag(selectedCode);
					Log.e("PURPOSE TAG",selectedCode);
				}else{
					Log.e("PURPOSE TAG","NOT SET");
				}

				indexForState = which;

				dialog.dismiss();
			}
		});

		//Utils.hideKeyboard(getActivity(), v);
		AlertDialog mDialog = alertStateCode.create();
		mDialog.show();

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(mDialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height = 600;
		mDialog.getWindow().setAttributes(lp);
	}

	public String getSelectedPopupSelection(Activity act){
		HashMap<String, String> init = prefManager.getSelectedPopupSelection();
		String selectedValue = init.get(SharedPrefManager.SELECTED);
		return selectedValue;
	}


	public JSONArray getTitle(Activity act){

		JSONArray json = null;

		prefManager = new SharedPrefManager(act);
		HashMap<String, String> init = prefManager.getTitle();
		String dataTitle = init.get(SharedPrefManager.TITLE);

		try {
			json = new JSONArray(dataTitle);
			Log.e("json",Integer.toString(json.length()));
		}catch (JSONException e){
			e.printStackTrace();
		}

		return json;
	}

	public String getTitleCode(Activity act,String title){

		String titleCode = null;
		JSONArray json = null;

		prefManager = new SharedPrefManager(act);
		HashMap<String, String> init = prefManager.getTitle();
		String dataTitle = init.get(SharedPrefManager.TITLE);

		try {
			json = new JSONArray(dataTitle);
		}catch (JSONException e){
			e.printStackTrace();
		}

		for (int i = 0; i < json.length(); i++)
		{
			JSONObject row = (JSONObject) json.opt(i);

			if(title.equals(row.optString("title_name"))){
				titleCode = row.optString("title_code");
			}
		}

		return titleCode;
	}

	public String getTravelDocCode(Activity act,String travelDocData){
		/*Travel Doc*/
		String travelDocCode = null;
		final String[] doc = getResources().getStringArray(R.array.travel_doc);
		for(int i = 0;i<doc.length; i++)
		{
			String travelDoc = doc[i];
			String[] splitDoc = travelDoc.split("-");

			if(travelDocData.equals(splitDoc[0])){
				travelDocCode = splitDoc[1];
			}
		}
		Log.e("travelDocData",travelDocData);
		Log.e("travelDocCode",travelDocCode);
		return travelDocCode;
	}

	public String getCountryCode(Activity act,String countryData){

		String countryCode = null;
		JSONArray json = null;

		prefManager = new SharedPrefManager(act);
		HashMap<String, String> init = prefManager.getCountry();
		String dataCountry = init.get(SharedPrefManager.COUNTRY);

		try {
			json = new JSONArray(dataCountry);
		}catch (JSONException e){
			e.printStackTrace();
		}

		for (int i = 0; i < json.length(); i++)
		{
			JSONObject row = (JSONObject) json.opt(i);

			if(countryData.equals(row.optString("country_name"))){
				countryCode = row.optString("country_code");
			}
		}

		return countryCode;

	}

	public JSONArray getState(Activity act){

		JSONArray json = null;

		prefManager = new SharedPrefManager(act);
		HashMap<String, String> init = prefManager.getState();
		String dataState = init.get(SharedPrefManager.STATE);

		try {
			json = new JSONArray(dataState);
		}catch (JSONException e){
			e.printStackTrace();
		}

		return json;
	}

	public static JSONArray getFlight(Activity act){

		JSONArray json = null;

		prefManager = new SharedPrefManager(act);
		HashMap<String, String> init = prefManager.getFlight();
		String dataFlight = init.get(SharedPrefManager.FLIGHT);

		try {
			json = new JSONArray(dataFlight);
			Log.e("Flight Size",Integer.toString(json.length()));

		}catch (JSONException e){
			e.printStackTrace();
		}

		return json;
	}


	/*Return month in alphabet*/
	public String getMonthAlphabet(int month) {
		return new DateFormatSymbols().getShortMonths()[month];
	}

	/*Get All Country From OS*/
	public JSONArray getCountry(Activity act)
	{
		JSONArray json = null;

		prefManager = new SharedPrefManager(act);
		HashMap<String, String> init = prefManager.getCountry();
		String dataCountry = init.get(SharedPrefManager.COUNTRY);
		Log.e("dataCountry",dataCountry);

		try {
			json = new JSONArray(dataCountry);
			Log.e("json",Integer.toString(json.length()));
		}catch (JSONException e){
			e.printStackTrace();
		}

		return json;

		/*Locale[] locales = Locale.getAvailableLocales();
		ArrayList<String> countries = new ArrayList<String>();

		for (Locale locale : locales) {
			String country = locale.getDisplayCountry();
			String countryCode = locale.getCountry();


			if (country.trim().length()>0 && !countries.contains(country)) {
				countries.add(country+"-"+countryCode);
			}
		}

		Collections.sort(countries);
		return countries;*/
	}



	/*Get All User Info From OS*/
	public JSONObject getUserInfo(Activity act)
	{
		JSONObject json = null;

		prefManager = new SharedPrefManager(act);
		HashMap<String, String> init = prefManager.getUserInfo();
		String userInfo = init.get(SharedPrefManager.USER_INFO);
		Log.e("user_info", userInfo);

		try {
			json = new JSONObject(userInfo);
			Log.e("json",Integer.toString(json.length()));
		}catch (JSONException e){
			e.printStackTrace();
		}

		return json;

	}




	/*Get All User Info From OS*/
	public JSONObject getCheckinInfo(Activity act)
	{
		JSONObject json = null;

		prefManager = new SharedPrefManager(act);
		HashMap<String, String> init = prefManager.getCheckinInfo();
		String checkinInfo = init.get(SharedPrefManager.CHECKIN_INFO);


		try {
			json = new JSONObject(checkinInfo);

		}catch (JSONException e){
			e.printStackTrace();
		}

		return json;

	}


	/*Get All Country From OS*/
	public JSONArray getTerm(Activity act) {
		JSONArray json = null;

		prefManager = new SharedPrefManager(act);
		HashMap<String, String> init = prefManager.getTermInfo();
		String dataTerm = init.get(SharedPrefManager.TERM_INFO);
		Log.e("dataTerm", dataTerm);

		try {
			json = new JSONArray(dataTerm);
			Log.e("json", Integer.toString(json.length()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}
	/*public static void showConnectionError(String test, Activity activity)
	{
        if(activity != null) {
            try {
                TextView txtUTC = (TextView) activity.findViewById(R.id.txtUTC);
                txtUTC.setText(test);

                FrameLayout mainFrame = (FrameLayout) activity.findViewById(R.id.utc_container);
                mainFrame.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	public static void baseInitiateLoading(Activity activity)
	{
        Log.e("Initiate Loading","TRUE");
		try
		{
			final FrameLayout mainFrame = (FrameLayout) activity.findViewById(R.id.container);
			mainFrame.setVisibility(View.VISIBLE);

			RelativeLayout mainFrameRelative = (RelativeLayout) activity.findViewById(R.id.mainFrameRelative);
			mainFrameRelative.setVisibility(View.VISIBLE);
			mainFrame.bringChildToFront(mainFrameRelative);
			mainFrame.invalidate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void baseRemoveLoading(Activity activity)
	{
		try
		{
			RelativeLayout mainFrameRelative = (RelativeLayout) activity.findViewById(R.id.mainFrameRelative);
			mainFrameRelative.setVisibility(View.GONE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		aq = new com.fly.firefly.base.AQuery(getActivity());
		pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}

	/*public void showUTCError(String msg)
	{
		Activity activity = getActivity();
		if (activity instanceof MainFragmentActivity)
		{
			MainFragmentActivity myactivity = (MainFragmentActivity) activity;
			myactivity.unableToConnectToServer(msg);
		}
	}*/

	public boolean isNetworkAvailable(Activity activity)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
