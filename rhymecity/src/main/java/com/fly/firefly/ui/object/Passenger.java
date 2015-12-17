package com.fly.firefly.ui.object;

import java.util.ArrayList;

/**
 * Created by Dell on 12/14/2015.
 */
public class Passenger {



    private String booking_id;
    private String signature;
    private ArrayList<PassengerInfo> passengers;
    private ArrayList<InfantInfo> infants;

    public Passenger(){

    }

    public Passenger(Passenger data){
        booking_id = data.getBooking_id();
        signature = data.getSignature();
        passengers = data.getPassengers();
        infants = data.getInfant();

    }


    public ArrayList<InfantInfo> getInfant() {
        return infants;
    }

    public void setInfant(ArrayList<InfantInfo> infant) {
        this.infants = infant;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ArrayList<PassengerInfo> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<PassengerInfo> passengers) {
        this.passengers = passengers;
    }

}
