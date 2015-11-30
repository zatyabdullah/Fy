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
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Register.RegisterActivity;
import com.fly.firefly.ui.module.PaymentFlightModule;
import com.fly.firefly.ui.module.PersonalDetailModule;
import com.fly.firefly.ui.module.SeatSelectionModule;
import com.fly.firefly.ui.presenter.BF_PaymentFlightPresenter;
import com.fly.firefly.ui.presenter.BF_PersonalDetailPresenter;
import com.fly.firefly.ui.presenter.SeatSelectionPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PaymentFlightFragment extends Fragment implements BF_PaymentFlightPresenter.PaymentFlightView {

    @Inject
    BF_PaymentFlightPresenter presenter;

    //@InjectView(R.id.btnPayment) Button btnPayment ;

    private int fragmentContainerId;


    public static PaymentFlightFragment newInstance() {

        PaymentFlightFragment fragment = new PaymentFlightFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new PaymentFlightModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.payment_flight, container, false);
        ButterKnife.inject(this, view);

        return view;
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
