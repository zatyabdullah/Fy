package com.fly.firefly.ui.fragment.BookingFlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.FlightDetailActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Picker.DatePickerFragment;
import com.fly.firefly.ui.activity.Register.RegisterActivity;
import com.fly.firefly.ui.module.SearchFlightModule;
import com.fly.firefly.ui.object.DatePickerObj;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
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

public class SearchFlightFragment extends BaseFragment implements BookingPresenter.SearchFlightView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.btnReturn) LinearLayout btnReturn;
    @InjectView(R.id.btnOneWay) LinearLayout btnOneWay;
    @InjectView(R.id.returnDateBlock) LinearLayout returnDateBlock;

    @InjectView(R.id.btnAdultIncrease) ImageView btnAdultIncrease;
    @InjectView(R.id.btnAdultDecrease) ImageView btnAdultDecrease;

    @InjectView(R.id.btnChildIncrease) ImageView btnChildIncrease;
    @InjectView(R.id.btnChildDecrease) ImageView btnChildDecrease;

    @InjectView(R.id.btnInfantIncrease) ImageView btnInfantIncrease;
    @InjectView(R.id.btnInfantDecrease) ImageView btnInfantDecrease;

    @InjectView(R.id.txtAdultTotal) TextView txtAdultTotal;
    @InjectView(R.id.txtChildTotal) TextView txtChildTotal;
    @InjectView(R.id.txtInfantTotal) TextView txtInfantTotal;

    @InjectView(R.id.btnSearchFlight) Button btnSearchFlight;

    @InjectView(R.id.btnDepartureFlight)
    LinearLayout btnDepartureFlight;

    @InjectView(R.id.btnArrivalFlight)
    LinearLayout btnArrivalFlight;

    @InjectView(R.id.txtArrivalFlight)
    TextView txtArrivalFlight;

    @InjectView(R.id.txtDepartureFlight)
    TextView txtDepartureFlight;

    @InjectView(R.id.departureBlock)
    LinearLayout departureBlock;

    @InjectView(R.id.bookFlightDepartureDate)
    TextView bookFlightDepartureDate;

    @InjectView(R.id.bookFlightReturnDate)
    TextView bookFlightReturnDate;

    private final String RETURN = "RETURN";
    private final String ONEWAY = "ONEWAY";
    private final String ADULT = "ADULT";
    private final String CHILDREN = "CHILDREN";
    private final String INFANT = "INFANT";
    private String flightType = "1";

    private int totalAdult = 1;
    private int totalChildren = 0;
    private int totalInfant = 0;

    private int fragmentContainerId;
    private boolean blockAdult = false;
    private boolean blockAdultNumber = false;

    private boolean blockChild = false;
    private boolean blockChildNumber = false;

    private boolean blockInfant = false;
    private boolean blockInfantNumber = false;
    private SharedPrefManager pref;
    private ArrayList<DropDownItem> dataFlightDeparture;
    private static ArrayList<DropDownItem> dataFlightArrival;
    private DatePickerObj date;
    private int a,b,c,d = 0;

    private String DEPARTURE_FLIGHT = "Please choose your departure airport";
    private String ARRIVAL_FLIGHT = "Please choose your arrival airport";
    private String DEPARTURE_FLIGHT_DATE = "Please choose your departure date.";
    private String ARRIVAL_FLIGHT_DATE = "Please choose your arrival date.";
    private String FLIGHT_OBJECT = "FLIGHT_OBJECT";

    public static SearchFlightFragment newInstance() {

        SearchFlightFragment fragment = new SearchFlightFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new SearchFlightModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.search_flight, container, false);
        ButterKnife.inject(this, view);

        pref = new SharedPrefManager(MainFragmentActivity.getContext());

        txtDepartureFlight.setTag(DEPARTURE_FLIGHT);
        txtArrivalFlight.setTag(ARRIVAL_FLIGHT);
        bookFlightDepartureDate.setTag(DEPARTURE_FLIGHT_DATE);
        bookFlightReturnDate.setTag(ARRIVAL_FLIGHT_DATE);

        /*Retrieve all - Display Flight Data*/
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
        btnDepartureFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(dataFlightDeparture, getActivity(), txtDepartureFlight);
                txtArrivalFlight.setText("ARRIVAL AIRPORT");
            }
        });

        /*Arrival Flight Clicked*/
        btnArrivalFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDepartureFlight.getTag().toString().equals("NOT SELECTED"))
                {
                    popupAlert("Select Departure Airport First");
                }else{
                    popupSelection(dataFlightArrival, getActivity(), txtArrivalFlight);
                }
            }
        });

        /*Arrival Date Clicked*/
        departureBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(view, "datepicker");
            }
        });

        /*Departure Date Clicked*/
        returnDateBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(view, "datepicker2");
            }
        });

        /*Initial*/
        btnOneWay.setBackgroundColor(getResources().getColor(R.color.grey));
        txtAdultTotal.setText("1");
        txtChildTotal.setText("0");

        //txtInfantTotal.setText("0");

        /*Return Button Clicked*/
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchWay(RETURN);
            }
        });

        /*One Way Button Clicked*/
        btnOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchWay(ONEWAY);
            }
        });

        /*Add & Remove ADULT Button */
        btnAdultIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!blockAdult && totalAdult <= 9){
                   totalAdult++;
                   setPassengerTotal(ADULT);
               }
            }
        });

        btnAdultDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalAdult == 1){
                    blockAdultNumber = true;
                }
                if(!blockAdultNumber){
                    totalAdult--;
                    setPassengerTotal(ADULT);
                }else if(blockAdultNumber && totalAdult > 9){
                    totalAdult--;
                    totalAdult--;
                    setPassengerTotal(ADULT);
                    Log.e("blockChildNumber", "True");
                    Log.e("totalChildren", Integer.toString(totalAdult));
                }
            }
        });

        /* END ADULT*/

        /*Add & Remove CHILDREN Button */
        btnChildIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!blockChild && totalChildren <= 9){
                    totalChildren++;
                    setPassengerTotal(CHILDREN);
                }
            }
        });

        btnChildDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(totalChildren == 0){
                    blockChildNumber = true;
                }

                if(!blockChildNumber){

                    totalChildren--;
                    setPassengerTotal(CHILDREN);

                }else if(blockChildNumber && totalChildren > 9){
                    totalChildren--;
                    totalChildren--;
                    setPassengerTotal(CHILDREN);

                }

            }
        });

        /* END - CHILDREN */

        /*Add & Remove INFANT Button */
        btnInfantIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!blockInfant && totalInfant <= 9){
                    totalInfant++;
                    setPassengerTotal(INFANT);
                }
            }
        });

        btnInfantDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalInfant == 0){
                    blockInfantNumber = true;
                }

                if(!blockInfantNumber){
                    totalInfant--;
                    setPassengerTotal(INFANT);
                }else if(blockInfantNumber && totalInfant > 9){
                    totalInfant--;
                    totalInfant--;
                    setPassengerTotal(INFANT);

                }
            }
        });

        /* END - INFANT */

        btnSearchFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String df = txtDepartureFlight.getTag().toString();
                String af = txtArrivalFlight.getTag().toString();
                String d1 = bookFlightDepartureDate.getTag().toString();
                String d2 = bookFlightReturnDate.getTag().toString();

                if(df.equals(DEPARTURE_FLIGHT)){

                    popupAlert(DEPARTURE_FLIGHT);

                }else if (af.equals(ARRIVAL_FLIGHT)){

                    popupAlert(ARRIVAL_FLIGHT);

                }else if (d1.equals(DEPARTURE_FLIGHT_DATE)){

                    popupAlert(DEPARTURE_FLIGHT_DATE);

                }else if (d2.equals(ARRIVAL_FLIGHT_DATE)){

                    popupAlert(ARRIVAL_FLIGHT_DATE);

                }else{
                    searchFlight();
                }


                // Define a constant in your class. Use a HashSet for performance
                /*Set<String> values = new HashSet<String>(Arrays.asList(df, af, d1,d2));

                if (!values.contains("NOT SELECTED")) {
                    searchFlight();
                }else
                {
                    popupAlert("Fill Empty Field");
                }*/



            }
        });



        return view;
    }

    public void searchFlight(){

        HashMap<String, String> init = pref.getSignatureFromLocalStorage();
        String signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);

        SearchFlightObj flightObj = new SearchFlightObj();
        flightObj.setAdult(txtAdultTotal.getText().toString());
        flightObj.setChildren(txtChildTotal.getText().toString());
        flightObj.setInfant(txtInfantTotal.getText().toString());
        flightObj.setType(flightType);
        flightObj.setDeparture_station(txtDepartureFlight.getTag().toString());
        flightObj.setDeparture_date(bookFlightDepartureDate.getTag().toString());
        flightObj.setArrival_station(txtArrivalFlight.getTag().toString());

                    /*Return Flight*/
        String returnDate = flightType.equals("1") ? bookFlightReturnDate.getTag().toString() : "";
        flightObj.setReturn_date(returnDate);

        flightObj.setSignature(signatureFromLocal);
        searchFlightFragment(flightObj);
        //goFlightDetailPage();
                    /*FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity_fragment_container, BF_FlightDetailFragment.newInstance(), "FLIGHT_DETAIL");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/

    }

    public void searchFlightFragment(SearchFlightObj flightObj){

        presenter.searchFlight(flightObj);
    }

    public void showTimePickerDialog(View v,String tag){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(SearchFlightFragment.this, 0);
        newFragment.show(getFragmentManager(), tag);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("enter here", "ok");
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            date = (DatePickerObj)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            String month = getMonthAlphabet(date.getMonth()-1);
            if (requestCode == 2) {
                bookFlightDepartureDate.setText(date.getDay()+" "+month+" "+date.getYear());
                bookFlightDepartureDate.setTag(date.getYear()+"-"+date.getMonth()+"-"+date.getDay());
            }else{
                bookFlightReturnDate.setText(date.getDay()+" "+month+" "+date.getYear());
                bookFlightReturnDate.setTag(date.getYear()+"-"+date.getMonth()+"-"+date.getDay());
            }
        }
    }

    /*Filter Arrival Airport*/
    public static void filterArrivalAirport(String code) {
        Log.e("Filter","TRUE");

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
        Log.e("Arrive",dataFlightArrival.toString());

    }


    /*Public-Inner Func*/
    public void goRegisterPage()
    {
        Intent loginPage = new Intent(getActivity(), RegisterActivity.class);
        getActivity().startActivity(loginPage);
    }

    public void goFlightDetailPage()
    {
        Intent flightDetail = new Intent(getActivity(), FlightDetailActivity.class);
        getActivity().startActivity(flightDetail);
    }

    //Switch Flight Type
    public void switchWay(String way)
    {
        if(way == RETURN) {
            returnDateBlock.setVisibility(View.VISIBLE);
            btnReturn.setBackgroundColor(getResources().getColor(R.color.white));
            btnOneWay.setBackgroundColor(getResources().getColor(R.color.grey));
            flightType = "1";
        }else {
            returnDateBlock.setVisibility(View.GONE);
            btnReturn.setBackgroundColor(getResources().getColor(R.color.grey));
            btnOneWay.setBackgroundColor(getResources().getColor(R.color.white));
            flightType = "0";

        }
    }

    public void setPassengerTotal(String passenger) {

        int totalPassenger = totalAdult+totalChildren+totalInfant;


        if(totalPassenger > 9){

            if(passenger == ADULT){ totalAdult--; }
            else if(passenger == CHILDREN){ totalChildren--; }
            else{ totalInfant--; }

            Utils.toastNotification(getActivity(), "9 Passenger Per Booking");

        }else{

            if (passenger == ADULT) {
                if(totalAdult < 10 && totalAdult > 0) {
                    txtAdultTotal.setText(Integer.toString(totalAdult));
                    blockAdult = false;
                    blockAdultNumber = false;
                }
                else if(totalAdult == 9) {
                    Utils.toastNotification(getActivity(), "Limit is 9");
                    blockAdult = true;
                }
                else
                {
                    blockAdultNumber = true;
                }

            }
            else if(passenger == CHILDREN)
            {
                if(totalChildren < 10 && totalChildren >= 0) {
                    Log.e("A",Integer.toString(totalChildren));
                    txtChildTotal.setText(Integer.toString(totalChildren));
                    blockChild = false;
                    blockChildNumber = false;
                }else if(totalChildren == 9) {
                    Utils.toastNotification(getActivity(), "Limit is 9");
                    Log.e("Reach Limit","children");
                    blockChild = true;
                }
                else
                {
                    blockChildNumber = true;
                }
            }
            else if(passenger == INFANT)
            {
                if(totalInfant < 10 && totalInfant >= 0) {
                    txtInfantTotal.setText(Integer.toString(totalInfant));
                    blockInfant = false;
                    blockInfantNumber = false;
                }
                else if(totalInfant == 9) {
                    Utils.toastNotification(getActivity(), "Limit is 9");
                    blockInfant = true;
                }
                else
                {
                    blockInfantNumber = true;
                }
            }
        }
    }

    @Override
    public void onBookingDataReceive(SearchFlightReceive obj) {

        Gson gson = new Gson();
        String countryList = gson.toJson(obj);

        if(obj.getJourneyObj().getStatus().equals("success")){

            SearchFlightReceive passObj = new SearchFlightReceive(obj);
            Intent flight = new Intent(getActivity(), FlightDetailActivity.class);
            flight.putExtra("FLIGHT_OBJ", (new Gson()).toJson(obj));
            getActivity().startActivity(flight);
            //getActivity().finish();
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
        Log.e("RESUME","TRUE");
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
