package com.fly.firefly.ui.activity.Homepage;

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

public class HomeActivity extends MainFragmentActivity implements FragmentContainerActivity {

    //implements ToolbarActivity, ProgressIndicatorActivity, FragmentContainerActivity {
    //@InjectView(R.id.main_activity_toolbar) Toolbar toolbar;
    //@InjectView(R.id.main_activity_progress_indicator) ProgressBar progressIndicator;
    private Tracker mTracker;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, HomeFragment.newInstance()).commit();

        hideTitle();

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]
    }

   /* private void goToSearchFragment() {
        fragmentManager.beginTransaction()
                .add(R.id.main_activity_fragment_container, SearchFragment.newInstance())
                .commit();
    }*/

   /* @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }*/

    /*@Override
    public ProgressBar getProgressIndicator() {
        return progressIndicator;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }*/
    @Override
    public void onResume() {
        super.onResume();
       // presenter.onResume();
        Log.i("Page Name", "Setting screen name: " + "homepage");
        mTracker.setScreenName("Homepage" + "A");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
