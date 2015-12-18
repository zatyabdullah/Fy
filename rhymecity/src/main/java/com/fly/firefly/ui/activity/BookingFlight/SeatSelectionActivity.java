package com.fly.firefly.ui.activity.BookingFlight;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.fragment.BookingFlight.SeatSelectionFragment;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class SeatSelectionActivity extends MainFragmentActivity implements FragmentContainerActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, SeatSelectionFragment.newInstance()).commit();

        hideTitle();
    }


    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
