package com.fly.firefly.ui.fragment.MobileCheckIn;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.FlightDetailActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity3;
import com.fly.firefly.ui.module.MobileCheckInModule2;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;
import com.google.gson.Gson;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MobileCheckInFragment2 extends BaseFragment implements MobileCheckInPresenter.MobileCheckInView {

    @Inject
    MobileCheckInPresenter presenter;

    @InjectView(R.id.mobileCheckInNext2) Button mobileCheckInNext2;
    @InjectView(R.id.flightdate) TextView flightdate;
    @InjectView(R.id.flightnumber)TextView flightnumber;
    @InjectView(R.id.stationcode)TextView stationcode;
    @InjectView(R.id.departuretime)TextView departuretime;
    @InjectView(R.id.passengerName)TextView passengerName;
    @InjectView(R.id.checkin_box)TextView checkin_box;

    //@InjectView(R.id.btnLogin) Button btnLogin;

    private int fragmentContainerId;

    public static MobileCheckInFragment2 newInstance() {

        MobileCheckInFragment2 fragment = new MobileCheckInFragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new MobileCheckInModule2(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mobile_checkin_2, container, false);
        ButterKnife.inject(this, view);
        
        
        
        /*Display Existing User Checkin Data*/
        JSONObject chekinInfo = getCheckinInfo(getActivity());

        String station_code = chekinInfo.optString("StationCode");
        String departure_station = chekinInfo.optString("DepartureStation");
        String arrival_station = chekinInfo.optString("ArrivalStation");
        String departure_date = chekinInfo.optString("DepartureDate");
        String arrival_date = chekinInfo.optString("ArrivalDate");
        String carrier_code = chekinInfo.optString("CarrierCode");
        String flight_code = chekinInfo.optString("FlightCode");


        stationcode.setText(station_code);
        flightdate.setText(departure_date);
        flightnumber.setText(flight_code);
        departuretime.setText(departure_date);
       // passengerName.setText();


        mobileCheckInNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        return view;
    }

    /*Public-Inner Func*/
    public void next()
    {
        Intent next = new Intent(getActivity(), MobileCheckInActivity3.class);
        getActivity().startActivity(next);
    }


    @Override
    public void onCheckindataReceive(MobileCheckinReceive obj) {

        dismissLoading();

        Gson gson = new Gson();
        String countryList = gson.toJson(obj);

        if(obj.getJourneyObj().getStatus().equals("success")){

            MobileCheckinReceive passObj = new MobileCheckinReceive(obj);
            Intent flight = new Intent(getActivity(), FlightDetailActivity.class);
            flight.putExtra("FLIGHT_OBJ", (new Gson()).toJson(obj));
            getActivity().startActivity(flight);

        }else if(obj.getStatus().equals("error_validation")){
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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
