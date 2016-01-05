package com.fly.firefly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ProgressBar;

import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.SplashScreen.SplashScreenFragment;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;

/**
 * Created by Dell on 1/5/2016.
 */
public class Controller extends BaseFragment {


    public static boolean connectionAvailable(Activity act){

        Boolean internet;
        if (!Utils.isNetworkAvailable(act)) {
            //croutonAlert(act, "No Internet Connection");
            internet = false;
        }else{
            internet = true;
        }

        return internet;
    }

    public static boolean getRequestStatus(String objStatus,String message,Activity act) {

        SharedPrefManager pref;
        pref = new SharedPrefManager(act);

        Boolean status = false;
        if (objStatus.equals("success")) {
            status = true;

        } else if (objStatus.equals("error")) {
            status = false;
            croutonAlert(act, message);

        } else if (objStatus.equals("401")) {
            status = false;
            croutonAlert(act, message);
            pref.clearSignatureFromLocalStorage();
        }
        return status;

    }


}
