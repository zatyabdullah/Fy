package com.fly.firefly.ui.fragment.MobileCheckIn;

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
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity3;
import com.fly.firefly.ui.module.MobileCheckInModule2;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MobileCheckInFragment2 extends Fragment implements MobileCheckInPresenter.MobileCheckInView {

    @Inject
    MobileCheckInPresenter presenter;

    @InjectView(R.id.mobileCheckInNext2) Button mobileCheckInNext2;
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
