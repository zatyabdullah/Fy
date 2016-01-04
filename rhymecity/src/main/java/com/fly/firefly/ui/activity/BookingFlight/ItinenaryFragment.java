package com.fly.firefly.ui.activity.BookingFlight;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.module.ItinenaryModule;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ItinenaryFragment extends BaseFragment implements BookingPresenter.ItinenaryView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.btnSummary)
    Button btnSummary;

    @InjectView(R.id.txtService)
    TextView txtService;

    @InjectView(R.id.insurance_details)
    TextView insurance_details;

    @InjectView(R.id.insurance_price)
    TextView insurance_price;

    @InjectView(R.id.tax_details)
    TextView tax_details;

    @InjectView(R.id.txtOneway)
    TextView txtOneway;

    @InjectView(R.id.txt_return)
    TextView txt_return;

    @InjectView(R.id.oneway_guest)
    TextView oneway_guest;

    @InjectView(R.id.oneway_guest_price)
    TextView oneway_guest_price;

    @InjectView(R.id.oneway_tax)
    TextView oneway_tax;

    @InjectView(R.id.oneway_tax_price)
    TextView oneway_tax_price;


    @InjectView(R.id.txt_sumtotalPrice)
    TextView txt_sumtotalPrice;

    @InjectView(R.id.sumtotalPrice)
    TextView sumtotalPrice;

    @InjectView(R.id.return_guest)
    TextView return_guest;

    @InjectView(R.id.return_guest_price)
    TextView return_guest_price;

    @InjectView(R.id.return_tax)
    TextView return_tax;

    @InjectView(R.id.return_tax_price)
    TextView return_tax_price;

    @InjectView(R.id.onewayblock)
    LinearLayout oneWayBlock;

    @InjectView(R.id.ServiceFeesblock)
    LinearLayout ServiceFeesblock;

    @InjectView(R.id.returnblock)
    LinearLayout returnblock;


    private int fragmentContainerId;
    private SharedPrefManager pref;
    private String bookingID,signature;
    private String flightType_return;

    View view;

    public static ItinenaryFragment newInstance(Bundle bundle) {

        ItinenaryFragment fragment = new ItinenaryFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ItinenaryModule(this)).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.itinenary, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        Bundle bundle = getArguments();

        String itinenary = bundle.getString("ITINENARY_INFORMATION");
        Log.e("itinenary", itinenary);

        Gson gson = new Gson();
        ContactInfoReceive obj = gson.fromJson(itinenary, ContactInfoReceive.class);

        /*Booking Id*/
        HashMap<String, String> initBookingID = pref.getBookingID();
        bookingID = initBookingID.get(SharedPrefManager.BOOKING_ID);


        //ONEWAY DETAILS
        txtOneway.setText(obj.getObj().getFlight_details().get(0).getStation());
        oneway_guest.setText(obj.getObj().getPrice_details().get(0).getGuest());
        oneway_guest_price.setText((obj.getObj().getPrice_details().get(0).getTotal_guest()));
        oneway_tax_price.setText(obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getTotal());

        //RETURN DETAILS
        if(obj.getObj().getFlight_details().toArray().length >1){
            txt_return.setText(obj.getObj().getFlight_details().get(1).getStation());
            return_guest.setText(obj.getObj().getPrice_details().get(1).getGuest());
            return_guest_price.setText((obj.getObj().getPrice_details().get(1).getTotal_guest()));
            return_tax_price.setText(obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getTotal());
        }

        //Total Price
        sumtotalPrice.setText(obj.getObj().getTotal_price());

        //Block Hidden If null
        if(obj.getObj().getFlight_details().toArray().length <2) {
            oneWayBlock.setVisibility(View.VISIBLE);
            returnblock.setVisibility(View.GONE);

        } else if(obj.getObj().getFlight_details().toArray().length >1) {
            oneWayBlock.setVisibility(View.VISIBLE);
            returnblock.setVisibility(View.VISIBLE);
        }

        if(obj.getStatus()!= null && obj.getStatus().equals("Services and Fees")) {
            ServiceFeesblock.setVisibility(View.VISIBLE);
            insurance_details.setText(obj.getObj().getServices().get(0).getService_name());
            insurance_price.setText(obj.getObj().getServices().get(0).getService_price());
        }else{
            ServiceFeesblock.setVisibility(View.GONE);

        }

        return view;
    }


    @Override
    public void onContactInfo(ContactInfoReceive obj) {

        String status = obj.getObj().getStatus();
        Log.e("Receive", "success");



        }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
