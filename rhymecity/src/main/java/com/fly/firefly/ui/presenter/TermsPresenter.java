package com.fly.firefly.ui.presenter;

import com.fly.firefly.api.obj.TermsReceive;
import com.fly.firefly.ui.object.TermsRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class TermsPresenter {

    public interface TermsView {
        void onSuccessUpdate(TermsReceive obj);
    }

    private final TermsView view;
    private final Bus bus;

    public TermsPresenter(TermsView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void onUpdateTerms(TermsRequest data) {
        bus.post(new TermsRequest(data));
    }


   @Subscribe
    public void onSuccessUpdate(TermsReceive event) {

        //Log.e("Messages", event.getStatus());
        //*Save Session And Redirect To Homepage*//*
        view.onSuccessUpdate(event.getTermObj());
    }
}
