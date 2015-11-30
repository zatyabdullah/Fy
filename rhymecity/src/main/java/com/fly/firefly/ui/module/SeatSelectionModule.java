package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.fragment.BookingFlight.SeatSelectionFragment;
import com.fly.firefly.ui.presenter.SeatSelectionPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = SeatSelectionFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class SeatSelectionModule {

    private final SeatSelectionPresenter.SeatSelectionView seatSelectionView;

    public SeatSelectionModule(SeatSelectionPresenter.SeatSelectionView seatSelectionView) {
        this.seatSelectionView = seatSelectionView;
    }

    @Provides
    @Singleton
    SeatSelectionPresenter provideSearchFlightPresenter(Bus bus) {
        return new SeatSelectionPresenter(seatSelectionView, bus);
    }
}
