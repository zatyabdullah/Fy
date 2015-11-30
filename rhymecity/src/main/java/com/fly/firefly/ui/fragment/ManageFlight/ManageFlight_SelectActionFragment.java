package com.fly.firefly.ui.fragment.ManageFlight;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.ManageFlight.ManageFlight_ChangeContact;
import com.fly.firefly.ui.activity.ManageFlight.ManageFlight_ChangeFlight;
import com.fly.firefly.ui.activity.ManageFlight.ManageFlight_EditPassenger;
import com.fly.firefly.ui.activity.ManageFlight.ManageFlight_SeatSelection;
import com.fly.firefly.ui.activity.ManageFlight.ManageFlight_SentItinerary;
import com.fly.firefly.ui.presenter.LoginPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ManageFlight_SelectActionFragment extends Fragment {

    @Inject
    LoginPresenter presenter;

    @InjectView(R.id.mfChangeContact)
    LinearLayout mfChangeContact;

    @InjectView(R.id.mfEditPassenger)
    LinearLayout mfEditPassenger;

    @InjectView(R.id.mfChangeSeat)
    LinearLayout mfChangeSeat;

    @InjectView(R.id.mfChangeFlight)
    LinearLayout mfChangeFlight;

    @InjectView(R.id.mfSendItinenary)
    LinearLayout mfSendItinenary;


    //private ProgressBar progressIndicator;
    private int fragmentContainerId;

    public static ManageFlight_SelectActionFragment newInstance() {

        ManageFlight_SelectActionFragment fragment = new ManageFlight_SelectActionFragment();
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

        View view = inflater.inflate(R.layout.manage_flight_select_action, container, false);
        ButterKnife.inject(this, view);

        mfChangeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangeContactPage();
            }
        });

        mfEditPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditPassenger();
            }
        });

        mfChangeSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSeatSelection();
            }
        });

        mfChangeFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangeFlight();
            }
        });

        mfSendItinenary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSentItenary();
            }
        });



        return view;
    }

    public void goToChangeContactPage()
    {
        Intent seatSelection = new Intent(getActivity(), ManageFlight_ChangeContact.class);
        getActivity().startActivity(seatSelection);
    }

    public void goToEditPassenger()
    {
        Intent seatSelection = new Intent(getActivity(), ManageFlight_EditPassenger.class);
        getActivity().startActivity(seatSelection);
    }

    public void goToSeatSelection()
    {
        Intent seatSelection = new Intent(getActivity(), ManageFlight_SeatSelection.class);
        getActivity().startActivity(seatSelection);
    }

    public void goToChangeFlight()
    {
        Intent seatSelection = new Intent(getActivity(), ManageFlight_ChangeFlight.class);
        getActivity().startActivity(seatSelection);
    }

    public void goToSentItenary()
    {
        Intent seatSelection = new Intent(getActivity(), ManageFlight_SentItinerary.class);
        getActivity().startActivity(seatSelection);
    }






    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
       // presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
       // presenter.onPause();
    }


}
