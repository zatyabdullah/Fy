package com.fly.firefly.ui.activity.BoardingPass;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.fragment.BoardingPass.BoardingPassFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class BoardingPassActivity extends MainFragmentActivity implements FragmentContainerActivity {

    //implements ToolbarActivity, ProgressIndicatorActivity, FragmentContainerActivity {
    //@InjectView(R.id.main_activity_toolbar) Toolbar toolbar;
    //@InjectView(R.id.main_activity_progress_indicator) ProgressBar progressIndicator;
    private Tracker mTracker;
    //private FragmentManager fragmentManager;
    FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        fragmentManager.beginTransaction().add(R.id.main_activity_fragment_container, BoardingPassFragment.newInstance()).commit();
        hideTitle();
    }


    @Override
    public void onResume() {
        super.onResume();
        // presenter.onResume();
        Log.i("Page Name", "Setting screen name: " + "Boarding Pass");
        mTracker.setScreenName("Boarding Pass" + "Main");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
