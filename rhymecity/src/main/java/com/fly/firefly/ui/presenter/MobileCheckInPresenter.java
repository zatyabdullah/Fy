package com.fly.firefly.ui.presenter;

import android.util.Log;

import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.rhymes.RhymesRequestedEvent;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class MobileCheckInPresenter {

    public interface MobileCheckInView {
        void onCheckindataReceive(MobileCheckinReceive obj);

    }

    private final MobileCheckInView view;
    private final Bus bus;

    public MobileCheckInPresenter(MobileCheckInView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void checkinFlight(MobileCheckinObj flightObj) {
        bus.post(new MobileCheckinObj(flightObj));
    }

    @Subscribe
    public void onRequestSuccess(MobileCheckinReceive event) {
        Log.e("HERE", "OK");
        Log.e("x", event.getJourneyObj().getStatus());

        /*Save Session And Redirect To Homepage*/
        view.onCheckindataReceive(event);

    }

    public void onRhymesForWordRequested(String word) {
        bus.post(new RhymesRequestedEvent(word));
    }
}
