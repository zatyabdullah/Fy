package com.fly.firefly.ui.presenter;

import android.util.Log;

import com.fly.firefly.api.obj.FailedConnectToServer;
import com.fly.firefly.api.obj.ForgotPasswordReceive;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.PasswordRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class LoginPresenter {

    public interface LoginView {

        void onLoginSuccess(LoginReceive obj);
        void onLoginFailed(String dumm);

        void onPasswordRequestSuccess(ForgotPasswordReceive obj);
        //void onPasswordRequesFailed(String dumm);


    }

    private final LoginView view;
    private final Bus bus;

    public LoginPresenter(LoginView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void loginFunction(LoginRequest data) {
        Log.e("xxxx",data.getUsername());
        bus.post(new LoginRequest(data));
    }


    public void forgotPassword(PasswordRequest data) {
        Log.e("xxxx",data.getEmail());
        bus.post(new PasswordRequest(data));
    }

    @Subscribe
    public void onUserSuccessLogin(LoginReceive event) {

        /*Save Session And Redirect To Homepage*/
        view.onLoginSuccess(event.getUserObj());
    }



    @Subscribe
    public void onUserSuccessLogin(FailedConnectToServer event) {

        /*Save Session And Redirect To Homepage*/
        view.onLoginFailed(event.getDummy());
    }


    @Subscribe
    public void onUserSuccessReqPassword(ForgotPasswordReceive event) {

        //*Save Session And Redirect To Homepage*//*
        view.onPasswordRequestSuccess(event.getUserObj());
    }

   // @Subscribe
    //public void onUserFailedReqPassword(FailedConnectToServer event) {
//
        //*Save Session And Redirect To Homepage*//*
  //      view.onPasswordRequesFailed(event.getDummy());
   // }

}
