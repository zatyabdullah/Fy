package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.fragment.BookingFlight.PersonalDetailFragment;
import com.fly.firefly.ui.presenter.BF_PersonalDetailPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = PersonalDetailFragment.class,
        addsTo = AppModule.class,
        complete = false
)

public class PersonalDetailModule {

    private final BF_PersonalDetailPresenter.PersonalDetailView personalDetailView;

    public PersonalDetailModule(BF_PersonalDetailPresenter.PersonalDetailView personalDetailView) {
        this.personalDetailView = personalDetailView;
    }

    @Provides
    @Singleton
    BF_PersonalDetailPresenter provideFlightDetailPresenter(Bus bus) {
        return new BF_PersonalDetailPresenter(personalDetailView, bus);
    }
}
