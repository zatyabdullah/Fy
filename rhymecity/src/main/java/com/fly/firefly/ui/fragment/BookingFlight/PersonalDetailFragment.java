package com.fly.firefly.ui.fragment.BookingFlight;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.BookingFlight.SeatSelectionActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Register.RegisterActivity;
import com.fly.firefly.ui.module.PersonalDetailModule;
import com.fly.firefly.ui.presenter.BF_PersonalDetailPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PersonalDetailFragment extends Fragment implements BF_PersonalDetailPresenter.PersonalDetailView {

    @Inject
    BF_PersonalDetailPresenter presenter;

   @InjectView(R.id.btnSeatSelection) Button btnSeatSelection;

    private int fragmentContainerId;


    public static PersonalDetailFragment newInstance() {

        PersonalDetailFragment fragment = new PersonalDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new PersonalDetailModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.personal_detail, container, false);
        ButterKnife.inject(this, view);

        btnSeatSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goSeatSelectionPage();

            }
        });


        return view;
    }

    public void goSeatSelectionPage()
    {
        Intent seatSelection = new Intent(getActivity(), SeatSelectionActivity.class);
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
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
