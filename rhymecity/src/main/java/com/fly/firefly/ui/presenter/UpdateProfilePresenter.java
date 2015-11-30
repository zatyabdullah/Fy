package com.fly.firefly.ui.presenter;

import android.util.Log;

import com.fly.firefly.ui.object.UpdateProfileRequest;
import com.squareup.otto.Bus;

public class UpdateProfilePresenter {

    public interface UpdateProfileView {

        //void onPasswordRequesFailed(String dumm);

    }

    private final UpdateProfileView view;
    private final Bus bus;

    public UpdateProfilePresenter(UpdateProfileView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void updateProfile(UpdateProfileRequest data) {
        Log.e("xxxx",data.getUsername());
       // Log.e("xxxx",data.getPassword());
        Log.e("xxxx",data.getNewPassword());
        Log.e("xxxx",data.getCountry());
        Log.e("xxxx",data.getState());
        Log.e("xxxx",data.getUsername());
        Log.e("xxxx",data.getPassword());
        Log.e("xxxx",data.getFirst_name());
        Log.e("xxxx",data.getDob());
        Log.e("xxxx",data.getLast_name());
        Log.e("xxxx",data.getAddress_1());
        Log.e("xxxx",data.getAddress_2());
        Log.e("xxxx",data.getAddress_3());
        Log.e("xxxx",data.getTitle());
        Log.e("xxxx",data.getCity());
        //registerAddressLine2 = data.getRegisterAddressLine2();
        Log.e("xxxx",data.getPostcode());
        Log.e("xxxx",data.getMobile_phone());
        Log.e("xxxx",data.getAlternate_phone());
        Log.e("xxxx", data.getFax());
        bus.post(new UpdateProfileRequest(data));
    }



   // @Subscribe
    //public void onUserFailedReqPassword(FailedConnectToServer event) {
//
        //*Save Session And Redirect To Homepage*//*
  //      view.onPasswordRequesFailed(event.getDummy());
   // }

}
