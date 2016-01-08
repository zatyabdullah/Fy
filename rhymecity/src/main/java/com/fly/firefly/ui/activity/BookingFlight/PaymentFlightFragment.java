package com.fly.firefly.ui.activity.BookingFlight;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.PaymentInfoReceive;
import com.fly.firefly.api.obj.PaymentReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.module.PaymentFlightModule;
import com.fly.firefly.ui.object.BaseObj;
import com.fly.firefly.ui.object.Payment;
import com.fly.firefly.ui.object.Signature;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
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

public class PaymentFlightFragment extends BaseFragment implements BookingPresenter.PaymentFlightView,Validator.ValidationListener {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.paymentChannelList)
    LinearLayout paymentChannelList;

    @Order(1) @NotEmpty
    @InjectView(R.id.txtCardType)
    TextView txtCardType;

    @Order(2) @NotEmpty
    @InjectView(R.id.txtCardNumber)
    EditText txtCardNumber;

    @Order(3) @NotEmpty
    @InjectView(R.id.txtPaymentMonth)
    TextView txtPaymentMonth;

    @Order(4) @NotEmpty
    @InjectView(R.id.txtPaymentYear)
    TextView txtPaymentYear;

    @Order(5) @NotEmpty
    @InjectView(R.id.txtCardHolderName)
    EditText txtCardHolderName;

    @Order(6) @NotEmpty
    @InjectView(R.id.txtCardCVV)
    EditText txtCardCVV;

    @Order(7) @NotEmpty
    @InjectView(R.id.txtIssuingBank)
    EditText txtIssuingBank;

    @InjectView(R.id.btnPay)
    Button btnPay;

    @InjectView(R.id.creditCardFormLayout)
    LinearLayout creditCardFormLayout;

    private int fragmentContainerId;
    private SharedPrefManager pref;
    private String signature;
    private View view;
    private String selectedCheckBoxTag = "1";
    private final List<String> channelType = new ArrayList<String>();
    private ArrayList<DropDownItem> cardType = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> monthList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> yearList = new ArrayList<DropDownItem>();
    private Validator mValidator;


    public static PaymentFlightFragment newInstance() {

        PaymentFlightFragment fragment = new PaymentFlightFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new PaymentFlightModule(this)).inject(this);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.payment_flight, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(MainFragmentActivity.getContext());

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        HashMap<String, String> init = pref.getPaymentDummy();
        String paymentDummy = init.get(SharedPrefManager.PAYMENT_DUMMY);

        //Signature baseObj = new Signature();
        //baseObj.setSignature(signature);
        //getPaymentInfo(baseObj);

        Gson gson = new Gson();
        PaymentInfoReceive obj = gson.fromJson(paymentDummy, PaymentInfoReceive.class);
        generatePaymentVendorList(obj);

        //Card Selection
        txtCardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(cardType, getActivity(), txtCardType, true);
            }
        });

        //setMonthList
        yearList = getListOfYear(getActivity());
        //Card Selection
        txtPaymentYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(yearList, getActivity(), txtPaymentYear, true);
            }
        });

        //setMonthList
        monthList = getListOfMonth(getActivity());
        //Card Selection
        txtPaymentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(monthList, getActivity(), txtPaymentMonth, true);
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
                Utils.hideKeyboard(getActivity(), v);
            }
        });
        return view;
    }

    @Override
    public void onValidationSucceeded() {

        initiateLoading(getActivity());
        //paymentRequest();
        Payment paymentObj = new Payment();
        paymentObj.setSignature(signature);
        paymentObj.setCardHolderName(txtCardHolderName.getText().toString());
        paymentObj.setCardNumber(txtCardNumber.getText().toString());
        paymentObj.setChannelCode(txtCardType.getTag().toString());
        paymentObj.setChannelType(selectedCheckBoxTag);
        paymentObj.setCvv(txtCardCVV.getText().toString());
        paymentObj.setExpirationDateMonth(txtPaymentMonth.getText().toString());
        paymentObj.setExpirationDateYear(txtPaymentYear.getText().toString());
        paymentObj.setIssuingBank(txtIssuingBank.getText().toString());

        presenter.paymentRequest(paymentObj);


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
            }
            else if (view instanceof TextView){
                ((TextView) view).setError(splitErrorMsg[0]);
            }
            croutonAlert(getActivity(), "Please fill empty field");

            Log.e("Validation Failed",splitErrorMsg[0]);

        }

    }

    @Override
    public void onPaymentReceive(PaymentReceive obj) {

        dismissLoading();
        if(obj.getObj().getStatus().equals("Redirect")){

            //Open Secure Site At Browser
            String sanitizeUrl = obj.getObj().getPass().replaceAll("[/]", "");
            String url = obj.getObj().getLink()+"/android/"+sanitizeUrl;



            Intent intent = new Intent(getActivity(), PaymentWebViewActivity.class);
            intent.putExtra("PAYMENT_URL", url);
            getActivity().startActivity(intent);
            //Intent i = new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse(url));
            //startActivity(i);

        }else{
            croutonAlert(getActivity(), obj.getObj().getMessage());
        }

    }

    @Override
    public void onPaymentInfoReceive(PaymentInfoReceive obj) {

        dismissLoading();
        Log.e("Status", obj.getObj().getStatus());
        Log.e("Status", obj.getObj().getPayment_channel().get(0).getChannel_code());
        Log.e("Size", Integer.toString(obj.getObj().getPayment_channel().size()));

        if (obj.getObj().getStatus().equals("success")){
            generatePaymentVendorList(obj);
        }else{
            croutonAlert(getActivity(), obj.getObj().getMessage());
        }

        Gson gson = new Gson();
        String payment = gson.toJson(obj);
        pref.setPaymentDummy(payment);

    }

    public void generatePaymentVendorList(final PaymentInfoReceive obj){

        for(int a = 0 ; a <  obj.getObj().getPayment_channel().size() ; a++){

            String paymentType = obj.getObj().getPayment_channel().get(a).getChannel_type();
            Log.e("PaymentType",paymentType);
            if (channelType.contains(paymentType)){
                /*SKIP*/
            }else {
                channelType.add(paymentType);
            }

            if(obj.getObj().getPayment_channel().get(a).getChannel_type().equals("1")){
                DropDownItem itemTitle = new DropDownItem();
                itemTitle.setText(obj.getObj().getPayment_channel().get(a).getChannel_name());
                itemTitle.setCode(obj.getObj().getPayment_channel().get(a).getChannel_code());
                itemTitle.setTag("Title");
                cardType.add(itemTitle);
            }


            //List<String> imageURLCreditCard = new ArrayList<String>();

/*            if(obj.getPayment_channel().get(a).getChannel_type().equals("1")){
                imageURL.add(obj.getPayment_channel().get(a).getChannel_logo());
            }
            else if(obj.getPayment_channel().get(a).getChannel_type().equals("2")){
                imageURL.add(obj.getPayment_channel().get(a).getChannel_logo());
            }


        }*/
        }

        for(int totalPaymentChannel = 0 ; totalPaymentChannel < channelType.size() ; totalPaymentChannel++){


            LinearLayout seatRow = new LinearLayout(getActivity());
            seatRow.setOrientation(LinearLayout.VERTICAL);
            seatRow.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);

            final LinearLayout imageRow = new LinearLayout(getActivity());
            imageRow.setOrientation(LinearLayout.HORIZONTAL);
            imageRow.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);

            for(int totalImage = 0 ; totalImage < obj.getObj().getPayment_channel().size() ; totalImage++){

                if(channelType.get(totalPaymentChannel).toString().equals(obj.getObj().getPayment_channel().get(totalImage).getChannel_type())){
                    Log.e(channelType.get(totalPaymentChannel),obj.getObj().getPayment_channel().get(totalImage).getChannel_type());

                    //Need to move this later
                    final AjaxCallback<Bitmap> cb = new AjaxCallback<Bitmap>() {
                        @Override
                        public void callback(String url, Bitmap bm, AjaxStatus status) {
                            // do whatever you want with bm (the image)
                            ImageView image = new ImageView(getActivity());
                            image.setImageBitmap(bm);
                            imageRow.addView(image);

                        }
                    };
                    final AQuery aq = new AQuery(getActivity());
                    aq.ajax(obj.getObj().getPayment_channel().get(totalImage).getChannel_logo(), Bitmap.class, 0, cb);

                }

            }

            final RadioButton selectPaymentChannel = new RadioButton(getActivity());
            selectPaymentChannel.setId(totalPaymentChannel + 1);
            selectPaymentChannel.setTag(channelType.get(totalPaymentChannel).toString());
            if(channelType.get(totalPaymentChannel).toString().equals("1")){
                selectPaymentChannel.setText("Credit Card");
            }


            seatRow.addView(selectPaymentChannel);
            seatRow.addView(imageRow);

            paymentChannelList.addView(seatRow);

        }
        //set first checkbox checked
        final RadioButton checkBox = (RadioButton) view.findViewWithTag(channelType.get(0).toString());
        checkBox.setChecked(true);

        for(int y = 0 ; y < channelType.size() ; y++){

            final RadioButton checkToRemove = (RadioButton) view.findViewWithTag(channelType.get(y).toString());

            checkToRemove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                         @Override
                                                         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                selectedCheckBoxTag = checkToRemove.getTag().toString();

             if(selectedCheckBoxTag.equals("1")){
                creditCardFormLayout.setVisibility(View.VISIBLE);
            }else{
                creditCardFormLayout.setVisibility(View.GONE);
            }

            for (int b = 0; b < channelType.size(); b++) {
            if (selectedCheckBoxTag != channelType.get(b).toString()) {
            RadioButton checkToRemove = (RadioButton) view.findViewWithTag(channelType.get(b).toString());
            checkToRemove.setChecked(false);


                                   }
                           }
                        }
             }
                          }
            );
        }


    }

    public void getPaymentInfo(Signature baseObj){
        initiateLoading(getActivity());
        presenter.paymentInfo(baseObj);
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
