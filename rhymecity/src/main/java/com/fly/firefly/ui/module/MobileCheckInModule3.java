package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.fragment.MobileCheckIn.MobileCheckInFragment3;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MobileCheckInFragment3.class,
        addsTo = AppModule.class,
        complete = false
)
public class MobileCheckInModule3 {

    private final MobileCheckInPresenter.MobileCheckInView view;

    public MobileCheckInModule3(MobileCheckInPresenter.MobileCheckInView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    MobileCheckInPresenter provideMobileCheckInPresenter1(Bus bus) {
        return new MobileCheckInPresenter(view, bus);
    }
}
