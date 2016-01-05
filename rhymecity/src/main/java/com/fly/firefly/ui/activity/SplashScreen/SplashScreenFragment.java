package com.fly.firefly.ui.activity.SplashScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.Controller;
import com.fly.firefly.api.obj.DeviceInfoSuccess;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.module.SplashScreenModule;
import com.fly.firefly.ui.object.DeviceInformation;
import com.fly.firefly.ui.presenter.HomePresenter;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SplashScreenFragment extends BaseFragment implements HomePresenter.SplashScreen {

    @Inject
    HomePresenter presenter;
    private int fragmentContainerId;
    private SharedPrefManager pref;
    private DeviceInformation info;
    private Boolean running = false;

    public static SplashScreenFragment newInstance() {

        SplashScreenFragment fragment = new SplashScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new SplashScreenModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.splash_screen, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());

        //retrieve data
        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        String version = android.os.Build.VERSION.RELEASE;
        int sdkVersion = android.os.Build.VERSION.SDK_INT;

        info = new DeviceInformation();
        info.setSdkVersion(Integer.toString(sdkVersion));
        info.setVersion(version);
        info.setDeviceId(deviceId);
        info.setBrand(Build.BRAND);
        info.setModel(Build.MODEL);
        info.setDataVersion("");
        info.setSignature("");
        info.setUsername("");
        info.setPassword("");
        sendDeviceInformationToServer(info);
        running = true;
        return view;
    }



    public void sendDeviceInformationToServer(DeviceInformation info){
        if(Controller.connectionAvailable(getActivity())){
            presenter.deviceInformation(info);
            Log.e("Internet", "Ok");
        }else{
            Log.e("Internet", "X");
            connectionRetry("No Internet Connection");
        }
    }

    public void connectionRetry(String msg){
        new AlertDialog.Builder(getActivity()).setTitle("Connection Error").setMessage(msg).setNeutralButton(getActivity().getString(R.string.TRY), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                sendDeviceInformationToServer(info);
            }
        }).show();
    }

    @Override
    public void loadingSuccess(DeviceInfoSuccess obj) {

        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if(status){
            String signature = obj.getObj().getSignature();
            String bannerUrl = obj.getObj().getBanner_default();
            String promoBannerUrl = obj.getObj().getBanner_promo();

        /*Save All to pref for reference*/
            Gson gsonTitle = new Gson();
            String title = gsonTitle.toJson(obj.getObj().getData_title());
            pref.setUserTitle(title);

            Gson gsonCountry = new Gson();
            String country = gsonCountry.toJson(obj.getObj().getData_country());
            pref.setCountry(country);

            Gson gsonState = new Gson();
            String state = gsonState.toJson(obj.getObj().getData_state());
            pref.setState(state);

            Gson gsonFlight = new Gson();
            String flight = gsonFlight.toJson(obj.getObj().getData_market());
            pref.setFlight(flight);
        /*End*/

        /*Save Signature to local storage*/
            pref.setSignatureToLocalStorage(signature);
            pref.setBannerUrl(bannerUrl);
            pref.setPromoBannerUrl(promoBannerUrl);

            //Redirect to homepage after success loading splashscreen
            Intent home = new Intent(getActivity(), HomeActivity.class);
            getActivity().startActivity(home);
            getActivity().finish();
        }
    }


    @Override
    public void onConnectionFailed() {
        connectionRetry("Unable to connect to server");
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
        if(!running){
            sendDeviceInformationToServer(info);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        running = false;
    }
}
