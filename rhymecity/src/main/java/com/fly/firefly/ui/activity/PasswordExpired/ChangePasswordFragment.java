package com.fly.firefly.ui.activity.PasswordExpired;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.module.ChangePasswordModule;
import com.fly.firefly.ui.presenter.ChangePasswordPresenter;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChangePasswordFragment extends BaseFragment implements ChangePasswordPresenter.ChangePasswordView {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;
    @Inject
    ChangePasswordPresenter presenter;

    //@InjectView(R.id.search_edit_text) EditText searchEditText;
    @InjectView(R.id.editTextemail) EditText editTextemail;
    @NotEmpty @InjectView(R.id.editTextforgotPasswordCurrent) EditText editTextforgotPasswordCurrent;
    @Password @InjectView(R.id.editTextforgotPasswordConfirm) EditText editTextforgotPasswordConfirm;
    @InjectView(R.id.editTextforgotPasswordNew) EditText editTextforgotPasswordNew;


    private AlertDialog dialog;
    private SharedPrefManager pref;

    private int fragmentContainerId;

    public static ChangePasswordFragment newInstance() {

        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ChangePasswordModule(this)).inject(this);
        // Validator
       /* mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_password_expired, container, false);
        ButterKnife.inject(this, view);

        pref = new SharedPrefManager(getActivity());

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]


        return view;
    }

    /* Validation Success - Start send data to server */
    /*@Override
    public void onValidationSucceeded() {
        // Toast.makeText(getActivity(), "Login Success!", Toast.LENGTH_SHORT).show();
      //  loginFromFragment(txtLoginEmail.getText().toString(), txtLoginPassword.getText().toString());

    }

    *//* Validation Failed - Toast Error *//*
    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();

            *//* Split Error Message. Display first sequence only *//*
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);
            } else {
                Toast.makeText(getActivity(), splitErrorMsg[0], Toast.LENGTH_LONG).show();
            }
        }
    }*/




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        Log.i("Page Name", "Setting screen name: " + "Login Page");
        mTracker.setScreenName("Login" + "B");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
