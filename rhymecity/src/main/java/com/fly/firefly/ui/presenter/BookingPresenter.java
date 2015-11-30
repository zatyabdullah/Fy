package com.fly.firefly.ui.presenter;

import android.util.Log;

import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.rhymes.RhymesRequestedEvent;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class BookingPresenter {

    public interface SearchFlightView {
        //void onBookingDataReceive();
        void onBookingDataReceive(SearchFlightReceive obj);
    }

    public interface ListFlightView {
        //void onBookingDataReceive(SearchFlightReceive obj);
    }

    private SearchFlightView view;
    private ListFlightView view2;

    private final Bus bus;

    public BookingPresenter(SearchFlightView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public BookingPresenter(ListFlightView view, Bus bus) {
        this.view2 = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void searchFlight(SearchFlightObj flightObj) {
        bus.post(new SearchFlightObj(flightObj));
    }

    @Subscribe
    public void onRequestSuccess(SearchFlightReceive event) {
        Log.e("HERE", "OK");
        Log.e("x", event.getJourneyObj().getStatus());

        /*Save Session And Redirect To Homepage*/
        view.onBookingDataReceive(event);

    }
    public void onRhymesForWordRequested(String word) {
        bus.post(new RhymesRequestedEvent(word));
    }
}
