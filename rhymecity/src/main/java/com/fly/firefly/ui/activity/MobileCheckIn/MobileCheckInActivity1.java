package com.fly.firefly.ui.activity.MobileCheckIn;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.fragment.MobileCheckIn.MobileCheckInFragment1;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class MobileCheckInActivity1 extends MainFragmentActivity implements FragmentContainerActivity {

    //@InjectView(R.id.btnLogin) Button btnLogin;

    //private FragmentManager fragmentManager;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        //FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, MobileCheckInFragment1.newInstance()).commit();
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
