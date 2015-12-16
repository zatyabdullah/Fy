package com.fly.firefly.ui.presenter;

import android.util.Log;

import com.fly.firefly.api.obj.FailedConnectToServer;
import com.fly.firefly.api.obj.FlightInfo;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SelectFlightReceive;
import com.fly.firefly.rhymes.RhymesRequestedEvent;
import com.fly.firefly.ui.object.Passenger;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.fly.firefly.ui.object.SelectFlight;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class BookingPresenter {

    public interface SearchFlightView {
        //void onBookingDataReceive();
        void onBookingDataReceive(SearchFlightReceive obj);
    }

    public interface ListFlightView {
        void onSeletFlightReceive(SelectFlightReceive obj);
    }

    public interface PassengerInfoView{
        void onPassengerInfo(PassengerInfoReveice obj);
    }

    private SearchFlightView view;
    private ListFlightView view2;
    private PassengerInfoView view3;

    private final Bus bus;

    public BookingPresenter(SearchFlightView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public BookingPresenter(ListFlightView view, Bus bus) {
        this.view2 = view;
        this.bus = bus;
    }

    public BookingPresenter(PassengerInfoView view, Bus bus) {
        this.view3 = view;
        this.bus = bus;
    }

    public void searchFlight(SearchFlightObj flightObj) {
        bus.post(new SearchFlightObj(flightObj));
    }

    public void selectFlight(SelectFlight flightObj) {
        bus.post(new SelectFlight(flightObj));
    }

    public void passengerInfo(Passenger obj) {
        bus.post(new Passenger(obj));
    }

    @Subscribe
    public void onSearchFlight(SearchFlightReceive event) {
        /*Save Session And Redirect To Homepage*/
        view.onBookingDataReceive(event);
    }

    @Subscribe
    public void onSelectFlight(SelectFlightReceive event) {
        view2.onSeletFlightReceive(event);
    }

    @Subscribe
    public void onPassengerInfo(PassengerInfoReveice event) {
        view3.onPassengerInfo(event);
    }


    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }
}
