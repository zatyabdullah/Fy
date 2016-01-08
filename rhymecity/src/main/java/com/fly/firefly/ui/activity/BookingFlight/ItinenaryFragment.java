package com.fly.firefly.ui.activity.BookingFlight;


import android.content.Intent;
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
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ItinenaryFragment extends BaseFragment implements BookingPresenter.ItinenaryView {

    @Inject
    BookingPresenter presenter;

    private ArrayList<DropDownItem> stateList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> countrysList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> purposeList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> titleList = new ArrayList<DropDownItem>();
    private String selectedCountryCode;
    private String selectedState;
    private Validator mValidator;
    private String insuranceTxt1,insuranceTxt2,insuranceTxt3,insuranceTxt4;
    private boolean withSeat = false;

    @InjectView(R.id.btnItinerary)
    Button btnItinerary;

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

    @InjectView(R.id.oneway_tax_link)
    TextView oneway_tax_link;

    @InjectView(R.id.oneway_tax_price)
    TextView oneway_tax_price;

    @InjectView(R.id.admin_fee_price)
    TextView admin_fee_price;

    @InjectView(R.id.airport_tax_price)
    TextView airport_tax_price;

    @InjectView(R.id.fuel_surcharge_price)
    TextView fuel_surcharge_price;

    @InjectView(R.id.good_services_price)
    TextView good_services_price;

    @InjectView(R.id.adminfee_return_price)
    TextView adminfee_return_price;

    @InjectView(R.id.airporttax_return_price)
    TextView airporttax_return_price;

    @InjectView(R.id.fuelsurcharge_return_price)
    TextView fuelsurcharge_return_price;

    @InjectView(R.id.goodservices_return_price)
    TextView goodservices_return_price;

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

    @InjectView(R.id.return_tax_link)
    TextView return_tax_link;

    @InjectView(R.id.return_tax_price)
    TextView return_tax_price;

    @InjectView(R.id.onewayblock)
    LinearLayout oneWayBlock;

    @InjectView(R.id.ServiceFeesblock)
    LinearLayout ServiceFeesblock;

    @InjectView(R.id.returnblock)
    LinearLayout returnblock;

    @InjectView(R.id.detailblockoneway)
    LinearLayout detailblockoneway;

    @InjectView(R.id.detailblockreturn)
    LinearLayout detailblockreturn;


    private int fragmentContainerId;
    private SharedPrefManager pref;
    private String bookingID,signature;
    private String flightType_return;
    boolean showingFirst = true;

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
        //pref = new SharedPrefManager(getActivity());
        Bundle bundle = getArguments();

        String itinenary = bundle.getString("ITINENARY_INFORMATION");
        Log.e("itinenary", itinenary);

        Gson gson = new Gson();
        ContactInfoReceive obj = gson.fromJson(itinenary, ContactInfoReceive.class);

        /*Booking Id*/
        //HashMap<String, String> initBookingID = pref.getBookingID();
        //bookingID = initBookingID.get(SharedPrefManager.BOOKING_ID);


        //ONEWAY DETAILS
        txtOneway.setText(obj.getObj().getFlight_details().get(0).getStation());
        oneway_guest.setText(obj.getObj().getPrice_details().get(0).getGuest());
        oneway_guest_price.setText((obj.getObj().getPrice_details().get(0).getTotal_guest()));
        oneway_tax_price.setText(obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getTotal());

        //Tax details
        admin_fee_price.setText(obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getAdmin_fee());
        airport_tax_price.setText(obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getAirport_tax());
        fuel_surcharge_price.setText(obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getFuel_surcharge());
        good_services_price.setText(obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getGoods_and_services_tax());


        //RETURN DETAILS
        if(obj.getObj().getFlight_details().toArray().length >1){
            txt_return.setText(obj.getObj().getFlight_details().get(1).getStation());
            return_guest.setText(obj.getObj().getPrice_details().get(1).getGuest());
            return_guest_price.setText((obj.getObj().getPrice_details().get(1).getTotal_guest()));
            return_tax_price.setText(obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getTotal());

            //Tax details
            adminfee_return_price.setText(obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getAdmin_fee());
            airporttax_return_price.setText(obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getAirport_tax());
            fuelsurcharge_return_price.setText(obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getFuel_surcharge());
            goodservices_return_price.setText(obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getGoods_and_services_tax());
        }

        //Total Price
        sumtotalPrice.setText(obj.getObj().getTotal_price());

        //Block Hidden If null
        if(obj.getObj().getFlight_details().toArray().length <2) {
            oneWayBlock.setVisibility(View.VISIBLE);
            returnblock.setVisibility(View.GONE);
            detailblockoneway.setVisibility(View.GONE);
            detailblockreturn.setVisibility(View.GONE);

        } else if(obj.getObj().getFlight_details().toArray().length >1) {
            oneWayBlock.setVisibility(View.VISIBLE);
            returnblock.setVisibility(View.VISIBLE);
            detailblockoneway.setVisibility(View.GONE);
            detailblockreturn.setVisibility(View.GONE);
        }

        if(obj.getStatus()!= null && obj.getStatus().equals("Services and Fees")) {
            ServiceFeesblock.setVisibility(View.VISIBLE);
            insurance_details.setText(obj.getObj().getServices().get(0).getService_name());
            insurance_price.setText(obj.getObj().getServices().get(0).getService_price());
        }else{
            ServiceFeesblock.setVisibility(View.GONE);

        }

        oneway_tax_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showingFirst == true) {
                    detailblockoneway.setVisibility(View.VISIBLE);
                 showingFirst = false;
                }else {
                    detailblockoneway.setVisibility(View.GONE);
                    showingFirst = true;
                }
            }
        });

        return_tax_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showingFirst == true) {
                    detailblockreturn.setVisibility(View.VISIBLE);
                    showingFirst = false;
                }else {
                    detailblockreturn.setVisibility(View.GONE);
                    showingFirst = true;
                }
            }
        });


        btnItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seatSelection = new Intent(getActivity(), PaymentFlightActivity.class);
                getActivity().startActivity(seatSelection);
            }
        });

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
