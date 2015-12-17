package com.fly.firefly.ui.activity.BookingFlight;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.fly.firefly.ui.module.PersonalDetailModule;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.object.InfantInfo;
import com.fly.firefly.ui.object.Passenger;
import com.fly.firefly.ui.object.PassengerInfo;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class ContactInfoFragment extends BaseFragment implements Validator.ValidationListener,DatePickerDialog.OnDateSetListener,BookingPresenter.ContactInfoView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.txtPurpose)
    TextView txtPurpose;

    @Optional
    @InjectView(R.id.txtCompanyAddress1)
    TextView txtCompanyAddress1;

    @Optional
    @InjectView(R.id.txtCompanyAddress3)
    TextView txtCompanyAddress2;

    @Optional
    @InjectView(R.id.txtCompanyAddress3)
    TextView txtCompanyAddress3;

    @InjectView(R.id.txtTitle)
    TextView txtTitle;

    @InjectView(R.id.btnContactInfo)
    Button btnContactInfo;

    @NotEmpty(sequence = 1)
    @Order(1)
    @InjectView(R.id.txtFirstName)
    TextView txtFirstName;

    @InjectView(R.id.txtLastName)
    TextView txtLastName;

    @InjectView(R.id.txtEmailAddress)
    TextView txtEmailAddress;

    @InjectView(R.id.txtCountry)
    TextView txtCountry;

    @InjectView(R.id.txtState)
    TextView txtState;

    @InjectView(R.id.txtCity)
    TextView txtCity;

    @InjectView(R.id.txtPostCode)
    TextView txtPostCode;

    @InjectView(R.id.txtPhone)
    TextView txtPhone;

    @InjectView(R.id.txtAlternatePhone)
    TextView txtAlternatePhone;

    @InjectView(R.id.companyBlock)
    LinearLayout companyBlock;


    private int fragmentContainerId;
    private String DATEPICKER_TAG = "DATEPICKER_TAG";

    private ArrayList<DropDownItem> travelDocList;
    private ArrayList<DropDownItem> adultPassengerList;

    private final String ADULT = "ADULT";
    private final String INFANT = "INFANT";
    private String adult,infant;
    private SharedPrefManager pref;
    private String bookingID,signature;
    private int clickedPassenger;
    private Boolean boolDob = false;
    private Boolean boolExpireDate = false;


    private ArrayList<DropDownItem> stateList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> countrysList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> purposeList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> titleList = new ArrayList<DropDownItem>();
    private String selectedCountryCode;
    private String selectedState;
    private Validator mValidator;

    View view;

    public static ContactInfoFragment newInstance(Bundle bundle) {

        ContactInfoFragment fragment = new ContactInfoFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ContactInfoModule(this)).inject(this);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.passenger_contact_info, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());

         /*Booking Id*/
        HashMap<String, String> initBookingID = pref.getBookingID();
        bookingID = initBookingID.get(SharedPrefManager.BOOKING_ID);

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        /*Travelling Purpose*/
        final String[] purpose = getResources().getStringArray(R.array.purpose);
        for(int i = 0;i<purpose.length; i++)
        {
            int purposeTag = i+1;
            DropDownItem itemPurpose = new DropDownItem();
            itemPurpose.setText(purpose[i]);
            itemPurpose.setCode(Integer.toString(purposeTag));
            purposeList.add(itemPurpose);
        }

         /*Display Title Data*/
        JSONArray jsonTitle = getTitle(getActivity());
        for (int i = 0; i < jsonTitle.length(); i++)
        {
            JSONObject row = (JSONObject) jsonTitle.opt(i);

            DropDownItem itemTitle = new DropDownItem();
            itemTitle.setText(row.optString("title_name"));
            itemTitle.setCode(row.optString("title_code"));
            itemTitle.setTag("Title");
            titleList.add(itemTitle);
        }

        /*Display Country Data*/
        JSONArray jsonCountry = getCountry(getActivity());

        for (int i = 0; i < jsonCountry.length(); i++)
        {
            JSONObject row = (JSONObject) jsonCountry.opt(i);

            DropDownItem itemCountry = new DropDownItem();
            itemCountry.setText(row.optString("country_name"));
            itemCountry.setCode(row.optString("country_code"));
            itemCountry.setTag("Country");
            itemCountry.setId(i);
            countrysList.add(itemCountry);
        }

        /*Select Country*/
        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(),countrysList);
            }
        });

        /*Select State*/
        txtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(), stateList);
            }
        });

        /*Select Purpose*/
        txtPurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelectionExtra(purposeList, getActivity(), txtPurpose, true, companyBlock,"Leisure");
            }
        });

        /*Select Title*/
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(titleList, getActivity(), txtTitle, true);
            }
        });

         /*Onclick Continue*/
        btnContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
                Utils.hideKeyboard(getActivity(), v);
            }
        });



        return view;
    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(ContactInfoFragment.this, 0);
                countryListDialogFragment.show(fm, "countryListDialogFragment");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            if (requestCode == 1) {
                DropDownItem selectedCountry = data.getParcelableExtra(CountryListDialogFragment.EXTRA_COUNTRY);

                if (selectedCountry.getTag() == "Country") {
                    txtCountry.setText(selectedCountry.getText());
                    selectedCountryCode = selectedCountry.getCode();

                   /*Each country click - reset state obj*/
                    stateList = new ArrayList<DropDownItem>();

                    /* Set state from selected Country Code*/
                    JSONArray jsonState = getState(getActivity());
                    for(int x = 0 ; x < jsonState.length() ; x++) {

                        JSONObject row = (JSONObject) jsonState.opt(x);
                        if(selectedCountryCode.equals(row.optString("country_code"))) {
                            DropDownItem itemCountry = new DropDownItem();
                            itemCountry.setText(row.optString("state_name"));
                            itemCountry.setCode(row.optString("state_code"));
                            itemCountry.setTag("State");
                            stateList.add(itemCountry);
                        }
                    }

                } else {
                    txtState.setText(selectedCountry.getText());
                    selectedState = selectedCountry.getCode();
                }

            }
        }
    }

    @Override
    public void onContactInfo(ContactInfoReceive obj){
        Log.e("Status",obj.getObj().getStatus());
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 10) {
            varMonth = "0";
        }
        if(day < 10){
            varDay = "0";
        }

    }

    @Override
    public void onValidationSucceeded() {
        requestContacInfo();
        Log.e("Success", "True");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();

             /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);
            }else if(view instanceof TextView){
                ((TextView) view).setError(splitErrorMsg[0]);
            }
            else if (view instanceof CheckBox){
                ((CheckBox) view).setError(splitErrorMsg[0]);
                croutonAlert(getActivity(), splitErrorMsg[0]);
            }

            Log.e("Validation Failed",splitErrorMsg[0]);

        }

    }

    public void requestContacInfo(){

        ContactInfo obj = new ContactInfo();
        obj.setBooking_id(bookingID);
        obj.setSignature(signature);
        obj.setContact_travel_purpose(txtPurpose.getTag().toString());
        obj.setContact_title(txtTitle.getTag().toString());
        obj.setContact_first_name(txtFirstName.getText().toString());
        obj.setContact_last_name(txtLastName.getText().toString());
        obj.setContact_email(txtEmailAddress.getText().toString());

        /*Exception*/
        try{
            obj.setContact_address1(txtCompanyAddress1.getText().toString());
            obj.setContact_address2(txtCompanyAddress2.getText().toString());
            obj.setContact_address3(txtCompanyAddress3.getText().toString());
        }catch(Exception e){

        }


        obj.setContact_country(selectedCountryCode);
        //obj.setContact_country(txtCountry.getTag().toString());
        //obj.setContact_state(txtState.getTag().toString());
        obj.setContact_state(selectedState);
        obj.setContact_city(txtCity.getText().toString());
        obj.setContact_postcode(txtPostCode.getText().toString());
        obj.setContact_mobile_phone(txtPhone.getText().toString());
        obj.setContact_alternate_phone(txtAlternatePhone.getText().toString());
        obj.setInsurance("0");

        presenter.contactInfo(obj);

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
