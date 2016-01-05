package com.fly.firefly.ui.presenter;

import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.api.obj.PaymentInfoReceive;
import com.fly.firefly.api.obj.PaymentReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SeatSelectionReveice;
import com.fly.firefly.api.obj.SelectFlightReceive;
import com.fly.firefly.ui.object.BaseObj;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.object.Passenger;
import com.fly.firefly.ui.object.Payment;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.fly.firefly.ui.object.SeatSelection;
import com.fly.firefly.ui.object.SelectFlight;
import com.fly.firefly.ui.object.Signature;
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

    public interface ContactInfoView{
        void onContactInfo(ContactInfoReceive obj);
    }

    public interface SeatSelectionView{
        void onSeatSelect(SeatSelectionReveice obj);

    }

    public interface ItinenaryView{
        void onContactInfo(ContactInfoReceive obj);
        //void onSeatSelect();
    }

    public interface PaymentFlightView{
       void onPaymentInfoReceive(PaymentInfoReceive obj);
       void onPaymentReceive(PaymentReceive obj);
    }


    private SearchFlightView view;
    private ListFlightView view2;
    private PassengerInfoView view3;
    private ContactInfoView view4;
    private SeatSelectionView view5;
    private ItinenaryView view6;
    private PaymentFlightView view7;


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

    public BookingPresenter(ContactInfoView view, Bus bus) {
        this.view4 = view;
        this.bus = bus;
    }

    public BookingPresenter(SeatSelectionView view, Bus bus) {
        this.view5 = view;
        this.bus = bus;
    }

    public BookingPresenter(ItinenaryView view, Bus bus) {
        this.view6 = view;
        this.bus = bus;
    }

    public BookingPresenter(PaymentFlightView view, Bus bus) {
        this.view7 = view;
        this.bus = bus;
    }

    /*User Search FLight*/
    public void searchFlight(SearchFlightObj flightObj) {
        bus.post(new SearchFlightObj(flightObj));
    }

    /*User Select Flight*/
    public void selectFlight(SelectFlight flightObj) {
        bus.post(new SelectFlight(flightObj));
    }

    /*User Input Passenger Info*/
    public void passengerInfo(Passenger obj) {
        bus.post(new Passenger(obj));
    }

    /*User Input Passenger Info*/
    public void contactInfo(ContactInfo obj) {
        bus.post(new ContactInfo(obj));
    }

    /*User Input Passenger Info*/
    public void seatSelect(SeatSelection obj) {
        bus.post(new SeatSelection(obj));
    }

    /*User Input Passenger Info*/
    public void paymentInfo(Signature obj) {
        bus.post(new Signature(obj));
    }

    /*User Payment*/
    public void paymentRequest(Payment obj) {
        bus.post(new Payment(obj));
    }


    @Subscribe
    public void onPaymentInfoReceive(PaymentInfoReceive event) {
        /*Save Session And Redirect To Homepage*/
        view7.onPaymentInfoReceive(event);
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

    @Subscribe
    public void onContactInfoReceive(ContactInfoReceive event) {
        view4.onContactInfo(event);
    }

    @Subscribe
    public void onSeatReceive(SeatSelectionReveice event) {
        view5.onSeatSelect(event);
    }

    @Subscribe
    public void onPaymentReceive(PaymentReceive event) {
        view7.onPaymentReceive(event);
    }

   //@Subscribe
   // public void onItineraryReceive(ContactInfoReceive event) {view6.onItineraryInfo(event);}

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }
}
