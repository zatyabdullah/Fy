package com.fly.firefly.api.obj;

import com.fly.firefly.ui.activity.BookingFlight.ContactInfoActivity;
import com.fly.firefly.ui.object.SeatInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 12/16/2015.
 */
public class ContactInfoReceive {

    private String status;
    private String booking_id;
    private ContactInfoReceive obj;
    private List<PasssengerInfo> passengers = new ArrayList<PasssengerInfo>();
    private List<Journeys> journeys = new ArrayList<Journeys>();

    public List<Journeys> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<Journeys> journeys) {
        this.journeys = journeys;
    }

    public class Journeys{

        private String departure_station;
        private String arrival_station;
        private List<SeatInfo> seat_info = new ArrayList<SeatInfo>();


        public String getDeparture_station() {
            return departure_station;
        }

        public void setDeparture_station(String departure_station) {
            this.departure_station = departure_station;
        }

        public String getArrival_station() {
            return arrival_station;
        }

        public void setArrival_station(String arrival_station) {
            this.arrival_station = arrival_station;
        }

        public List<SeatInfo> getSeat_info() {
            return seat_info;
        }

        public void setSeat_info(List<SeatInfo> seat_info) {
            this.seat_info = seat_info;
        }

    }

   /* public class PassengerSeatStatus{

        private boolean passengerSelected;
        private String passengerSeat;
        private String passengerCompartment;
        private String flightStatus;

        public boolean getPassengerSelected() {
            return passengerSelected;
        }

        public void setPassengerSelected(boolean passengerSelected) {
            this.passengerSelected = passengerSelected;
        }

        public String getPassengerSeat() {
            return passengerSeat;
        }

        public void setPassengerSeat(String passengerSeat) {
            this.passengerSeat = passengerSeat;
        }

        public String getPassengerCompartment() {
            return passengerCompartment;
        }

        public void setPassengerCompartment(String passengerCompartment) {
            this.passengerCompartment = passengerCompartment;
        }

        public String getFlightStatus() {
            return flightStatus;
        }

        public void setFlightStatus(String flightStatus) {
            this.flightStatus = flightStatus;
        }

    }
    */


































    public class PasssengerInfo{

        private String title;
        private String first_name;
        private String last_name;
        private String dob;
        private String passenger_below_182;
        private String with_infant;
        private boolean selected;
        private boolean active;
        private String seat;
        private String compartment;
        private String compartmentReturn;
        private String seatReturn;


       /* private PassengerSeatStatus seatStatus;

        public PassengerSeatStatus getSeatStatus() {
            return seatStatus;
        }

        public void setSeatStatus(PassengerSeatStatus seatStatus) {
            this.seatStatus = seatStatus;
        }*/



        public String getCompartmentReturn() {
            return compartmentReturn;
        }

        public void setCompartmentReturn(String compartmentReturn) {
            this.compartmentReturn = compartmentReturn;
        }

        public String getSeatReturn() {
            return seatReturn;
        }

        public void setSeatReturn(String seatReturn) {
            this.seatReturn = seatReturn;
        }



        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getCompartment() {
            return compartment;
        }

        public void setCompartment(String compartment) {
            this.compartment = compartment;
        }


        public String getSeat() {
            return seat;
        }

        public void setSeat(String seat) {
            this.seat = seat;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getPassenger_below_18() {
            return passenger_below_182;
        }

        public void setPassenger_below_18(String passenger_below_18) {
            this.passenger_below_182 = passenger_below_18;
        }

        public String getWith_infant() {
            return with_infant;
        }

        public void setWith_infant(String with_infant) {
            this.with_infant = with_infant;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }














    public List<PasssengerInfo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PasssengerInfo> passengers) {
        this.passengers = passengers;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public ContactInfoReceive(ContactInfoReceive param_obj){
        this.obj = param_obj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ContactInfoReceive getObj() {
        return obj;
    }

    public void setObj(ContactInfoReceive obj) {
        this.obj = obj;
    }

}
