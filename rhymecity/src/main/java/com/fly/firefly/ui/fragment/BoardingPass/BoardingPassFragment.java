package com.fly.firefly.ui.fragment.BoardingPass;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.presenter.LoginPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class BoardingPassFragment extends Fragment {

    @Inject
    LoginPresenter presenter;

    //@InjectView(R.id.search_edit_text) EditText searchEditText;
    //@InjectView(R.id.registerBtn) Button registerButton;


    //private ProgressBar progressIndicator;
    private int fragmentContainerId;

    public static BoardingPassFragment newInstance() {

        BoardingPassFragment fragment = new BoardingPassFragment();
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

        View view = inflater.inflate(R.layout.boarding_pass, container, false);
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
        // presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // presenter.onPause();
    }


}
