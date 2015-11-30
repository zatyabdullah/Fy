package com.fly.firefly.ui.fragment.ManageFlight;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.presenter.LoginPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ManageFlight_SeatSelectionFragment extends Fragment {

    @Inject
    LoginPresenter presenter;
    @InjectView(R.id.btnPayment)
    Button btnPayment ;
    @InjectView(R.id.seatList) LinearLayout seatList ;

    private int fragmentContainerId;
    List<String> seatTag;

    public static ManageFlight_SeatSelectionFragment newInstance() {

        ManageFlight_SeatSelectionFragment fragment = new ManageFlight_SeatSelectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FireFlyApplication.get(getActivity()).createScopedGraph(new ManageFlightModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.manage_flight_seat_selection, container, false);
        ButterKnife.inject(this, view);

        /*3 passenger -- set array to 3 only*/
        seatTag = new ArrayList<>(2);

        for (int label = 0; label < 3; label++)
        {

            LinearLayout seatRow = new LinearLayout(getActivity());
            seatRow.setOrientation(LinearLayout.HORIZONTAL);
            seatRow.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);
            seatRow.setWeightSum(1);
            for (int x = 1; x < 5; x++)
            {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.25f);

                final TextView txtDetailList = new TextView(getActivity());
                txtDetailList.setText("S" + x);
                txtDetailList.setGravity(Gravity.CENTER);
                txtDetailList.setTextColor(getResources().getColor(R.color.white));
                txtDetailList.setPadding(5, 20, 5, 20);
                txtDetailList.setTag("id" + label + x);

                txtDetailList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (seatTag.size() == 2) {

                            Log.e("NEED TO REMOVE", seatTag.get(0));

                            TextView seatToRemove = (TextView) view.findViewWithTag(seatTag.get(0));
                            seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                            seatToRemove.setClickable(true);

                            seatTag.remove(0);
                            seatTag.add(txtDetailList.getTag().toString());

                            txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));

                        } else {
                            seatTag.add(txtDetailList.getTag().toString());
                            txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                            txtDetailList.setClickable(false);
                        }
                        Log.e("seatTagOnClick", seatTag.toString());

                    }

                });

                /*Only for 4 seat row flight - change accordingly*/
                if(x == 2){
                    param.setMargins(5,5,20,5);
                }
                else if(x == 3){
                    param.setMargins(20,5,5,5);
                }
                else if(x == 1){
                    param.setMargins(20,5,5,5);
                }
                else if(x == 4){
                    param.setMargins(5,5,20,5);
                }
                txtDetailList.setLayoutParams(param);


                //Set color and clickable
                if ( x % 2 == 0 ) {
                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.grey_background));
                    txtDetailList.setClickable(true);
                }
                else {
                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.red));
                    txtDetailList.setClickable(false);
                }
                seatRow.addView(txtDetailList);
            }

            Log.e("seatTag",seatTag.toString());
            seatList.addView(seatRow);

        }


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
        // presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // presenter.onPause();
    }


}
