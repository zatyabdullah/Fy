package com.fly.firefly.ui.presenter;

import android.util.Log;

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

    public void onUpdateTerms() {
        bus.post(new TermsRequest());
    }


   @Subscribe
    public void onSuccessUpdate(TermsReceive event) {

       try{
           Log.e("Term", event.getTermObj().getTerm().get(0).getTitle());

       }catch (Exception e){
           Log.e("E2","E2");
       }

      view.onSuccessUpdate(event.getTermObj());
    }
}
