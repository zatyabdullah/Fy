package com.fly.firefly.ui.activity.BookingFlight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.FlightInfo;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SelectFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.adapter.FlightDetailAdapter;
import com.fly.firefly.ui.module.FlightDetailModule;
import com.fly.firefly.ui.module.SelectFlightModule;
import com.fly.firefly.ui.object.SelectFlight;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.ExpandAbleGridView;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FlightDetailFragment extends BaseFragment implements BookingPresenter.ListFlightView {

    @Inject BookingPresenter presenter;
    @InjectView(R.id.btnListFlight)Button btnListFlight;
    @InjectView(R.id.flightDeparture)ExpandAbleGridView flightDeparture;
    @InjectView(R.id.flightArrival)ExpandAbleGridView flightArrival;
    @InjectView(R.id.returnFlightBlock)LinearLayout returnFlightBlock;
    @InjectView(R.id.returnBasicPremier)LinearLayout returnBasicPremier;

    @InjectView(R.id.txtDepartAirport)TextView txtDepartAirport;
    @InjectView(R.id.txtFlightType)TextView txtFlightType;
    @InjectView(R.id.txtDepartureDate)TextView txtDepartureDate;
    @InjectView(R.id.txtReturnType)TextView txtReturnType;
    @InjectView(R.id.txtReturnAirport)TextView txtReturnAirport;
    @InjectView(R.id.txtReturnDate)TextView txtReturnDate;

    @InjectView(R.id.btnBasic)LinearLayout btnBasic;
    @InjectView(R.id.btnPremier)LinearLayout btnPremier;
    @InjectView(R.id.btnBasicReturn)LinearLayout btnBasicReturn;
    @InjectView(R.id.btnPremierReturn)LinearLayout btnPremierReturn;

    @InjectView(R.id.premierFlightDeparture)ExpandAbleGridView premierFlightDeparture;
    @InjectView(R.id.premierFlightArrival)ExpandAbleGridView premierFlightArrival;

    private int fragmentContainerId;
    private FlightDetailAdapter departListBasic,departListPremier, returnListBasic,returnListPremier;
    private String departPort,departDatePlain,arrivalPort,departPortCode,flightType;
    private String returnDepartPort,returnArrivalPort,returnDatePlain,arrivalPortCode;
    private String adult,infant;

    private String departClass,returnClass;

    private final String DEPART_BASIC = "DEPART_BASIC";
    private final String DEPART_PREMIER = "DEPART_PREMIER";
    private final String RETURN_PREMIER = "RETURN_PREMIER";
    private final String RETURN_BASIC = "RETURN_BASIC";
    private final String BASIC = "BASIC";
    private final String PREMIER = "PREMIER";
    private static final String SCREEN_LABEL = "Flight Detail";
    private final String FLIGHT_TYPE = "FLIGHT_TYPE";
    private final String ADULT = "ADULT";
    private final String INFANT = "INFANT";
    private final String DEPARTURE_DATE = "DEPARTURE_DATE";
    private final String RETURN_DATE = "RETURN_DATE";
    private final String RETURN = "RETURN";
    private final String DEPART = "DEPART";
    private Boolean proceed = false;
    private SharedPrefManager pref;

    private String departFlightNumber,departFlightDepartureTime,departFlightArrivalTime,departFlightJourneyKey,
            departFlightFareSellKey;
    private String returnFlightNumber,returnFlightDepartureTime,returnFlightArrivalTime,returnFlightJourneyKey,
            returnFlightFareSellKey;


    public static FlightDetailFragment newInstance(Bundle bundle) {

        FlightDetailFragment fragment = new FlightDetailFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new SelectFlightModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flight_detail, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        flightType = bundle.getString(FLIGHT_TYPE);
        adult = bundle.getString(ADULT);
        infant = bundle.getString(INFANT);
        departDatePlain = bundle.getString(DEPARTURE_DATE);
        returnDatePlain = bundle.getString(RETURN_DATE);
        pref = new SharedPrefManager(getActivity());



        btnPremier.setBackgroundColor(getResources().getColor(R.color.grey));
        btnPremierReturn.setBackgroundColor(getResources().getColor(R.color.grey));

        String dataFlight2 = bundle.getString("FLIGHT_OBJ");
        Gson gson = new Gson();
        SearchFlightReceive obj = gson.fromJson(dataFlight2, SearchFlightReceive.class);

        /*Departure*/
        List<FlightInfo> departFlight = obj.getJourneyObj().getJourneys().get(0).getFlights();

        //Depart Airport
        departPort = obj.getJourneyObj().getJourneys().get(0).getDeparture_station_name();
        arrivalPort = obj.getJourneyObj().getJourneys().get(0).getArrival_station_name();

        departPortCode = obj.getJourneyObj().getJourneys().get(0).getDeparture_station_code();
        arrivalPortCode = obj.getJourneyObj().getJourneys().get(0).getArrival_station_code();

        String type = obj.getJourneyObj().getJourneys().get(0).getType();
        txtDepartAirport.setText(departPort+" - "+arrivalPort);
        txtFlightType.setText(type);

        //Reformat Date
        String departDate = obj.getJourneyObj().getJourneys().get(0).getDeparture_date();
        //String[] output = departDate.split(" ");
        //String month = output[1];
        txtDepartureDate.setText(departDate);

        /*Basic*/
        departListBasic = new FlightDetailAdapter(getActivity(),departFlight,departPort,arrivalPort,BASIC,DEPART,this);
        flightDeparture.setAdapter(departListBasic);

        /*Premier*/
        departListPremier = new FlightDetailAdapter(getActivity(),departFlight,departPort,arrivalPort,PREMIER,DEPART,this);
        premierFlightDeparture.setAdapter(departListPremier);

        /*Return If Available*/
        if(obj.getJourneyObj().getJourneys().size() > 1){
          List<FlightInfo> returnFlight = obj.getJourneyObj().getJourneys().get(1).getFlights();
          returnFlightBlock.setVisibility(View.VISIBLE);
          returnBasicPremier.setVisibility(View.VISIBLE);


          //Return Airport
          returnDepartPort = obj.getJourneyObj().getJourneys().get(1).getDeparture_station_name();
          returnArrivalPort = obj.getJourneyObj().getJourneys().get(1).getArrival_station_name();
          String returnType = obj.getJourneyObj().getJourneys().get(1).getType();
          txtReturnAirport.setText(returnDepartPort + " - " + returnArrivalPort);
          txtReturnType.setText(returnType);

            //Reformat Date
            String returnDate = obj.getJourneyObj().getJourneys().get(1).getDeparture_date();
            txtReturnDate.setText(returnDate);

            returnListBasic = new FlightDetailAdapter(getActivity(),returnFlight,returnDepartPort,returnArrivalPort,BASIC,RETURN,this);
            flightArrival.setAdapter(returnListBasic);

            returnListPremier = new FlightDetailAdapter(getActivity(),returnFlight,returnDepartPort,returnArrivalPort,PREMIER,RETURN,this);
            premierFlightArrival.setAdapter(returnListPremier);

        }

        btnListFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "Select Flight");
                //check if flight checked
                if(flightType.equals("0")){
                    if(departFlightNumber == null){
                        Utils.toastNotification(getActivity(),"Please Check Departure Flight");
                    }else{
                        proceed = true;
                    }
                }else{
                    if(departFlightNumber == null){
                        Utils.toastNotification(getActivity(),"Please Check Departure Flight");
                    }else if(returnFlightNumber == null){
                        Utils.toastNotification(getActivity(),"Please Check Return Flight");
                    }else{
                        proceed = true;
                    }
                }
                if(proceed){
                  goPersonalDetail();
                }
            }
        });

        btnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnBasic");
                switchFare(DEPART_BASIC);
            }
        });

        btnPremier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnPremier");
                switchFare(DEPART_PREMIER);
            }
        });

        btnBasicReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnBasicReturn");
                switchFare(RETURN_BASIC);
            }
        });

        btnPremierReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnPremierReturn");
                switchFare(RETURN_PREMIER);
            }
        });


        return view;
    }

    /*Inner Func*/
    public void goPersonalDetail()
    {

        initiateLoading(getActivity());

        SelectFlight selectFlightObj = new SelectFlight();
        selectFlightObj.setType(flightType);

        selectFlightObj.setDeparture_station(departPortCode);
        selectFlightObj.setArrival_station(arrivalPortCode);
        selectFlightObj.setDeparture_date(departDatePlain);
        selectFlightObj.setReturn_date(returnDatePlain);
        selectFlightObj.setAdult(adult);
        selectFlightObj.setInfant(infant);
        selectFlightObj.setUsername(""); //Get Username from Pref manager

        selectFlightObj.setFlight_number_1(departFlightNumber);
        selectFlightObj.setDeparture_time_1(departFlightDepartureTime);
        selectFlightObj.setArrival_time_1(departFlightArrivalTime);
        selectFlightObj.setJourney_sell_key_1(departFlightJourneyKey);
        selectFlightObj.setFare_sell_key_1(departFlightFareSellKey);

        selectFlightObj.setFlight_number_2(returnFlightNumber);
        selectFlightObj.setDeparture_time_2(returnFlightDepartureTime);
        selectFlightObj.setArrival_time_2(returnFlightArrivalTime);
        selectFlightObj.setJourney_sell_key_2(returnFlightJourneyKey);
        selectFlightObj.setFare_sell_key_2(returnFlightFareSellKey);

        presenter.selectFlight(selectFlightObj);

        AnalyticsApplication.sendEvent("StartActivity", "Passenger Information");
    }

    @Override
    public void onSeletFlightReceive(SelectFlightReceive obj) {

        dismissLoading();
        pref.setBookingID(obj.getFlightObj().getBookingId());
        Intent passengerInfo = new Intent(getActivity(), PersonalDetailActivity.class);
        passengerInfo.putExtra(ADULT, adult );
        passengerInfo.putExtra(INFANT, infant );
        getActivity().startActivity(passengerInfo);

    }

    //Switch Flight Type
    public void switchFare(String way)
    {
        if(way == DEPART_BASIC) {

            premierFlightDeparture.setVisibility(View.GONE);
            flightDeparture.setVisibility(View.VISIBLE);
            btnBasic.setBackgroundColor(getResources().getColor(R.color.white));
            btnPremier.setBackgroundColor(getResources().getColor(R.color.grey));
            departListBasic.invalidateSelected();
            departFlightNumber = null;

        }else if (way == DEPART_PREMIER){

            premierFlightDeparture.setVisibility(View.VISIBLE);
            flightDeparture.setVisibility(View.GONE);
            btnBasic.setBackgroundColor(getResources().getColor(R.color.grey));
            btnPremier.setBackgroundColor(getResources().getColor(R.color.white));
            departListPremier.invalidateSelected();
            departFlightNumber = null;

        }else if (way == RETURN_BASIC){

            premierFlightArrival.setVisibility(View.GONE);
            flightArrival.setVisibility(View.VISIBLE);
            btnPremierReturn.setBackgroundColor(getResources().getColor(R.color.grey));
            btnBasicReturn.setBackgroundColor(getResources().getColor(R.color.white));
            returnListBasic.invalidateSelected();
            returnFlightNumber = null;

        }else if (way == RETURN_PREMIER)
        {
            premierFlightArrival.setVisibility(View.VISIBLE);
            flightArrival.setVisibility(View.GONE);
            btnBasicReturn.setBackgroundColor(getResources().getColor(R.color.grey));
            btnPremierReturn.setBackgroundColor(getResources().getColor(R.color.white));
            returnListPremier.invalidateSelected();
            returnFlightNumber = null;

        }
    }


    public void selectedInfo(FlightInfo obj,String type,String way) {

        if (way.equals(DEPART)) {

            departFlightNumber = obj.getFlight_number();
            departFlightDepartureTime = obj.getDeparture_time();
            departFlightArrivalTime = obj.getArrival_time();
            departFlightJourneyKey = obj.getJourney_sell_key();
            if (type.equals(BASIC)) {
                departFlightFareSellKey = obj.getBasicObj().getFare_sell_key();
            } else {
                departFlightFareSellKey = obj.getFlexObj().getFare_sell_key();
            }
        } else if (way.equals(RETURN)) {

            returnFlightNumber = obj.getFlight_number();
            returnFlightDepartureTime = obj.getDeparture_time();
            returnFlightArrivalTime = obj.getArrival_time();
            returnFlightJourneyKey = obj.getJourney_sell_key();

            if (type.equals(BASIC)) {
                returnFlightFareSellKey = obj.getBasicObj().getFare_sell_key();
            } else {
                returnFlightFareSellKey = obj.getFlexObj().getFare_sell_key();
            }
        }
    }

        public void alertNotAvailable(){
            Utils.toastNotification(getActivity(),"Not Available");
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
        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
