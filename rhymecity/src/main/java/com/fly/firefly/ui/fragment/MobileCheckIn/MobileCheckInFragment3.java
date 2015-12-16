package com.fly.firefly.ui.fragment.MobileCheckIn;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.FlightDetailActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.module.MobileCheckInModule3;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MobileCheckInFragment3 extends BaseFragment implements MobileCheckInPresenter.MobileCheckInView {

    @Inject
    MobileCheckInPresenter presenter;

    private int fragmentContainerId;

    public static MobileCheckInFragment3 newInstance() {

        MobileCheckInFragment3 fragment = new MobileCheckInFragment3();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new MobileCheckInModule3(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mobile_checkin_3, container, false);
        ButterKnife.inject(this, view);


        return view;
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
