package com.fly.firefly.ui.presenter;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.api.obj.DeviceInfoSuccess;
import com.fly.firefly.rhymes.RhymesRequestedEvent;
import com.fly.firefly.ui.object.DeviceInformation;
import com.fly.firefly.utils.SharedPrefManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class HomePresenter {

    private SharedPrefManager pref;

    public interface HomeView {

    }

    public interface SplashScreen {
        void loadingSuccess(DeviceInfoSuccess obj);
    }

    private HomeView view;
    private SplashScreen view2;

    private final Bus bus;

    public HomePresenter(HomeView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public HomePresenter(SplashScreen view, Bus bus) {
        this.view2 = view;
        this.bus = bus;
    }

   // public HomePresenter(SplashScreen view, Bus bus) {
    //    this.view2 = view;
    //    this.bus = bus;
    //}

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void onRequestGoogleData() {
        bus.post(new RhymesRequestedEvent("adam"));
    }

    public void deviceInformation(DeviceInformation info) {
        bus.post(new DeviceInformation(info));
    }

    @Subscribe
    public void onSuccessSendDeviceInformation(DeviceInfoSuccess event) {
        pref = new SharedPrefManager(MainFragmentActivity.getContext());
        view2.loadingSuccess(event);
    }

}
