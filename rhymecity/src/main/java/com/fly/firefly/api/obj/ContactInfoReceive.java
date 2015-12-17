package com.fly.firefly.api.obj;

import com.fly.firefly.ui.activity.BookingFlight.ContactInfoActivity;

/**
 * Created by Dell on 12/16/2015.
 */
public class ContactInfoReceive {

    private String status;
    private ContactInfoReceive obj;


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
