package com.fly.firefly.ui.fragment.MobileCheckIn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity2;
import com.fly.firefly.ui.module.MobileCheckInModule1;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MobileCheckInFragment1 extends BaseFragment implements MobileCheckInPresenter.MobileCheckInView {

    @Inject
    MobileCheckInPresenter presenter;

    @InjectView(R.id.mobileCheckInNext1) Button mobileCheckInNext1;
    @InjectView(R.id.editPnr) EditText editPnr;
    @InjectView(R.id.txtDeparture) TextView txtDeparture;
    @InjectView(R.id.txtArrive) TextView txtArrive;

    private ArrayList<DropDownItem> dataFlightDeparture;
    private static ArrayList<DropDownItem> dataFlightArrival;
    private SharedPrefManager pref;
    private String DEPARTURE_FLIGHT = "Please choose your departure airport";
    private String ARRIVAL_FLIGHT = "Please choose your arrival airport";
    private static final String SCREEN_LABEL = "Mobile Check In";

    //@InjectView(R.id.btnLogin) Button btnLogin;

    private int fragmentContainerId;

    public static MobileCheckInFragment1 newInstance() {

        MobileCheckInFragment1 fragment = new MobileCheckInFragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new MobileCheckInModule1(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mobile_checkin_1, container, false);
        ButterKnife.inject(this, view);

        /*Preference Manager*/
        pref = new SharedPrefManager(MainFragmentActivity.getContext());

        txtDeparture.setTag(DEPARTURE_FLIGHT);
        txtArrive.setTag(ARRIVAL_FLIGHT);

        /*Retrieve All Flight Data From Preference Manager - Display Flight Data*/
        JSONArray jsonFlight = getFlight(getActivity());
        dataFlightDeparture = new ArrayList<>();

        ArrayList<String> tempFlight = new ArrayList<>();

        /*Get All Airport - remove redundant*/
        List<String> al = new ArrayList<>();
        Set<String> hs = new LinkedHashSet<>();
        for (int i = 0; i < jsonFlight.length(); i++) {
            JSONObject row = (JSONObject) jsonFlight.opt(i);
            al.add(row.optString("location")+"-"+row.optString("location_code"));
        }
        hs.addAll(al);
        al.clear();
        al.addAll(hs);

        /*Display Airport*/
        for (int i = 0; i < al.size(); i++)
        {
            String flightSplit = al.get(i).toString();
            String[] str1 = flightSplit.split("-");
            String p1 = str1[0];
            String p2 = str1[1];

            DropDownItem itemFlight = new DropDownItem();
            itemFlight.setText(p1);
            itemFlight.setCode(p2);
            itemFlight.setTag("FLIGHT");
            dataFlightDeparture.add(itemFlight);

        }


       /*Departure Flight Clicked*/
        txtDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "txtDeparture");
                popupSelection(dataFlightDeparture, getActivity(), txtDeparture,true);
                //txtDeparture.setText("ARRIVAL AIRPORT");
            }
        });

         /*Arrival Flight Clicked*/
        txtArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnArrivalFlight");
                if(txtDeparture.getTag().toString().equals("NOT SELECTED"))
                {
                    popupAlert("Select Departure Airport First");
                }else{
                    popupSelection(dataFlightArrival, getActivity(), txtArrive,true);
                }
            }
        });

        mobileCheckInNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkinFlight();
                //goCheckin2Page();
            }
        });
        return view;
    }



    public void checkinFlight(){

        initiateLoading(getActivity());

        HashMap<String, String> init = pref.getSignatureFromLocalStorage();
        String signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);

        MobileCheckinObj flightObj = new MobileCheckinObj();
        flightObj.setPnr(editPnr.getText().toString());
        flightObj.setDeparture_station(txtDeparture.getTag().toString());
        flightObj.setArrival_station(txtArrive.getTag().toString());
        flightObj.setSignature(signatureFromLocal);
        searchFlightFragment(flightObj);
                    /*FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity_fragment_container, BF_FlightDetailFragment.newInstance(), "FLIGHT_DETAIL");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/

    }

    public void searchFlightFragment(MobileCheckinObj flightObj){

        presenter.checkinFlight(flightObj);
    }


    /*Filter Arrival Airport*/
    public static void filterArrivalAirport(String code) {
        Log.e("Filter", "TRUE");

        JSONArray jsonFlight = getFlight(MainFragmentActivity.getContext());
        dataFlightArrival = new ArrayList<>();

            /*Display Arrival*/
        for (int i = 0; i < jsonFlight.length(); i++)
        {
            JSONObject row = (JSONObject) jsonFlight.opt(i);
            if(code.equals(row.optString("location_code")) && row.optString("status").equals("Y")) {
                Log.e(code,row.optString("location_code"));

                DropDownItem itemFlight = new DropDownItem();
                itemFlight.setText(row.optString("travel_location"));
                itemFlight.setCode(row.optString("travel_location_code" +
                        ""));
                itemFlight.setTag("FLIGHT_DEPARTURE");
                dataFlightArrival.add(itemFlight);

            }
        }
        Log.e("Arrive", dataFlightArrival.toString());

    }

    /*Public-Inner Func*/
    public void goCheckin2Page()
    {
        Intent next = new Intent(getActivity(), MobileCheckInActivity2.class);
        getActivity().startActivity(next);
    }


    @Override
    public void onCheckindataReceive(MobileCheckinReceive obj) {

        dismissLoading();

        Gson gson = new Gson();
        String countryList = gson.toJson(obj);


        if(obj.getJourneyObj().getStatus().equals("success")){

            MobileCheckinReceive passObj = new MobileCheckinReceive(obj);
            Intent flight = new Intent(getActivity(), MobileCheckInActivity2.class);
            flight.putExtra("FLIGHT_OBJ", (new Gson()).toJson(obj));

            Gson gsonCheckinInfo = new Gson();
            String checkinInfo = gsonCheckinInfo.toJson(obj.getCheckin_info());
            pref.setUserInfo(checkinInfo);
         //   Log.e("getCheckin_info", obj.getCheckin_info().toString());

            getActivity().startActivity(flight);



        }else if(obj.getStatus().equals("error_validation")){
            croutonAlert(getActivity(), obj.getMessage());
        }else if(obj.getStatus().equals("error")){
            croutonAlert(getActivity(), obj.getMessage());
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        Log.e("RESUME", "TRUE");
        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
