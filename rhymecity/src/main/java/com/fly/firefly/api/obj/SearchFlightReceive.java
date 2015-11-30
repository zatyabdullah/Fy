package com.fly.firefly.api.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 11/23/2015.
 */
public class SearchFlightReceive{

    private String status;
    private List<JourneyInfo> journeys = new ArrayList<JourneyInfo>();
    private SearchFlightReceive journeyObj;

    public SearchFlightReceive(){
    }

    public SearchFlightReceive(SearchFlightReceive param_obj){
        this.journeyObj = param_obj;
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

    public SearchFlightReceive getJourneyObj() {
        return journeyObj;
    }

    public void setJourneyObj(SearchFlightReceive journeyObj) {
        this.journeyObj = journeyObj;
    }

}
