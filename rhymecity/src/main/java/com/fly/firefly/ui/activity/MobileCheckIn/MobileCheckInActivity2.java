package com.fly.firefly.ui.activity.MobileCheckIn;

import android.app.FragmentManager;
import android.os.Bundle;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.fragment.MobileCheckIn.MobileCheckInFragment2;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class MobileCheckInActivity2 extends MainFragmentActivity implements FragmentContainerActivity {

    //@InjectView(R.id.btnLogin) Button btnLogin;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_activity_fragment_container, MobileCheckInFragment2.newInstance()).commit();

    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
