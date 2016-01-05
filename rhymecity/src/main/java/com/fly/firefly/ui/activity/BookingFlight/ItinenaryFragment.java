package com.fly.firefly.ui.activity.BookingFlight;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.module.ContactInfoModule;
import com.fly.firefly.ui.module.ItinenaryModule;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class ItinenaryFragment extends BaseFragment implements BookingPresenter.ItinenaryView {

    @Inject
    BookingPresenter presenter;

    private int fragmentContainerId;



    private ArrayList<DropDownItem> stateList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> countrysList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> purposeList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> titleList = new ArrayList<DropDownItem>();
    private String selectedCountryCode;
    private String selectedState;
    private Validator mValidator;
    private String insuranceTxt1,insuranceTxt2,insuranceTxt3,insuranceTxt4;
    private boolean withSeat = false;
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
        Log.e("itinenary",itinenary);

        /*Booking Id*/
        //HashMap<String, String> initBookingID = pref.getBookingID();
        //bookingID = initBookingID.get(SharedPrefManager.BOOKING_ID);

        return view;
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
