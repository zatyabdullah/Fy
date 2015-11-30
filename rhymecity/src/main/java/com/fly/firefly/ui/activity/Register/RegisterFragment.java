package com.fly.firefly.ui.activity.Register;

import android.app.Activity;
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

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.RegisterReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Login.LoginActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.activity.Picker.DatePickerFragment;
import com.fly.firefly.ui.module.RegisterModule;
import com.fly.firefly.ui.object.DatePickerObj;
import com.fly.firefly.ui.object.RegisterObj;
import com.fly.firefly.ui.presenter.RegisterPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

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

public class RegisterFragment extends BaseFragment implements RegisterPresenter.RegisterView,Validator.ValidationListener {


    // Validator Attributes
    private Validator mValidator;
    @Inject
    RegisterPresenter presenter;

    @Order(14) @NotEmpty
    @InjectView(R.id.editTextCountry)
    TextView editTextCountry;

    @InjectView(R.id.editTextState)
    TextView editTextState;

    @NotEmpty(sequence = 1)
    @Order(1)
    @InjectView(R.id.txtUsername)
    EditText txtUsername;

    @Order(2)
    @NotEmpty(sequence = 1)
    @Length(sequence = 2, min = 6, message = "Must at least 6 character")
    @Password(sequence =3,scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS,message = "Must have uppercase char,number and symbols") // Password validator
    @InjectView(R.id.txtPassword)
    EditText txtPassword;

    @Order(3)
    @NotEmpty(sequence = 1)
    @ConfirmPassword(sequence = 2)
    @InjectView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;

    @Order(4)
    @NotEmpty
    @InjectView(R.id.txtFirstName)
    EditText txtFirstName;

    @Order(5)
    @NotEmpty(sequence = 1)
    @InjectView(R.id.txtRegisterDatePicker)
    TextView txtRegisterDatePicker;

    @Order(6) @NotEmpty(sequence = 1)
    @InjectView(R.id.txtLastName)
    EditText txtLastName;

    @Order(7) @NotEmpty(sequence = 1)
    @InjectView(R.id.txtAddressLine1)
    EditText txtAddressLine1;

    @Order(8) @Optional
    @InjectView(R.id.txtAddressLine2)
    EditText txtAddressLine2;

    @Order(9) @NotEmpty(sequence = 1) @Length(sequence = 2, min = 5,max = 7, message = "invalid postcode")
    @InjectView(R.id.editTextPostcode)
    EditText editTextPostcode;

    @Order(10) @NotEmpty(sequence = 1)
    @Length(sequence = 2, min = 6,max = 14, message = "invalid phone number")
    @InjectView(R.id.editTextMobilePhone) EditText editTextMobilePhone;

    @InjectView(R.id.editTextAlternatePhone)
    EditText editTextAlternatePhone;

    @Order(12)@Optional
    @InjectView(R.id.editTextFax)
    EditText editTextFax;

    @Order(13)@NotEmpty
    @InjectView(R.id.txtCity)
    EditText txtCity;

    @Order(15)@NotEmpty
    @InjectView(R.id.txtTitle)
    TextView txtTitle;

    @Order(16)
    @Checked(message = "You must agree with tem & condition")
    @InjectView(R.id.chkTNC)
    CheckBox chkTNC;

    @InjectView(R.id.registerContinueButton)
    Button registerContinueButton;


    private int currentPage;
    private ArrayList<DropDownItem> countrys;
    private ArrayList<DropDownItem> state;
    private int day;
    private int month;
    private int year;
    private int fragmentContainerId;
    private SharedPrefManager pref;
    private int month_number;
    private DatePickerObj date;
    private ArrayList<DropDownItem> titleList;
    private String selectedTitle;
    String[] state_val;
    private String selectedState;
    private String selectedCountryCode;

    public static RegisterFragment newInstance() {

        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new RegisterModule(this)).inject(this);
        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.register, container, false);
        ButterKnife.inject(this, view);


        pref = new SharedPrefManager(getActivity());
        countrys = new ArrayList<DropDownItem>();
        state = new ArrayList<DropDownItem>();
        titleList = new ArrayList<DropDownItem>();


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

         /*Switch register info block*/
        editTextCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(),countrys);
            }
        });

        editTextState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(), state);
            }
        });

        txtRegisterDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(view);

            }
        });

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.e("Clicked", "Ok");
               popupSelection(titleList, getActivity(),txtTitle);
            }
        });

        registerContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate form
                mValidator.validate();
                Utils.hideKeyboard(getActivity(), v);
            }
        });


        return view;
    }

    /*Date Picker -> need to move to main activity*/
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(RegisterFragment.this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }


    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(RegisterFragment.this, 0);
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
                   editTextCountry.setText(selectedCountry.getText());
                   selectedCountryCode = selectedCountry.getCode();
               } else {
                   editTextState.setText(selectedCountry.getText());
                   selectedState = selectedCountry.getCode();
               }

           }else{

               date = (DatePickerObj)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
               String month =  getMonthAlphabet(date.getMonth());
               month_number = date.getMonth();

               txtRegisterDatePicker.setText(date.getDay() + " " + month + " " + date.getYear());
        }
       }
    }

    public void requestRegister(){

            HashMap<String, String> init = pref.getSignatureFromLocalStorage();
            String signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);

            RegisterObj regObj = new RegisterObj();

            //Reconstruct DOB
            String varMonth = "";
            String varDay = "";

            if(date.getMonth() < 10){
                varMonth = "0";
            }
            if(date.getDay() < 10){
                varDay = "0";
            }


            String dob = date.getYear()+"-"+(varMonth+""+date.getMonth())+"-"+varDay+""+date.getDay();

            regObj.setUsername(txtUsername.getText().toString());
            regObj.setFirst_name(txtFirstName.getText().toString());
            regObj.setLast_name(txtLastName.getText().toString());
            regObj.setPassword(txtPassword.getText().toString());
            regObj.setTitle(txtTitle.getTag().toString());
            regObj.setDob(dob);
            regObj.setAddress_1(txtAddressLine1.getText().toString());
            regObj.setAddress_2(txtAddressLine2.getText().toString());
            regObj.setAddress_3(txtAddressLine2.getText().toString());
            regObj.setAlternate_phone(editTextAlternatePhone.getText().toString());
            regObj.setMobile_phone(editTextMobilePhone.getText().toString());
            regObj.setCountry(selectedCountryCode);
            regObj.setState(selectedState);
            regObj.setCity(txtCity.getText().toString());
            regObj.setPostcode(editTextPostcode.getText().toString());
            regObj.setFax(editTextFax.getText().toString());
            regObj.setSignature("");

            presenter.onRequestRegister(regObj);
    }

    @Override
    public void onSuccessRegister(RegisterReceive obj) {

        if (obj.getStatus().equals("success")) {

            Intent home = new Intent(getActivity(), LoginActivity.class);
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(home);
            getActivity().finish();

        }
        else if (obj.getStatus().equals("error")) {

            Crouton.makeText(getActivity(), obj.getMessage(), Style.ALERT).show();

        }else{

            Crouton.makeText(getActivity(), obj.getMessage(), Style.ALERT).show();

        }
    }

    @Override
    public void onValidationSucceeded() {

        requestRegister();
        Log.e("Success","True");
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
            }
            else if (view instanceof CheckBox){
                ((CheckBox) view).setError(splitErrorMsg[0]);
                Crouton.makeText(getActivity(), splitErrorMsg[0],Style.ALERT).show();
            }

            Log.e("Validation Failed",splitErrorMsg[0]);

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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    /*public void registerBackFunction()
    {
            showHiddenBlock(currentPage-1);

    }*/

}
