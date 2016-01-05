package com.fly.firefly.ui.activity.BookingFlight;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class PaymentFlightActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, PaymentFlightFragment.newInstance()).commit();

        hideTitle();
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        // presenter.onResume();
        Log.i("Page Name", "Setting screen name: " + "Payment Flight");
        mTracker.setScreenName("Payment Flight" + "A");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_content;
    }
}
