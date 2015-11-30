package com.fly.firefly.ui.activity.Homepage;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.Beacon.BeaconRanging;
import com.fly.firefly.ui.activity.BoardingPass.BoardingPassActivity;
import com.fly.firefly.ui.activity.BookingFlight.SearchFlightActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Login.LoginActivity;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity1;
import com.fly.firefly.ui.module.HomeModule;
import com.fly.firefly.ui.presenter.HomePresenter;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

//import com.estimote.sdk.BeaconManager;

public class HomeFragment extends BaseFragment implements HomePresenter.HomeView {


    private Tracker mTracker;

    @Inject
    HomePresenter presenter;

    @InjectView(R.id.homeBookFlight)
    LinearLayout bookFlight;

    @InjectView(R.id.homeMobileCheckIn)
    LinearLayout mobileCheckIn;

    @InjectView(R.id.homeBeacon)
    LinearLayout homeBeacon;

    @InjectView(R.id.homeManageFlight)
    LinearLayout homeManageFlight;

    @InjectView(R.id.homeMobileBoardingPass)
    LinearLayout homeMobileBoardingPass;

    @InjectView(R.id.bannerImg)
    ImageView bannerImg;

    /* Beacon - Testing Module */
    //private BeaconManager beaconManager;

    private int fragmentContainerId;
    private SharedPrefManager pref;

    public static HomeFragment newInstance() {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new HomeModule(this)).inject(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);
        ButterKnife.inject(this, view);

        aq.recycle(view);
        pref = new SharedPrefManager(getActivity());

        /*SET BANNER*/
        HashMap<String, String> init = pref.getDefaultBanner();
        String defaultBanner = init.get(SharedPrefManager.DEFAULT_BANNER);
        Log.e("defaultBanner",defaultBanner);
        //aq.id(R.id.bannerImg).image(defaultBanner);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        bookFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToLoginPage();
                goBookingPage();
            }
        });
        //getSampleData();

        mobileCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMobileCheckIn();
            }
        });

        homeManageFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToManageFlight();
            }
        });


        /*mobileFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBeacon();
            }
        });*/

        homeBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBeacon();
            }
        });

        homeMobileBoardingPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBoardingPass();
            }
        });


        return view;
    }

    public void triggerNotification()
    {
        int notificationId = 001;
        // Build intent for notification content
        Intent viewIntent = new Intent(getActivity(), LoginActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(getActivity(), 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.departure_icon)
                        .setContentTitle("Title")
                        .setContentText("Location")
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    /*Public-Inner Func*/
    public void goToLoginPage(){
       Intent loginPage = new Intent(getActivity(), LoginActivity.class);
       // Intent loginPage = new Intent(getActivity(), SensorActivity.class);
       //Intent loginPage = new Intent(getActivity(), Touch.class);
       // Intent loginPage = new Intent(getActivity(), RelativeFragment.class);
        getActivity().startActivity(loginPage);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Click Book Flight")
                .build());

    }

    public void goToManageFlight()
    {
        //Intent loginPage = new Intent(getActivity(), ManageFlight_Activity.class);
        //getActivity().startActivity(loginPage);
    }

    /*Public-Inner Func*/
    public void goToMobileCheckIn()
    {
        Intent mcheckin = new Intent(getActivity(), MobileCheckInActivity1.class);
        getActivity().startActivity(mcheckin);
    }

    public void goToBeacon()
    {
        Intent loginPage = new Intent(getActivity(), BeaconRanging.class);
        //Intent loginPage = new Intent(getActivity(), CurrentGateActivity.class);
        getActivity().startActivity(loginPage);
    }

    public void goToBoardingPass()
    {
        //Intent loginPage = new Intent(getActivity(), BeaconRanging.class);
        Intent loginPage = new Intent(getActivity(), BoardingPassActivity.class);
        getActivity().startActivity(loginPage);
    }

    public void goBookingPage()
    {
        Intent loginPage = new Intent(getActivity(), SearchFlightActivity.class);
        getActivity().startActivity(loginPage);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Click booking page")
                .build());
        //getActivity().finish();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //progressIndicator = ((ProgressIndicatorActivity) getActivity()).getProgressIndicator();
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
        //((ToolbarActivity) getActivity()).getToolbar().setTitle(getString(R.string.app_name));
    }

    private void getSampleData() {
        presenter.onRequestGoogleData();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        Log.i("Page Name", "Setting screen name: " + "homepage");
        mTracker.setScreenName("Image~" + "A");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
