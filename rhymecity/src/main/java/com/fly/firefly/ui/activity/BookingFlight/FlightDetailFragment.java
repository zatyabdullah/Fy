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
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.adapter.FlightDetailAdapter;
import com.fly.firefly.ui.module.FlightDetailModule;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.ExpandAbleGridView;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FlightDetailFragment extends BaseFragment implements BookingPresenter.SearchFlightView {

    @Inject BookingPresenter presenter;
    @InjectView(R.id.btnListFlight)Button btnListFlight;
    @InjectView(R.id.flightDeparture)ExpandAbleGridView flightDeparture;
    @InjectView(R.id.flightArrival)ExpandAbleGridView flightArrival;
    @InjectView(R.id.returnFlightBlock)LinearLayout returnFlightBlock;
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

    private final String DEPART_BASIC = "DEPART_BASIC";
    private final String DEPART_PREMIER = "DEPART_PREMIER";
    private final String RETURN_PREMIER = "RETURN_PREMIER";
    private final String RETURN_BASIC = "RETURN_BASIC";
    private final String BASIC = "BASIC";
    private final String PREMIER = "PREMIER";
    private static final String SCREEN_LABEL = "Flight Detail";

    public static FlightDetailFragment newInstance(Bundle bundle) {

        FlightDetailFragment fragment = new FlightDetailFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new FlightDetailModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flight_detail, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        btnPremier.setBackgroundColor(getResources().getColor(R.color.grey));
        btnPremierReturn.setBackgroundColor(getResources().getColor(R.color.grey));

        String dataFlight2 = bundle.getString("FLIGHT_OBJ");
        Gson gson = new Gson();
        SearchFlightReceive obj = gson.fromJson(dataFlight2, SearchFlightReceive.class);

        /*Departure*/
        List<FlightInfo> departFlight = obj.getJourneyObj().getJourneys().get(0).getFlights();

        //Depart Airport
        String departPort = obj.getJourneyObj().getJourneys().get(0).getDeparture_station_name();
        String arrivalPort = obj.getJourneyObj().getJourneys().get(0).getArrival_station_name();
        String type = obj.getJourneyObj().getJourneys().get(0).getType();
        txtDepartAirport.setText(departPort+" - "+arrivalPort);
        txtFlightType.setText(type);

        //Reformat Date
        String departDate = obj.getJourneyObj().getJourneys().get(0).getDeparture_date();
        //String[] output = departDate.split(" ");
        //String month = output[1];
        txtDepartureDate.setText(departDate);

        /*Basic*/
        departListBasic = new FlightDetailAdapter(getActivity(),departFlight,departPort,arrivalPort,BASIC);
        flightDeparture.setAdapter(departListBasic);

        /*Premier*/
        departListPremier = new FlightDetailAdapter(getActivity(),departFlight,departPort,arrivalPort,PREMIER);
        premierFlightDeparture.setAdapter(departListPremier);

        /*Return If Available*/
        if(obj.getJourneyObj().getJourneys().size() > 1){
          List<FlightInfo> returnFlight = obj.getJourneyObj().getJourneys().get(1).getFlights();
          returnFlightBlock.setVisibility(View.VISIBLE);

          //Return Airport
          String returnDepartPort = obj.getJourneyObj().getJourneys().get(1).getDeparture_station_name();
          String returnArrivalPort = obj.getJourneyObj().getJourneys().get(1).getArrival_station_name();
          String returnType = obj.getJourneyObj().getJourneys().get(1).getType();
          txtReturnAirport.setText(returnDepartPort + " - " + returnArrivalPort);
          txtReturnType.setText(returnType);

            //Reformat Date
            String returnDate = obj.getJourneyObj().getJourneys().get(1).getDeparture_date();
            txtReturnDate.setText(returnDate);

            returnListBasic = new FlightDetailAdapter(getActivity(),returnFlight,returnDepartPort,returnArrivalPort,BASIC);
            flightArrival.setAdapter(returnListBasic);

            returnListPremier = new FlightDetailAdapter(getActivity(),returnFlight,returnDepartPort,returnArrivalPort,PREMIER);
            premierFlightArrival.setAdapter(returnListPremier);

        }


        btnListFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnListFlight");
                goPersonalDetail();
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

    @Override
    public void onBookingDataReceive(SearchFlightReceive obj) {

       // if(obj.getJourneyObj().getStatus().equals("success")){
            SearchFlightReceive passObj = new SearchFlightReceive(obj);
       // }
    }

    /*Inner Func*/
    public void goPersonalDetail()
    {
        Intent loginPage = new Intent(getActivity(), PersonalDetailActivity.class);
        getActivity().startActivity(loginPage);
        AnalyticsApplication.sendEvent("StartActivity", "personal detail");
    }

    //Switch Flight Type
    public void switchFare(String way)
    {
        if(way == DEPART_BASIC) {
            premierFlightDeparture.setVisibility(View.GONE);
            flightDeparture.setVisibility(View.VISIBLE);
            btnBasic.setBackgroundColor(getResources().getColor(R.color.white));
            btnPremier.setBackgroundColor(getResources().getColor(R.color.grey));
            //flightClass = "1";
        }else if (way == DEPART_PREMIER){
            premierFlightDeparture.setVisibility(View.VISIBLE);
            flightDeparture.setVisibility(View.GONE);
            btnBasic.setBackgroundColor(getResources().getColor(R.color.grey));
            btnPremier.setBackgroundColor(getResources().getColor(R.color.white));
           //flightClass = "0";
        }else if (way == RETURN_BASIC){
            premierFlightArrival.setVisibility(View.GONE);
            flightArrival.setVisibility(View.VISIBLE);
            btnPremierReturn.setBackgroundColor(getResources().getColor(R.color.grey));
            btnBasicReturn.setBackgroundColor(getResources().getColor(R.color.white));
        }else if (way == RETURN_PREMIER)
        {
            premierFlightArrival.setVisibility(View.VISIBLE);
            flightArrival.setVisibility(View.GONE);
            btnBasicReturn.setBackgroundColor(getResources().getColor(R.color.grey));
            btnPremierReturn.setBackgroundColor(getResources().getColor(R.color.white));
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
        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
