package com.fly.firefly.ui.fragment.ManageFlight;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.presenter.LoginPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ManageFlight_Fragment extends BaseFragment {

    @InjectView(R.id.btnManageFlightContinue)
    Button btnManageFlightContinue;

    private int fragmentContainerId;

    public static ManageFlight_Fragment newInstance() {

        ManageFlight_Fragment fragment = new ManageFlight_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_flight, container, false);
        ButterKnife.inject(this, view);

       btnManageFlightContinue.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               goBookingPage();
           }
       });


        return view;
    }

    public void goBookingPage()
    {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, ManageFlight_SelectActionFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
