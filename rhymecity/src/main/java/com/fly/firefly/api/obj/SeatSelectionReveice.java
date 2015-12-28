package com.fly.firefly.api.obj;

/**
 * Created by Dell on 12/22/2015.
 */
public class SeatSelectionReveice {

    private String status;
    private SeatSelectionReveice obj;

    public SeatSelectionReveice(SeatSelectionReveice returnObj) {
        this.obj = returnObj;
    }

    public SeatSelectionReveice getObj() {
        return obj;
    }

    public void setObj(SeatSelectionReveice obj) {
        this.obj = obj;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
