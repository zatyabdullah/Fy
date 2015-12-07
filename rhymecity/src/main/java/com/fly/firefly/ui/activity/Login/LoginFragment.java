package com.fly.firefly.ui.activity.Login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ForgotPasswordReceive;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.SearchFlightActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.PasswordExpired.ChangePasswordActivity;
import com.fly.firefly.ui.activity.Register.RegisterActivity;
import com.fly.firefly.ui.module.LoginModule;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.PasswordRequest;
import com.fly.firefly.ui.presenter.LoginPresenter;
import com.fly.firefly.utils.AESCBC;
import com.fly.firefly.utils.App;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class LoginFragment extends BaseFragment implements LoginPresenter.LoginView,Validator.ValidationListener {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    @Inject
    LoginPresenter presenter;

    //@InjectView(R.id.search_edit_text) EditText searchEditText;

    @InjectView(R.id.edit_area) LinearLayout edit_area;
    @InjectView(R.id.registerBtn) Button registerButton;
    @InjectView(R.id.btnLogin) Button btnLogin;

    @InjectView(R.id.txtForgotPassword)
    TextView txtForgotPassword;

    @NotEmpty(sequence = 1)
    @Email(sequence = 2)
    @Order(1)
    @InjectView(R.id.txtLoginEmail) EditText txtLoginEmail;


    @NotEmpty(sequence = 1)
    //@Length(sequence = 2, min = 6, message = "Must at least 6 character")
    //@Password(sequence =3,scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS,message = "Must have uppercase char,number and symbols") // Password validator
    @Order(2)
    @InjectView(R.id.txtLoginPassword) EditText txtLoginPassword;

    private AlertDialog dialog;
    private SharedPrefManager pref;

    private int fragmentContainerId;

    public static LoginFragment newInstance() {

        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new LoginModule(this)).inject(this);
        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);
        ButterKnife.inject(this, view);

        pref = new SharedPrefManager(getActivity());

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegisterPage();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate form
                mValidator.validate();
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(edit_area);
                Utils.hideKeyboard(getActivity(), v);
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        return view;
    }

    public void loginFromFragment(String username,String password){

        /*Start Loading*/
        initiateLoading(getActivity());

        LoginRequest data = new LoginRequest();
        data.setUsername(username);
        data.setPassword(password);
        presenter.loginFunction(data);
    }


    /*Public-Inner Func*/
    public void goRegisterPage()
    {
        Intent loginPage = new Intent(getActivity(), RegisterActivity.class);
        getActivity().startActivity(loginPage);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Click register page")
                .build());
    }

    public void goBookingPage()
    {
        Intent loginPage = new Intent(getActivity(), SearchFlightActivity.class);
        getActivity().startActivity(loginPage);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Click booking page")
                .build());
        getActivity().finish();
    }


    public void goChangePasswordPage()
    {
        Intent loginPage = new Intent(getActivity(), ChangePasswordActivity.class);
        getActivity().startActivity(loginPage);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Click booking page")
                .build());
        getActivity().finish();
    }



    public void goChangePassword()
    {
        Intent loginPage = new Intent(getActivity(), ChangePasswordActivity.class);
        getActivity().startActivity(loginPage);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("passwordexpired")
                .build());
        getActivity().finish();
    }


    @Override
    public void onLoginSuccess(LoginReceive obj) {

        /*Dismiss Loading*/
        dismissLoading();

        if (obj.getStatus().equals("success")) {
            pref.setLoginStatus("Y");
            Log.e("X", obj.getUser_info().getFirst_name());
            pref.setUsername(obj.getUser_info().getFirst_name());

            Gson gsonUserInfo = new Gson();
            String userInfo = gsonUserInfo.toJson(obj.getUser_info());
            pref.setUserInfo(userInfo);

            goBookingPage();
        }
        else if (obj.getStatus().equals("change_password")) {
            pref.setLoginStatus("Y");
            goChangePasswordPage();
        }else{
            croutonAlert(getActivity(),obj.getMessage());
           /*Crouton.makeText(getActivity(), obj.getMessage(), Style.ALERT)
           .setConfiguration(new Configuration.Builder()
                   .setDuration(Configuration.DURATION_LONG).build())
                   .show();*/
        }

    }

    /*IF Login Failed*/
    @Override
    public void onLoginFailed(String obj) {
        Crouton.makeText(getActivity(), obj, Style.ALERT).show();


    }


    @Override
    public void onPasswordRequestSuccess(ForgotPasswordReceive obj) {

        if (obj.getStatus().equals("success")) {
            Crouton.makeText(getActivity(), obj.getMessage(), Style.CONFIRM).show();
        }else{
            croutonAlert(getActivity(),obj.getMessage());
        }

    }

    /* Validation Success - Start send data to server */
    @Override
    public void onValidationSucceeded() {
        loginFromFragment(txtLoginEmail.getText().toString(), AESCBC.encrypt(App.KEY, App.IV,txtLoginPassword.getText().toString()));
    }

    /* Validation Failed - Toast Error */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();

            /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
               ((EditText) view).setError(splitErrorMsg[0]);
            } else {
                croutonAlert(getActivity(), splitErrorMsg[0]);
              // Toast.makeText(getActivity(), splitErrorMsg[0], Toast.LENGTH_LONG).show();


            }
        }
    }

    /*Popup Forgot Password*/
    public void forgotPassword(){

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View myView = li.inflate(R.layout.forgot_password_screen, null);
        Button cont = (Button)myView.findViewById(R.id.btncontinue);

        final EditText editEmail = (EditText)myView.findViewById(R.id.editTextemail);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForgotPassword(editEmail.getText().toString(),"");
                dialog.dismiss();
            }

        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myView);

        dialog = builder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = 570;
        dialog.getWindow().setAttributes(lp);
        dialog.show();


    }


    public void requestForgotPassword(String username,String signature){
        PasswordRequest data = new PasswordRequest();
        data.setEmail(username);
        data.setSignature(signature);
        presenter.forgotPassword(data);
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
