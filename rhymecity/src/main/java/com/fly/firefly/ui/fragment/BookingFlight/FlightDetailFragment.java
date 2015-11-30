package com.fly.firefly.ui.fragment.BookingFlight;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.FlightInfo;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.PersonalDetailActivity;
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

    @InjectView(R.id.premierFlightDeparture)ExpandAbleGridView premierFlightDeparture;

    private int fragmentContainerId;
    private FlightDetailAdapter departListBasic,departListPremier,returnList;
    private final String BASIC = "BASIC";
    private final String PREMIER = "PREMIER";

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
        String[] output = departDate.split("-");
        String month = getMonthAlphabet(Integer.parseInt(output[1]));
        txtDepartureDate.setText(output[0] + " " + month + " " + output[2]);

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
            String[] returnDateOutput = returnDate.split("-");
            String returnMonth = getMonthAlphabet(Integer.parseInt(returnDateOutput[1]));
            txtReturnDate.setText(returnDateOutput[0]+" "+returnMonth+" "+returnDateOutput[2]);

            returnList = new FlightDetailAdapter(getActivity(),returnFlight,returnDepartPort,returnArrivalPort,BASIC);
            flightArrival.setAdapter(returnList);

        }


        btnListFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPersonalDetail();
            }
        });

        btnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFare(BASIC);
            }
        });

        btnPremier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFare(PREMIER);
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
    }

    //Switch Flight Type
    public void switchFare(String way)
    {
        if(way == BASIC) {
            premierFlightDeparture.setVisibility(View.GONE);
            flightDeparture.setVisibility(View.VISIBLE);
            btnBasic.setBackgroundColor(getResources().getColor(R.color.white));
            btnPremier.setBackgroundColor(getResources().getColor(R.color.grey));
            //flightClass = "1";
        }else {
            premierFlightDeparture.setVisibility(View.VISIBLE);
            flightDeparture.setVisibility(View.GONE);
            btnBasic.setBackgroundColor(getResources().getColor(R.color.grey));
            btnPremier.setBackgroundColor(getResources().getColor(R.color.white));

            //flightClass = "0";

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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
