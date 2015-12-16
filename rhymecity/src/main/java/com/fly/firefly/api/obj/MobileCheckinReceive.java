package com.fly.firefly.api.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 11/23/2015.
 */
public class MobileCheckinReceive {

    private String status;
    private String message;
    private List<JourneyInfo> journeys = new ArrayList<JourneyInfo>();
    private MobileCheckinReceive flightdataObj;
    private checkin_info checkin_info;


    public MobileCheckinReceive(){

    }
    public class checkin_info{

    private String StationCode;
    private String DepartureStation;
    private String ArrivalStation;
    private String DepartureDate;
    private String ArrivalDate;
    private String CarrierCode;
    private String JourneySellKey;
    private String FlightCode;

    public String getArrivalStation() {
        return ArrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        ArrivalStation = arrivalStation;
    }

    public String getDepartureDate() {
        return DepartureDate;
    }

    public void setDepartureDate(String departureDate) {
        DepartureDate = departureDate;
    }

    public String getArrivalDate() {
        return ArrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        ArrivalDate = arrivalDate;
    }

    public String getCarrierCode() {
        return CarrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        CarrierCode = carrierCode;
    }

    public String getJourneySellKey() {
        return JourneySellKey;
    }

    public void setJourneySellKey(String journeySellKey) {
        JourneySellKey = journeySellKey;
    }

    public String getFlightCode() {
        return FlightCode;
    }

    public void setFlightCode(String flightCode) {
        FlightCode = flightCode;
    }

    public String getDepartureStation() {
        return DepartureStation;
    }

    public void setDepartureStation(String departureStation) {
        DepartureStation = departureStation;
    }

    public String getStationCode() {
        return StationCode;
    }

    public void setStationCode(String stationCode) {
        StationCode = stationCode;
    }


}

    public checkin_info getCheckin_info() {return checkin_info;}
    public MobileCheckinReceive(MobileCheckinReceive param_obj){
        this.flightdataObj = param_obj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<JourneyInfo> getJourneys() {
        return journeys;
    }

    public void setJourneys(ArrayList journeys) {
        this.journeys = journeys;
    }

    public MobileCheckinReceive getJourneyObj() {
        return flightdataObj;
    }

    public void setJourneyObj(MobileCheckinReceive journeyObj) {
        this.flightdataObj = journeyObj;
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

}
