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

public class ContactInfoFragment extends BaseFragment implements Validator.ValidationListener,DatePickerDialog.OnDateSetListener,BookingPresenter.ContactInfoView {

    @Inject
    BookingPresenter presenter;

    @Order(1) @NotEmpty
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

    @Order(2) @NotEmpty
    @InjectView(R.id.txtTitle)
    TextView txtTitle;

    @InjectView(R.id.btnSeatSelection)
    Button btnSeatSelection;

    @InjectView(R.id.btnWithoutSeatSelection)
    Button btnWithoutSeatSelection;

    @NotEmpty
    @Order(3)
    @InjectView(R.id.txtFirstName)
    TextView txtFirstName;


    @NotEmpty
    @Order(4)
    @InjectView(R.id.txtLastName)
    TextView txtLastName;

    @NotEmpty
    @Order(5)
    @InjectView(R.id.txtEmailAddress)
    TextView txtEmailAddress;

    @NotEmpty
    @Order(6)
    @InjectView(R.id.txtCountry)
    TextView txtCountry;

    @NotEmpty
    @Order(7)
    @InjectView(R.id.txtState)
    TextView txtState;

    @NotEmpty
    @Order(8)
    @InjectView(R.id.txtCity)
    TextView txtCity;

    @NotEmpty
    @Order(9)
    @InjectView(R.id.txtPostCode)
    TextView txtPostCode;

    @NotEmpty
    @Order(10)
    @InjectView(R.id.txtPhone)
    TextView txtPhone;

    @NotEmpty
    @Order(11)
    @InjectView(R.id.txtAlternatePhone)
    TextView txtAlternatePhone;

    @InjectView(R.id.companyBlock)
    LinearLayout companyBlock;

    @InjectView(R.id.txtInsurance1)
    TextView txtInsurance1;

    @InjectView(R.id.txtInsurance2)
    TextView txtInsurance2;

    @InjectView(R.id.txtInsurance3)
    TextView txtInsurance3;

    @InjectView(R.id.txtInsurance4)
    TextView txtInsurance4;

   /* @InjectView(R.id.txtInsuranceDetail)
    TextView txtInsuranceDetail;*/

    @InjectView(R.id.insuranceCheckBox)
    CheckBox insuranceCheckBox;

    @InjectView(R.id.insuranceBlock)
    LinearLayout insuranceBlock;



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
    private String insuranceTxt1,insuranceTxt2,insuranceTxt3,insuranceTxt4;
    private boolean withSeat = false;
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
        Bundle bundle = getArguments();

        String insurance = bundle.getString("INSURANCE_STATUS");

        Gson gson = new Gson();
        PassengerInfoReveice obj = gson.fromJson(insurance, PassengerInfoReveice.class);

        String insuranceStatus = obj.getObj().getInsuranceObj().getStatus();
        if(insuranceStatus.equals("Y")){
            insuranceBlock.setVisibility(View.VISIBLE);

            insuranceTxt1 = obj.getObj().getInsuranceObj().getHtml().get(0).toString();
            insuranceTxt2 = obj.getObj().getInsuranceObj().getHtml().get(1).toString();
            insuranceTxt3 = obj.getObj().getInsuranceObj().getHtml().get(2).toString();
            insuranceTxt4 = obj.getObj().getInsuranceObj().getHtml().get(3).toString();


            txtInsurance1.setText(Html.fromHtml(insuranceTxt1));
            txtInsurance2.setText(Html.fromHtml(insuranceTxt2));
            txtInsurance2.setMovementMethod(LinkMovementMethod.getInstance());

            txtInsurance3.setText(Html.fromHtml(insuranceTxt3));
            txtInsurance3.setMovementMethod(LinkMovementMethod.getInstance());

            txtInsurance4.setText(Html.fromHtml(insuranceTxt4));



           //txtInsuranceDetail.setText(Html.fromHtml("<html><b>Be sure to protect yourself with Firefly Travel Protection!</b></br><p></p></br>You got a good deal on our promo fares - but don't risk unexpected expenses!</br><p></p></br>>>Comprehensive coverage at phenomenal rates</br> <p></p>>>Added flexibility via the Trip Cancellation benefit if you are unable to proceed with your travels</br><p></p>>>Medical Coverage includes hospital admission and emergency medical evacuation*</br><p></p>>>24 Hour Worldwide Travel Assistance by our travel partner, AIG Travel</br><p></p><p></p></br>* For the full list of benefits, please refer to the <a href='https://www.aig.my/Chartis/internet/Malaysia/English/Firefly%20Travel%20Protection%20Product%20Disclosure%20Sheet_tcm4009-671123.pdf' target='_blank'>Terms and Conditions</a></br><p></p></br><b>The following passenger(s) are eligible for travel insurance:</b></br><p></p><li>Ggjji Gghjj</li><p></p></br><b>Firefly Travel Protection's Promo Plan is only 17.00 MYR MYR (inclusive of GST, when applicable)</b></br><p></p></br></html>"));
           // txtInsuranceDetail.setMovementMethod(LinkMovementMethod.getInstance());
        }

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
                popupSelectionExtra(purposeList, getActivity(), txtPurpose, true, companyBlock, "Leisure");
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
        btnSeatSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withSeat = true;
                mValidator.validate();
                Utils.hideKeyboard(getActivity(), v);
            }
        });

         /*Onclick Continue*/
        btnWithoutSeatSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withSeat = false;
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
        dismissLoading();
        String status = obj.getObj().getStatus();
        if(status.equals("success")){

            if(withSeat){
                Gson gsonFlight = new Gson();
                String seat = gsonFlight.toJson(obj);
                pref.setSeat(seat);

                Intent intent = new Intent(getActivity(), SeatSelectionActivity.class);
                intent.putExtra("SEAT_INFORMATION", (new Gson()).toJson(obj));
                getActivity().startActivity(intent);
            }else{

                Intent intent = new Intent(getActivity(), PaymentFlightActivity.class);
                intent.putExtra("ITINENARY_INFORMATION", (new Gson()).toJson(obj));
                getActivity().startActivity(intent);
            }


        }
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
               // croutonAlert(getActivity(), splitErrorMsg[0]);
            }
            croutonAlert(getActivity(), "Fill empty field");

            Log.e("Validation Failed",splitErrorMsg[0]);

        }

    }

    public void requestContacInfo(){

        String seatSelectionStatus;

        initiateLoading(getActivity());
        ContactInfo obj = new ContactInfo();

        if(withSeat){
            seatSelectionStatus = "Y";
        }else{
            seatSelectionStatus = "N";
        }
        Log.e("Seat Selection",seatSelectionStatus);
        obj.setBooking_id(bookingID);
        obj.setSeat_selection_status(seatSelectionStatus);
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

        if(insuranceCheckBox.isChecked()){
            obj.setInsurance("1");
        }else{
            obj.setInsurance("0");
        }
        obj.setContact_country(selectedCountryCode);
        obj.setContact_state(selectedState);
        obj.setContact_city(txtCity.getText().toString());
        obj.setContact_postcode(txtPostCode.getText().toString());
        obj.setContact_mobile_phone(txtPhone.getText().toString());
        obj.setContact_alternate_phone(txtAlternatePhone.getText().toString());

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
