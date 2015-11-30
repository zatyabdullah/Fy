package com.fly.firefly.ui.presenter;

import android.util.Log;

import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.PasswordRequest;
import com.squareup.otto.Bus;

public class ChangePasswordPresenter {

    public interface ChangePasswordView {

        //void onPasswordRequesFailed(String dumm);

    }

    private final ChangePasswordView view;
    private final Bus bus;

    public ChangePasswordPresenter(ChangePasswordView view, Bus bus) {
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



   // @Subscribe
    //public void onUserFailedReqPassword(FailedConnectToServer event) {
//
        //*Save Session And Redirect To Homepage*//*
  //      view.onPasswordRequesFailed(event.getDummy());
   // }

}
