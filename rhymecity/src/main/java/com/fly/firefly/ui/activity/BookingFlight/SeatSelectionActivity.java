package com.fly.firefly.ui.activity.BookingFlight;

import android.app.FragmentManager;
import android.os.Bundle;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.fragment.BookingFlight.SearchFlightFragment;
import com.fly.firefly.ui.fragment.BookingFlight.SeatSelectionFragment;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class SeatSelectionActivity extends MainFragmentActivity implements FragmentContainerActivity {

    FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        fragmentManager.beginTransaction().add(R.id.main_activity_fragment_container, SeatSelectionFragment.newInstance()).commit();

        hideTitle();
    }


    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
