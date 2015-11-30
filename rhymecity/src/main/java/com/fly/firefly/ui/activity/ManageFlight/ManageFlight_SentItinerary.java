package com.fly.firefly.ui.activity.ManageFlight;

import android.app.FragmentManager;
import android.os.Bundle;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.fragment.ManageFlight.ManageFlight_SentItineraryFragment;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class ManageFlight_SentItinerary extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_activity_fragment_container, ManageFlight_SentItineraryFragment.newInstance()).commit();

        hideTitle();
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }

}
