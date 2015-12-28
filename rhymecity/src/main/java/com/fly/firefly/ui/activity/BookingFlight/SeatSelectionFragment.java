package com.fly.firefly.ui.activity.BookingFlight;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.adapter.FlightDetailAdapter;
import com.fly.firefly.ui.adapter.PassengerSeatAdapter;
import com.fly.firefly.ui.module.SeatSelectionModule;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.object.SeatInfo;
import com.fly.firefly.ui.object.SeatSelect;
import com.fly.firefly.ui.object.SeatSelection;
import com.fly.firefly.ui.object.SeatSetup;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.ui.presenter.SeatSelectionPresenter;
import com.fly.firefly.utils.ExpandAbleGridView;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SeatSelectionFragment extends BaseFragment implements BookingPresenter.SeatSelectionView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.btnSeat) Button btnSeat ;
    @InjectView(R.id.seatList) LinearLayout seatList ;

    @InjectView(R.id.listPassenger)ExpandAbleGridView listPassenger;

    private SharedPrefManager pref;
    private PassengerSeatAdapter passengerSeatList;

    private int fragmentContainerId;
    private List<String> seatTag;
    private List<String> selectedSeatTag;
    private int passengerNo;
    private View view;
    private String bookingID,signature;


    public static SeatSelectionFragment newInstance(Bundle bundle) {

        SeatSelectionFragment fragment = new SeatSelectionFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new SeatSelectionModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.seat_selection, container, false);
        ButterKnife.inject(this, view);

        /*Preference Manager*/
        pref = new SharedPrefManager(MainFragmentActivity.getContext());
        HashMap<String, String> init = pref.getSeat();
        String seatHash = init.get(SharedPrefManager.SEAT);

           /*Booking Id*/
        HashMap<String, String> initBookingID = pref.getBookingID();
        bookingID = initBookingID.get(SharedPrefManager.BOOKING_ID);

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);


        //Bundle bundle = getArguments();
        //String seatGSON = bundle.getString("SEAT_INFORMATION");

        Gson gson = new Gson();
        ContactInfoReceive obj = gson.fromJson(seatHash, ContactInfoReceive.class);
        List<SeatInfo> seatInfo = obj.getObj().getJourneys().get(0).getSeat_info();
        int seatSize = seatInfo.size();

        final List<ContactInfoReceive.PasssengerInfo> passengers = obj.getObj().getPassengers();
        passengers.get(0).setSelected(true);

        passengerSeatList = new PassengerSeatAdapter(getActivity(),passengers,this);
        listPassenger.setAdapter(passengerSeatList);

        Log.e("Total Seat Row", Integer.toString(seatInfo.size()));
        int seatCount = 0;
        /*Set Seat Row*/
        List<String> tempSeatStorage = new ArrayList<String>();
        List<SeatSetup> tempSeatValue = new ArrayList<SeatSetup>();

        for(int seatNumber = 0 ; seatNumber < seatSize ; seatNumber++){

            boolean injectSeatSetup;

            String seatNumberEx = seatInfo.get(seatNumber).getSeat_number();
            String seatNumberWithoutAlphabet = seatNumberEx.substring(0, seatNumberEx.length()-1);
            String lastSeatCharacter = seatNumberEx.substring(seatNumberEx.length() - 1);

            if (tempSeatStorage.contains(seatNumberWithoutAlphabet)){
                /*SKIP*/
                injectSeatSetup = false;
            }else {
                tempSeatStorage.add(seatNumberWithoutAlphabet);
                seatCount++;
                injectSeatSetup = true;
            }

            if(injectSeatSetup){
                /*SeatNumberByRow*/
                ArrayList<SeatInfo> seat = new ArrayList<SeatInfo>();

                for(int seatNumberRow = 0 ; seatNumberRow < seatSize ; seatNumberRow++){

                    String seatNumberExx = seatInfo.get(seatNumberRow).getSeat_number();
                    String seatNumberWithoutAlphabex = seatNumberExx.substring(0, seatNumberExx.length() - 1);

                    if(tempSeatStorage.get(seatCount-1).equals(seatNumberWithoutAlphabex)){
                        SeatInfo rebuildSeatInfo = new SeatInfo();
                        rebuildSeatInfo.setCompartment_designator(seatInfo.get(seatNumberRow).getCompartment_designator());
                        rebuildSeatInfo.setSeat_number(seatInfo.get(seatNumberRow).getSeat_number());
                        rebuildSeatInfo.setSeat_type(seatInfo.get(seatNumberRow).getSeat_type());
                        rebuildSeatInfo.setStatus(seatInfo.get(seatNumberRow).getStatus());
                        seat.add(rebuildSeatInfo);
                    }
                }

                SeatSetup seatSetup = new SeatSetup();
                seatSetup.setSeatRow(tempSeatStorage.get(seatCount - 1));
                seatSetup.setSeatRowArray(seat);
                tempSeatValue.add(seatSetup);
            }

        }

        /*3 passenger -- set array to 3 only*/
        seatTag = new ArrayList<>(1);
        selectedSeatTag = new ArrayList<>(2);

        for (int label = 0; label < tempSeatStorage.size(); label++)
        {
            LinearLayout seatRow = new LinearLayout(getActivity());
            seatRow.setOrientation(LinearLayout.HORIZONTAL);
            seatRow.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);
            seatRow.setWeightSum(1);


            for (int x = 1; x < tempSeatValue.get(label).getSeatRowArray().size()+1; x++)
            {

                String seatNumber = tempSeatValue.get(label).getSeatRowArray().get(x-1).getSeat_number();
                String seatType = tempSeatValue.get(label).getSeatRowArray().get(x-1).getSeat_type();
                String seatStatus = tempSeatValue.get(label).getSeatRowArray().get(x-1).getStatus();
                final String compartment = tempSeatValue.get(label).getSeatRowArray().get(x-1).getCompartment_designator();

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.25f);


                final TextView txtDetailList = new TextView(getActivity());
                txtDetailList.setText(seatNumber);
                txtDetailList.setGravity(Gravity.CENTER);
                txtDetailList.setTextColor(getResources().getColor(R.color.white));
                txtDetailList.setPadding(5, 20, 5, 20);
                txtDetailList.setTag(seatNumber);
                txtDetailList.setBackgroundColor(getResources().getColor(R.color.grey_background));

                txtDetailList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (seatTag.size() == 1) {

                            Log.e("NEED TO REMOVE", seatTag.get(0));

                            //TextView seatToRemove = (TextView) view.findViewWithTag(seatTag.get(0));
                            TextView seatToRemove = (TextView) view.findViewWithTag(passengerSeatList.getSelected(passengerNo));

                            seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                            seatToRemove.setClickable(true);
                            //seatToRemove.setTextColor(getResources().getColor(R.color.white));

                            seatTag.remove(0);
                            seatTag.add(txtDetailList.getText().toString());

                            txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                            txtDetailList.setClickable(false);

                            passengerSeatList.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                            passengerSeatList.setSelectedCompartmentSeat(compartment);
                            //passengerSeatList.setSelectedSeatCompartment(passengerSeatList.getSelected(passengerNo));
                            //selectedSeatTag.add(txtDetailList.getText().toString());

                        } else {

                            if(passengerSeatList.getSelected(passengerNo) != null){
                                TextView seatToRemove = (TextView) view.findViewWithTag(passengerSeatList.getSelected(passengerNo));
                                seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                                seatToRemove.setClickable(true);
                            }

                            seatTag.add(txtDetailList.getText().toString());
                            txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                            txtDetailList.setClickable(false);
                            passengerSeatList.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                            passengerSeatList.setSelectedCompartmentSeat(compartment);

                            //selectedSeatTag.add(txtDetailList.getText().toString());
                        }

                    }

                });


                //"seat_type":"standard",
               //Set color and clickable

                if(seatType.equals("standard")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_standard));

                }else if(seatType.equals("preferred")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_preferred));

                }else if(seatType.equals("desired")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_desired));
                }

                if(!seatStatus.equals("available")){
                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.red));
                    txtDetailList.setClickable(false);
                }

                /*Only for 4 seat row flight - change accordingly*/
                if(x == 2){
                    param.setMargins(5, 5,20,5);
                }
                else if(x == 3){
                    param.setMargins(20, 5,5,5);
                }
                else if(x == 1){
                    param.setMargins(20,5,5,5);
                }
                else if(x == 4){
                    param.setMargins(5,5,20,5);
                }
                txtDetailList.setLayoutParams(param);


                seatRow.addView(txtDetailList);
            }

            //Log.e("seatTag", seatTag.toString());
            seatList.addView(seatRow);

        }

        listPassenger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                ContactInfoReceive.PasssengerInfo selectedFromList =(ContactInfoReceive.PasssengerInfo) (listPassenger.getItemAtPosition(myItemInt));

                //Clear Previous
                passengerSeatList.clearSelected();
                seatTag = new ArrayList<>(1);

                passengerNo = myItemInt;
                //Set selected
                LinearLayout clickedPassenger = (LinearLayout) myView.findViewById(R.id.passengerLinearLayout);
                clickedPassenger.setBackgroundColor(getResources().getColor(R.color.blue));
                selectedFromList.setSelected(true);

                Log.e("Selected Passenger", selectedFromList.getFirst_name());

            }
        });


        btnSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<SeatSelect> goingSeat = new ArrayList<SeatSelect>();
                ArrayList<SeatSelect> returnSeat = new ArrayList<SeatSelect>();

                for(int x = 0 ; x < passengers.size() ; x++){
                    SeatSelect obj = new SeatSelect();
                    obj.setCompartment_designator(passengers.get(x).getCompartment());
                    obj.setSeat_number(passengers.get(x).getSeat());
                    goingSeat.add(obj);
                }

                //Validate
                SeatSelection seatObj = new SeatSelection();
                seatObj.setBooking_id(bookingID);
                seatObj.setSignature(signature);
                seatObj.setGoing(goingSeat);
                seatObj.setReturnFlight(returnSeat);

                Log.e("SEND", seatObj.getGoing().get(0).getSeat_number());
                seatSelect(seatObj);
            }
        });

        return view;
    }



    public void clearSelectedOnFragment(String seat){

        Log.e("seat",seat);
        TextView seatToRemove = (TextView) view.findViewWithTag(seat);
        seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
        seatToRemove.setClickable(true);
        seatTag = new ArrayList<>(1);

    }

    public void goPaymentPage()
    {
        Intent paymentPage = new Intent(getActivity(), PaymentFlightActivity.class);
        getActivity().startActivity(paymentPage);
    }

    public void seatSelect(SeatSelection obj){

        initiateLoading(getActivity());
        presenter.seatSelect(obj);
    }

    @Override
    public void onSeatSelect() {
        dismissLoading();
        Log.e("Success","true");
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
