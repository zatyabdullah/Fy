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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.UpdateProfileReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.activity.Picker.DatePickerFragment;
import com.fly.firefly.ui.module.UpdateProfileModule;
import com.fly.firefly.ui.object.DatePickerObj;
import com.fly.firefly.ui.object.UpdateProfileRequest;
import com.fly.firefly.ui.presenter.UpdateProfilePresenter;
import com.fly.firefly.utils.AESCBC;
import com.fly.firefly.utils.App;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
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
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class UpdateProfileFragment extends BaseFragment implements
        DatePickerDialog.OnDateSetListener,UpdateProfilePresenter.UpdateProfileView,Validator.ValidationListener  {

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
    private String title;
    private AlertDialog dialog;
    private SharedPrefManager pref;
    private int fragmentContainerId;
    public static final String DATEPICKER_TAG = "datepicker";
    private String fullDate;
    private static final String SCREEN_LABEL = "Update Profile";
    DropDownItem selectedCountry,selectTitle_code;
    JSONObject countJ,stateJ,titleJ;
    String selectedTitle_code,selectedTitle_name;




    @Inject UpdateProfilePresenter presenter;

    @InjectView(R.id.editEmail)
    TextView editEmail;

    @Order(1)@Optional
    //@NotEmpty
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

    @Order(16)@Optional
    @InjectView(R.id.editAddress2)
    EditText editAddressLine2;

    @Order(17)@Optional
    @InjectView(R.id.editAddress3)
    EditText editAddressLine3;

    @Order(18) @NotEmpty
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

    @Order(16)
    @InjectView(R.id.checkSubscribe)
    CheckBox checkSubscribe;

    @Order(17)
    @Checked(message = "You must agree with tem & condition")
    @InjectView(R.id.checkTNC)
    CheckBox checkTNC;

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

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        pref = new SharedPrefManager(getActivity());
        countrys = new ArrayList<DropDownItem>();
        state = new ArrayList<DropDownItem>();
        titleList = new ArrayList<DropDownItem>();


        /*Display Existing User Data*/

        JSONObject jsonUserInfo = getUserInfo(getActivity());


        String email = jsonUserInfo.optString("username");
        String title = jsonUserInfo.optString("title");
        String first_name = jsonUserInfo.optString("first_name");
        String last_name = jsonUserInfo.optString("last_name");
        String addressline1 = jsonUserInfo.optString("contact_address1");
        String addressline2 = jsonUserInfo.optString("contact_address2");
        String addressline3 = jsonUserInfo.optString("contact_address3");
        String country = jsonUserInfo.optString("contact_country");//code
        String stateU = jsonUserInfo.optString("contact_state");//code
        String city = jsonUserInfo.optString("contact_city");
        String postcode = jsonUserInfo.optString("contact_postcode");
        String dob = jsonUserInfo.optString("DOB");
        String mobile_phone = jsonUserInfo.optString("contact_mobile_phone");
        String alternate_phone = jsonUserInfo.optString("contact_alternate_phone");
        String fax = jsonUserInfo.optString("contact_fax");



        HashMap<String, String> userinfo = pref.getUserInfo();
        String username = userinfo.get(SharedPrefManager.USER_EMAIL);


        editEmail.setText(email);
        editTitle.setText(title, TextView.BufferType.EDITABLE);
        editFirstName.setText(first_name, TextView.BufferType.EDITABLE);
        editLastName.setText(last_name, TextView.BufferType.EDITABLE);
        editAddressLine1.setText(addressline1, TextView.BufferType.EDITABLE);
        editAddressLine2.setText(addressline2, TextView.BufferType.EDITABLE);
        editAddressLine3.setText(addressline3, TextView.BufferType.EDITABLE);
        editMobilePhone.setText(mobile_phone, TextView.BufferType.EDITABLE);
        editAltPhone.setText(alternate_phone, TextView.BufferType.EDITABLE);
        editPostcode.setText(postcode, TextView.BufferType.EDITABLE);
        editCity.setText(city, TextView.BufferType.EDITABLE);
        editFax.setText(fax, TextView.BufferType.EDITABLE);
        txtRegisterDatePicker.setText(dob);
        editCurrentPassword.setText("",TextView.BufferType.EDITABLE);





        /*Dropdown Country Data*/
        JSONArray jsonCountry = getCountry(getActivity());

        for (int i = 0; i < jsonCountry.length(); i++)
        {
            countJ = (JSONObject) jsonCountry.opt(i);

            DropDownItem itemCountry = new DropDownItem();
            itemCountry.setText(countJ.optString("country_name"));
            itemCountry.setCode(countJ.optString("country_code"));
            itemCountry.setTag("Country");
            itemCountry.setId(i);
            countrys.add(itemCountry);


            if(country.equals(countJ.optString("country_code"))){
                editCountry.setText(countJ.optString("country_name"));
            }
        }


        /*Dropdown Title Data*/
        JSONArray jsonTitle = getTitle(getActivity());
        for (int i = 0; i < jsonTitle.length(); i++) {
            titleJ = (JSONObject) jsonTitle.opt(i);

            DropDownItem itemTitle = new DropDownItem();
            itemTitle.setText(titleJ.optString("title_name"));
            itemTitle.setCode(titleJ.optString("title_code"));
            itemTitle.setTag("Title");
            titleList.add(itemTitle);


            if (title.equals(titleJ.optString("title_code"))) {
                editTitle.setText(titleJ.optString("title_name"));
            }


        }

        /* Get State From Local String*/
            JSONArray jsonState = getState(getActivity());
            for(int x = 0 ; x < jsonState.length() ; x++) {

            stateJ = (JSONObject) jsonState.opt(x);

                if(stateU.equals(stateJ.optString("state_code"))){
                editState.setText(stateJ.optString("state_name"));
                    Log.e("state", stateJ.optString("state_name"));
            }


        }



        /*Select dropdown list*/
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
                //datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                //datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);

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

                AnalyticsApplication.sendEvent("Click", "btnUpdateProfile");
                //Validate form
                Log.e("Clicked", "Ok");
                mValidator.validate();
                requestUpdateProfile();
                Utils.hideKeyboard(getActivity(), v);
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

        JSONObject jsonUserInfo = getUserInfo(getActivity());
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            if (requestCode == 1) {
                selectedCountry = data.getParcelableExtra(CountryListDialogFragment.EXTRA_COUNTRY);

                if (selectedCountry.getTag() == "Country") {
                    editCountry.setText(selectedCountry.getText());
                    selectedCountryCode = selectedCountry.getCode();

                   /*Each country click - reset state obj*/
                    state = new ArrayList<DropDownItem>();

                    /* Set state from selected Country Code*/
                    JSONArray jsonState = getState(getActivity());
                    for(int x = 0 ; x < jsonState.length() ; x++) {

                        JSONObject row = (JSONObject) jsonState.opt(x);
                        if(selectedCountryCode.equals(row.optString("country_code"))) {
                            DropDownItem itemCountry = new DropDownItem();
                            itemCountry.setText(row.optString("state_name"));
                            itemCountry.setCode(row.optString("state_code"));
                            itemCountry.setTag("State");
                            state.add(itemCountry);
                        }
                    }

                } else {
                    editState.setText(selectedCountry.getText());
                    selectedState = selectedCountry.getCode();

                }

            }
        }
    }

    public void requestUpdateProfile() {


        initiateLoading(getActivity());

        HashMap<String, String> init = pref.getSignatureFromLocalStorage();
        String signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);

        HashMap<String, String> init2 = pref.getNewsletterStatus();
        String newsletter = init2.get(SharedPrefManager.ISNEWSLETTER);

        JSONObject jsonUserInfo = getUserInfo(getActivity());
        UpdateProfileRequest data = new UpdateProfileRequest();

        String currentPassword= AESCBC.encrypt(App.KEY, App.IV, editCurrentPassword.getText().toString());
        String newPassword = AESCBC.encrypt(App.KEY, App.IV, editNewPassword.getText().toString());
        data.setUsername(editEmail.getText().toString());
        data.setFirst_name(editFirstName.getText().toString());
        data.setLast_name(editLastName.getText().toString());
        data.setAddress_1(editAddressLine1.getText().toString());
        data.setAddress_2(editAddressLine2.getText().toString());
        data.setAddress_3(editAddressLine3.getText().toString());
        data.setMobile_phone(editMobilePhone.getText().toString());
        data.setAlternate_phone(editAltPhone.getText().toString());
        data.setCity(editCity.getText().toString());
        data.setPostcode(editPostcode.getText().toString());
        data.setFax(editFax.getText().toString());
        data.setSignature(signatureFromLocal);

        //Post newsletter
        if (checkSubscribe.isChecked()) {
            pref.setNewsletterStatus("Y");
        }else{
            pref.setNewsletterStatus("N");
        }
        data.setNewsletter(newsletter);

        //currentpassword
        if (editCurrentPassword.getText().toString().equals("")) {
            data.setPassword("");
        }else{
            data.setPassword(currentPassword);
        }

        //Post new password
        if (editNewPassword.getText().toString().equals("")) {
            data.setNew_password("");
        }else{
            data.setNew_password(newPassword);
        }


        //Post title
            if (editTitle.getTag() == null ) {
                data.setTitle(jsonUserInfo.optString("title"));
            }else{
              data.setTitle(editTitle.getTag().toString());
        }



        //Post country
        if (selectedCountryCode == null) {
            data.setCountry(jsonUserInfo.optString("contact_country"));
        }else {
            data.setCountry(selectedCountryCode);
        }


        //Post state
        if (selectedState ==null) {
            data.setState(jsonUserInfo.optString("contact_state"));
        }else {
            data.setState(selectedState);
        }


        //Post dob
        if(txtRegisterDatePicker.getText().toString().equals(jsonUserInfo.optString("DOB"))) {
            data.setDob(jsonUserInfo.optString("DOB"));
        }else{
            data.setDob(fullDate);

        }


        presenter.onUpdateProfile(data);


    }

   @Override
    public void onSuccessUpdate(UpdateProfileReceive obj) {
       dismissLoading();
       Log.e("Update","success");
        if (obj.getStatus().equals("success")) {
            Crouton.makeText(getActivity(), "Profile Successfully Updated", Style.CONFIRM).show();
            //goHomePage();
           // pref.setLoginStatus("Y");
            Log.e("X", obj.getUserInfo().getFirst_name());
            pref.setUsername(obj.getUserInfo().getFirst_name());
            Gson gsonUserInfo = new Gson();
            String userInfo = gsonUserInfo.toJson(obj.getUserInfo());
            pref.setUserInfo(userInfo);
        }
        else if (obj.getStatus().equals("error_validation")) {
            croutonAlert(getActivity(), obj.getMessage());
            Log.e("error validation", obj.getMessage());
        }
    }


    public void goHomePage()
    {
        Intent loginPage = new Intent(getActivity(), HomeActivity.class);
        getActivity().startActivity(loginPage);
        getActivity().finish();

    }

    //Validator Result//
    @Override
    public void onValidationSucceeded() {
        //requestUpdateProfile();
        Log.e("Validation", "success");

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Log.e("Validation","fail");
       for (ValidationError error : errors) {
            View view = error.getView();

            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

             // Display error messages
           if (view instanceof EditText) {
               ((EditText) view).setError(splitErrorMsg[0]);
           }
           else if (view instanceof CheckBox){
               ((CheckBox) view).setError(splitErrorMsg[0]);
               croutonAlert(getActivity(), splitErrorMsg[0]);
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
        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }



    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String monthInAlphabet = getMonthAlphabet(month);
        txtRegisterDatePicker.setText(day + " " + monthInAlphabet + " " + year);

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 11){
            varMonth = "0";
        }
        if(day < 10){
            varDay = "0";
        }
        //fullDate = varDay+""+day+ "-" + varMonth+""+month + "-" + year;
        //fullDate = year + "-" + varMonth+""+(month+1)+"-"+varDay+""+day;
        fullDate = year + "-" + varMonth+""+(month+1)+"-"+varDay+""+day;
        Log.e("fullDate", fullDate);
    }
}
