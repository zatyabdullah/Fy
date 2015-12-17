package com.fly.firefly.api.obj;

/**
 * Created by Dell on 12/15/2015.
 */
public class PassengerInfoReveice {

    private String status;
    private PassengerInfoReveice Obj;

    public PassengerInfoReveice(PassengerInfoReveice param_obj){
        this.Obj = param_obj;
    }
    public PassengerInfoReveice getObj() {
        return Obj;
    }

    public void setObj(PassengerInfoReveice obj) {
        Obj = obj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
