package com.fly.firefly.ui.fragment.ManageFlight;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.presenter.LoginPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ManageFlight_Fragment extends Fragment {

    @Inject
    LoginPresenter presenter;

    //@InjectView(R.id.search_edit_text) EditText searchEditText;
    //@InjectView(R.id.registerBtn) Button registerButton;
    @InjectView(R.id.btnManageFlightContinue)
    Button btnManageFlightContinue;


    //private ProgressBar progressIndicator;
    private int fragmentContainerId;

    public static ManageFlight_Fragment newInstance() {

        ManageFlight_Fragment fragment = new ManageFlight_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FireFlyApplication.get(getActivity()).createScopedGraph(new ManageFlightModule(this)).inject(this);
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
        fragmentManager.beginTransaction().add(R.id.main_activity_fragment_container, ManageFlight_SelectActionFragment.newInstance()).addToBackStack(null).commit();
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
