package com.fly.firefly.ui.activity.UpdateProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.activity.Picker.DatePickerFragment;
import com.fly.firefly.ui.module.UpdateProfileModule;
import com.fly.firefly.ui.object.DatePickerObj;
import com.fly.firefly.ui.object.UpdateProfileRequest;
import com.fly.firefly.ui.presenter.UpdateProfilePresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class UpdateProfileFragment extends BaseFragment implements
        UpdateProfilePresenter.UpdateProfileView,Validator.ValidationListener  {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;
    private ArrayList<DropDownItem> countrys;
    private ArrayList<DropDownItem> state;
    private int day;
    private int month;
    private int year;
    private int month_number;
    private DatePickerObj date;
    private ArrayList<DropDownItem> titleList;
    private String selectedTitle;
    String[] state_val;
    private String selectedState;
    private String selectedCountryCode;
    private AlertDialog dialog;
    private SharedPrefManager pref;
    private int fragmentContainerId;

    @Inject UpdateProfilePresenter presenter;

    @InjectView(R.id.editEmail) TextView editEmail;

    @Order(1)@NotEmpty
    //@Length(sequence = 1, min = 6, message = "Must at least 6 character")
    //@Password(sequence =2,scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS,message = "Must have uppercase char,number and symbols") // Password validator
    @InjectView(R.id.editCurrentPassword)
    EditText editCurrentPassword;

    @Order(2)@Optional
    @InjectView(R.id.editNewPassword)
    EditText editNewPassword;

    @Order(3)@Optional
    //@ConfirmPassword(sequence =1)
    @InjectView(R.id.editConfirmPassword)
    EditText editConfirmPassword;

    @Order(4)@NotEmpty
    @InjectView(R.id.editTitle)
    TextView editTitle;

    @Order(5)@NotEmpty
    @InjectView(R.id.editFirstName)
    EditText editFirstName;

    @Order(6)
    @InjectView(R.id.editLastName)
    EditText editLastName;

    @Order(7)@NotEmpty
    @InjectView(R.id.editAddress)
    EditText editAddressLine1;

    @Order(8) @NotEmpty
    @InjectView(R.id.editCountry)
    TextView editCountry;

    @Order(9)@NotEmpty
    @InjectView(R.id.editCity)
    EditText editCity;

    @Order(10) @NotEmpty
    @InjectView(R.id.editState)
    TextView editState;

    @Order(11)@NotEmpty
    //@Length(sequence = 1, min = 5,max = 7, message = "invalid postcode")
    @InjectView(R.id.editPostcode)
    EditText editPostcode;

    @Order(12)@NotEmpty
    //@Length(sequence = 1, min = 6,max = 14, message = "invalid phone number")
    @InjectView(R.id.editMobilePhone)
    EditText editMobilePhone;


    @Order(13)
    @Optional
    //@Length(sequence = 1, min = 6,max = 14, message = "invalid phone number")
    @InjectView(R.id.editAltPhone)
    EditText editAltPhone;

    @Order(14)@Optional
    @InjectView(R.id.editFax)
    EditText editFax;

    @Order(15)@NotEmpty
    @InjectView(R.id.txtRegisterDatePicker)
    TextView txtRegisterDatePicker;


    @InjectView(R.id.btnUpdateProfile)
    Button btnUpdateProfile;



    public static UpdateProfileFragment newInstance() {

        UpdateProfileFragment fragment = new UpdateProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new UpdateProfileModule(this)).inject(this);
        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.update_profile, container, false);
        ButterKnife.inject(this, view);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]


        pref = new SharedPrefManager(getActivity());
        countrys = new ArrayList<DropDownItem>();
        state = new ArrayList<DropDownItem>();
        titleList = new ArrayList<DropDownItem>();


        /*Display Existing User Data*/

        HashMap<String, String> userinfo = pref.getUserInfo();
        String username = userinfo.get(SharedPrefManager.USER_INFO);
        Log.e("", username);

        editEmail.setText(username);
        editCurrentPassword.setText("Zatyzaty123!", TextView.BufferType.EDITABLE);
        editTitle.setText("Ms.", TextView.BufferType.EDITABLE);

           /* editFirstName.setText(obj.getUserInfo().getFirst_name(), TextView.BufferType.EDITABLE);
            editLastName.setText(obj.getUserInfo().getLast_name(), TextView.BufferType.EDITABLE);*/
        editFirstName.setText("zaty", TextView.BufferType.EDITABLE);
        editLastName.setText("abdullah", TextView.BufferType.EDITABLE);

        editAddressLine1.setText("30,Jalan Pungguk,Pengkalan Pegoh", TextView.BufferType.EDITABLE);
        editCountry.setText("Malaysia");
        editState.setText("Selangor");
        editCity.setText("Ipoh", TextView.BufferType.EDITABLE);
        editPostcode.setText("31500", TextView.BufferType.EDITABLE);
        editMobilePhone.setText("0123456789", TextView.BufferType.EDITABLE);
        editAltPhone.setText("0123456789", TextView.BufferType.EDITABLE);
        editFax.setText("0123456789", TextView.BufferType.EDITABLE);
        txtRegisterDatePicker.setText("31 Dec 2015");


       /* *//*Display user Data*//*
        JSONObject jsonUserInfo = getUserInfo(getActivity());


        String password = jsonUserInfo.optString("password");*/







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
            countrys.add(itemCountry);
        }


        /* Get State From Local String*/
        state_val = getResources().getStringArray(R.array.state);
        for(int x = 0 ; x < state_val.length ; x++) {

            /*Split data*/
            String[] parts = state_val[x].split("-");
            String state_name = parts[0];
            String state_code = parts[1];

            DropDownItem itemCountry = new DropDownItem();
            itemCountry.setText(state_name);
            itemCountry.setCode(state_code);
            itemCountry.setTag("State");
            itemCountry.setId(x);
            state.add(itemCountry);
            //titleList.add(itemCountry);

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

        /*Select list*/
        editCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(),countrys);
            }
        });

        editState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(), state);
            }
        });

        txtRegisterDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);

            }
        });

        editTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Clicked", "Ok");
                popupSelection(titleList, getActivity(),editTitle);
            }
        });



        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate form
                // Log.e("selectedTitle",selectedTitle);
                mValidator.validate();
                requestUpdateProfile();
                //requestChangePassword(editTextemail.getText().toString(), editTextPasswordCurrent.getText().toString(), editTextPasswordNew.getText().toString());

            }
        });

        return view;
    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(UpdateProfileFragment.this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }
    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(UpdateProfileFragment.this, 0);
                countryListDialogFragment.show(fm, "countryListDialogFragment");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("enter here", "ok");
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            if (requestCode == 1) {
                // if (requestCode == 1) {
                Log.e("requestCode", "1");
                DropDownItem selectedCountry = data.getParcelableExtra(CountryListDialogFragment.EXTRA_COUNTRY);

                if (selectedCountry.getTag() == "Country") {
                    editCountry.setText(selectedCountry.getText());
                    selectedCountryCode = selectedCountry.getCode();
                } else {
                    editState.setText(selectedCountry.getText());
                    selectedState = selectedCountry.getCode();
                }

            }else{

                date = (DatePickerObj)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                String month =  getMonthAlphabet(date.getMonth());
                month_number = date.getMonth();
                Log.e("Date Picker",Integer.toString(date.getMonth()));
                txtRegisterDatePicker.setText(date.getDay()+" "+month+" "+date.getYear());
            }
        }
    }






    public void requestUpdateProfile() {

        try {
            HashMap<String, String> init = pref.getSignatureFromLocalStorage();
            String signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);


            UpdateProfileRequest data = new UpdateProfileRequest();

            //Reconstruct DOB
            String var = "";
            if (date.getMonth() < 10) {
                var = "0";
            }

            String dob = date.getYear() + "-" + (var + "" + date.getMonth()) + "-" + date.getDay();

            data.setUsername(editEmail.getText().toString());
            data.setFirst_name(editFirstName.getText().toString());
            data.setLast_name(editLastName.getText().toString());
            data.setPassword(editCurrentPassword.getText().toString());
            data.setNewPassword(editNewPassword.getText().toString());
            data.setTitle(editTitle.getTag().toString());
            data.setDob(dob);
            data.setAddress_1(editAddressLine1.getText().toString());
            //data.setAddress_2(txtAddressLine2.getText().toString());
            // data.setAddress_3(txtAddressLine2.getText().toString());
            data.setAlternate_phone(editAltPhone.getText().toString());
            data.setMobile_phone(editMobilePhone.getText().toString());
            data.setCountry(selectedCountryCode);
            data.setState(selectedState);
            data.setCity(editCity.getText().toString());
            data.setPostcode(editPostcode.getText().toString());
            data.setFax(editFax.getText().toString());
            data.setSignature("");

            presenter.updateProfile(data);

        } catch (Exception e) {

        }
    }




    //Validator Result//
    @Override
    public void onValidationSucceeded() {
        Crouton.makeText(getActivity(), "Profile Successfully Updated", Style.CONFIRM).show();
        requestUpdateProfile();
        //goHomePage();
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
            } else {
                Crouton.makeText(getActivity(), message, Style.ALERT).show();
            }
        }
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
        Log.i("Page Name", "Setting screen name: " + "Change password Page");
        mTracker.setScreenName("Login" + "B");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
